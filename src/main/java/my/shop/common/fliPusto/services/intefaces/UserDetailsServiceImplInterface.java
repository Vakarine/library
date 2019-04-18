package my.shop.common.fliPusto.services.intefaces;

import my.shop.common.fliPusto.origins.Book;
import my.shop.common.fliPusto.origins.User;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserDetailsServiceImplInterface {

    boolean createAndAddUser(String username, String password, MultipartFile profilePic) throws IOException;

    Boolean createBookmark(Long user, Long bookId);

    User initForProfiles(Long id, Model model);

    void showSpectatingBooks(Long userId, Pageable pageable, Model model);

    List<Book> getCreatedBooks(User user);

    User redactUser(String username, String password, MultipartFile profilePic, Long userId, Map<String, String> roles) throws IOException;

    void setBookmark(Long bookId, User user, Long chapterId);
}

