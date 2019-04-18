package my.shop.common.fliPusto.services;

import my.shop.common.fliPusto.origins.Book;
import my.shop.common.fliPusto.origins.Comment;
import my.shop.common.fliPusto.origins.User;
import my.shop.common.fliPusto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(BookRepository bookRepository, TagRepository tagRepository, ChapterRepository chapterRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.chapterRepository = chapterRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void addCommentToBook(Long bookId, Model model, Long userId, Comment comment) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        User user = userRepository.findById(userId).get();

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            comment.setTargetBook(book);
            comment.setAuthor(user);

            commentRepository.save(comment);
        }
    }

    @Transactional
    public void addAnswerToComment(Model model, User user, Comment comment, Long commentId) {
        Optional<Comment> optional = commentRepository.findById(commentId);
        if (optional.isPresent()) {
            comment.setTargetComment(optional.get());
            comment.setAuthor(user);

            commentRepository.save(comment);
        }
    }

    @Transactional
    public Comment findCommentById(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Comment updateComment(Long commentId, Comment comment) {
        Comment commentOriginal = findCommentById(commentId);

        if (commentOriginal == null) {
            return null;
        }

        commentOriginal.setContent(comment.getContent());

        commentRepository.save(commentOriginal);
        return commentOriginal;
    }
}
