package social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import social.entity.User;
import social.repository.UserRepository;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }


}