package org.itu.btp.controller.client;

import jakarta.servlet.http.HttpSession;
import org.itu.btp.model.Devis;
import org.itu.btp.model.Utilisateur;
import org.itu.btp.service.DevisService;
import org.itu.btp.service.MaisonTravauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/client/devis")
public class DevisClientController {
    private final DevisService devisService;
    private final MaisonTravauxService maisonTravauxService;

    @Autowired
    public DevisClientController(DevisService devisService, MaisonTravauxService maisonTravauxService) {
        this.devisService = devisService;
        this.maisonTravauxService = maisonTravauxService;
    }

    @GetMapping("/insert")
    public String pageDevis(Model model) {
        model.addAttribute("listTypeFinition", maisonTravauxService.getAllTypeFinition());
        model.addAttribute("listMaison", maisonTravauxService.getAllMaison());
        return "client/devis/insert";
    }

    @GetMapping("/list")
    public String listeDevis(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        model.addAttribute("listDevis", devisService.getAllDevisByUtilisateur(utilisateur));
        return "client/devis/list";
    }

    @GetMapping("/list/{id}")
    public String detailsDevis(@PathVariable Integer id, Model model) {
        Optional<Devis> optionalDevis = devisService.getDevisById(id);

        if (optionalDevis.isEmpty()) {
            model.addAttribute("message", "Devis non trouvée !");
            return "redirect:/client/devis/list";
        }

        Devis devis = optionalDevis.get(); // Extraire l'objet réel
        model.addAttribute("devis", devis);
        model.addAttribute("listTravaux", devisService.getListTravauxByDevis(devis));

        return "client/devis/detailDevis";
    }
}