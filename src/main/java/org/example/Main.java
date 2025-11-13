package org.example;

import java.sql.*;

public class Main {

    private static Connection conn = null;

    public static void main(String[] args) {
        try {
            Connection connection = connect();
            if (connection != null) {
                System.out.println("Verbinding met de database is gelukt!");

                String sql = "SELECT * FROM reiziger;";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        int id = rs.getInt("reiziger_id");
                        String voorletters = rs.getString("voorletters");
                        String tussenvoegsel = rs.getString("tussenvoegsel");
                        String achternaam = rs.getString("achternaam");
                        Date datum = rs.getDate("geboortedatum");

                        System.out.println(id + ": " + voorletters + " " +
                                (tussenvoegsel != null ? tussenvoegsel + " " : "") +
                                achternaam + " (" + datum + ")");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Fout bij databasebewerking:");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Verbinding gesloten.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Connection connect() {
        if (conn != null) {
            System.out.println("bestaande verbinding hergebruiken.....");
            return conn;
        }

        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "nhf";
        String password = "password";

        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Kan geen verbinding maken met de database:");
            e.printStackTrace();
        }

        return conn;
    }
}
