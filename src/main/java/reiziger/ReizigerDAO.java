package reiziger;

import java.util.List;

public interface ReizigerDAO {
    Reiziger findById(int id);
    List<Reiziger> findAll();
    boolean save(Reiziger reiziger);   // Create
    boolean update(Reiziger reiziger); // Update
    boolean delete(Reiziger reiziger); // Delete
}
