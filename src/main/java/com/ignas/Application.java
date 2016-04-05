package com.ignas;

import com.ignas.simulation.Simulation;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ignas on 2016-04-03.
 */
public class Application {

    public static void main(String... args) throws SQLException {

        Simulation.define();
    }
}
