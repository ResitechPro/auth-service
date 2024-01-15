package com.taskflow.config.resolver;

import com.taskflow.service.JwtService;
import com.taskflow.utils.OrganizationNameExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtTenantResolver implements TenantResolver<HttpServletRequest> {
    private final JwtService jwtService;
    private final OrganizationNameExtractor organizationNameExtractor;
    public JwtTenantResolver(JwtService jwtService, OrganizationNameExtractor organizationNameExtractor) {
        this.jwtService = jwtService;
        this.organizationNameExtractor = organizationNameExtractor;
    }

    @Override
    public String resolveTenant(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String jwtToken = authHeader.substring(7);
            String email = jwtService.extractUserName(jwtToken);
            return organizationNameExtractor.extract(email);
        }
        if( request.getHeader("organizationName") != null){
            return request.getHeader("organizationName");
        }
        return "public";
    }
}
