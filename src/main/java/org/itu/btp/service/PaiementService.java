package org.itu.btp.service;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.itu.btp.dto.MontantDevis;
import org.itu.btp.model.*;
import org.itu.btp.repository.DevisRepository;
import org.itu.btp.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaiementService {
    private final PaiementRepository paiementRepository;
    private final DevisRepository devisRepository;

    @Autowired
    public PaiementService(PaiementRepository paiementRepository,DevisRepository devisRepository) {
        this.paiementRepository = paiementRepository;
        this.devisRepository = devisRepository;
    }

    public List<Paiement> getAllPaiement() {
        return paiementRepository.findAll();
    }
    public List<Paiement> getAllPaiementByUtilisateur(Utilisateur utilisateur) {
        return paiementRepository.findByUtilisateur(utilisateur);
    }

    @Transactional
    public void importCsv(MultipartFile fichier) throws Exception {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(fichier.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Paiement> listPaiement = new ArrayList<>();
            Map<String, Devis> devisExistant = devisRepository.findAll().stream()
                    .collect(Collectors.toMap(Devis::getReference, devis -> devis));

            Set<Devis> devisAmettreAJour = new HashSet<>(); // Contient uniquement les devis modifiés

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (CSVRecord record : csvParser) {
                String refDevis = record.get("ref_devis").trim();
                String refPaiement = record.get("ref_paiement").trim();
                String datePaiementStr = record.get("date_paiement").trim();
                String montantStr = record.get("montant").trim();

                Devis devis = devisExistant.get(refDevis);
                if (devis == null) {
                    throw new Exception("Le devis avec la référence " + refDevis + " n'existe pas.");
                }

                // Création du paiement
                double montant = Double.parseDouble(montantStr);
                Paiement paiement = new Paiement();
                paiement.setReference(refPaiement);
                paiement.setMontant(montant);
                paiement.setDatePaiement(new Date(dateFormat.parse(datePaiementStr).getTime()));
                paiement.setDevis(devis);

                listPaiement.add(paiement);

                // Mise à jour immédiate du montant payé du devis et ajout au Set
                devis.setMontantPaye(devis.getMontantPaye() + montant);
                devisAmettreAJour.add(devis); // On ajoute uniquement les devis modifiés
            }

            // Sauvegarde des paiements
            if (!listPaiement.isEmpty()) paiementRepository.saveAll(listPaiement);

            // Sauvegarde uniquement des devis qui ont été modifiés
            if (!devisAmettreAJour.isEmpty()) devisRepository.saveAll(devisAmettreAJour);

            System.out.println("Total Paiements insérés : " + listPaiement.size());
            System.out.println("Total Devis mis à jour : " + devisAmettreAJour.size());

        } catch (Exception e) {
            throw new Exception("Erreur lors de l'importation des paiements : " + e.getMessage(), e);
        }
    }

    public List<MontantDevis> getListMontantDevis(Integer annee){
        List<Object[]> results = this.paiementRepository.getMontantDevisParMois(annee);
        return results.stream().map(
                obj -> new MontantDevis((String) obj[0],(Double)obj[1])
        ).collect(Collectors.toList());
    }

    public double[] getMontantTotalDevis(){
        List<Object[]> results = this.paiementRepository.getMontantTotalDevis();

        double[] montantDevis = new double[4];
        montantDevis[0] = Double.parseDouble(results.get(0)[0].toString());
        montantDevis[1] = 0;
        montantDevis[2] = 0;

        if (results.get(0)[1] != null){
            montantDevis[1] = Double.parseDouble(results.get(0)[1].toString());
            montantDevis[2] = Double.parseDouble(results.get(0)[2].toString());
        }
        montantDevis[3] = montantDevis[1] - montantDevis[2];

        return montantDevis;
    }
}
