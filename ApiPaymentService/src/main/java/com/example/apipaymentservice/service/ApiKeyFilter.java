package com.example.apipaymentservice.service;

import com.example.apipaymentservice.model.Project;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ProjectService projectService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("api-key");

        if (apiKey != null) {
            Project project = projectService.getProjectByApiKey(apiKey);

            // Проверяем и используем токен
            if (!projectService.useToken(project)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Недостаточно токенов для выполнения операции.");
                return;
            }
        }

        // Если всё хорошо, продолжаем выполнение запроса
        filterChain.doFilter(request, response);
    }
    }
