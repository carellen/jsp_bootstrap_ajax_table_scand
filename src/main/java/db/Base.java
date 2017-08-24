package db;

import java.util.Collection;
import java.util.Map;

public interface Base {
    void addEntity(int id, Entity entity);
    Map<Integer, Entity> getAllEntities();
    void updateEntity(int id, Entity entity);
    void deleteEntity(int id);
}
