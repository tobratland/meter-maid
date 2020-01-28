package com.example.MeterMaid.dao;

import com.example.MeterMaid.Model.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

public interface IUserDao {
    void setDataSource(DataSource dataSource);

    int create(User user);

    User getUserById(UUID id);

    User getUserByEmail(String email);

    List<User> getAll();

    int delete(UUID id);

    User update(User user, UUID id);


}
