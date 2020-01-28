package com.example.MeterMaid.controller;

import com.example.MeterMaid.Model.User;
import com.example.MeterMaid.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
                    "Not found, " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/users/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") UUID id)
    {
        try{
            User user = userDao.getUserById(id);
            return ResponseEntity.ok(user);
        }catch (Exception ex){
            return new ResponseEntity(
                    "Not found, " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/users/")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        try{
            user.setId(UUID.randomUUID());
            User createdUser = userDao.create(user);
            return ResponseEntity.ok(createdUser);
        }catch (Exception ex){
            return new ResponseEntity(
                    "Not found, " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/id/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") UUID id, @Valid @RequestBody User recievedUser) {
        try{
            recievedUser.setId(id);
            User updatedUser = userDao.update(recievedUser, id);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception ex){
            return new ResponseEntity(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/id/{id}")
    public ResponseEntity deleteUser(@PathVariable(value = "id") UUID id){
        try {
            int didUserGetDeleted = userDao.delete(id);
            if(didUserGetDeleted == 1){
                return new ResponseEntity(
                        "User with id " + id + " has been deleted permanently",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity(
                        "User not deleted, something went wrong",
                        HttpStatus.NOT_MODIFIED);
            }
        }catch (Exception ex){
            return new ResponseEntity(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
