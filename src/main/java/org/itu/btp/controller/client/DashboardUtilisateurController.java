package org.itu.btp.controller.client;

import org.itu.btp.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class DashboardUtilisateurController {
    @Autowired
    public DashboardUtilisateurController(PaiementService paiementService) {}

    @GetMapping("/accueil")
    public String acceuilClient() {
        return "client/accueil";
    }
}
