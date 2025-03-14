package org.itu.btp.config;

import org.itu.btp.model.Role;
import org.itu.btp.model.Utilisateur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur)session.getAttribute("utilisateur");

        if (utilisateur == null) {
            response.sendRedirect("/");
            return false;
        }

        Role role = utilisateur.getRole();
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/admin") && !Role.ROLE_ADMIN.equals(role)) {
            response.sendRedirect("/access-denied");
            return false;
        }

        if (requestURI.startsWith("/client") && !Role.ROLE_USER.equals(role)) {
            response.sendRedirect("/access-denied");
            return false;
        }

        return true;
    }
}
