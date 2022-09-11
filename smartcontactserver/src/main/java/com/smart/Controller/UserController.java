//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smart.Controller;

import com.smart.Repository.ContactRepository;
import com.smart.Repository.UserRepository;
import com.smart.helper.Message;
import com.smart.model.Contact;
import com.smart.model.User;
import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/user"})
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    public UserController() {
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("USERNAME " + userName);
        User user = this.userRepository.getUserByUserName(userName);
        System.out.println("USER " + user);
        model.addAttribute("user", user);
    }

    @RequestMapping({"/index"})
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping({"/add-contact"})
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    //Processing and add contact form
    @PostMapping({"/process-contact"})
    public String processContact(Model model, @ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            if(3>2){
                throw new Exception();
            }

            //process and uploading file
            if (file.isEmpty()) {
                //if the image is empty then try our message
                System.out.println("Image is empty");
            } else {
                //file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());
                File saveFile = (new ClassPathResource("static/img")).getFile();
                String var10000 = saveFile.getAbsolutePath();
                Path path = Paths.get(var10000 + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                System.out.println("Image is uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);
            System.out.println("DATA " + contact);
            System.out.println("Added to database");

            //Successfully uploaded
            session.setAttribute("message", new Message("Your contact is Added..!! Add more.", "success"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            //Image is not uploaded successfully
            session.setAttribute("message", new Message("Something went wrong !! try again..", "danger"));
        }
        return "normal/add_contact_form";
    }

    //Show contacts handler
    @GetMapping("/show-contacts")
    public String showContacts(Model model, Principal principal) {
        model.addAttribute("title", "View Contact");

        //Send Contacts list - user is logged in
        String userName = principal.getName();
        User userByUserName = this.userRepository.getUserByUserName(userName);

        List<Contact> contacts = this.contactRepository.findContactByUser(userByUserName.getId());
        model.addAttribute("contacts", contacts);

        return "normal/show_contacts";
    }
}
