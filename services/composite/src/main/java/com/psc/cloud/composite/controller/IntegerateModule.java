package com.psc.cloud.composite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psc.cloud.api.controller.ProductControllerInterface;
import com.psc.cloud.api.controller.RecommendControllerInterface;
import com.psc.cloud.api.controller.ReviewControllerInterface;
import com.psc.cloud.api.dto.Product;
import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.dto.Review;

import com.psc.cloud.api.exception.InvalidInputException;
import com.psc.cloud.api.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Component
public class IntegerateModule implements ProductControllerInterface, RecommendControllerInterface, ReviewControllerInterface {

   private final RestTemplate restTemplate;
   private final ObjectMapper mapper;

   private final String productServiceUrl;
   private final String recommendServiceUrl;
   private final String reviewServiceUrl;

    public IntegerateModule(
       RestTemplate restTemplate,
       ObjectMapper mapper,

       @Value("${app.product.protocol}") String productProtocol,
       @Value("${app.product.host}") String productHost,
       @Value("${app.product.port}") int productPort,
       @Value("${app.product.service}") String productService,

       @Value("${app.recommend.protocol}") String recommendProtocol,
       @Value("${app.recommend.host}") String recommendHost,
       @Value("${app.recommend.port}") int recommendPort,
       @Value("${app.recommend.service}") String recommendService,

       @Value("${app.review.protocol}") String reviewProtocol,
       @Value("${app.review.host}") String reviewHost,
       @Value("${app.review.port}") int reviewPort,
       @Value("${app.review.service}") String reviewService

    ) {
       this.restTemplate = restTemplate;
       this.mapper = mapper;
       this.productServiceUrl = productProtocol + "://" + productHost + ":" + productPort + "/" + productService;
       this.recommendServiceUrl = recommendProtocol + "://" + recommendHost + ":" + recommendPort + "/" + recommendService;
       this.reviewServiceUrl = reviewProtocol + "://" + reviewHost + ":" + reviewPort + "/" + reviewService;

       log.debug("==============");
       log.debug(productServiceUrl);
       log.debug(recommendServiceUrl);
       log.debug(reviewServiceUrl);
       log.debug("===============");
    }

    private RuntimeException httpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {
            case NOT_FOUND:
                return new NotFoundException(ex.getResponseBodyAsString());
            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(ex.getResponseBodyAsString());
            default:
                log.warn("Got a unexpected HTTP error: {}, will return it", ex.getStatusCode());
                log.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    @Override
    public Product createProduct(Product body) {
        try {
            String url = productServiceUrl;
            Product product = restTemplate.postForObject(url, body, Product.class);
            log.debug("##################################################");
            log.debug("createProduct: {}", url);
            log.debug("product {}", product.toString());
            log.debug("##################################################");
            return product;
        } catch (HttpClientErrorException ex) {
           throw httpClientException(ex);
        }
    }

    @Override
    public Product getProduct(int productId) {
        try {
            log.debug("호출 ##################################################");
            String url = productServiceUrl + "/" + productId;
            Product product = restTemplate.getForObject(url, Product.class);
            log.debug("##################################################");
            log.debug("getProduct: {}", url);
            log.debug("product {}", product.toString());
            log.debug("##################################################");
            return product;
        } catch (HttpClientErrorException ex) {
            throw httpClientException(ex);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            String url = productServiceUrl + "/" + productId;
            restTemplate.delete(url);
            log.debug("##################################################");
            log.debug("deleteProduct: {}", url);
            log.debug("productId {}", productId);
            log.debug("##################################################");
        } catch (HttpClientErrorException ex) {
            throw httpClientException(ex);
        }
    }

    @Override
    public Recommend createRecommend(Recommend body) {
        try {
            String url = recommendServiceUrl;
            Recommend recommend = restTemplate.postForObject(url, body, Recommend.class);
            log.debug("##################################################");
            log.debug("createRecommend: {}", url);
            log.debug("recommend {}", recommend.toString());
            log.debug("##################################################");
            return recommend;
        } catch (HttpClientErrorException ex) {
            throw httpClientException(ex);
        }
    }

    @Override
    public List<Recommend> getRecommends(int productId) {
        try {
            String url = recommendServiceUrl + "?productId=" + productId;
            List<Recommend> recommends = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Recommend>>(){}).getBody();

            log.debug("##################################################");
            log.debug("getRecommends: {}", url);
            log.debug("productId:{} recommends size: {}", productId, recommends.size());
            log.debug("##################################################");
            return recommends;
        } catch (Exception ex) {
            log.warn("getRecommends exception, error msg: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteRecommends(int productId) {
        try {
            String url = recommendServiceUrl + "?productId=" + productId;
            restTemplate.delete(url);
            log.debug("##################################################");
            log.debug("deleteRecommends: {}", url);
            log.debug("productId {}", productId);
            log.debug("##################################################");
        } catch (HttpClientErrorException ex) {
            throw httpClientException(ex);
        }
    }

    @Override
    public Review createReview(Review body) {
        try {
            String url = reviewServiceUrl;
            Review review = restTemplate.postForObject(url, body, Review.class);
            log.debug("##################################################");
            log.debug("createReview: {}", url);
            log.debug("review {}", review.toString());
            log.debug("##################################################");
            return review;
        } catch (HttpClientErrorException ex) {
            throw httpClientException(ex);
        }
    }

    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = reviewServiceUrl + "?productId=" + productId;
            List<Review> reviews = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>(){}).getBody();

            log.debug("##################################################");
            log.debug("getReviews: {}", url);
            log.debug("productId:{} reviews size: {}", productId, reviews.size());
            log.debug("##################################################");
            return reviews;
        } catch (Exception ex) {
            log.warn("getReviews exception, error msg: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteReviews(int productId) {
        try {
            String url = reviewServiceUrl + "?productId=" + productId;
            restTemplate.delete(url);
            log.debug("##################################################");
            log.debug("deleteReviews: {}", url);
            log.debug("productId {}", productId);
            log.debug("##################################################");
        } catch (HttpClientErrorException ex) {
            throw httpClientException(ex);
        }
    }
}
