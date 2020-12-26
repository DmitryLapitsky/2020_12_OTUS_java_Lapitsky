package ru.otus;


import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class HelloOtus {

    public static void main(String[] args) {
        Map<String, Integer> items = ImmutableMap.of("one", 1, "two", 2, "four", 4);
        System.out.println(items);
    }
}