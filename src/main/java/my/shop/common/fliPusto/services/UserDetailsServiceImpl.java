
package my.shop.common.fliPusto.services;

import my.shop.common.fliPusto.origins.Book;
import my.shop.common.fliPusto.origins.Comment;
import my.shop.common.fliPusto.origins.Role;
import my.shop.common.fliPusto.origins.User;
import my.shop.common.fliPusto.repositories.BookRepository;
import my.shop.common.fliPusto.repositories.UserRepository;
import my.shop.common.fliPusto.services.intefaces.UserDetailsServiceImplInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsServiceImplInterface {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, BookRepository bookRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s);
    }

    @Transactional(readOnly = true)
    public User initForProfiles(Long id, Model model) throws UsernameNotFoundException {
        Optional<User> userOp = userRepository.findById(id);

        if (userOp.isPresent()) {
            User user = userOp.get();

            List<Comment> comments = user.getComments();
            comments.size();
            List<Book> books = user.getBooks();
            books.size();

            model.addAttribute("books", books);
            model.addAttribute("comments", comments);


            return user;
        }
        return null;
    }

    @Transactional
    public boolean createAndAddUser(String username, String password, MultipartFile profilePic) throws IOException {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        User newbie = new User(username, passwordEncoder.encode(password));
        newbie.setRoles(Collections.singleton(Role.USER));


        if (!profilePic.isEmpty()) {
            newbie.setProfilePicture(Utils.createFile(profilePic, "images/profiles"));
        }

        userRepository.save(newbie);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(newbie, newbie.getPassword(), newbie.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        return true;
    }

    @Transactional
    public User redactUser(String username, String password, MultipartFile profilePic, Long userId, Map<String, String> form) throws IOException {
        User user = userRepository.findById(userId).get();

        if (userRepository.findByUsername(username) != null && !username.equals(user.getUsername())) {
            return null;
        }

        if (form != null) {
            Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

            Set<Role> userRoles = new HashSet<Role>();

            Set<String> keySet = form.keySet();
            for (String key : keySet) {
                if (roles.contains(key)) {
                    userRoles.add(Role.valueOf(key));
                }
            }

            user.setRoles(userRoles);
        }

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        if (!profilePic.isEmpty() && !user.getProfilePicture().equals(profilePic.getOriginalFilename())) {
            if (user.getProfilePicture() != null) {
                Utils.removeFile(user.getProfilePicture(), "/images/profiles");
            }
            user.setProfilePicture(Utils.createFile(profilePic, "images/profiles"));
        }


        userRepository.save(user);
        return user;
    }

    @Transactional
    public Boolean createBookmark(Long userId, Long bookId) {
        Optional<Book> bookOp = bookRepository.findById(bookId);
        Optional<User> userOp = userRepository.findById(userId);

        if (bookOp.isPresent() && userOp.isPresent()) {
            User user = userOp.get();
            Book book = bookOp.get();

            user.getSpectatingBooks().size();
            user.getSpectatingBooksIndexes().size();

            user.getSpectatingBooks().put(book.getId(), Integer.toUnsignedLong(0));
            user.getSpectatingBooksIndexes().put(book.getId(), 0);

            book.getUsers().size();
            book.getUsers().add(user);

            bookRepository.save(book);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public void showSpectatingBooks(Long userId, Pageable pageable, Model model) {
        User user = userRepository.findById(userId).get();

        Map<Long, Integer> bookId = user.getSpectatingBooksIndexes();
        bookId.size();
        //ChapterInitialization need for count of chapters in book
        Page<Book> books = bookService.loadSpectatingBooks(bookId.keySet(), true, pageable);

        Map<Book, Integer> spectatingBooks = new HashMap<Book, Integer>();
        for (Book book : books) {
            spectatingBooks.put(book, bookId.get(book.getId()));
        }

        model.addAttribute("spectatingBooks", spectatingBooks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getCreatedBooks(User user) {
        List<Book> books = userRepository.findById(user.getId()).get().getBooks();
        books.size();
        return books;
    }

    @Transactional
    public void setBookmark(Long bookId, User principle, Long chapterId) {
        User user = userRepository.findById(principle.getId()).get();
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent() && user.getSpectatingBooks().keySet().contains(bookId)) {
            Book book = bookOptional.get();
            book.getChapters().size();

            user.getSpectatingBooks().size();
            user.getSpectatingBooks().put(bookId, chapterId);

            user.getSpectatingBooksIndexes().size();
            user.getSpectatingBooksIndexes().put(bookId, book.findChapterIndexById(chapterId) + 1);

            userRepository.save(user);
        }
    }
}

