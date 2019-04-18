package my.shop.common.fliPusto.origins;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "У книги должно быть название.")
    private String title;

    @NotBlank(message = "У книги должен быть жанр.")
    private String genre;

    private String headerImage;

    private Double rating = (double) 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_voted", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "voted_id"))
    private Set<User> voted = new HashSet<User>();

    @NotBlank(message = "У книги должно быть описание")
    @Size(min = 150, message = "Описание должно содержать по меньшей мере 150 символов")
    @Column(columnDefinition = "text")
    private String description;


    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE})
    @OrderBy("id asc")
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "book_tag", joinColumns = @JoinColumn(name="book_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> bookTags = new HashSet<Tag>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "targetBook", fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<Comment> comments = new ArrayList<Comment>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "books_spectators", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<User>();

    public Double resolveRating() {
        return voted.size() == 0? 0.0 : rating/voted.size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Tag> getBookTags() {
        return bookTags;
    }

    public void setBookTags(Set<Tag> bookTags) {
        this.bookTags = bookTags;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book() {
    }

    public Book(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    public void addTag(Tag tag) {
        bookTags.add(tag);
    }

    public void addTags(Set<Tag> tags) {
        for (Tag tag : tags) {
            addTag(tag);
        }
    }

    public User getAuthor() {
        return author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<User> getVoted() {
        return voted;
    }

    public void setVoted(Set<User> voted) {
        this.voted = voted;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Integer findChapterIndexById(Long chapterId) {
        for (int i = 0; i < chapters.size(); i++) {
            if (chapters.get(i).getId().equals(chapterId)) {
                return i;
            }
        }
        return -1;
    }
}
