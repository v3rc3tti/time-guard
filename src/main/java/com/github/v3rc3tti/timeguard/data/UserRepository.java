package com.github.v3rc3tti.timeguard.data;

import com.github.v3rc3tti.timeguard.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@Repository
public class UserRepository {

    private static final RowMapper<User> USER_MAPPER = new RowMapper<User>() {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("id"), rs.getString("name"));
        }
    };

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Iterable<User> findAll() {
        return jdbc.query("SELECT * FROM Users", USER_MAPPER);
    }

    public User findOne(long id) {
        return jdbc.queryForObject("SELECT * FROM Users WHERE id = ?",
                new Object[]{id},
                new int[] {Types.BIGINT},
                USER_MAPPER);
    }

    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(c -> {
            PreparedStatement ps = c.prepareStatement("INSERT INTO Users(name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            return ps;
            }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }
}
