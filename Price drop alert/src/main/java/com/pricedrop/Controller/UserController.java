package com.pricedrop.Controller;

import com.pricedrop.dao.ProductRepository;
import com.pricedrop.dao.UserRepository;
import com.pricedrop.entities.Product;
import com.pricedrop.entities.User;
import com.pricedrop.helper.Message;
import com.pricedrop.services.ProductService;
import com.pricedrop.services.UrlCoding;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.Binding;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String Dashboard(Principal principal,Model model){
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user",user);

        return "dashboard1";
    }
    @RequestMapping("/add-product")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String product(Principal principal,Model model){
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("product",new Product());
        return "add_product1";
    }

    @RequestMapping("/added-product")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String AddedProduct(@Valid @ModelAttribute Product product, BindingResult result, Principal principal, HttpSession session, Model model) {
        try {
            String email = principal.getName();
            User user = this.userRepository.getUserByUserName(email);
            if (result.hasErrors()) {
                model.addAttribute("product", product);
                model.addAttribute("user", user);
                return "add_product1";
            } else {
                // Check if the product already exists for the user
                if (isProductExistsForUser(product, user)) {
                    session.setAttribute("message", new Message("You have already added this product.", "alert-danger"));
                } else if (ProductService.getCurrentPrice(UrlCoding.extractProductName(product.getP_url())) != null && product.getP_url().contains("https://www.example.com/product/")) {
                    if(ProductService.getCurrentPrice(UrlCoding.extractProductName(product.getP_url())) <= product.getT_price() ){
                        session.setAttribute("message", new Message("Please enter correct threshold price!!", "alert-danger"));
                    }
                    else {
                        product.setT_price(Double.parseDouble(String.valueOf(product.getT_price())));
                        user.getProduct().add(product);
                        product.setUser(user);
                        this.userRepository.save(user);
                        session.setAttribute("message", new Message("Your product has been added!", "alert-success"));
                    }
                } else {
                    session.setAttribute("message", new Message("Invalid URL. Please try again.", "alert-danger"));
                }
            }
        } catch (Exception e) {
            session.setAttribute("message", new Message("Something went wrong! Please try again.", "alert-danger"));
        }
        return "redirect:/user/add-product";
    }
    private boolean isProductExistsForUser(Product product, User user) {
        List<Product> userProducts = user.getProduct();
        for (Product userProduct : userProducts) {
            if (userProduct.getP_name().equals(product.getP_name()) && userProduct.getP_url().equals(product.getP_url())) {
                return true; // Product already exists for the user
            }
        }
        return false; // Product does not exist for the user
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping("/product-list")
    public String List(Model model, Principal principal){
        try{
            String email = principal.getName();
            User user = this.userRepository.getUserByUserName(email);
            List<Product> products = user.getProduct();
            model.addAttribute("user",user);
            model.addAttribute("list",products);
            model.addAttribute("productService", new ProductService());
            model.addAttribute("UrlCoding",new UrlCoding());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "product_list1";
    }

    @RequestMapping("/deleteproduct/{productID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String DeleteProduct(@PathVariable("productID") int productID,Principal principal,HttpSession session){
        try {
            Optional<Product> product = this.productRepository.findById(productID);
            if(product.get().getUser() == this.userRepository.getUserByUserName(principal.getName())){
                Product pro = product.get();
                User user = pro.getUser();
                user.getProduct().remove(pro);
                this.userRepository.save(user); // Update the user entity
                this.productRepository.deleteById(productID); // Delete the product
            }else {
                session.setAttribute("message", new Message("You cannot access other's product!", "alert-danger"));
                System.out.println("you are accessing other contact");
                return "redirect:/user/dashboard";
            }


        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
        }

        return "redirect:/user/product-list";
    }

    @RequestMapping("/editproduct/{productID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String EditProduct(@PathVariable("productID") int productID,Model model,Principal principal,HttpSession session){
        try{
            Optional<Product> product = this.productRepository.findById(productID);
            if(product.get().getUser() == this.userRepository.getUserByUserName(principal.getName())){
                //username
                String email = principal.getName();
                User user = this.userRepository.getUserByUserName(email);
                model.addAttribute("user",user);

                //edit

                Product pro = product.get();
//            System.out.println(pro.getP_name()+" "+pro.getP_url()+" "+pro.getT_price());
                model.addAttribute("product",pro);
                return "edit";
            }else {
                session.setAttribute("message", new Message("You are the owner of that product!", "alert-danger"));
                System.out.println("you are accessing other contact");
                return "redirect:/user/dashboard";
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return "edit";
    }

    @RequestMapping("/edit-product")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String Edit_Product(@ModelAttribute Product product,Principal principal,HttpSession session){
        try {
            String email = principal.getName();
            User user = this.userRepository.getUserByUserName(email);
            if(product.getUser() == this.userRepository.getUserByUserName(principal.getName())){
                product.setUser(user);
                this.productRepository.save(product);
                session.setAttribute("message", new Message("Your product has been modified!", "alert-success"));
            }else {
                session.setAttribute("message", new Message("You are the owner of that product!", "alert-danger"));
                System.out.println("you are accessing other contact");
                return "redirect:/user/dashboard";
            }


        }catch (Exception e){
            session.setAttribute("message", new Message("Something went wrong! Please try again.", "alert-danger"));

        }
        return "redirect:/user/product-list";
    }



}
