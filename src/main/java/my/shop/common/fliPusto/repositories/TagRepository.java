package my.shop.common.fliPusto.repositories;

import my.shop.common.fliPusto.origins.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    Page<Tag> findAll(Pageable pageable);
}
