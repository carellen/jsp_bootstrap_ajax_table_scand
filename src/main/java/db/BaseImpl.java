package db;

import java.util.*;
//temporary stub instead database
public class BaseImpl implements Base {
    private static Map<Integer, Entity> entityList = new HashMap<Integer, Entity>();

    public void addEntity(int id, Entity entity) {
        entityList.put(id, entity);
    }

    public Map<Integer, Entity> getAllEntities() {
        return entityList;
    }

    public void updateEntity(int id, Entity entity) {
        entityList.put(id, entity);
    }

    public void deleteEntity(int id) {
        entityList.remove(id);
    }
}
