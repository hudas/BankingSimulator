package com.ignas;

import com.ignas.iot.configuration.PropertiesFileReader;
import com.ignas.iot.operations.provider.OperationsFactory;
import com.ignas.iot.Simulation;

import java.sql.SQLException;

import static com.ignas.iot.operations.provider.OperationsFactory.mongo;
import static com.ignas.iot.operations.provider.OperationsFactory.postgres;
import static com.ignas.iot.operations.provider.OperationsFactory.volt;

public class Application {



    public static void main(String... args) throws SQLException {
        if (args.length < 1) {
            throw new RuntimeException("Nekorektiškas kvietimas! pvz: domain.jar volt def");
        }

        String dbName = args[0];
        OperationsFactory ops = getConnectionProvider(dbName);
        if (ops == null) {
            throw new RuntimeException("Neegzistuojanti DB! Galimos: (volt, mongo, postgres)");
        }

        Simulation simulation = new Simulation(ops, new PropertiesFileReader().getConfiguration());


        if (args.length == 2 && args[1] != null && "def".equals(args[1].toLowerCase())) {
            simulation.define();
        } else {
            simulation.run();
        }
    }

    private static OperationsFactory getConnectionProvider(String dbName) {
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
