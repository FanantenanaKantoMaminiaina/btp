package org.itu.btp.controller;

import org.itu.btp.model.Role;
import org.itu.btp.model.Utilisateur;
import org.itu.btp.service.PasswordEncoderService;
import org.itu.btp.service.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class InitialiserController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoderService passwordEncoderService;

    public InitialiserController(UtilisateurService utilisateurService, PasswordEncoderService passwordEncoderService) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoderService = passwordEncoderService;
    }

    @GetMapping("/init")
    public String initialiserAdmin(RedirectAttributes redirectAttributes) {
//        String email = "admin@gmail.com";
//
//        Optional<Utilisateur> utilisateurOpt = utilisateurService.findUtilisateurByEmail(email);
//        if (utilisateurOpt.isPresent()) {
//            redirectAttributes.addFlashAttribute("message", "L'utilisateur admin existe déjà.");
//            return "redirect:/";
//        }
//
//        Utilisateur admin = new Utilisateur();
//        admin.setNom("admin");
//        admin.setEmail(email);
//        admin.setMdp(passwordEncoderService.encodePassword("admin"));
//        admin.setContact("");
//        admin.setRole(Role.ROLE_ADMIN);
//
//        try {
//            utilisateurService.saveUtilisateur(admin);
//            redirectAttributes.addFlashAttribute("message", "Utilisateur admin créé avec succès.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("message", "Erreur lors de la création de l'utilisateur.");
//        }

        return "redirect:/";
    }
}
