package com.psc.cloud.recommend.controller;

import com.mongodb.DuplicateKeyException;
import com.psc.cloud.api.controller.RecommendControllerInterface;
import com.psc.cloud.api.dto.Recommend;
import com.psc.cloud.api.exception.InvalidInputException;
import com.psc.cloud.recommend.domain.RecommendEntity;
import com.psc.cloud.recommend.domain.RecommendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class RecommendController implements RecommendControllerInterface {

    private final RecommendRepository repository;
    private final RecommendMapper mapper;


    @Override
    public Recommend createRecommend(Recommend body) {
       try {
           RecommendEntity entity = mapper.dtoToEntity(body);
           RecommendEntity newEntity = repository.save(entity);
           return mapper.entityToDto(newEntity);
       } catch (DuplicateKeyException dke) {
           throw new InvalidInputException(("Duplicate key, Product Id: " + body.getProductId() + ", Recommend Id: " + body.getRecommendId()));
       }
    }

    @Override
    public List<Recommend> getRecommends(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        List<RecommendEntity> entityList = repository.findByProductId(productId);
        List<Recommend> list = mapper.entityListToApiList(entityList);
        log.debug("getRecommends: response size: {}", list.size());
        return list;
    }

    @Override
    public void deleteRecommends(int productId) {
        repository.deleteAll(repository.findByProductId(productId));
    }
}
