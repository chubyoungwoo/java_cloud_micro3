package com.psc.cloud.recommend.domain;

import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRecommendRepository {

    @Autowired
    private RecommendRepository repository;
    private RecommendEntity savedEntity;

    private void assertEqualsRecommendation(RecommendEntity expectedEntity, RecommendEntity actualEntity) {
        Assertions.assertEquals(expectedEntity.getId(),      actualEntity.getId());
        Assertions.assertEquals(expectedEntity.getVersion(),      actualEntity.getVersion());
        Assertions.assertEquals(expectedEntity.getProductId(),      actualEntity.getProductId());
        Assertions.assertEquals(expectedEntity.getRecommendId(),      actualEntity.getRecommendId());
        Assertions.assertEquals(expectedEntity.getAuthor(),      actualEntity.getAuthor());
        Assertions.assertEquals(expectedEntity.getContent(),      actualEntity.getContent());
    }

    @BeforeEach
    public void setupDb() {
        repository.deleteAll();
        RecommendEntity entity = new RecommendEntity(1, 1, "a", "a");
        savedEntity = repository.save(entity);
        assertEqualsRecommendation(entity,savedEntity);
    }

    @Test
    @Order(1)
    public void _create() {
        System.out.println("1. create");
        RecommendEntity newEntity = new RecommendEntity(1,3,"a","a");
        repository.save(newEntity);
        RecommendEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEqualsRecommendation(newEntity, foundEntity);
        Assertions.assertEquals(2,repository.count());
    }

    @Test
    @Order(2)
    public void _update() {
        System.out.println("2. update");
        savedEntity.setAuthor("a2");
        repository.save(savedEntity);

        RecommendEntity foundEntity = repository.findById(savedEntity.getId()).get();
        Assertions.assertEquals(1,(long) foundEntity.getVersion());
        Assertions.assertEquals("a2", foundEntity.getAuthor());
    }

    @Test
    @Order(3)
    public void _delete() {
        System.out.println("3. delete");
        repository.delete(savedEntity);
        Assertions.assertEquals(false, repository.existsById(savedEntity.getId()));
    }

    @Test
    @Order(4)
    public void _getByProductId() {
        System.out.println("4. _getByProductId");
        List<RecommendEntity> entityList = repository.findByProductId(savedEntity.getProductId());

        Assertions.assertEquals(entityList.size(),1);
        assertEqualsRecommendation(savedEntity, entityList.get(0));

    }

   // @Test
    @Order(5)
    public void _duplicateError() {
        System.out.println("5. _duplicateError");
        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            RecommendEntity entity = new RecommendEntity(1,1,"a","a");
            repository.save(entity);
        });
    }

    @Test
    @Order(6)
    public void _optimisticLockError() {
        System.out.println("6. _optimisticLockError");

        RecommendEntity entity1 = repository.findById(savedEntity.getId()).get();
        RecommendEntity entity2 = repository.findById(savedEntity.getId()).get();

        entity1.setAuthor("a1");
        repository.save(entity1);

        try {
            entity2.setAuthor("a2");
            repository.save(entity2);
            Assertions.fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {
            e.printStackTrace();
        }
        RecommendEntity updateEntity = repository.findById(savedEntity.getId()).get();
        Assertions.assertEquals(1,(int)updateEntity.getVersion());
        Assertions.assertEquals("a1", updateEntity.getAuthor());
    }

    @Test
    @Order(7)
    public void _deleteAll() {
        repository.deleteAll();
    }

}
