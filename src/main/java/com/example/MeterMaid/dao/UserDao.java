package com.example.MeterMaid.dao;

import com.example.MeterMaid.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDao implements IUserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User create(User user) {
        jdbcTemplate.update(
                "insert into users(id, firstname, lastname, email) values(?,?,?,?)",
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        String sql = "select * from users where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{user.getId()}, ((rs, rowNum) ->
                new User(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        (UUID) rs.getObject("id")
                )
        ));
    }

    @Override
    public User getUserById(UUID id) {
        String sql = "select * from users where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, ((rs, rowNum) ->
                new User(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        (UUID) rs.getObject("id")
                )
                ));
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "select * from users where email = ? limit 1";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, ((rs, rowNum) ->
                new User(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        (UUID) rs.getObject("id")
                )
        ));
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from users";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new User(
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("email"),
                                (UUID) rs.getObject("id")
                        )
        );
    }

    @Override
    public int delete(UUID id) {
        String sql = "delete from users where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public User update(User user, UUID id) {
        String sql = "update users set firstname = ?, lastname = ?, email = ?, id = ? where id = ?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getId(), id);

        String returnSql = "select * from users where id = ?";
        return jdbcTemplate.queryForObject(returnSql, new Object[]{user.getId()}, ((rs, rowNum) ->
                new User(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        (UUID) rs.getObject("id")
                )
        ));
    }
}
