package com.example.MeterMaid.controller;

import com.example.MeterMaid.Model.User;
import com.example.MeterMaid.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/metermaid/api")
public class UserController {
    @Autowired
    UserDao userDao;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers()
    {
        try{
            return ResponseEntity.ok().body(userDao.getAll());
        }catch (Exception ex){
            return new ResponseEntity(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable(value = "email") String email)
    {
        try{
            User user = userDao.getUserByEmail(email);
            return ResponseEntity.ok(user);

        }catch (Exception ex){
            return new ResponseEntity(
                    "Not found" + ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

    }

}
