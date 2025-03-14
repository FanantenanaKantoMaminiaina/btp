package org.itu.btp.controller.admin;

import org.itu.btp.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/paiement")
public class PaiementAdminController {
    private final PaiementService paiementService;

    @Autowired
    public PaiementAdminController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping("/list")
    public String listePaiement(Model model) {
        model.addAttribute("listPaiement", paiementService.getAllPaiement());
        return "admin/paiement/list";
    }

    @GetMapping("/import")
    public String importPage() {
        return "admin/paiement/import";
    }
}
