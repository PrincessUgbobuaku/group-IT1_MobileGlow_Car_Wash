package za.ac.cput.repository.review;

import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.repository.review.ServiceReviewRepository;
import za.ac.cput.repository.review.impl.IServiceReviewRepository;

import java.util.ArrayList;
import java.util.List;

public class ServiceReviewRepository implements IServiceReviewRepository {

    private final List<ServiceReview> reviewList = new ArrayList<>();

    @Override
    public ServiceReview create(ServiceReview review) {
        reviewList.add(review);
        return review;
    }

    @Override
    public ServiceReview read(String reviewID) {
        return reviewList.stream()
                .filter(r -> r.getReviewID().equals(reviewID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ServiceReview update(ServiceReview review) {
        ServiceReview existing = read(review.getReviewID());
        if (existing != null) {
            reviewList.remove(existing);
            reviewList.add(review);
            return review;
        }
        return null;
    }

    @Override
    public boolean delete(String reviewID) {
        ServiceReview review = read(reviewID);
        return review != null && reviewList.remove(review);
    }
}
