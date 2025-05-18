package za.ac.cput.factory.review;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.factory.review.ServiceReviewFactory;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServiceReviewFactoryTest {

    @Test
    void testBuildValidServiceReview() {
        ServiceReview review = ServiceReviewFactory.createServiceFactory1(5, "Excellent wash!", new Date());
        assertNotNull(review);
        assertNotNull(review.getReviewID());
        assertEquals(5, review.getRating());
        assertEquals("Excellent wash!", review.getComments());

        System.out.println(review);
    }

    @Test
    void testBuildInvalidServiceReviewReturnsNull() {
        ServiceReview review = ServiceReviewFactory.createServiceFactory1(0, "", null);
        assertNull(review);

        System.out.println(review);
    }
}

