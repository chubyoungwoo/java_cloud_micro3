package com.psc.cloud.review.controller;

import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.dto.Review;
import com.psc.cloud.review.domain.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mappings({
    })
    Review entityToDto(ReviewEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)

    })
    ReviewEntity dtoToEntity(Review dto);

    List<Review> entityListToDtoList(List<ReviewEntity> entity);
    List<ReviewEntity> dotListToEntityList(List<Review> dto);
}
