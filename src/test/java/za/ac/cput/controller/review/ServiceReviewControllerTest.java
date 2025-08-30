package za.ac.cput.controller.review;

/* MobileGlow Car Wash
   Service Review Controller Test Class
   Author: Inga Zekani (221043756)
 */

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.factory.review.ServiceReviewFactory;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ServiceReviewControllerTest {

    @LocalServerPort
    private int port;

    private static ServiceReview serviceReview = ServiceReviewFactory.createServiceReview(
            5,
            "Excellent service!",
            new Date());
    private static ServiceReview serviceReview_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/api/service-reviews";
    

    @Test
    void a_create() {
        assertNotNull(serviceReview, "Factory should create a valid service review");
        String url = BASE_URL + "/create";

        ResponseEntity<ServiceReview> postResponse = restTemplate.postForEntity(url, serviceReview, ServiceReview.class);
        assertNotNull(postResponse.getBody());
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

        serviceReview_with_Id = postResponse.getBody();
        assertNotNull(serviceReview_with_Id.getReviewID());
        System.out.println("Created ServiceReview: " + serviceReview_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(serviceReview_with_Id, "ServiceReview is null");
        String url = BASE_URL + "/read/" + serviceReview_with_Id.getReviewID();

        ResponseEntity<ServiceReview> response = restTemplate.getForEntity(url, ServiceReview.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Read ServiceReview: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(serviceReview_with_Id, "ServiceReview is null");
        String url = BASE_URL + "/update/" + serviceReview_with_Id.getReviewID();

        // Use factory to create updated review data
        ServiceReview updatedReviewData = ServiceReviewFactory.createServiceReview(
                4,
                "Very good service, but could be improved",
                new Date());
        assertNotNull(updatedReviewData, "Factory should create updated review");

        // Set the same ID for update
        ServiceReview updatedReview = new ServiceReview.Builder()
                .copy(updatedReviewData)
                .setReviewID(serviceReview_with_Id.getReviewID())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ServiceReview> requestEntity = new HttpEntity<>(updatedReview, headers);

        ResponseEntity<ServiceReview> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ServiceReview.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ServiceReview updated = response.getBody();
        assertEquals(4, updated.getRating());
        assertEquals("Very good service, but could be improved", updated.getComments());
        System.out.println("Updated ServiceReview: " + updated);
    }

    @Test
    void d_getAll() {
        String url = BASE_URL + "/all";
        ResponseEntity<ServiceReview[]> response = restTemplate.getForEntity(url, ServiceReview[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("All ServiceReviews (" + response.getBody().length + "):");
        for (ServiceReview review : response.getBody()) {
            System.out.println(review);
        }
    }

    @Test
    void e_delete() {
        String url = BASE_URL + "/delete/" + serviceReview_with_Id.getReviewID();
        restTemplate.delete(url);

        // Use String.class to handle error messages
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + "/read/" + serviceReview_with_Id.getReviewID(),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("ServiceReview deleted successfully: " + response.getBody());
    }
}
