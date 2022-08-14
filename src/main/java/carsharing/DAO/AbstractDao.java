package carsharing.DAO;

import java.util.List;
import java.util.Optional;

public interface AbstractDao <E>{
    Optional<E> getByName(String name);
    List<E> getAll();
    E create(E entity);
    boolean delete(long id);
    boolean update(E entity);
}