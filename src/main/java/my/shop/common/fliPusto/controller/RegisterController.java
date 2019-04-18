package my.shop.common.fliPusto.controller;

import my.shop.common.fliPusto.services.intefaces.UserDetailsServiceImplInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/registration")
public class RegisterController {
    private final UserDetailsServiceImplInterface userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserDetailsServiceImplInterface userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"", "/"})
    public String showRegistrationForm() {
        return "registrationForm";
    }

    @PostMapping({"", "/"})
    public String createUser(
            @RequestParam String username, @RequestParam String password,
            @RequestPart MultipartFile profilePic, Model model
    ) throws IOException {
        boolean error = false;

        if (username == null) {
            model.addAttribute("errorName", "Никнейм не должен быть пустой.");
            error = true;
        }
        if (password == null) {
            model.addAttribute("errorPassword", "Пароль не должен быть пустой.");
            error = true;
        }

        if (error) {
            return "registrationForm";
        }

        if (!userService.createAndAddUser(username, password, profilePic)) {
            return "redirect:/registration";
        }
        return "redirect:/book";
    }
}
