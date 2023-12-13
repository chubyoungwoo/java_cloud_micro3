package com.psc.cloud.review.controller;

import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.dto.Review;
import com.psc.cloud.review.domain.ReviewEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

public class TestReviewMapper {

    private ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

    @Test
    public void mapperTests() {
        Assertions.assertNotNull(mapper);

        Review dto = new Review(1,1,"author","subject","content");

        ReviewEntity entity = mapper.dtoToEntity(dto);

        Assertions.assertEquals(dto.getProductId(),entity.getProductId());
        Assertions.assertEquals(dto.getReviewId(),entity.getReviewId());
        Assertions.assertEquals(dto.getAuthor(),entity.getAuthor());
        Assertions.assertEquals(dto.getSubject(),entity.getSubject());
        Assertions.assertEquals(dto.getContent(),entity.getContent());

        Review dto2 = mapper.entityToDto(entity);

        Assertions.assertEquals(dto.getProductId(),dto2.getProductId());
        Assertions.assertEquals(dto.getReviewId(),dto2.getReviewId());
        Assertions.assertEquals(dto.getAuthor(),dto2.getAuthor());
        Assertions.assertEquals(dto.getSubject(),dto2.getSubject());
        Assertions.assertEquals(dto.getContent(),dto2.getContent());

    }

    @Test
    public void _mapperListTests() {
        Assertions.assertNotNull(mapper);
        Review dto = new Review(1,1,"author","subject","content");
        List<Review> apiList = Collections.singletonList(dto);

        List<ReviewEntity> entityList = mapper.dotListToEntityList(apiList);
        Assertions.assertEquals(apiList.size(), entityList.size());

        ReviewEntity entity = entityList.get(0);

        Assertions.assertEquals(dto.getProductId(),entity.getProductId());
        Assertions.assertEquals(dto.getReviewId(),entity.getReviewId());
        Assertions.assertEquals(dto.getAuthor(),entity.getAuthor());
        Assertions.assertEquals(dto.getSubject(),entity.getSubject());
        Assertions.assertEquals(dto.getContent(),entity.getContent());

        List<Review> api2List = mapper.entityListToDtoList(entityList);
        Assertions.assertEquals(apiList.size(), api2List.size());

        Review api2 = api2List.get(0);

        Assertions.assertEquals(dto.getProductId(),api2.getProductId());
        Assertions.assertEquals(dto.getReviewId(),api2.getReviewId());
        Assertions.assertEquals(dto.getAuthor(),api2.getAuthor());
        Assertions.assertEquals(dto.getSubject(),api2.getSubject());
        Assertions.assertEquals(dto.getContent(),api2.getContent());

    }
}
