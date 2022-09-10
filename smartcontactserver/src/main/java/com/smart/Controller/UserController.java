package com.smart.Controller;

import com.smart.Repository.UserRepository;
import com.smart.model.Contact;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Method for adding common data to response
    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String userName = principal.getName();
        System.out.println("USERNAME "+userName);

        //get the user using username(email)
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USER "+user);
        model.addAttribute("user", user);
    }

    // Dashboard Home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    //Open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){

        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    //Processing Add Contact Form
    @PostMapping("/process-contact")
    public String processContact(Model model, Contact contact, Principal principal) {

        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);

        contact.setUser(user);

        user.getContacts().add(contact);
        this.userRepository.save(user);

        System.out.println("DATA "+contact);
        System.out.println("Added to database");

        return "normal/add_contact_form";
    }
}
