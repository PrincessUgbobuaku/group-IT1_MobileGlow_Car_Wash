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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ServiceReviewControllerTest {

    @LocalServerPort
    private int port;


    private static ServiceReview serviceReview = new ServiceReview.Builder()
            .setReviewID(1L)
            .setRating(5)
            .setComments("Excellent service!")
            .setReviewDate(new Date())
            .build();

    private static ServiceReview serviceReview_with_Id;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/service-reviews";
    }

    @Test
    void a_create() {
        String url = getBaseUrl() + "/create";
        System.out.println("POST URL: " + url);

        ResponseEntity<ServiceReview> postResponse = restTemplate.postForEntity(url, serviceReview, ServiceReview.class);

        assertNotNull(postResponse.getBody());
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        System.out.println("postResponse: " + postResponse.getBody());

        serviceReview_with_Id = postResponse.getBody();
        assertNotNull(serviceReview_with_Id);
        assertNotNull(serviceReview_with_Id.getReviewID());
        System.out.println("serviceReview_with_Id: " + serviceReview_with_Id);
    }

    @Test
    void b_read() {
        assertNotNull(serviceReview_with_Id, "ServiceReview is null");
        String url = getBaseUrl() + "/read/" + serviceReview_with_Id.getReviewID();
        System.out.println("GET URL: " + url);

        ResponseEntity<ServiceReview> response = restTemplate.getForEntity(url, ServiceReview.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(serviceReview_with_Id.getReviewID(), response.getBody().getReviewID());
        System.out.println("Read response: " + response.getBody());
    }

    @Test
    void c_update() {
        assertNotNull(serviceReview_with_Id, "ServiceReview is null");
        String url = getBaseUrl() + "/update/" + serviceReview_with_Id.getReviewID();
        System.out.println("PUT URL: " + url);

        ServiceReview updatedReview = new ServiceReview.Builder()
                .copy(serviceReview_with_Id)
                .setRating(4)
                .setComments("Very good service, but could be improved")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ServiceReview> requestEntity = new HttpEntity<>(updatedReview, headers);

        ResponseEntity<ServiceReview> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ServiceReview.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ServiceReview updated = response.getBody();
        assertEquals(4, updated.getRating());
        assertEquals("Very good service, but could be improved", updated.getComments());
        System.out.println("Updated ServiceReview: " + updated);
    }

    @Test
    void d_getAll() {
        String url = getBaseUrl() + "/all";
        System.out.println("GET ALL URL: " + url);

        ResponseEntity<ServiceReview[]> response = restTemplate.getForEntity(url, ServiceReview[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("All ServiceReviews (" + response.getBody().length + "):");
        for (ServiceReview review : response.getBody()) {
            System.out.println(review);
        }
    }

//    @Test
//    void e_delete() {
//        String url = getBaseUrl() + "/delete/" + serviceReview_with_Id.getReviewID();
//        System.out.println("Deleting service review: " + serviceReview_with_Id);
//        this.restTemplate.delete(url);
//
//        // Verify if the object is deleted
//        ResponseEntity<ServiceReview> readReview =
//                this.restTemplate.getForEntity(getBaseUrl() + "/read/" + serviceReview_with_Id.getReviewID(), ServiceReview.class);
//
//        // Expect 404 after deletion
//        assertEquals(HttpStatus.NOT_FOUND, readReview.getStatusCode());
//        System.out.println("After deletion, status code: " + readReview.getStatusCode());
//    }
}