package com.pricedrop.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ProductService {

    public static Double getCurrentPrice(String productUrl) throws IOException, InterruptedException {
        try {
            var url = "http://localhost:8080/admin/getproductbyurl/" + productUrl;
            System.out.println(url);
            var username = "shubham@gmail.com";
            var password = "jain";

            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("Authorization", "Basic " + encodedAuth) // Add basic authentication header
                    .build();

            var client = HttpClient.newBuilder().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            ApiProduct productApisArray = objectMapper.readValue(response.body(), ApiProduct.class);

            // Convert array to List


            // Access specific fields
            System.out.println(productApisArray.getProduct_price());
            return Double.valueOf(productApisArray.getProduct_price());
        }catch (Exception e){
            return null;
        }
    }
    public String getImageUrl(String productUrl){
        try {
            var url = "http://localhost:8080/admin/getproductbyurl/" + productUrl;
            System.out.println(url);
            var username = "shubham@gmail.com";
            var password = "jain";

            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("Authorization", "Basic " + encodedAuth) // Add basic authentication header
                    .build();

            var client = HttpClient.newBuilder().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            ApiProduct productApisArray = objectMapper.readValue(response.body(), ApiProduct.class);

            // Convert array to List


            // Access specific fields

            return String.valueOf(productApisArray.getProduct_image());
        }catch (Exception e){
            return null;
        }

    }
}
