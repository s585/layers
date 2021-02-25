package tech.itpark.service;

import tech.itpark.entity.UserEntity;
import tech.itpark.exception.PasswordInvalidException;
import tech.itpark.exception.SecretInvalidException;
import tech.itpark.exception.UsernameAlreadyExistsException;
import tech.itpark.exception.UsernameDoesNotExistException;
import tech.itpark.model.*;
import tech.itpark.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Set;

public class UserServiceDefaultImpl implements UserService {
    private final UserRepository repository;

    public UserServiceDefaultImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserModel register(RegistrationModel model) {
        if (repository.existsByLogin(model.getLogin())) {
            throw new UsernameAlreadyExistsException(model.getLogin());
        }

        UserEntity entity = repository.save(new UserEntity(
                0L,
                model.getLogin(),
                //FIXME: hash password
                model.getPassword(),
                model.getName(),
                model.getSecret(),
                // FIXME: extract hardcoded roles
                Set.of("ROLE_USER"),
                false,
                OffsetDateTime.now().toEpochSecond()
        ));

        return new UserModel(
                entity.getId(),
                entity.getLogin(),
                entity.getName(),
                entity.getRoles(),
                entity.isRemoved(),
                entity.getCreated()
        );
    }

    @Override
    public UserModel login(AuthentificationModel model) {
        UserEntity entity = repository
                .findByLogin(model.getLogin())
                .orElseThrow(() -> new UsernameDoesNotExistException(model.getLogin()));

        if (entity.isRemoved()) {
            throw new UsernameDoesNotExistException(model.getLogin());
        }

        if (!entity.getPassword().equals(model.getPassword())) {
            throw new PasswordInvalidException();
        }

        // FIXME:
        return new UserModel(
                entity.getId(),
                entity.getLogin(),
                entity.getName(),
                entity.getRoles(),
                entity.isRemoved(),
                entity.getCreated()
        );
    }

    @Override
    public UserModel reset(ResetModel model) {
        UserEntity entity = repository
                .findByLogin(model.getLogin())
                .orElseThrow(() -> new UsernameDoesNotExistException(model.getLogin()));
        if (entity.isRemoved()) {
            throw new UsernameDoesNotExistException(model.getLogin());
        }
        if (!entity.getSecret().equals(model.getSecret())) {
            throw new SecretInvalidException(model.getLogin());
        }
        entity.setPassword(model.getNewPassword());

        return new UserModel(
                entity.getId(),
                entity.getLogin(),
                entity.getName(),
                entity.getRoles(),
                entity.isRemoved(),
                entity.getCreated()
        );
    }

    @Override
    public boolean remove(RemoveModel model) {
        UserEntity entity = repository
                .findByLogin(model.getLogin())
                .orElseThrow(() -> new UsernameDoesNotExistException(model.getLogin()));
        if (entity.isRemoved()) {
            throw new UsernameDoesNotExistException(model.getLogin());
        }
        if (!entity.getPassword().equals(model.getPassword())) {
            throw new PasswordInvalidException(model.getLogin());
        }
        entity.setRemoved(true);
        return entity.isRemoved();

    }
}
