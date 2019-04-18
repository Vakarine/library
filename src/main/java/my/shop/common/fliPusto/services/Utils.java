package my.shop.common.fliPusto.services;

import my.shop.common.fliPusto.origins.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class Utils {

    private static String upload;

    public static void createUserImage(@RequestPart("image") MultipartFile file, User user, String path) throws IOException {
        String UName = createFile(file, path);
    }

    public static void removeFile(String headerImage, String path) {
        String newUpload = upload + "/" + path + "/" + headerImage;
        File file = new File(newUpload);

        if (file.exists()) {
            file.delete();
        }
    }

    public static String createFile(@RequestPart("image") MultipartFile file, String path) throws IOException {
        String newUpload = upload + "/" + path;
        File fileDir = new File(newUpload);

        if (!fileDir.exists()) {
            fileDir.mkdir();
        }

        String UName = UUID.randomUUID().toString() + file.getOriginalFilename();
        file.transferTo(new File(newUpload + "/" + UName));
        return UName;
    }

    public static boolean checkUserPassword(Boolean error, String username, String password, Model model) {
        if (username == null) {
            error = true;
            model.addAttribute("usernameError", "Никнейм не может быть пустой");
        }
        if (password == null) {
            error = true;
            model.addAttribute("passwordError", "Пароль не может быть пустой");
        }
        return error;
    }

    public static Map<String, String> parseBindingResult(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );

        return bindingResult.getFieldErrors().stream().collect(collector);
    }


    @Value("${uploadBook.path}")
    public void setUpload(String upload) {
        Utils.upload = upload;
    }
}
