package my.shop.common.fliPusto.repositories;

import my.shop.common.fliPusto.origins.Book;
import my.shop.common.fliPusto.origins.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
    Page<Book> findAllByBookTagsContaining(Tag tag, Pageable pageable);
    Page<Book> findAllByTitleContaining(String title, Pageable pageable);
    Page<Book> findByGenre(String genre, Pageable pageable);
    Page<Book> findByTitleAndGenre(String title, String genre, Pageable pageable);
    Page<Book> findAllByIdIn(Set<Long> id, Pageable pageable);
}
