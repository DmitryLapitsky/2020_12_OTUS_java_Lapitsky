package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V>, HwListener<K, V> {
//Надо реализовать эти методы

    Map<K, V> cache = new WeakHashMap<>();

    List<WeakReference<HwListener<K, V>>> listener = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        notify(key, cache.get(key), "removing");
        cache.remove(key);
        notify(key, cache.get(key), "remove");
    }

    @Override
    public V get(K key) {
        notify(key, cache.get(key), "get");
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listener.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        for (WeakReference<HwListener<K, V>> el : this.listener) {
            if (el.equals(listener)) {
                this.listener.remove(el);
                break;
            }
        }
    }

    @Override
    public void notify(K key, V value, String action) {
        for (WeakReference<HwListener<K, V>> el : this.listener) {
            el.get().notify(key, value, action);
        }
    }

    @Override
    public String toString() {
        return "MyCache{" +
                "cache=" + cache.toString() +
                ", listener=" + listener +
                '}';
    }
}
