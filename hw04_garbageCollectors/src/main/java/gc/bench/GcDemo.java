package gc.bench;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

/*
О формате логов
http://openjdk.java.net/jeps/158


-Xms512m
-Xmx512m
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/dump
-XX:+UseG1GC
*/

/*
Serial Collector        -XX:+UseSerialGC
Parallel Collector      -XX:+UseParallelGC
CMS                     -XX:+UseConcMarkSweepGC
G1                      -XX:+UseG1GC
ZGC                     -XX:+UnlockExperimentalVMOptions -XX:+UseZGC
 */

/*
-Xms256m
-Xmx256m
-XX:+UseG1GC
-Xlog:gc=debug:file=./hw04_garbageCollectors/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

public class GcDemo {

    static int youngTimes = 0;
    static long youngMs = 0;
    static int oldTimes = 0;
    static long oldMs = 0;
    static long totalTime = 0;
    static int totalS = 0;

    public static void main(String... args) throws Exception {
        long begin = System.currentTimeMillis();
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        int size = 15000;
        int loopCounter = 1000_000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark benchmark = new Benchmark(loopCounter);
        mbs.registerMBean(benchmark, name);
        benchmark.setSize(size);
        try {
            benchmark.getOOM();
        } finally {
            String msg = "\n" + new SimpleDateFormat("HH:mm").format(new Date());
            msg = msg + "\n" + "Test time " + ((System.currentTimeMillis() - begin) / 1000) + " [s], incremental size: " + benchmark.getSize() + ", stop id:" + benchmark.getId();
            msg = msg + "\n" + "gc duration:\t" + totalTime + "\t, count:\t" + totalS;
            msg = msg + "\n" + "young count:\t" + youngTimes + "\ttime:" + youngMs;
            msg = msg + "\n" + "old count:\t" + oldTimes + "\ttime:" + oldMs + "\n\n";
            System.out.println(msg);
        }


    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                    totalTime += duration;
                    totalS++;
                    if (gcName.contains("Young")) {
                        youngMs += duration;
                        youngTimes++;
                    }
                    if (gcName.contains("Old")) {
                        oldMs += duration;
                        oldTimes++;
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
