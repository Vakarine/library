package my.shop.common.fliPusto.controller;

import my.shop.common.fliPusto.origins.User;
import my.shop.common.fliPusto.services.Utils;
import my.shop.common.fliPusto.services.intefaces.UserDetailsServiceImplInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserDetailsServiceImplInterface userService;

    @Autowired
    public UserController(UserDetailsServiceImplInterface userService) {
        this.userService = userService;
    }

    @GetMapping("/library")
    public String showLibrary(@AuthenticationPrincipal User user, @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        userService.showSpectatingBooks(user.getId(), pageable, model);

        return "library";
    }

    @GetMapping("/addBookmark/{bookId}")
    public String addBookmark(@AuthenticationPrincipal User user, @PathVariable Long bookId) {
        userService.createBookmark(user.getId(), bookId);
        return "redirect:/book/" + bookId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showPrivateProfile(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "false") boolean enabled, Model model) {
        if (userService.initForProfiles(user.getId(), model) != null) {
            model.addAttribute("enabled", enabled);
            return "privateProfile";
        } else {
            return "redirect:/book";
        }
    }

    @PostMapping("/profile")
    public String editPrivateProfile(
            @RequestParam String username, @RequestParam String password,
            @RequestPart(required = false) MultipartFile profilePic, @AuthenticationPrincipal User user,
            Model model
    ) throws IOException {
        boolean error = false;
        error = Utils.checkUserPassword(error, username, password, model);

        if (error) {
            return showPrivateProfile(user, false, model);
        }

        user = userService.redactUser(username, password, profilePic, user.getId(), null);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        return "redirect:/user/profile";
    }

    @GetMapping("/profile/{userId}")
    public String showPublicProfile(@PathVariable Long userId, Model model) { //Проверить ид по репозиторию и так
        if (userService.initForProfiles(userId, model) != null) {
            return "publicProfile";
        } else {
            return "redirect:/book";
        }
    }
}
