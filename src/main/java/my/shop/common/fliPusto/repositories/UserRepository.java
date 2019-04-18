package my.shop.common.fliPusto.repositories;

import my.shop.common.fliPusto.origins.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findAllByOrderByIdAsc();
}
