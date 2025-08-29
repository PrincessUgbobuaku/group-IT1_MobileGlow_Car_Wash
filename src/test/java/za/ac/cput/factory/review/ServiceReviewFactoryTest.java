package za.ac.cput.factory.review;

/* MobileGlow Car Wash
   Service Review Factory
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.review.ServiceReview;
import za.ac.cput.util.Helper;

import java.util.Date;

public class ServiceReviewFactory {

    public static ServiceReview createServiceReview(int rating, String comments, Date reviewDate) {
        if (!Helper.isValidString(comments) || !Helper.isValidRating(rating)) {
            return null;
        }

        return new ServiceReview.Builder()
                .setRating(rating)
                .setComments(comments)
                .setReviewDate(reviewDate)
                .build();
    }

}
