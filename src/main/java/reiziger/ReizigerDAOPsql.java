package reiziger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn; // dependency injection
    }

    @Override
    public Reiziger findById(int id) {
        try {
            String sql = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reiziger");
            while (rs.next()) {
                reizigers.add(new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            String sql = "INSERT INTO reiziger (voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, reiziger.getVoorletters());
            pstmt.setString(2, reiziger.getTussenvoegsel());
            pstmt.setString(3, reiziger.getAchternaam());
            pstmt.setDate(4, reiziger.getGeboortedatum());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            // ID ophalen
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reiziger.setReizigerId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean update(Reiziger reiziger) {
        try {
            String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reiziger.getVoorletters());
            pstmt.setString(2, reiziger.getTussenvoegsel());
            pstmt.setString(3, reiziger.getAchternaam());
            pstmt.setDate(4, reiziger.getGeboortedatum());
            pstmt.setInt(5, reiziger.getReizigerId());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reiziger.getReizigerId());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
