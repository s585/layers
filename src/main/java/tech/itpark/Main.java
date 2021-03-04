package tech.itpark;

import tech.itpark.entity.UserEntity;
import tech.itpark.exception.DataAccessException;
import tech.itpark.repository.UserRepository;
import tech.itpark.repository.UserRepositoryJDBCImpl;
import tech.itpark.service.UserService;
import tech.itpark.service.UserServiceDefaultImpl;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Main {
    public static void main(String[] args) {
        // TODO: add env
        String dsn = "jdbc:postgresql://localhost:5400/appdb?user=app&password=pass";

        try (Connection connection = DriverManager.getConnection(dsn)
        ) {
            UserRepository repository = new UserRepositoryJDBCImpl(connection);
            UserService service = new UserServiceDefaultImpl(repository);
//            System.out.println(repository.existsByLogin("admin"));
//            System.out.println(repository.removeById(1L));
//            System.out.println(userById.get().getLogin());
            System.out.println(repository.save(new UserEntity(
                    0L,
                    "s585",
                    "pass",
                    "s585",
                    "secret",
                    Set.of("ROLE_USER"),
                    false,
                    Timestamp.valueOf(LocalDateTime.now()).getTime())));


            } catch (SQLException e) {

        }
    }
}
