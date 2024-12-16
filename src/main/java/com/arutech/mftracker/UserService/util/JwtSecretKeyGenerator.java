package com.arutech.mftracker.UserService.util;
import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        // Generate a 256-bit (32-byte) cryptographically secure secret key
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);

        // Encode to Base64 for easy storage and transmission
        String base64EncodedSecret = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("Generated JWT Secret Key:");
        System.out.println(base64EncodedSecret);

        // Additional recommendations for secret management
        System.out.println("\nKey Management Recommendations:");
        System.out.println("1. Store this key in environment variables");
        System.out.println("2. Never commit the key to source control");
        System.out.println("3. Rotate keys periodically");
    }
}