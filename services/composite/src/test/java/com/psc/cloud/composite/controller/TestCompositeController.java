package com.psc.cloud.composite.controller;

import com.psc.cloud.api.dto.Product;
import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.dto.Review;
import com.psc.cloud.api.exception.InvalidInputException;
import com.psc.cloud.api.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class TestCompositeController {

    private static final int PRODUCT_ID_OK = 1;
    private static final int PRODUCT_ID_NOT_FOUND = 2;
    private static final int PRODUCT_ID_INVALID = 3;

    @Autowired
    private WebTestClient client;

    @MockBean
    private IntegerateModule integerateModule;

    @BeforeEach
    public void setUp() {
        System.out.println("시작");
        when(integerateModule.getProduct(PRODUCT_ID_OK))
                .thenReturn(new Product(PRODUCT_ID_OK, "name", null));
        when(integerateModule.getRecommends(PRODUCT_ID_OK))
               .thenReturn(singletonList(new Recommend(PRODUCT_ID_OK, 1, "author", "content")));
        when(integerateModule.getReviews(PRODUCT_ID_OK))
                .thenReturn(singletonList(new Review(PRODUCT_ID_OK,1,"author","subject","content")));


        // 실제 테스트 되는 것은 RestControllerExceptionHandler @RestControllerAdvice 입니다.
        when(integerateModule.getProduct(PRODUCT_ID_NOT_FOUND))
                .thenThrow(new NotFoundException("No productId:" + PRODUCT_ID_NOT_FOUND));
        when(integerateModule.getProduct(PRODUCT_ID_INVALID))
               .thenThrow(new InvalidInputException("Invalid productId:" + PRODUCT_ID_INVALID));


    }

    @Test
    public void composite() {

        System.out.println("시작1");

        String result = client.get()
                .uri("/composite/" + PRODUCT_ID_OK)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
                .jsonPath("$.recommendList.length()").isEqualTo(1)
                .jsonPath("$.reviewList.length()").isEqualTo(1)
                .returnResult().toString();

        System.out.println(result);


    }
}
