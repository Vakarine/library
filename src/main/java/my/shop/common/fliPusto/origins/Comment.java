package my.shop.common.fliPusto.origins;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "book_id")
    private Book targetBook;

    @NotBlank(message = "Комментарий не может быть пустой")
    private String content;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Comment targetComment;


    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "targetComment")
    private List<Comment> answers;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Comment> answers) {
        this.answers = answers;
    }

    public Book getTargetBook() {
        return targetBook;
    }

    public void setTargetBook(Book targetBook) {
        this.targetBook = targetBook;
    }

    public Comment getTargetComment() {
        return targetComment;
    }

    public void setTargetComment(Comment targetComment) {
        this.targetComment = targetComment;
    }
}
