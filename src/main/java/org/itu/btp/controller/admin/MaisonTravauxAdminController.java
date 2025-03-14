package org.itu.btp.controller.admin;

import org.itu.btp.model.Maison;
import org.itu.btp.service.MaisonTravauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin/maison_travaux")
public class MaisonTravauxAdminController {
    private final MaisonTravauxService maisonTravauxService;

    @Autowired
    public MaisonTravauxAdminController(MaisonTravauxService maisonTravauxService) {
        this.maisonTravauxService = maisonTravauxService;
    }

    @GetMapping("/listMaison")
    public String listeMaison(Model model) {
        model.addAttribute("listMaison", maisonTravauxService.getAllMaison());
        return "admin/maison_travaux/listMaison";
    }

    @GetMapping("/listMaison/{id}")
    public String detailsMaison(@PathVariable Integer id, Model model) {
        Optional<Maison> optionalMaison = maisonTravauxService.getMaisonById(id);

        if (optionalMaison.isEmpty()) {
            model.addAttribute("message", "Maison non trouvée !");
            return "redirect:/admin/maison_travaux/listMaison";
        }

        Maison maison = optionalMaison.get(); // Extraire l'objet réel
        model.addAttribute("maison", maison);
        model.addAttribute("listTravaux", maisonTravauxService.getListTravauxByMaison(maison));

        return "admin/maison_travaux/detailMaison"; // Vérifier que ce fichier existe bien !
    }

    @GetMapping("/listTravaux")
    public String listeTravaux(Model model) {
        model.addAttribute("listTravaux", maisonTravauxService.getAllTravaux());
        return "admin/maison_travaux/listTravaux";
    }
}
