package com.psc.cloud.review.controller;

import com.psc.cloud.api.controller.ReviewControllerInterface;
import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.dto.Review;
import com.psc.cloud.api.exception.InvalidInputException;
import com.psc.cloud.review.domain.ReviewEntity;
import com.psc.cloud.review.domain.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ReviewController implements ReviewControllerInterface {

    private final ReviewRepository repository;
    private final ReviewMapper mapper;


    @Override
    public Review createReview(Review body) {
        try {
            ReviewEntity entity = mapper.dtoToEntity(body);
            ReviewEntity newEntity = repository.save(entity);

            log.debug("createReview: product Id {}/ review Id {}", body.getProductId(), body.getReviewId());
            return mapper.entityToDto(newEntity);

        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException(("Duplicate key, Product Id: " + body.getProductId() + ", Review Id: " + body.getReviewId()));
        }
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        List<ReviewEntity> entityList = repository.findByProductId(productId);
        List<Review> list = mapper.entityListToDtoList(entityList);
        log.debug("getReviews: size: {}", list.size());
        return list;
    }

    @Override
    public void deleteReviews(int productId) {
        log.debug("deleteReviews: productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }
}
