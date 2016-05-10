package com.ignas.iot.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ignas on 5/10/16.
 */
public class PropertiesFileReader implements ProvidesConfig {

    public static SimulationConfig loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            Integer patientsCount = Integer.parseInt(prop.getProperty("patients.count"));
            Integer preparedConditionsCount = Integer.parseInt(prop.getProperty("prepared_conditions.count"));

            Integer threadCount = Integer.parseInt(prop.getProperty("thread.count"));
            Integer workIterations = Integer.parseInt(prop.getProperty("work_iterations.count"));

            Integer insertCondFreq = Integer.parseInt(prop.getProperty("insert_condition.freq"));
            Integer findLatestFreq = Integer.parseInt(prop.getProperty("find_latest.freq"));
            Integer insertCondStatFreq = Integer.parseInt(prop.getProperty("insert_condition_with_stats.freq"));
            Integer findLatestStatFreq = Integer.parseInt(prop.getProperty("find_latest_stats.freq"));
            Integer dailyCondStatFreq = Integer.parseInt(prop.getProperty("daily_condition_stats.freq"));
            Integer findLatestFromViewFreq = Integer.parseInt(prop.getProperty("find_latest_view.freq"));

            SimulationConfig config = new SimulationConfig(patientsCount, preparedConditionsCount,
                    threadCount, workIterations,
                    insertCondFreq, findLatestFreq, insertCondStatFreq, findLatestStatFreq,
                    dailyCondStatFreq, findLatestFromViewFreq);


            input.close();
            return config;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Bad configuration");
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Bad configuration");
        }
    }


    @Override
    public SimulationConfig getConfiguration() {
        return loadProperties();
    }
}
