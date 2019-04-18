package my.shop.common.fliPusto.origins;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String profilePicture;

    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "spectatingBooks", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "book_id")
    @Column(name = "lastChapter")
    private Map<Long, Long> spectatingBooks = new HashMap<Long, Long>();

    //Faster than search indexes for chapters in book each request
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "spectatingBooksIndexes", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "book_id")
    @Column(name = "lastChapterInd")
    private Map<Long, Integer> spectatingBooksIndexes = new HashMap<Long, Integer>();

    private Boolean enabled = true;
    private Boolean credetialsExpired = false;
    private Boolean locked = false;
    private Boolean accountExpired = false;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")})
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credetialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<Long, Long> getSpectatingBooks() {
        return spectatingBooks;
    }

    public void setSpectatingBooks(Map<Long, Long> spectatingBooks) {
        this.spectatingBooks = spectatingBooks;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setCredetialsExpired(Boolean credetialsExpired) {
        this.credetialsExpired = credetialsExpired;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Map<Long, Integer> getSpectatingBooksIndexes() {
        return spectatingBooksIndexes;
    }

    public void setSpectatingBooksIndexes(Map<Long, Integer> spectatingBooksIndexes) {
        this.spectatingBooksIndexes = spectatingBooksIndexes;
    }
}
