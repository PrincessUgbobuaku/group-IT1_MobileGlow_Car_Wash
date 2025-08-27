//package za.ac.cput.factory.review;
//
///* MobileGlow Car Wash
//   Service Review class
//   Author: Inga Zekani (221043756)
// */
//
//import za.ac.cput.domain.review.ServiceReview;
//
//import java.util.Date;
//import java.util.UUID;
//import za.ac.cput.util.Helper;
//
//public class ServiceReviewFactory {
//
//    public static ServiceReview createServiceFactory1(int rating, String comments, Date reviewDate){
//        //Create a unique ID for Service Review
//        String reviewID = UUID.randomUUID().toString();
//
//        if (!Helper.isValidInt(rating) ||
//                !Helper.isValidString(comments) ||
//                !Helper.isValidObject(reviewDate)) {
//            return null;
//        }
//        return new ServiceReview.Builder()
//                .setReviewID(reviewID)
//                .setRating(rating)
//                .setComments(comments)
//                .setReviewDate(reviewDate)
//                .build();
//
//    }
//}
