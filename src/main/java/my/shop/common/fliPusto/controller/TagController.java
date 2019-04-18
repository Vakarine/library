package my.shop.common.fliPusto.controller;

import my.shop.common.fliPusto.repositories.TagRepository;
import my.shop.common.fliPusto.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tag")
public class TagController {
    private final TagRepository tagRepository;
    private final BookService bookService;

    @Autowired
    public TagController(TagRepository tagRepository, BookService bookService) {
        this.tagRepository = tagRepository;
        this.bookService = bookService;
    }

    @GetMapping({"/", ""})
    public String showTags(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("tagsPage", tagRepository.findAll(pageable));

        return "allTags";
    }

    @GetMapping("/{tag}")
    public String showTags(@PathVariable String tag, @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        bookService.showBooksByTag(tag, pageable, model);

        return "bookList";
    }
}
