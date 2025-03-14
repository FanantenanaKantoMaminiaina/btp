package org.itu.btp.controller;

import jakarta.servlet.http.HttpSession;
import org.itu.btp.model.Role;
import org.itu.btp.model.Utilisateur;
import org.itu.btp.service.PasswordEncoderService;
import org.itu.btp.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/authentification")
public class AuthentificationController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoderService passwordEncoderService;

    @Autowired
    public AuthentificationController(UtilisateurService utilisateurService, PasswordEncoderService passwordEncoderService) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoderService = passwordEncoderService;
    }

    @GetMapping("/admin")
    public String loginPageAdmin() {
        return "authentification/loginAdmin";
    }

    @PostMapping("/admin")
    public String loginAdmin(@RequestParam String email, @RequestParam String mdp, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Utilisateur> utilisateur = utilisateurService.findUtilisateurByEmail(email);

        if (utilisateur.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Utilisateur invalide");
            return "redirect:/authentification/admin";
        }

        Utilisateur admin = utilisateur.get();
        if (!passwordEncoderService.matchesPassword(mdp, admin.getMdp())) {
            redirectAttributes.addFlashAttribute("message", "Mot de passe incorrect");
            return "redirect:/authentification/admin";
        }

        if (!admin.getRole().equals(Role.ROLE_ADMIN)) {
            redirectAttributes.addFlashAttribute("message", "Vous n'avez pas le droit de vous connecter en tant qu'admin");
            return "redirect:/authentification/admin";
        }

        session.setAttribute("utilisateur", admin);
        return "redirect:/admin/accueil";
    }

    @GetMapping("/client")
    public String loginPageClient() {
        return "authentification/loginClient";
    }

    @PostMapping("/client")
    public String loginClient(@RequestParam String contact, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findUtilisateurByContact(contact);

        Utilisateur utilisateur = null;
        if (!utilisateurOpt.isEmpty()) {
            utilisateur = utilisateurOpt.get();
        }else {
            utilisateur = new Utilisateur();
            utilisateur.setContact(contact);
            utilisateur.setRole(Role.ROLE_USER);

            try {
                utilisateur = utilisateurService.saveUtilisateur(utilisateur);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "Erreur lors de la création de l'utilisateur.");
                return "redirect:/authentification/client?error=true";
            }
        }

        session.setAttribute("utilisateur", utilisateur);
        return "redirect:/client/accueil";
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
