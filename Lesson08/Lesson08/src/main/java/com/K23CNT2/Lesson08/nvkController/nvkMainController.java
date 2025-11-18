package com.K23CNT2.Lesson08.nvkController;

import com.K23CNT2.Lesson08.nvkEntity.nvkAuthor;
import com.K23CNT2.Lesson08.nvkEntity.nvkBook;
import com.K23CNT2.Lesson08.nvkService.nvkAuthorService;
import com.K23CNT2.Lesson08.nvkService.nvkBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

@Controller
public class nvkMainController {
    @Autowired private nvkBookService bookService;
    @Autowired private nvkAuthorService authorService;

    // ============ PHẦN QUẢN LÝ TÁC GIẢ (AUTHOR) ============
    @GetMapping("/nvkAuthor")
    public String listAuthor(Model model) {
        model.addAttribute("nvkListAuthor", authorService.nvkGetAll());
        return "nvkAuthor-list";
    }

    @GetMapping("/nvkAuthor/create")
    public String createAuthor(Model model) {
        model.addAttribute("nvkAuthor", new nvkAuthor());
        return "nvkAuthor-create";
    }

    @PostMapping("/nvkAuthor/save")
    public String saveAuthor(@ModelAttribute nvkAuthor nvkAuthor,
                             @RequestParam("nvkImageFile") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Tạo tên file mới để tránh trùng lặp
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path uploadPath = Paths.get("src/main/resources/static/images/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                nvkAuthor.setNvkImgUrl("/images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        authorService.nvkSave(nvkAuthor);
        return "redirect:/nvkAuthor";
    }

    @GetMapping("/nvkAuthor/edit/{id}")
    public String editAuthor(@PathVariable Long id, Model model) {
        nvkAuthor author = authorService.nvkGetById(id);
        model.addAttribute("nvkAuthor", author);
        return "nvkAuthor-create";
    }

    // ============ PHẦN QUẢN LÝ SÁCH (BOOK) ============
    @GetMapping("/nvkBook")
    public String listBook(Model model) {
        model.addAttribute("nvkListBook", bookService.nvkGetAll());
        return "nvkBook-list";
    }

    @GetMapping("/nvkBook/create")
    public String createBook(Model model) {
        model.addAttribute("nvkBook", new nvkBook());
        model.addAttribute("nvkListAuthor", authorService.nvkGetAll());
        return "nvkBook-create";
    }

    @PostMapping("/nvkBook/save")
    public String saveBook(@ModelAttribute nvkBook nvkBook,
                           @RequestParam("nvkAuthorIds") List<Long> authorIds,
                           @RequestParam("nvkImageFile") MultipartFile file) {


        if (!file.isEmpty()) {
            try {

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path uploadPath = Paths.get("src/main/resources/static/images/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                nvkBook.setNvkImgUrl("/images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2. Xử lý danh sách tác giả
        if (authorIds != null) {
            List<nvkAuthor> selectedAuthors = authorService.nvkFindAllByIds(authorIds);
            nvkBook.setNvkAuthors(selectedAuthors);
        }

        // 3. Lưu vào Database
        bookService.nvkSave(nvkBook);
        return "redirect:/nvkBook";
    }

    @GetMapping("/nvkBook/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        nvkBook book = bookService.nvkGetById(id);
        model.addAttribute("nvkBook", book);
        model.addAttribute("nvkListAuthor", authorService.nvkGetAll());
        return "nvkBook-create";
    }
}