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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

            //process and uploading file
            if (file.isEmpty()) {
                //if the image is empty then try our message
                System.out.println("Image is empty");
                contact.setImage("contact.png");
            } else {
                //file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get( saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
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

    //Show contacts handler -- show 5[n] contact per page and current page [page]
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "View Contact");

        //Send Contacts list - user is logged in
        String userName = principal.getName();
        User userByUserName = this.userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findContactByUser(userByUserName.getId(), pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contacts.getTotalPages());

        return "normal/show_contacts";
    }

    //Showing particular contact details
    @RequestMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal)
    {
        System.out.println("CID"+cId);

        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        //Which user is login
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if(user.getId()==contact.getUser().getId()){
            model.addAttribute("title", contact.getName());
            model.addAttribute("contact", contact);
        }
        return "normal/contact_detail";
    }

    //Delete contact handler
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cId, Model model, HttpSession session) {
       Contact contact = this.contactRepository.findById(cId).get();
       contact.setUser(null);

        //delete contact...
        this.contactRepository.delete(contact);

        session.setAttribute("message", new Message("Contact deleted successfully", "success"));
        return "redirect:/user/show-contacts/0";
    }

    //Update contact detail
    @PostMapping("/update-contact/{cid}")
    public String updateContact(@PathVariable("cid") Integer cid,Model model) {

        model.addAttribute("title", "Update Contact");

        Contact contact = this.contactRepository.findById(cid).get();
        model.addAttribute("contact", contact);

        return "normal/update_contact";
    }
}
