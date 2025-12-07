package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/customer")
public class nvkCustomerController {

    @Autowired
    private nvkCustomerRepository customerRepository;

    @GetMapping("")
    public String listCustomer(Model model) {
        List<nvkCustomer> customers = customerRepository.findAll();
        model.addAttribute("nvkCustomers", customers);
        return "admin/customer/list";
    }

    @GetMapping("/create")
    public String createCustomer(Model model) {
        model.addAttribute("nvkCustomer", new nvkCustomer());
        return "admin/customer/form";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") Long id, Model model) {
        nvkCustomer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            model.addAttribute("nvkCustomer", customer);
            return "admin/customer/form";
        }
        return "redirect:/nvkAdmin/customer";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("nvkCustomer") nvkCustomer nvkCustomer,
                               @RequestParam("nvkImageFile") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }

                nvkCustomer.setNvkAvatar("/images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        customerRepository.save(nvkCustomer);
        return "redirect:/nvkAdmin/customer";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        }
        return "redirect:/nvkAdmin/customer";
    }
}