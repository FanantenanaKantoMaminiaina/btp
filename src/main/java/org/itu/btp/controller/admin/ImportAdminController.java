package org.itu.btp.controller.admin;

import org.itu.btp.service.DevisService;
import org.itu.btp.service.MaisonTravauxService;
import org.itu.btp.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/import")
public class ImportAdminController {
    private final MaisonTravauxService maisonTravauxService;
    private final DevisService devisService;
    private final PaiementService paiementService;

    @Autowired
    public ImportAdminController(MaisonTravauxService maisonTravauxService, DevisService devisService, PaiementService paiementService) {
        this.maisonTravauxService = maisonTravauxService;
        this.devisService = devisService;
        this.paiementService = paiementService;
    }

    @GetMapping("/maison_travaux_devis")
    public String importPageMaisonTravauxDevis() {
        return "admin/import/maison_travaux_devis";
    }

    @PostMapping("/maison_travaux_devis")
    public String importMaisonTravauxDevis(@RequestParam("maison_travaux") MultipartFile maison_travaux, @RequestParam("devis") MultipartFile devis, Model model, RedirectAttributes redirectAttributes) {
        if (maison_travaux.isEmpty() || devis.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Les 2 fichiers ne doievent pas etre null,Veuillez-selectioner des fichiers CSV.");
            return "redirect:/admin/import/maison_travaux_devis";
        }
        try {
            maisonTravauxService.importCsv(maison_travaux);

            devisService.importCsv(devis);
            model.addAttribute("listDevis", devisService.getAllDevis());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erreur lors de l'importation : " + e.getMessage());
            return "redirect:/admin/import/maison_travaux_devis";
        }

        return "admin/devis/list";
    }

    @GetMapping("/paiement")
    public String importPagePaiement() {
        return "admin/import/paiement";
    }

    @PostMapping("/paiement")
    public String importPaiement(@RequestParam("paiement") MultipartFile paiement, Model model, RedirectAttributes redirectAttributes) {
        if (paiement.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Le fichier ne doit pas etre null,Veuillez-selectioner un fichier CSV.");
            return "redirect:/admin/import/paiement";
        }
        try {
            paiementService.importCsv(paiement);
            model.addAttribute("listPaiement", paiementService.getAllPaiement());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erreur lors de l'importation : " + e.getMessage());
            return "redirect:/admin/import/paiement";
        }

        return "admin/paiement/list";
    }
}
