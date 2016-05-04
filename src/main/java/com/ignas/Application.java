package com.ignas;

import com.ignas.iot.simulation.IOTSimulation;

import java.sql.SQLException;

/**
 * Created by Ignas on 2016-04-03.
 */
public class Application {

    public static void main(String... args) throws SQLException {
        //IOTSimulation.ofVolt().run();
        //IOTSimulation.ofMongo().run();
        IOTSimulation.ofPostgres().run();
    }
}
