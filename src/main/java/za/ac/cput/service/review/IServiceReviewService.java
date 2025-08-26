package za.ac.cput.service.review;

/* MobileGlow Car Wash
   IServiceReviewService Class
   Author: Inga Zekani (221043756)
 */

import za.ac.cput.domain.review.ServiceReview ;
import za.ac.cput.service.IService;

import java.util.List;

public interface IServiceReviewService extends IService<ServiceReview, Long> {
    List<ServiceReview> getAll();

    }



