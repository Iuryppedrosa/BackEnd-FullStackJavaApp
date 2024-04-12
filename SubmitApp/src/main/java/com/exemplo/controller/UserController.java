package com.exemplo.controller;

import com.exemplo.exception.UserNotFoundException;
import com.exemplo.model.User;
import com.exemplo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity<User> postUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> getUserById (@PathVariable Long id) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));

        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "Usuário deletado com sucesso!" + id;
    }
}
