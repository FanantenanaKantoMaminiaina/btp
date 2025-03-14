package org.itu.btp.controller.admin;


import org.itu.btp.dto.MontantDevis;
import org.itu.btp.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class DashboardAdminController {
    private final PaiementService paiementService;
    private static final Integer currentYear = java.time.Year.now().getValue();

    @Autowired
    public DashboardAdminController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping("/accueil")
    public String acceuilAdmin() {
        return "admin/accueil";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        List<MontantDevis> montantDevisList = paiementService.getListMontantDevis(DashboardAdminController.currentYear);
        double[] montantDevis = paiementService.getMontantTotalDevis();

        List<Integer> years = new ArrayList<>();
        List<String> mois = new ArrayList<>();
        List<Double> montants = new ArrayList<>();

        for (int i = DashboardAdminController.currentYear; i > 2020; i--) {
            years.add(i);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM yyyy");

        for (MontantDevis devis : montantDevisList) {
            try {
                // Conversion de la chaîne de date en objet Date
                Date date = sdf.parse(devis.getDate());

                // Formatage de la date au format "MMMM yyyy"
                String moisAnnee = outputFormat.format(date);

                // Ajout des données formatées dans les listes
                mois.add(moisAnnee);
                montants.add(devis.getMontant());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Ajout des attributs dans le modèle pour l'affichage
        model.addAttribute("nbDevis", montantDevis[0]);
        model.addAttribute("totalDevis", montantDevis[1]);
        model.addAttribute("totalPaye", montantDevis[2]);
        model.addAttribute("totalNonPaye", montantDevis[3]);
        model.addAttribute("years", years);
        model.addAttribute("mois", mois);
        model.addAttribute("montants", montants);

        return "admin/dashboard/dashboard";
    }

    @PostMapping("/dashboard")
    @ResponseBody
    public Map<String, Object> dashboardRecherche(@RequestParam("annee") Integer annee) {
        System.out.println("Annee = " + annee);
        List<MontantDevis> montantDevisList = paiementService.getListMontantDevis(annee);

        List<String> mois = new ArrayList<>();
        List<Double> montants = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM yyyy");

        for (MontantDevis devis : montantDevisList) {
            try {
                Date date = sdf.parse(devis.getDate());

                String moisAnnee = outputFormat.format(date);

                mois.add(moisAnnee);
                montants.add(devis.getMontant());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("mois", mois);
        response.put("montants", montants);

        return response;
    }
}
