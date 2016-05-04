package com.ignas;

import com.google.common.collect.ImmutableMap;
import com.ignas.iot.DataAccessException;
import com.ignas.iot.IOTOperations;
import com.ignas.iot.OperationsFactory;
import com.ignas.iot.simulation.IOTSimulation;
import com.ignas.iot.simulation.PredefinedTask;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.SQLException;

import static com.ignas.iot.OperationsFactory.mongo;
import static com.ignas.iot.OperationsFactory.postgres;
import static com.ignas.iot.OperationsFactory.volt;

public class Application {



    public static void main(String... args) throws SQLException {
        if (args.length < 1) {
            throw new RuntimeException("Nekorektiškas kvietimas! pvz: simulation.jar volt def");
        }

        String dbName = args[0];
        IOTOperations ops = resolveDBOperations(dbName);
        if (ops == null) {
            throw new RuntimeException("Neegzistuojanti DB! Galimos: (volt, mongo, postgres)");
        }

        IOTSimulation simulation = new IOTSimulation(ops);


        if (args.length == 2 && args[1] != null && "def".equals(args[1].toLowerCase())) {
            simulation.define();
        } else {
            simulation.run();
        }
    }

    private static IOTOperations resolveDBOperations(String dbName) {
        if (dbName.equals("volt")) {
            return volt();
        } else if (dbName.equals("mongo")) {
            return mongo();
        } else if (dbName.equals("postgres")) {
            return postgres();
        }

        return null;
    }
}
