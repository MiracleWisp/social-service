package social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import social.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}