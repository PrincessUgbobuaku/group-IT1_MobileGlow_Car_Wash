package za.ac.cput.domain.review;

/* MobileGlow Car Wash
   Service Review
   Author: Inga Zekani (221043756)
 */

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "review")
public class ServiceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// or IDENTITY
    @Column( name = "reviewid")
    private Long reviewID;
    private int rating;
    private String comments;
    @Column(name = "review_date")
    private Date reviewDate;

    //default constructor
    public ServiceReview(){

    }

    public ServiceReview(Builder builder){
        this.reviewID = builder.reviewID;
        this.rating = builder.rating;
        this.comments = builder.comments;
        this.reviewDate = builder.reviewDate;

    }

    public Long getReviewID() {

        return reviewID;
    }
    public int getRating() {

        return rating;
    }
    public String getComments() {

        return comments;
    }
    public Date getReviewDate() {

        return reviewDate;
    }

    @Override
    public String toString() {
        return "ServiceReview{" +
                "reviewID='" + reviewID + '\'' +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", reviewDate='" + reviewDate + '\'' +
                '}';
    }

    public static class Builder{
        private Long reviewID;
        private int rating;
        private String comments;
        private Date reviewDate;

        public Builder setReviewID(Long reviewID) {
            this.reviewID = reviewID;
            return this;
        }
        public Builder setRating(int rating) {
            this.rating = rating;
            return this;
        }
        public Builder setComments(String comments) {
            this.comments = comments;
            return this;
        }
        public Builder setReviewDate(Date reviewDate) {
            this.reviewDate = reviewDate;
            return this;
        }
        public Builder copy(ServiceReview review){
            this.reviewID = review.reviewID;
            this.rating = review.rating;
            this.comments = review.comments;
            this.reviewDate = review.reviewDate;
            return this;
        }
        public ServiceReview build(){
            return new ServiceReview(this);
        }
    }
}
