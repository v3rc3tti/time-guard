package com.github.v3rc3tti.timeguard.storage;

import com.github.v3rc3tti.timeguard.model.Task;
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
public class TaskRepository {

    private static final String SQL_FIND_ALL = "SELECT * FROM Tasks";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Tasks WHERE id = ?";
    private static final String SQL_FIND_BY_USER = "SELECT * FROM Tasks WHERE userId = ?";
    private static final String SQL_SAVE = "INSERT INTO Tasks(description, done, userId) VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM Tasks WHERE id = ?";

    private static final RowMapper<Task> TASK_MAPPER = new RowMapper<>() {

        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Task(rs.getLong("id"),
                    rs.getString("description"),
                    rs.getBoolean("done"),
                    rs.getLong("userId"));
        }
    };

    private final JdbcTemplate jdbc;

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Iterable<Task> findAll() {
        return jdbc.query(SQL_FIND_ALL, TASK_MAPPER);
    }

    public Task findOne(long id) {
        return jdbc.queryForObject(SQL_FIND_BY_ID,
                new Object[]{id},
                new int[] {Types.BIGINT},
                TASK_MAPPER);
    }

    public Iterable<Task> findByUserId(long userId) {
        return jdbc.query(SQL_FIND_BY_USER, TASK_MAPPER, userId);
    }

    public Task save(Task task) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(c -> {
            PreparedStatement ps = c.prepareStatement(SQL_SAVE,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getDescription());
            ps.setBoolean(2, task.getDone());
            ps.setLong(3, task.getUserId());
            return ps;
        }, keyHolder);

        task.setId(keyHolder.getKey().longValue());
        return task;
    }

    public void deleteById(Long id) {
        jdbc.update(SQL_DELETE, id);
    }
}
