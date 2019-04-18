package my.shop.common.fliPusto.controller;

import my.shop.common.fliPusto.origins.*;
import my.shop.common.fliPusto.repositories.BookRepository;
import my.shop.common.fliPusto.services.BookService;
import my.shop.common.fliPusto.services.ChapterService;
import my.shop.common.fliPusto.services.CommentService;
import my.shop.common.fliPusto.services.Utils;
import my.shop.common.fliPusto.services.intefaces.UserDetailsServiceImplInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final CommentService commentService;
    private final UserDetailsServiceImplInterface userService;
    private final ChapterService chapterService;


    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository, CommentService commentService, UserDetailsServiceImplInterface userService, ChapterService chapterService) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.commentService = commentService;
        this.userService = userService;
        this.chapterService = chapterService;
    }

    @GetMapping({"/", ""})
    public String showBooks(Model model, @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        bookService.initAllBooks(model, pageable);

        return "bookList";
    }

    @GetMapping("/add")
    public String createView(Model model) {
        return "createBookPage";
    }

    @PostMapping("/add")
    public String addBook(
            @RequestParam("tags") Set<String> tags, @RequestPart("image") MultipartFile file, @AuthenticationPrincipal User author,
            @Valid Book book, BindingResult bindingResult, Model model
    ) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = Utils.parseBindingResult(bindingResult);
            model.addAllAttributes(errorsMap);
            return "createBookPage";
        }

        book.setAuthor(author);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            book.setHeaderImage(Utils.createFile(file, "images"));
        }
        bookService.addBook(book, tags);

        return "redirect:/book";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        User author = bookService.initBookById(id, model);

        if (author != null) {
            model.addAttribute("author", author);
            model.addAttribute("isAuthor", author.getId().equals(user.getId()));
//            model.addAttribute("userId", user.getId());

            return "bookPage";
        } else {
            return "redirect:/book";
        }
    }

    @GetMapping("/{id}/remove")
    public String removeBook(@PathVariable Long id, @AuthenticationPrincipal User user) {
        bookService.removeBook(id, user);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editBookShow(@AuthenticationPrincipal User user,@PathVariable Long id, Model model) {
        if (checkBookAuthor(user, id)) return "redirect:/book";

        if (bookService.initBookById(id, model) == null) {
            return "redirect:/book";
        }
        return "createBookPage";
    }

    @PostMapping("/{id}/edit")
    public String editBook(@PathVariable Long id, @RequestParam("tags") Set<String> tags, @RequestPart("image") MultipartFile file, @AuthenticationPrincipal User user,
                           @Valid Book book, BindingResult bindingResult, Model model) throws IOException {
        checkBookAuthor(user, id);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = Utils.parseBindingResult(bindingResult);
            model.addAllAttributes(errorsMap);
            return editBookShow(user, id, model);
        }

        Book originalBook = bookService.redactBook(id, file, book);

        if (originalBook == null) {
            return "redirect:/book";
        }

        bookService.addBook(originalBook, tags);

        return showBook(id, user, model);    //TODO : editBook
    }

    @GetMapping("/{bookId}/rate")
    public String rateBook(@PathVariable Long bookId, @RequestParam Integer rating, @AuthenticationPrincipal User user, Model model) {
        if (rating > 5) rating = 5;
        else if (rating < 0) rating = 0;

        if (!bookService.rateBook(bookId, rating, user)) {
            model.addAttribute("ratingError", "Вы уже голосовали");
            return showBook(bookId, user, model);
        }

        return "redirect:/book/" + bookId;
    }

    @PostMapping("/{id}/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult, @PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        commentService.addCommentToBook(id, model, user.getId(), comment);

        return "redirect:/book/" + id;
    }

    @PostMapping("/{id}/{commentId}/answer")
    public String addCommentAnswer(@Valid Comment comment, BindingResult bindingResult, @PathVariable Long id, @PathVariable Long commentId, @AuthenticationPrincipal User user, Model model) {

        commentService.addAnswerToComment(model, user, comment, commentId);


        return "redirect:/book/" + id;
    }

    @GetMapping("/{id}/{commentId}/cm_edit")
    public String editCommentView(@PathVariable Long id, @PathVariable Long commentId, @AuthenticationPrincipal User user, Model model) {
        Comment comment = commentService.findCommentById(commentId);

        if (comment == null || !comment.getAuthor().getId().equals(user.getId()) && isNotModerator(user)) {
            return "redirect:/book";
        }

        model.addAttribute("comment", comment);

        return showBook(id, user, model);
    }

    //TODO
    @PostMapping("/{id}/{commentId}/cm_edit")
    public String editComment(@Valid Comment comment, BindingResult bindingResult, @PathVariable Long id, @PathVariable Long commentId, @AuthenticationPrincipal User user, Model model) {
        Comment updateComment = commentService.updateComment(commentId, comment);
        if (updateComment == null || !updateComment.getAuthor().getId().equals(user.getId()) && isNotModerator(user)) {
            return "redirect:/book";
        }

        return "redirect:/book/" + id;
    }

    @GetMapping("/{bookId}/{commentId}/cm_remove")
    public String commentRemove(@PathVariable Long bookId, @PathVariable Long commentId, @AuthenticationPrincipal User user) {
        bookService.removeComment(bookId, commentId, user, isNotModerator(user));

        return "redirect:/book/" + bookId;
    }

    @GetMapping("/{bookId}/{chapterId}")
    public String showChapter(@PathVariable Long bookId, @PathVariable Long chapterId, @AuthenticationPrincipal User user, Model model) {
        userService.setBookmark(bookId, user, chapterId);
        if (bookService.getChapterByIdFromBook(bookId, chapterId, model)) {
            return "chapterPage";
        }

        return "redirect:/book/" + bookId;
    }
    @GetMapping("/addChapter/{bookId}")
    public String addChapterView(@PathVariable Long bookId, @AuthenticationPrincipal User user, Model model) throws AccessDeniedException {


        Optional<Book> findedBook = bookRepository.findById(bookId);
        if (findedBook.isPresent()) {
            if (!findedBook.get().getAuthor().getId().equals(user.getId())) {
                throw new AccessDeniedException("Доступ к этой странице имеет только создатель книги.");
            }

            model.addAttribute("bookId", findedBook.get().getId());
            return "createChapter";
        } else {
            return "redirect:/book";
        }
    }

    @PostMapping("/addChapter/{bookId}")
    public String addChapterToBook(
            @PathVariable Long bookId, @Valid Chapter chapter, @AuthenticationPrincipal User user,
            BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = Utils.parseBindingResult(bindingResult);
            model.addAllAttributes(errors);
            model.addAttribute("bookId", bookId);
            return "createChapter";
        }

        bookService.addChapterToBook(bookId, chapter, user);
        return "redirect:/book/" + bookId;
    }

    @GetMapping("/{id}/{chapterId}/ch_remove")
    public String removeChapter(@PathVariable Long id, @PathVariable Long chapterId, @AuthenticationPrincipal User user) {
        bookService.removeChapterFromBook(id, chapterId, user, isNotModerator(user));
        return "redirect:/";
    }

    @GetMapping("/{id}/{chapterId}/ch_edit")
    public String editChapterView(@PathVariable Long id, @PathVariable Long chapterId, @AuthenticationPrincipal User user, Model model) {
        checkBookAuthor(user, id);

        Chapter chapter = chapterService.findChapterById(chapterId, false);

        if (chapter == null) {
            return "redirect:/book";
        }

        model.addAttribute("chapter", chapter);
        model.addAttribute("bookId", id);

        return "createChapter";
    }


    @PostMapping("/{id}/{chapterId}/ch_edit")
    public String editChapter(@Valid Chapter chapter, BindingResult bindingResult, @PathVariable Long id, @PathVariable Long chapterId, @AuthenticationPrincipal User user, Model model) {
        checkBookAuthor(user, id);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = Utils.parseBindingResult(bindingResult);
            model.addAllAttributes(errors);
            return editChapterView(id, chapterId, user, model);
        }

        if (!chapterService.updateChapter(chapter, chapterId)) {
            return "redirect:/book";
        }
        return "redirect:/book/" + id;
    }

    private boolean isNotModerator(@AuthenticationPrincipal User user) {
        return !user.getRoles().contains(Role.valueOf("MODERATOR"));
    }

    private boolean checkBookAuthor(@AuthenticationPrincipal User user, @PathVariable Long id) {
        Book book = bookService.findBookById(id, false, false);

        if (book == null) {
            return true;
        }

        if (!book.getAuthor().getId().equals(user.getId()) && isNotModerator(user)) {
            throw new AccessDeniedException("У вас нет доступа к этому действию");
        }
        return false;
    }
}
