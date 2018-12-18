package com.example.core.player;

import java.util.LinkedHashMap;

public class ProgressUtil {

    private static LinkedHashMap<Long, Long> progressMap = new LinkedHashMap<>();

    public static void saveProgress(long createTime, long progress) {
        progressMap.put(createTime, progress);
    }

    public static long getSavedProgress(long createTime) {
        return progressMap.containsKey(createTime) ? progressMap.get(createTime) : 0;
    }

    /**
     * clear all progress
     */
    public static void clearAllSavedProgress() {
        progressMap.clear();
    }

    /**
     * remove progress by url
     */
    public static void clearSavedProgressByUrl(long createTime) {
        progressMap.remove(createTime);
    }

}
