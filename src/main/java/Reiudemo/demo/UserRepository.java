package Reiudemo.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Boot sẽ tự động viết code tìm kiếm cho bạn
    Optional<User> findByUsername(String username);
}
