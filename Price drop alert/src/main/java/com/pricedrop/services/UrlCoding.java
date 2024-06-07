package com.pricedrop.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
public class UrlCoding {

    private static final String SECRET_KEY = "16";
    public static String encodeUrl(String originalUrl) {
        try {
            return URLEncoder.encode(originalUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Handle encoding error
            e.printStackTrace();
            return null;
        }
    }
    public static String decodeUrl(String encodedUrl) {
        try {
            return URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Handle decoding error
            e.printStackTrace();
            return null;
        }
    }

    private static final Map<Character, Character> encryptionMap = new HashMap<>();
    private static final Map<Character, Character> decryptionMap = new HashMap<>();

    static {
        // Define encryption and decryption mapping using Caesar cipher with shift 3
        int shift = 3;

        for (char c = 'a'; c <= 'z'; ++c) {
            char encryptedChar = (char) ('a' + (c - 'a' + shift) % 26);
            encryptionMap.put(c, encryptedChar);
            decryptionMap.put(encryptedChar, c);
        }
    }

    public static String encrypt(String url) {
        StringBuilder encryptedUrl = new StringBuilder();
        for (char c : url.toCharArray()) {
            char encryptedChar = encryptionMap.getOrDefault(Character.toLowerCase(c), c);
            encryptedUrl.append(encryptedChar);
        }
        return encryptedUrl.toString();
    }

    public static String decrypt(String encryptedUrl) {
        StringBuilder decryptedUrl = new StringBuilder();
        for (char c : encryptedUrl.toCharArray()) {
            char decryptedChar = decryptionMap.getOrDefault(Character.toLowerCase(c), c);
            decryptedUrl.append(decryptedChar);
        }
        return decryptedUrl.toString();
    }

    public static String extractProductName(String url) {
        try {
            // Parse the URL
            URI uri = new URI(url);

            // Get the path of the URL
            String path = uri.getPath();

            // Remove leading and trailing slashes, if any
            path = path.replaceAll("^/|/$", "");

            // Split the path by slashes
            String[] segments = path.split("/");

            // Return the last segment (product name)
            return segments[segments.length - 1];
        } catch (URISyntaxException e) {
            // Handle invalid URL
            e.printStackTrace();
            return null;
        }
    }

}
