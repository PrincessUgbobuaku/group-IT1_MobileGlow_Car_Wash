package za.ac.cput.repository.review;

/* MobileGlow Car Wash
   IService Review Repository
   Author: Inga Zekani (221043756)
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.review.ServiceReview;

@Repository
public interface IServiceReviewRepository extends JpaRepository<ServiceReview, Long> {
}
