package org.itu.btp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResetDataController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/reset")
    public String resetDatabase(RedirectAttributes redirectAttributes) {
        String sql = """
        DO $$ 
        DECLARE 
            r RECORD;
        BEGIN
            FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') 
            LOOP
                EXECUTE 'TRUNCATE TABLE ' || r.tablename || ' CASCADE';
            END LOOP;
        END $$;
    """;

        try {
            jdbcTemplate.execute(sql);
            redirectAttributes.addFlashAttribute("message", "Les données ont été réinitialisées avec succès, à l'exception des tables utilisateur et rôle.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la réinitialisation de la base de données : " + e.getMessage());
        }

        return "redirect:/";
    }

//    @GetMapping("/reset")
//    public String resetDatabase(RedirectAttributes redirectAttributes) {
//        String sql = """
//        DO $$
//        DECLARE
//            r RECORD;
//        BEGIN
//            FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename NOT IN ('utilisateur'))
//            LOOP
//                EXECUTE 'TRUNCATE TABLE ' || r.tablename || ' CASCADE';
//            END LOOP;
//        END $$;
//    """;
//
//        try {
//            jdbcTemplate.execute(sql);
//            redirectAttributes.addFlashAttribute("message", "Les données ont été réinitialisées avec succès, à l'exception des tables utilisateur et rôle.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la réinitialisation de la base de données : " + e.getMessage());
//        }
//
//        return "redirect:/";
//    }
}

