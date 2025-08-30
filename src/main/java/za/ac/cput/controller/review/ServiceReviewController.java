package za.ac.cput.controller.review;

/* MobileGlow Car Wash
   Service Review Controller Class
   Author: Inga Zekani (221043756)
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.service.review.IServiceReviewService;
import za.ac.cput.service.review.ServiceReviewService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/service-reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceReviewController {

    private final IServiceReviewService serviceReviewService;

    @Autowired
    public ServiceReviewController(IServiceReviewService serviceReviewService) {
        this.serviceReviewService = serviceReviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ServiceReview serviceReview) {
        try {
            ServiceReview createdReview = serviceReviewService.create(serviceReview);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating service review");
        }
    }

    @GetMapping("/read/{reviewID}")
    public ResponseEntity<?> read(@PathVariable Long reviewID) {
        try {
            ServiceReview serviceReview = serviceReviewService.read(reviewID);
            return ResponseEntity.ok(serviceReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{reviewID}")
    public ResponseEntity<?> update(@PathVariable Long reviewID, @RequestBody ServiceReview serviceReview) {
        if (!reviewID.equals(serviceReview.getReviewID())) {
            return ResponseEntity.badRequest().body("ID in path and body do not match");
        }

        try {
            ServiceReview updatedReview = serviceReviewService.update(serviceReview);
            return ResponseEntity.ok(updatedReview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{reviewID}")
    public ResponseEntity<?> delete(@PathVariable Long reviewID) {
        try {
            boolean deleted = serviceReviewService.delete(reviewID);
            return ResponseEntity.ok().body("Service review deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceReview>> getAll() {
        List<ServiceReview> reviews = serviceReviewService.getAll();
        return ResponseEntity.ok(reviews);
    }

}
