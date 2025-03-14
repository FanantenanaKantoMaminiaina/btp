package org.itu.btp.controller.admin;

import org.itu.btp.model.Devis;
import org.itu.btp.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin/devis")
public class DevisAdminController {
    private final DevisService devisService;

    @Autowired
    public DevisAdminController(DevisService devisService) {
        this.devisService = devisService;
    }

    @GetMapping("/list")
    public String listeDevis(Model model) {
        model.addAttribute("listDevis", devisService.getAllDevis());
        return "admin/devis/list";
    }

    @GetMapping("/list/enCours")
    public String listeDevisEnCours(Model model) {
        model.addAttribute("listDevis", devisService.getAllDevis());
        return "admin/devis/listEnCours";
    }

    @GetMapping("/list/{id}")
    public String detailsDevis(@PathVariable Integer id, Model model) {
        Optional<Devis> optionalDevis = devisService.getDevisById(id);

        if (optionalDevis.isEmpty()) {
            model.addAttribute("message", "Devis non trouvée !");
            return "redirect:/admin/devis/list";
        }

        Devis devis = optionalDevis.get(); // Extraire l'objet réel
        model.addAttribute("devis", devis);
        model.addAttribute("listTravaux", devisService.getListTravauxByDevis(devis));

        return "admin/devis/detailDevis";
    }
}