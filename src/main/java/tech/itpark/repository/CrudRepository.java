package tech.itpark.repository;

import tech.itpark.entity.UserEntity;

import java.util.List;
import java.util.Optional;

// CRUD repository
public interface CrudRepository<T, ID> {
    //crud
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    boolean removeById(ID id);
}
