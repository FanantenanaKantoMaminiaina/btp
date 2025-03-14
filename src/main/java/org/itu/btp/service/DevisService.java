package org.itu.btp.service;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.itu.btp.model.*;
import org.itu.btp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DevisService {
    private final DevisRepository devisRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TypeFinitionRepository typeFinitionRepository;
    private final MaisonRepository maisonRepository;
    private final MaisonTravauxService maisonTravauxService;

    @Autowired
    public DevisService(DevisRepository devisRepository, UtilisateurRepository utilisateurRepository, TypeFinitionRepository typeFinitionRepository, MaisonRepository maisonRepository, MaisonTravauxService maisonTravauxService) {
        this.devisRepository = devisRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.typeFinitionRepository = typeFinitionRepository;
        this.maisonRepository = maisonRepository;
        this.maisonTravauxService = maisonTravauxService;
    }

    public List<Devis> getAllDevis() { return devisRepository.findAll(); }
    public List<Devis> getAllDevisEnCours() { return devisRepository.findAll(); }
    public Optional<Devis> getDevisById(int id) { return devisRepository.findById(id); }
    public List<Devis> getAllDevisByUtilisateur(Utilisateur utilisateur) { return devisRepository.findByUtilisateur(utilisateur); }

    public List<MaisonTravaux> getListTravauxByDevis(Devis devis) { return maisonTravauxService.getListTravauxByMaison(devis.getMaison()); }

    @Transactional
    public void importCsv(MultipartFile fichier) throws Exception {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(fichier.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Devis> listDevis = new ArrayList<>();
            Set<TypeFinition> newFinitions = new HashSet<>();
            Set<Utilisateur> newClients = new HashSet<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE); // Permet d'interpréter "6,45" comme 6.45

            // Charger les références existantes pour vérification
            Map<String, Utilisateur> clientExistant = utilisateurRepository.findAll().stream()
                    .collect(Collectors.toMap(Utilisateur::getContact, utilisateur -> utilisateur));

            Map<String, Maison> maisonExistant = maisonRepository.findAll().stream()
                    .collect(Collectors.toMap(Maison::getType, maison -> maison));

            Map<String, TypeFinition> finitionExistant = typeFinitionRepository.findAll().stream()
                    .collect(Collectors.toMap(TypeFinition::getType, finition -> finition));

            String clientTel;String refDevis;String typeMaison;String finition;String tauxFinition;String lieu;String dateDevis;String dateDebut;

            int ligne = 0;

            for (CSVRecord record : csvParser) {
                ligne++;
                clientTel = record.get("client").trim();
                refDevis = record.get("ref_devis").trim();
                typeMaison = record.get("type_maison").trim();
                finition = record.get("finition").trim();
                tauxFinition = record.get("taux_finition").trim();
                lieu = record.get("lieu").trim();
                dateDevis = record.get("date_devis").trim();
                dateDebut = record.get("date_debut").trim();

                // Vérification et insertion des clés étrangères si nécessaire
                Utilisateur client = clientExistant.get(clientTel);
                if (client == null) {
                    client = new Utilisateur();
                    client.setContact(clientTel);
                    client.setRole(Role.ROLE_USER);
                    newClients.add(client);
                    clientExistant.put(clientTel, client);
                }

                Maison maison = maisonExistant.get(typeMaison);
                if (maison == null) {
                    throw new Exception("Maison n'existe pas pour la maison "+typeMaison+" a la ligne "+ligne);
                }

                TypeFinition typeFinition = finitionExistant.get(finition);
                if (typeFinition == null) {
                    typeFinition = new TypeFinition();
                    typeFinition.setType(finition);
                    typeFinition.setPourcentage(numberFormat.parse(tauxFinition).doubleValue());
                    newFinitions.add(typeFinition);
                    finitionExistant.put(finition, typeFinition);
                }

                // Création de l'objet Devis
                Devis devis = new Devis();
                devis.setUtilisateur(client);
                devis.setReference(refDevis);
                devis.setMaison(maison);
                devis.setTypeFinition(typeFinition);
                devis.setLieu(lieu);
                devis.setDateDevis(new Date(dateFormat.parse(dateDevis).getTime()));
                devis.setDebutTravaux(new Date(dateFormat.parse(dateDebut).getTime()));

                devis.setMontant(maisonTravauxService.calculerCoutTotal(maison));
                devis.setMontantPaye(0);

                LocalDate localDebut = devis.getDebutTravaux().toLocalDate(); // Conversion en LocalDate
                LocalDate localFin = localDebut.plusDays(maison.getDurre()); // Ajout des jours
                devis.setFinTravaux(Date.valueOf(localFin));

                listDevis.add(devis);
            }

            // Sauvegarde des nouvelles entités
            if (!newClients.isEmpty()) utilisateurRepository.saveAll(newClients);
            if (!newFinitions.isEmpty()) typeFinitionRepository.saveAll(newFinitions);

            // Sauvegarde en batch des devis
            if (!listDevis.isEmpty()) devisRepository.saveAll(listDevis);
            System.out.println("Total Devis inseres : "+listDevis.size());
        } catch (Exception e) {
            throw new Exception("Devis : " + e.getMessage(), e);
        }
    }
}
