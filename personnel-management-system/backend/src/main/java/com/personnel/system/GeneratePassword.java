package com.personnel.system;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encoded = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Encoded: " + encoded);
        
        // Verify
        boolean matches = encoder.matches(password, encoded);
        System.out.println("Matches: " + matches);
    }
}
