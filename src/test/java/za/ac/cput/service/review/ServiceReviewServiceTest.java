package za.ac.cput.service.review;

/* MobileGlow Car Wash
   Service Review Service Test class
   Author: Inga Zekani (221043756)
 */

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.factory.review.ServiceReviewFactory;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ServiceReviewServiceTest {

    @Autowired
    private ServiceReviewService serviceReviewService;

    private static ServiceReview testServiceReview;
    private static ServiceReview savedServiceReviewId;

    @Test
    void a_testCreateServiceReview() {
        // Use Factory class to create service review
        testServiceReview = ServiceReviewFactory.createServiceReview(
                5,
                "Excellent service!",
                new Date());
        assertNotNull(testServiceReview, "Factory should create a valid service review");
        assertNull(testServiceReview.getReviewID(), "New service review should not have ID before saving");

        assertEquals(5, testServiceReview.getRating());
        assertEquals("Excellent service!", testServiceReview.getComments());
        assertNotNull(testServiceReview.getReviewDate());

        ServiceReview saved = serviceReviewService.create(testServiceReview);
        assertNotNull(saved);
        assertNotNull(saved.getReviewID(), "Saved service review should have an ID");
        assertEquals(5, saved.getRating());
        assertEquals("Excellent service!", saved.getComments());
        assertNotNull(saved.getReviewDate());

        savedServiceReviewId = saved;
        System.out.println("Created ServiceReview: " + saved);

    }



    @Test
    void c_testReadServiceReview() {
        assertNotNull(savedServiceReviewId, "ServiceReview ID is null - create test might have failed");

        ServiceReview found = serviceReviewService.read(savedServiceReviewId.getReviewID());
        assertNotNull(found);
        //assertEquals(savedServiceReviewId, found.getReviewID());
        assertEquals(5, found.getRating());
        assertEquals("Excellent service!", found.getComments());
        assertNotNull(found.getReviewDate());
        System.out.println("Read ServiceReview: " + found);
    }

    @Test
    void d_testUpdateServiceReview() {
        assertNotNull(testServiceReview, "Test ServiceReview is null - create test might have failed");

        // Create updated service review using factory with new values
        ServiceReview updatedServiceReview = ServiceReviewFactory.createServiceReview(4, "Very good service, but could be improved", new Date());
        assertNotNull(updatedServiceReview, "Factory should create updated service review");

        // Set the same ID as the original service review for update
        updatedServiceReview = new ServiceReview.Builder()
                .copy(updatedServiceReview)
                .setReviewID(testServiceReview.getReviewID())
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
    void e_testGetAllServiceReviews() {
        List<ServiceReview> serviceReviews = serviceReviewService.getAll();
        assertNotNull(serviceReviews);
        assertFalse(serviceReviews.isEmpty());
        assertTrue(true);

        System.out.println("All ServiceReviews (" + serviceReviews.size() + "):");
        for (ServiceReview serviceReview : serviceReviews) {
            System.out.println(serviceReview);
            // Verify that our test service review is in the list

            // Verify all service reviews have valid data (testing factory validation indirectly)
            assertTrue(serviceReview.getRating() >= 1 && serviceReview.getRating() <= 5,
                    "Rating should be between 1-5");
            assertNotNull(serviceReview.getComments());
            assertNotNull(serviceReview.getReviewDate());
        }
    }

    @Test
    void f_testDeleteServiceReview() {
        assertNotNull(savedServiceReviewId, "ServiceReview ID is null - create test might have failed");

        // First verify the service review exists
        ServiceReview beforeDelete = serviceReviewService.read(savedServiceReviewId.getReviewID());
        assertNotNull(beforeDelete, "ServiceReview should exist before deletion");

        // Delete the service review
        boolean deleteResult = serviceReviewService.delete(savedServiceReviewId.getReviewID());
        assertTrue(deleteResult, "Delete operation should return true");

        System.out.println("ServiceReview deleted successfully. ID: " + savedServiceReviewId.getReviewID());
    }

}
