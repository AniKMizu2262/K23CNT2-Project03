package K23CNT2.nvklesson05.K23CNT2.nvklesson05.controller;

import K23CNT2.nvklesson05.K23CNT2.nvklesson05.entity.Info;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        List<Info> profile = new ArrayList<>();

        profile.add(new Info(
                "Admin",
                "A",
                "email123@gmail.com",
                "https://preview.colorlib.com/theme/"
        ));

        model.addAttribute("nvkProfile", profile);
        return "profile";
    }

}

