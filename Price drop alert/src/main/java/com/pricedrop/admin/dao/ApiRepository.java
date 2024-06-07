package com.pricedrop.admin.dao;

import com.pricedrop.admin.entities.Productapi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ApiRepository extends JpaRepository<Productapi,Integer> {
    @Query("SELECT p FROM Productapi p WHERE p.product_name = :productName")
    Optional<Productapi> findByProductName(String productName);
    @Query("SELECT p FROM Productapi p WHERE p.product_url = :productUrl")
    Productapi findByProductUrl(String productUrl);

    @Query("SELECT p FROM Productapi p WHERE p.product_name LIKE %:productName%")
    List<Productapi> findByProductNameContains(@Param("productName") String productName);

//    public List<Productapi> findByProduct_nameContaining(String product);

}
