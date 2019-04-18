package my.shop.common.fliPusto.controller;

import my.shop.common.fliPusto.origins.Role;
import my.shop.common.fliPusto.origins.User;
import my.shop.common.fliPusto.repositories.UserRepository;
import my.shop.common.fliPusto.services.Utils;
import my.shop.common.fliPusto.services.intefaces.UserDetailsServiceImplInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final UserDetailsServiceImplInterface userService;

    @Autowired
    public AdminController(UserRepository userRepository, UserDetailsServiceImplInterface userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/user")
    public String usersTableView(Model model) {
        model.addAttribute("users", userRepository.findAllByOrderByIdAsc());
        return "userList";
    }

    @GetMapping("/user/{userId}")
    public String userInfoView(@PathVariable Long userId, Model model) {
        User user = userService.initForProfiles(userId, model);
        model.addAttribute("roles", Role.values());
        model.addAttribute("selectedUser", user);
        return "userEdit";
    }

    @PostMapping("/user/{userId}")
    public String editPrivateProfile(
            @RequestParam String username, @RequestParam String password,
            @RequestPart(required = false) MultipartFile profilePic, @PathVariable Long userId,
            Model model, @AuthenticationPrincipal User user, @RequestParam Map<String, String> form
            ) throws IOException {
        boolean error = false;
        error = Utils.checkUserPassword(error, username, password, model);


        if (error) {
            return "userEdit";
        }


        User userResult = userService.redactUser(username, password, profilePic, userId, form);
        if(user.getId().equals(userId)) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userResult, userResult.getPassword(), userResult.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        return "redirect:/admin/user/" + userId;
    }
}
