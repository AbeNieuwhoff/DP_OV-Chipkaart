import reiziger.Reiziger;
import reiziger.ReizigerDAOPsql;

import java.sql.*;

public class Main {

    private static Connection conn = null;

    public static void main(String[] args) {
        System.out.println("p1: \n");
        try {
            conn = connect();
            if (conn != null) {
                System.out.println("Verbinding met de database is gelukt!");

                String sql = "SELECT * FROM reiziger;";
                try (Statement stmt = conn.createStatement();
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
                System.out.println("\n p2: \n");
                testReizigerDAO(conn);
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
    public static void testReizigerDAO(Connection conn) {
        ReizigerDAOPsql dao = new ReizigerDAOPsql(conn);

        // Nieuwe reiziger aanmaken
        Reiziger nieuw = new Reiziger(0, "L", null, "Testman", Date.valueOf("2000-01-01"));
        dao.save(nieuw);
        System.out.println("Nieuw opgeslagen: " + nieuw + '\n');

        // Alle reizigers tonen
        for (Reiziger r : dao.findAll()) {
            System.out.println(r);
        }

        // Reiziger updaten
        nieuw.setAchternaam("UpdateTest");
        dao.update(nieuw);
        System.out.println("Na update: " + dao.findById(nieuw.getReizigerId()));

        // Reiziger verwijderen
        dao.delete(nieuw);
        System.out.println("Na delete:");
        for (Reiziger r : dao.findAll()) {
            System.out.println(r);
        }
    }
}
