package org.itu.btp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordEncoderService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Hache un mot de passe en utilisant BCrypt.
     *
     * @param rawPassword Le mot de passe brut à hacher.
     * @return Le mot de passe haché.
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Vérifie si un mot de passe brut correspond au mot de passe haché.
     *
     * @param rawPassword   Le mot de passe brut.
     * @param encodedPassword Le mot de passe haché.
     * @return true si les mots de passe correspondent, sinon false.
     */
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
