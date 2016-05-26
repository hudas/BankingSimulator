package com.ignas;

import com.ignas.iot.configuration.PropertiesFileReader;
import com.ignas.iot.operations.provider.OperationsFactory;
import com.ignas.iot.Simulation;

import java.sql.SQLException;

import static com.ignas.iot.operations.provider.OperationsFactory.mongo;
import static com.ignas.iot.operations.provider.OperationsFactory.postgres;
import static com.ignas.iot.operations.provider.OperationsFactory.volt;


/**
 * Main Entry point of application
 */
public class Application {

    public static void main(String... args) throws SQLException {
        if (args.length < 1) {
            throw new RuntimeException("Nekorektiškas kvietimas! pvz: domain.jar volt def");
        }

        String dbName = args[0];
        String hostName = args[1];
        OperationsFactory ops = getConnectionProvider(dbName, hostName);
        if (ops == null) {
            throw new RuntimeException("Neegzistuojanti DB! Galimos: (volt, mongo, postgres)");
        }

        Simulation simulation = new Simulation(ops, new PropertiesFileReader().getConfiguration(), 0);


        if (args.length == 3 && args[2] != null && "def".equals(args[2].toLowerCase())) {
            simulation.define();
        } else {
            simulation.run();
        }
    }

    private static OperationsFactory getConnectionProvider(String dbName, String host) {
        if (dbName.equals("volt")) {
            return volt(host);
        } else if (dbName.equals("mongo")) {
            return mongo(host);
        } else if (dbName.equals("postgres")) {
            return postgres(host);
        }

        return null;
    }
}
