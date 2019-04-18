package my.shop.common.fliPusto.services;

import my.shop.common.fliPusto.origins.*;
import my.shop.common.fliPusto.repositories.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public BookService(BookRepository bookRepository, TagRepository tagRepository, ChapterRepository chapterRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.chapterRepository = chapterRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void addBook(Book book, Set<String> tagNames) {

        for (String tag : tagNames) {
            Tag findedTag;

            if ((findedTag = tagRepository.findByName(tag)) != null) {
                book.addTag(findedTag);
            } else {
                findedTag = new Tag(tag);
                book.addTag(findedTag);
            }

            tagRepository.save(findedTag);
        }
        bookRepository.save(book);

    }

    @Transactional
    public Book redactBook(@PathVariable Long id, @RequestPart("image") MultipartFile file,
                           @Valid Book book) throws IOException {

        Book originalBook = findBookById(id, true, false);

        if (originalBook == null) {
            return null;
        }

        if(file != null && !file.getOriginalFilename().equals("") && !originalBook.getHeaderImage().equals(file.getOriginalFilename())) {
            if (originalBook.getHeaderImage() != null) {
                Utils.removeFile(originalBook.getHeaderImage(), "/images");
            }
            originalBook.setHeaderImage(Utils.createFile(file, "/images"));
        }

        originalBook.setGenre(book.getGenre());
        originalBook.setTitle(book.getTitle());
        originalBook.setDescription(book.getDescription());

        return originalBook;
    }

    @Transactional(readOnly = true)
    public boolean getChapterByIdFromBook(Long bookId, Long chapterId, Model model) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            book.getChapters().size();
            List<Chapter> chapters = book.getChapters();

            for (int i = 0; i < chapters.size(); i++) {
                if (chapters.get(i).getId().equals(chapterId)) {
                    if (i > 0) {
                        model.addAttribute("previous", chapters.get(i - 1));
                    }

                    Hibernate.initialize(chapters.get(i).getBook());
                    model.addAttribute("current", chapters.get(i));

                    if (i < chapters.size() - 1) {
                        model.addAttribute("next", chapters.get(i + 1));
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public User initBookById(Long id, Model model) {
        Optional<Book> bookOp = bookRepository.findById(id);
        if (bookOp.isPresent()) {
            Book book = bookOp.get();

            book.getChapters().size();

            Set<Tag> bookTags = book.getBookTags();
            bookTags.size();

            List<Comment> comments = book.getComments();
            comments.size();

            if (model != null) {
                model.addAttribute("book", book);
                model.addAttribute("bookTags", bookTags);
                model.addAttribute("comments", comments);
                model.addAttribute("votedCount", book.getVoted().size());
            }
            return book.getAuthor();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public void initAllBooks(Model model, Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);

        for (Book book : books) {
            book.getBookTags().size();
        }

        model.addAttribute("booksPage", books);
    }

    @Transactional(readOnly = true)
    public void showBooksByTag(String tag, Pageable pageable, Model model) {
        Tag selectedTag = tagRepository.findByName(tag);
        Page<Book> books = bookRepository.findAllByBookTagsContaining(selectedTag, pageable);
        initTags(books);

        model.addAttribute("booksPage", books);
    }

    private void initTags(Page<Book> books) {
        for (Book book : books) {
            book.getBookTags().size();
        }
    }

    @Transactional(readOnly = true)
    public boolean search(String name, String genre, Pageable pageable, Model model) {
        Page<Book> bookPage;

        Boolean nameEmpty = name == null || name.equals("");
        Boolean genreEmpty = genre == null || genre.equals("");

        if (nameEmpty && genreEmpty) {
            return true;
        } else if (nameEmpty) {
            bookPage = bookRepository.findByGenre(genre, pageable);
        } else if (genreEmpty) {
            bookPage = bookRepository.findAllByTitleContaining(name, pageable);
        } else {
            bookPage = bookRepository.findByTitleAndGenre(name, genre, pageable);
        }

        initTags(bookPage);

        model.addAttribute("booksPage", bookPage);
        return false;
    }

    @Transactional
    public void addChapterToBook(Long bookId, Chapter chapter, User user) {
        Optional<Book> findedBook = bookRepository.findById(bookId);
        if (findedBook.isPresent()) {
            Book book = findedBook.get();

            if (!book.getAuthor().getId().equals(user.getId())) {
                throw new AccessDeniedException("Доступ к этой странице имеет только создатель книги.");
            }

            book.addChapter(chapter);
            chapter.setBook(book);

            chapterRepository.save(chapter);
            bookRepository.save(book);
        }
    }

    @Transactional
    public boolean rateBook(Long bookId, Integer rating, User user) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            Set<User> voted = book.getVoted();
            voted.size();

            boolean alreadyVoted = false;
            for (User votedUser : voted) {
                if (votedUser.getId().equals(user.getId())) {
                    alreadyVoted = true;
                    break;
                }
            }

            if (!alreadyVoted) {
                book.setRating(book.getRating() + rating);
                voted.add(user);

                bookRepository.save(book);
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Transactional
    public void removeBook(Long id, User userCheck) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (!book.getAuthor().getId().equals(userCheck.getId()) && !userCheck.getRoles().contains(Role.valueOf("MODERATOR"))) {
                throw new AccessDeniedException("Доступ к этому дествию имеет только создатель.");
            }


            List<User> users = book.getUsers();
            for (User user : users) {
                user.getSpectatingBooks().size();
                user.getSpectatingBooks().remove(book.getId());
                user.getSpectatingBooksIndexes().size();
                user.getSpectatingBooksIndexes().remove(book.getId());

                userRepository.save(user);
            }

            bookRepository.delete(book);
        }
    }

    private void chapterInit(Page<Book> bookPage) {
        for (Book book : bookPage) {
            book.getChapters().size();
        }
    }

    @Transactional(readOnly = true)
    public Page<Book> loadSpectatingBooks(Set<Long> keySet, boolean withChapterInit, Pageable pageable) {
        Page<Book> books = bookRepository.findAllByIdIn(keySet, pageable);

        if (withChapterInit) {
            chapterInit(books);
        }

        return books;
    }

    @Transactional
    public void removeChapterFromBook(Long id, Long chapterId, User user, boolean notModerator) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (!book.getAuthor().getId().equals(user.getId()) && notModerator) {
                throw new AccessDeniedException("Доступ к этому действию имеет только создатель");
            }

            List<Chapter> chapters = book.getChapters();
            Chapter selectedChap = null;
            for (Chapter chapter : chapters) {
                if (chapter.getId().equals(chapterId)) {
                    selectedChap = chapter;
                    break;
                }

            }
            if (selectedChap != null) {
                chapterRepository.delete(selectedChap);
                chapters.remove(selectedChap);
            }

            bookRepository.save(book);
        }
    }

    @Transactional
    public void removeComment(Long bookId, Long commentId, User user, boolean notModerator) {
        Optional<Comment> byId = commentRepository.findById(commentId);

        if (byId.isPresent()) {
            Comment comment = byId.get();

            if (!comment.getAuthor().getId().equals(user.getId()) && notModerator) {
                throw new AccessDeniedException("Это действие доступно только создателю");
            }

            commentRepository.delete(comment);
        }
    }

    @Transactional(readOnly = true)
    public Book findBookById(Long id, boolean bookRelatesInit, boolean userRelatesInit) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (bookRelatesInit) {
                List<Chapter> chapters = book.getChapters();
                chapters.size();

                Set<Tag> tags = book.getBookTags();
                tags.size();

                List<Comment> comments = book.getComments();
                comments.size();
            }
            if (userRelatesInit) {
                Set<User> voted = book.getVoted();
                voted.size();

                List<User> users = book.getUsers();
                users.size();
            }

            return book;
        }

        return null;
    }
}
