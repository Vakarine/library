package my.shop.common.fliPusto.controller;

import my.shop.common.fliPusto.origins.Book;
import my.shop.common.fliPusto.origins.Tag;
import my.shop.common.fliPusto.repositories.BookRepository;
import my.shop.common.fliPusto.repositories.TagRepository;
import my.shop.common.fliPusto.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private UserDetailsService userDetailsService;

    @Autowired
    public MainController(BookService bookService, BookRepository bookRepository, TagRepository tagRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(value = "/")
    public String showMainPage(Model model) {
        return "main";
    }

    // Need for security login POST method
    @PostMapping(value = "/")
    public String mainPagePost(Model model) {
        return "redirect:/";
    }

    @GetMapping(value = "/clear")
    public String clear() {
        tagRepository.deleteAll();
        return "redirect:/book";
    }
    @GetMapping(value = "/clearBook")
    public String clearBook() {
        bookRepository.deleteAll();
        return "redirect:/book";
    }

    @GetMapping(value = "/test/user")
    public String testUser() {
        return "redirect:/";
    }

    @GetMapping(value = "/testTag")
    public String test() {
        Book book = new Book();
        book.setTitle("asda");
        Tag tag = new Tag("asda");
        book.addTag(tag);
        tagRepository.save(tag);
        bookRepository.save(book);
        return "redirect:/book";
    }

    @GetMapping(value = "/search")
    public String showFoundedBooks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String genre,
            Pageable pageable,
            Model model) {

        if (bookService.search(name, genre, pageable, model)) return "redirect:/book";


        return "bookList";
    }


//    TODO : Последние добавленные
//    TODO : Вывод книг страницами
}
