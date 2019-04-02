package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import social.dto.Response;
import social.entity.User;
import social.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        User userExists = userService.findUserByUsername(user.getUsername());
        if (userExists != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(true, "User already exist"));

        }
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, "User created"));
    }
}
