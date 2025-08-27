package za.ac.cput.service.review;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.review.ServiceReview;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ServiceReviewServiceTest {

    @Autowired
    private ServiceReviewService serviceReviewService;

    private static ServiceReview testServiceReview;
    private static Long savedServiceReviewId;

    @Test
    void a_testCreateServiceReview() {
        ServiceReview serviceReview = new ServiceReview.Builder()
                .setRating(5)
                .setComments("Excellent service!")
                .setReviewDate(new Date())
                .build();

        ServiceReview saved = serviceReviewService.create(serviceReview);
        assertNotNull(saved.getReviewID());
        assertEquals(5, saved.getRating());
        assertEquals("Excellent service!", saved.getComments());
        assertNotNull(saved.getReviewDate());
        System.out.println("Created ServiceReview: " + saved);

        testServiceReview = saved;
        savedServiceReviewId = saved.getReviewID();
    }

    @Test
    void b_testReadServiceReview() {
        assertNotNull(savedServiceReviewId, "ServiceReview ID is null - create test might have failed");

        ServiceReview found = serviceReviewService.read(savedServiceReviewId);
        assertNotNull(found);
        assertEquals(savedServiceReviewId, found.getReviewID());
        assertEquals(5, found.getRating());
        assertEquals("Excellent service!", found.getComments());
        assertNotNull(found.getReviewDate());
        System.out.println("Read ServiceReview: " + found);
    }

    @Test
    void c_testUpdateServiceReview() {
        assertNotNull(testServiceReview, "Test ServiceReview is null - create test might have failed");

        ServiceReview updatedServiceReview = new ServiceReview.Builder()
                .copy(testServiceReview)
                .setRating(4)
                .setComments("Very good service, but could be improved")
                .setReviewDate(new Date())
                .build();

        ServiceReview updated = serviceReviewService.update(updatedServiceReview);
        assertNotNull(updated);
        assertEquals(testServiceReview.getReviewID(), updated.getReviewID());
        assertEquals(4, updated.getRating());
        assertEquals("Very good service, but could be improved", updated.getComments());
        assertNotNull(updated.getReviewDate());
        System.out.println("Updated ServiceReview: " + updated);

        testServiceReview = updated; // Update the reference for later tests
    }

    @Test
    void d_testGetAllServiceReviews() {
        List<ServiceReview> serviceReviews = serviceReviewService.getAll();
        assertNotNull(serviceReviews);
        assertFalse(serviceReviews.isEmpty());
        assertTrue(serviceReviews.size() >= 1);

        System.out.println("All ServiceReviews (" + serviceReviews.size() + "):");
        for (ServiceReview serviceReview : serviceReviews) {
            System.out.println(serviceReview);
            // Verify that our test service review is in the list
            if (serviceReview.getReviewID().equals(savedServiceReviewId)) {
                assertEquals(4, serviceReview.getRating());
                assertEquals("Very good service, but could be improved", serviceReview.getComments());
            }
        }
    }

    @Test
    void e_testDeleteServiceReview() {
        assertNotNull(savedServiceReviewId, "ServiceReview ID is null - create test might have failed");

        // First verify the service review exists
        ServiceReview beforeDelete = serviceReviewService.read(savedServiceReviewId);
        assertNotNull(beforeDelete, "ServiceReview should exist before deletion");

        // Delete the service review
        boolean deleteResult = serviceReviewService.delete(savedServiceReviewId);
        assertTrue(deleteResult, "Delete operation should return true");

        // Verify the service review no longer exists
        ServiceReview afterDelete = serviceReviewService.read(savedServiceReviewId);
        assertNull(afterDelete, "ServiceReview should be null after deletion");

        System.out.println("ServiceReview deleted successfully. ID: " + savedServiceReviewId);
    }

}