package tech.itpark.repository;

import tech.itpark.entity.UserEntity;
import tech.itpark.exception.DataAccessException;
import tech.itpark.jdbc.RowMapper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserRepositoryJDBCImpl implements UserRepository {
    private final Connection conn;
    private final RowMapper<UserEntity> mapper = rs -> {
        try {
            return new UserEntity(
                    rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("secret"),
                    Set.of((String[])rs.getArray("roles").getArray()),
                    rs.getBoolean("removed"),
                    rs.getLong("created")
            );
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    };

    public UserRepositoryJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<UserEntity> findAll() {
        final String query = "SELECT id, login, password, name, secret, roles," +
                " EXTRACT(EPOCH FROM created) created, removed FROM users ORDER BY id";
        try (
                final Statement stmt = conn.createStatement();
                final ResultSet rs = stmt.executeQuery(query)
        ) {
            List<UserEntity> result = new LinkedList<>();
            while (rs.next()) {
                final UserEntity entity = mapper.map(rs);
                result.add(entity);
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        final String query = "SELECT id, login, password, name, secret, roles, EXTRACT(EPOCH FROM created) created, removed" +
                " FROM users WHERE id = ?";
        try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (final ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        final String query = "SELECT id, login, password, name, secret, roles, EXTRACT(EPOCH FROM created) created, removed" +
                " FROM users WHERE login = ?";
        try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, login);
            try (final ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        final String query = "SELECT login FROM users WHERE login = ?";
        try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, login);
            try (final ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (entity.getId() == 0) {
            final String query = "INSERT INTO users (login, password, name, secret, roles) VALUES (?,?,?,?,?)" +
                    " RETURNING id, login, password, name, secret, roles, removed, EXTRACT(EPOCH FROM created) created";
            try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, entity.getLogin());
                pstmt.setString(2, entity.getPassword());
                pstmt.setString(3, entity.getName());
                pstmt.setString(4, entity.getSecret());
                pstmt.setObject(5, entity.getRoles().toArray(), Types.ARRAY);

                try (final ResultSet rs = pstmt.executeQuery()) {
                    try {
                        if (rs.next()) {
                             return mapper.map(rs);
                        }
                    } catch (SQLException e) {
                        throw new DataAccessException(e);
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }

        final String query = "UPDATE users SET login = ?, password = ?, name = ?, secret = ?, roles = ? WHERE id = ? " +
                "RETURNING id, login, password, name, secret, roles, removed, EXTRACT(EPOCH FROM created) created";
        try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, entity.getLogin());
            pstmt.setString(2, entity.getPassword());
            pstmt.setString(3, entity.getName());
            pstmt.setString(4, entity.getSecret());
            pstmt.setObject(5, entity.getRoles().toArray(), Types.ARRAY);
            pstmt.setLong(6, entity.getId());
            UserEntity savedEntity = null;
            try (final ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    savedEntity = mapper.map(rs);
                }
            } return savedEntity;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean removeById(Long id) {
        final String query = "UPDATE users SET removed = true WHERE id = ? RETURNING removed";
        try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (final ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getBoolean("removed");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
