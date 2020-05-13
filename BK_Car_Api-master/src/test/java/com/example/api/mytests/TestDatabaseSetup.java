package com.example.api.mytests;

import java.sql.DriverManager;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

public class TestDatabaseSetup {
    public static void execute(Operation op) {
        try {
            DriverManager.deregisterDriver(new com.mysql.jdbc.Driver());
//            DriverManagerDestination dest = new DriverManagerDestination(
//                    "jdbc:sqlite:/home/exodus/Documents/BK_Car_Api.db", "", "");
            DriverManagerDestination dest = new DriverManagerDestination(
                    "jdbc:mysql://localhost:3306/BK_Car_Api", "root", "");
            DbSetup ds = new DbSetup(dest, op);
            ds.launch();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
