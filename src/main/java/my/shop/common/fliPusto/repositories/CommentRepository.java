package my.shop.common.fliPusto.repositories;

import my.shop.common.fliPusto.origins.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
