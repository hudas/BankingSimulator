package com.ignas;

import com.ignas.simulation.Simulation;
import java.sql.SQLException;

/**
 * Created by Ignas on 2016-04-03.
 */
public class Application {

    public static void main(String... args) throws SQLException {
        Simulation.define();
    }
}
