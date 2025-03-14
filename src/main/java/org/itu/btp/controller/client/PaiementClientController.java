package org.itu.btp.controller.client;

import jakarta.servlet.http.HttpSession;
import org.itu.btp.model.Utilisateur;
import org.itu.btp.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/paiement")
public class PaiementClientController {
    private final PaiementService paiementService;

    @Autowired
    public PaiementClientController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping("/list")
    public String listePaiement(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        model.addAttribute("listPaiement", paiementService.getAllPaiementByUtilisateur(utilisateur));
        return "client/paiement/list";
    }
}
