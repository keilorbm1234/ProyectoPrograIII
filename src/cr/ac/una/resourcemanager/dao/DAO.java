package cr.ac.una.resourcemanager.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    void create(T entity) throws Exception;
    Optional<T> read(ID id) throws Exception;
    void update(T entity) throws Exception;
    void delete(ID id) throws Exception;
    List<T> readAll() throws Exception;
}
