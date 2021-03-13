package tech.itpark.repository;

import tech.itpark.entity.UserEntity;
import tech.itpark.exception.UserNotFoundException;

import java.util.*;

public class UserRepositoryInMemoryImpl implements UserRepository {
    private long nextId = 1;
    private final Map<Long, UserEntity> idToEntity = new HashMap<>();
    private final Map<String, UserEntity> loginToEntity = new HashMap<>();

    @Override
    public List<UserEntity> findAll() {
        return List.copyOf(idToEntity.values());
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(idToEntity.get(id));
    }

    @Override
    public UserEntity save(UserEntity entity) {
        // FIXME: login has to be unique
        if (entity.getId() == 0){
            entity.setId(nextId++);
        }
        // FIXME: check if user changed login
        loginToEntity.put(entity.getLogin(), entity);
        return idToEntity.put(entity.getId(), entity);
    }

    @Override
    public boolean removeById(Long id) {
        try {
            idToEntity.get(id).setRemoved(true);
            return idToEntity.get(id).isRemoved();
        } catch (NullPointerException e) {
            throw new UserNotFoundException(e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        return loginToEntity.containsKey(login);
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        return Optional.ofNullable(loginToEntity.get(login));
    }
}
