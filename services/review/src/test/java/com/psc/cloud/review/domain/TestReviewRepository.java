package com.psc.cloud.review.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestReviewRepository {

    @Autowired
    private ReviewRepository repository;
    private ReviewEntity savedEntity;

    @BeforeEach
    public void setupDb() {
        repository.deleteAll();
        ReviewEntity entity = new ReviewEntity(1,2,"a","s","c");
        savedEntity = repository.save(entity);
    }

    private void assertEqualsReview(ReviewEntity expectedEntity, ReviewEntity actualEntity) {
        Assertions.assertEquals(expectedEntity.getId(),      actualEntity.getId());
        Assertions.assertEquals(expectedEntity.getVersion(),      actualEntity.getVersion());
        Assertions.assertEquals(expectedEntity.getProductId(),      actualEntity.getProductId());
        Assertions.assertEquals(expectedEntity.getReviewId(),      actualEntity.getReviewId());
        Assertions.assertEquals(expectedEntity.getAuthor(),      actualEntity.getAuthor());
        Assertions.assertEquals(expectedEntity.getSubject(),      actualEntity.getSubject());
        Assertions.assertEquals(expectedEntity.getContent(),      actualEntity.getContent());
    }

    @Test
    @Order(1)
    public void _create() {
        System.out.println("1. create");
        ReviewEntity newEntity = new ReviewEntity(1,3,"a","s","c");
        repository.save(newEntity);
        ReviewEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEqualsReview(newEntity, foundEntity);
        Assertions.assertEquals(2,repository.count());
    }

    @Test
    @Order(2)
    public void _update() {
        System.out.println("2. update");
        savedEntity.setAuthor("a2");
        repository.save(savedEntity);

        ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();
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
        List<ReviewEntity> entityList = repository.findByProductId(savedEntity.getProductId());

        Assertions.assertEquals(entityList.size(),1);
        assertEqualsReview(savedEntity, entityList.get(0));

    }

     @Test
    @Order(5)
    public void _duplicateError() {
        System.out.println("5. _duplicateError");
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            ReviewEntity entity = new ReviewEntity(1,2,"a","s","c");
            repository.save(entity);
        });
    }

    @Test
    @Order(6)
    public void _optimisticLockError() {
        System.out.println("6. _optimisticLockError");

        ReviewEntity entity1 = repository.findById(savedEntity.getId()).get();
        ReviewEntity entity2 = repository.findById(savedEntity.getId()).get();

        entity1.setAuthor("a1");
        repository.save(entity1);

        try {
            entity2.setAuthor("a2");
            repository.save(entity2);
            Assertions.fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {
            e.printStackTrace();
        }
        ReviewEntity updateEntity = repository.findById(savedEntity.getId()).get();
        Assertions.assertEquals(1,(int)updateEntity.getVersion());
        Assertions.assertEquals("a1", updateEntity.getAuthor());
    }
}
