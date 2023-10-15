package br.com.cunhaedu.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<UserEntity> create(@RequestBody() UserEntity userData) {
    var user = this.userRepository.findByUsername(userData.getUsername());

    if(user != null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "User already exists"
      );
    }

    var hashedPassword = BCrypt
      .withDefaults()
      .hashToString(12, userData.getPassword().toCharArray());

    userData.setPassword(hashedPassword);

    var savedUser = this.userRepository.save(userData);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }
}
