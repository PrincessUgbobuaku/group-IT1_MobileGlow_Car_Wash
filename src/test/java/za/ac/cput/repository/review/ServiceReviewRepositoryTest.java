package za.ac.cput.repository.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.factory.review.ServiceReviewFactory;
import za.ac.cput.repository.review.ServiceReviewRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceReviewRepositoryTest {

    private ServiceReviewRepository repository;
    private ServiceReview review;

    @BeforeEach
    void setUp() {
        repository = new ServiceReviewRepository();
        review = ServiceReviewFactory.createServiceFactory1(5, "Great service!", new Date());
        repository.create(review);
    }

    @Test
    void testCreateAndRead() {
        ServiceReview saved = repository.read(review.getReviewID());
        assertNotNull(saved);
        assertEquals("Great service!", saved.getComments());
    }

    @Test
    void testUpdate() {
        ServiceReview updated = new ServiceReview.Builder()
                .copy(review)
                .setComments("Updated review comment")
                .build();
        repository.update(updated);

        ServiceReview result = repository.read(updated.getReviewID());
        assertEquals("Updated review comment", result.getComments());
    }

    @Test
    void testDelete() {
        boolean deleted = repository.delete(review.getReviewID());
        assertTrue(deleted);
        assertNull(repository.read(review.getReviewID()));
    }
}
