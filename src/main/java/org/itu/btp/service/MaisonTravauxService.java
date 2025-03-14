package org.itu.btp.service;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.itu.btp.model.*;
import org.itu.btp.repository.*;
import org.itu.btp.util.Utilitaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaisonTravauxService {
    private final MaisonTravauxRepository maisonTravauxRepository;
    private final MaisonRepository maisonRepository;
    private final TravauxRepository travauxRepository;
    private final UniteRepository uniteRepository;
    private final TypeFinitionRepository typeFinitionRepository;

    @Autowired
    public MaisonTravauxService(MaisonTravauxRepository maisonTravauxRepository,MaisonRepository maisonRepository,TravauxRepository travauxRepository,UniteRepository uniteRepository, TypeFinitionRepository typeFinitionRepository) {
        this.maisonTravauxRepository = maisonTravauxRepository;
        this.maisonRepository = maisonRepository;
        this.travauxRepository = travauxRepository;
        this.uniteRepository = uniteRepository;
        this.typeFinitionRepository = typeFinitionRepository;
    }

    public List<Maison> getAllMaison() {
        return maisonRepository.findAll();
    }
    public List<Travaux> getAllTravaux() {
        return travauxRepository.findAll();
    }
    public Optional<Maison> getMaisonById(int id) { return maisonRepository.findById(id); }
    public List<MaisonTravaux> getListTravauxByMaison(Maison maison) { return maisonTravauxRepository.findByMaison(maison); }
    public List<TypeFinition> getAllTypeFinition() { return typeFinitionRepository.findAll(); }

    public double calculerCoutTotal(Maison maison) {
        // Récupérer tous les travaux associés à la maison donnée
        List<MaisonTravaux> maisonTravauxList = this.getListTravauxByMaison(maison);

        double coutTotal = 0;

        for (MaisonTravaux maisonTravaux : maisonTravauxList) {
            // Calcul du coût pour chaque travail associé à la maison
            coutTotal += maisonTravaux.getQuantite() * maisonTravaux.getTravaux().getPrixUnitaire();
        }

        return coutTotal;
    }

    @Transactional
    public void importCsv(MultipartFile fichier) throws Exception {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(fichier.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE); // Permet d'interpréter "6,45" comme 6.45

            List<MaisonTravaux> listMaisonTravaux = new ArrayList<>();

            // Charger les références existantes pour éviter les doublons
            Map<String, Maison> maisonExistant = maisonRepository.findAll().stream()
                    .collect(Collectors.toMap(maison -> maison.getType() + "_" + maison.getSurface(), maison -> maison));

            Map<String, Travaux> travauxExistant = travauxRepository.findAll().stream()
                    .collect(Collectors.toMap(Travaux::getCode, travaux -> travaux));

            Map<String, Unite> uniteExistant = uniteRepository.findAll().stream()
                    .collect(Collectors.toMap(Unite::getNom, unite -> unite));

            String typeMaison;String description;String surface;String codeTravaux;String typeTravaux;String nomUnite;String prixUnitaire;String quantite;String dureeTravaux;

            // Parcours des lignes du fichier CSV
            for (CSVRecord record : csvParser) {
                typeMaison = record.get("type_maison").trim();
                description = record.get("description").trim();
                surface = record.get("surface").trim();
                codeTravaux = record.get("code_travaux").trim();
                typeTravaux = record.get("type_travaux").trim();
                nomUnite = record.get("unité").trim();
                prixUnitaire = record.get("prix_unitaire").trim();
                quantite = record.get("quantite").trim();
                dureeTravaux = record.get("duree_travaux").trim();

                // Conversion des valeurs en types numériques
                double valSurface = numberFormat.parse(surface).doubleValue();
                double valPrixUnitaire = numberFormat.parse(prixUnitaire).doubleValue();
                double valQuantite = numberFormat.parse(quantite).doubleValue();
                int valDureeTravaux = Utilitaire.parseInt(dureeTravaux, 0);

                // Vérification et insertion des unités
                Unite unite = uniteExistant.get(nomUnite);
                if (unite == null) {
                    unite = new Unite();
                    unite.setNom(nomUnite);
                    unite = uniteRepository.save(unite);
                    uniteExistant.put(nomUnite, unite);
                }

                // Vérification et insertion des maisons
                String maisonKey = typeMaison + "_" + valSurface;
                Maison maison = maisonExistant.get(maisonKey);
                if (maison == null) {
                    maison = new Maison();
                    maison.setType(typeMaison);
                    maison.setDescription(description);
                    maison.setSurface(valSurface);
                    maison.setDurre(valDureeTravaux);
                    maison = maisonRepository.save(maison);
                    maisonExistant.put(maison.getType() + "_" + maison.getSurface(),maison);
                }

                // Vérification et insertion des travaux
                Travaux travaux = travauxExistant.get(codeTravaux);
                if (travaux == null) {
                    travaux = new Travaux();
                    travaux.setCode(codeTravaux);
                    travaux.setDescription(typeTravaux);
                    travaux.setUnite(unite);
                    travaux.setPrixUnitaire(valPrixUnitaire);
                    travaux = travauxRepository.save(travaux);
                    travauxExistant.put(travaux.getCode(),travaux);
                }

                if (maison == null || travaux == null) {
                    throw new Exception("Maison & Travaux : La maison et travaux n'existent pas(NULL) pour le MaisonTravaux");
                }

                MaisonTravaux maisonTravaux = new MaisonTravaux();
                maisonTravaux.setIdMaisonTravaux(new IdMaisonTravaux(maison.getIdMaison(), travaux.getIdTravaux()));
                maisonTravaux.setMaison(maison);
                maisonTravaux.setTravaux(travaux);
                maisonTravaux.setQuantite(valQuantite);
                maisonTravaux.setTotal(valQuantite * travaux.getPrixUnitaire());

                listMaisonTravaux.add(maisonTravaux);
            }

            // Sauvegarde en batch des MaisonTravaux
            if (!listMaisonTravaux.isEmpty()) maisonTravauxRepository.saveAll(listMaisonTravaux);

            System.out.println("Total MaisonTravaux insérés : " + listMaisonTravaux.size());
        } catch (Exception e) {
            throw new Exception("Maison & Travaux : " + e.getMessage(), e);
        }
    }
}
