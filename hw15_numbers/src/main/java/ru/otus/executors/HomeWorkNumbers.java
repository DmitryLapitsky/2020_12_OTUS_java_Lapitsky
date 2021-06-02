package ru.otus.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeWorkNumbers {
    private static final Logger logger = LoggerFactory.getLogger(HomeWorkNumbers.class);
    private String last = "Поток 2";
    static int count = 0;

    private synchronized void action(String threadName) {
        while (true) {
            try {
                while (last.equals(threadName)) {
                    this.wait();
                }
                int max = 10;
                int els = max + (max - 2) + max + (max - 2);//{1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2}.length = 36 элементов (аналог периода 2*PI) (2 раза от 1 до 10 -> max*2, еще 2 раза от 9 до 2 -> (max-2)*2)
                int period = (count - els * (count / els)) + 3;//из последовательности 1,2... разбиваем по периодам 1 - 36 (например, 73-> (73 - 36*({целое}73/36) = 73-36*2 = 1) и добавляем 3 для решения проблемы с 0 (см. далее)
                period = (period + period % 2 - 2) / 2; // для {3,4,5,6...} (добавили 3 на предыдущем шаге) к нечетным добавляем 1, чтобы стало {4,4,6,6,8,8...38,38} отнимаем 2 ->{2,2,4,4,6,6...36,36} и делим на 2->{1,1,2,2,3,3...18,18}
                int overMax = (period / max) * (period % max);//вычисляет превышение 10, например для 17: (17/10) * (17%10) = 1 * 7 = 7
                period = period - overMax * 2;//17 - 7*2(первая 7-ка до середины {10} и вторая 7-ка добивает) = 3 (для 18 будет 2 и это конец периода)
                count++;
                logger.info(threadName + ":\t" + period);
                last = threadName;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new NotInterestingException(ex);
            }
        }
    }

    public static void main(String[] args) {
        HomeWorkNumbers pingPong = new HomeWorkNumbers();
        Thread thread1 = new Thread(() -> pingPong.action(Thread.currentThread().getName()));
        thread1.setName("Поток 1");
        thread1.start();
        Thread thread2 = new Thread(() -> pingPong.action(Thread.currentThread().getName()));
        thread2.setName("Поток 2");
        thread2.start();
    }

    private static void sleep() {
        try {
            Thread.sleep(333);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class NotInterestingException extends RuntimeException {
        NotInterestingException(InterruptedException ex) {
            super(ex);
        }
    }
}
