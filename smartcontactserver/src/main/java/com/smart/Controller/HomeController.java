//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smart.Controller;

import com.smart.Repository.UserRepository;
import com.smart.helper.Message;
import com.smart.model.User;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public HomeController() {
    }

    @RequestMapping({"/"})
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping({"/about"})
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @RequestMapping({"/signup"})
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(
            value = {"/do_register"},
            method = {RequestMethod.POST}
    )
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, HttpSession session) {
        try {
            if (!agreement) {
                System.out.println("You have not agreed the term and conditions");
                throw new Exception("You have not agreed the term and conditions");
            } else if (result1.hasErrors()) {
                System.out.println("ERROR " + result1.toString());
                model.addAttribute("user", user);
                return "signup";
            } else {
                user.setRole("ROLE_USER");
                user.setEnabled(true);
                user.setImageUrl("default.png");
                user.setPassword(this.passwordEncoder.encode(user.getPassword()));
                System.out.println("Agreement " + agreement);
                System.out.println("USER " + user);
                User result = (User)this.userRepository.save(user);
                model.addAttribute("user", new User());
                session.setAttribute("message", new Message("Succesfully Registered !!", "alert-primary"));
                return "signup";
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong !!" + var7.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    @GetMapping({"/signin"})
    public String customLogin(Model model) {
        model.addAttribute("title", "Login page");
        return "login";
    }
}
