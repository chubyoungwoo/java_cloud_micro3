package com.psc.cloud.composite.controller;

import com.psc.cloud.api.controller.CompositeControllerInterface;
import com.psc.cloud.api.dto.Composite;
import com.psc.cloud.api.dto.Product;
import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.dto.Review;
import com.psc.cloud.api.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class CompositeController implements CompositeControllerInterface {

    private final IntegerateModule integeration;

    @Override
    public void createComposite(Composite body) {
        try {
            Product product = new Product(body.getProductId(), body.getProductName(),null);
            integeration.createProduct(product);

            if (body.getRecommendList() != null) {
                body.getRecommendList().forEach((r) -> {
                    Recommend recommend = new Recommend(body.getProductId(), r.getRecommendId(),r.getAuthor(),r.getContent());
                    integeration.createRecommend(recommend);
                });
            }

            if (body.getReviewList() != null) {
                body.getReviewList().forEach((r) -> {
                    Review review = new Review(body.getProductId(), r.getReviewId(),r.getAuthor(),r.getSubject(), r.getContent());
                    integeration.createReview(review);
                });
            }

        } catch (RuntimeException re) {
            log.error("createComposite failed", re);
            throw re;
        }

    }

    @Override
    public Composite getComposite(int productId) {
        Product product = integeration.getProduct(productId);
        if (product == null) throw new NotFoundException("No productId: " + productId);

        List<Recommend> recommendList = integeration.getRecommends(productId);
        List<Review> reviewList = integeration.getReviews(productId);
        return  new Composite(product.getProductId(), product.getProductName(),recommendList, reviewList);
    }

    @Override
    public void deleteComposite(int productId) {
        integeration.deleteProduct(productId);
        integeration.deleteRecommends(productId);
        integeration.deleteReviews(productId);

    }
}
