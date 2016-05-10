package com.ignas.iot.configuration;

/**
 * Created by ignas on 5/10/16.
 */
public class SimulationConfig {
    private Integer threadCount = 5;
    private Integer workIterations = 1;

    // Must Be 100proc
    private Integer insertCondFreq = 100;
    private Integer findLatestFreq = 0;
    private Integer insertCondWithStatsFreq = 0;
    private Integer findLatestStatFreq = 0;
    private Integer dailyCondStatsFreq = 0;
    private Integer findLatestFromViewFreq = 0;

    public SimulationConfig(Integer threadCount, Integer workIterations, Integer insertCondFreq,
                            Integer findLatestFreq, Integer insertCondWithStatsFreq, Integer findLatestStatFreq,
                            Integer dailyCondStatsFreq, Integer findLatestFromViewFreq) {
        this.threadCount = threadCount;
        this.workIterations = workIterations;
        this.insertCondFreq = insertCondFreq;
        this.findLatestFreq = findLatestFreq;
        this.insertCondWithStatsFreq = insertCondWithStatsFreq;
        this.findLatestStatFreq = findLatestStatFreq;
        this.dailyCondStatsFreq = dailyCondStatsFreq;
        this.findLatestFromViewFreq = findLatestFromViewFreq;
    }


    public Integer getThreadCount() {
        return threadCount;
    }

    public Integer getWorkIterations() {
        return workIterations;
    }

    public Integer getInsertCondFreq() {
        return insertCondFreq;
    }

    public Integer getFindLatestFreq() {
        return findLatestFreq;
    }

    public Integer getInsertCondWithStatsFreq() {
        return insertCondWithStatsFreq;
    }

    public Integer getFindLatestStatFreq() {
        return findLatestStatFreq;
    }

    public Integer getDailyCondStatsFreq() {
        return dailyCondStatsFreq;
    }

    public Integer getFindLatestFromViewFreq() {
        return findLatestFromViewFreq;
    }
}
