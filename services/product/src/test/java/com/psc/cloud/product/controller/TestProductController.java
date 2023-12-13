package com.psc.cloud.product.controller;

import com.psc.cloud.api.dto.Product;
import com.psc.cloud.product.domain.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureWebTestClient
public class TestProductController {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    public void setupDb() {
        System.out.println("BeforeEach");
        repository.deleteAll();
    }

    private WebTestClient.BodyContentSpec getProduct(String productId, HttpStatus expectedStatus) {
        return client.get()
                .uri("/product" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec addProduct(int productId, HttpStatus expectedStatus) {
        Product product = new Product(productId, productId + "_name", productId + "_info");
        WebTestClient.BodyContentSpec result =
          client.post()
                .uri("/product")
                .body(Mono.just(product),Product.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
        return result;
    }

    private WebTestClient.BodyContentSpec deleteProduct(String productId, HttpStatus expectedStatus) {
            return client.delete()
                        .uri("/product" + productId)
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isEqualTo(expectedStatus)
                        .expectBody();
    }

    @Test
    @Order(1)
    public void _getProduct() {
        int productId = 1;
        addProduct(productId,OK);
        Assertions.assertTrue(repository.findByProductId(productId).isPresent());
        getProduct("/"+ productId, OK).jsonPath("$.productId").isEqualTo(productId);
    }

    @Test
    @Order(2)
    public void _duplicateProductError() {
        int productId = 1;
        addProduct(productId, OK);
        Assertions.assertEquals(true, repository.findByProductId(productId).isPresent());
      //  addProduct(productId, UNPROCESSABLE_ENTITY)
      //        .jsonPath("$.path").isEqualTo("/product");
      //          .jsonPath("$.message").isEqualTo("Duplicate Product Id: " + productId);


    }

    @Test
    @Order(3)
    public void _deleteProduct() {
        int productId = 1;
        addProduct(productId, OK);
        Assertions.assertTrue(repository.findByProductId(productId).isPresent());

        deleteProduct("/" + productId, OK);
        Assertions.assertFalse(repository.findByProductId(productId).isPresent());
    }

    @Test
    @Order(4)
    public void _setProductInvalidParameter() {
        getProduct("/one", BAD_REQUEST)
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    @Order(5)
    public void _getProductInvalidNumber() {
        int productIdInvalid = -1;
        getProduct("/" + productIdInvalid, UNPROCESSABLE_ENTITY)
                .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }

    @Test
    @Order(6)
    public void _getProductNotFound() {
        int productIdNotFound = 13;
        getProduct("/" + productIdNotFound, NOT_FOUND)
                .jsonPath("$.message").isEqualTo("No productId: " + productIdNotFound);
    }



}
