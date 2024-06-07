package com.pricedrop.dao;

import com.pricedrop.entities.Product;
import com.pricedrop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("delete from Product p where p.user.u_id = :pid")
    void deleteProductOfUser(@Param("pid") int pid);

}
