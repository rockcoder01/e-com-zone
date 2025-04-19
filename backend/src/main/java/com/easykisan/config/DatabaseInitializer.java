package com.easykisan.config;

import com.easykisan.model.Role;
import com.easykisan.model.Role.ERole;
import com.easykisan.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing database with default roles");
        
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    new Role(ERole.ROLE_USER),
                    new Role(ERole.ROLE_ADMIN),
                    new Role(ERole.ROLE_VENDOR),
                    new Role(ERole.ROLE_INFLUENCER)
            );
            
            roleRepository.saveAll(roles);
            logger.info("Default roles created successfully");
        } else {
            logger.info("Roles already exist, skipping initialization");
        }
    }
}