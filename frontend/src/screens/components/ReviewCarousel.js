import React from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import butterfly from "../../assets/butterfly.png"; // Adjust path as needed
import googleLogo from "../../assets/image.png";

const reviews = [
  {
    name: "Lucy Smith",
    date: "Sep 10, 2024",
    location: "Cape Town",
    rating: 5,
    text: "Best booking system! So convenient having my car cleaned while at work.",
  },
  {
    name: "James Johnson",
    date: "Aug 22, 2024",
    location: "Durban",
    rating: 4,
    text: "Quick, reliable, and convenient service. My car has never looked better!",
  },
  {
    name: "Anele Moyo",
    date: "Jul 30, 2024",
    location: "Johannesburg",
    rating: 5,
    text: "Professional and always on time. Worth every penny!",
  },
  {
    name: "Sarah Lee",
    date: "Jun 15, 2024",
    location: "Pretoria",
    rating: 5,
    text: "Excellent service and friendly staff. My car looks fantastic after every visit!",
  },
  {
    name: "Michael Brown",
    date: "May 12, 2024",
    location: "Port Elizabeth",
    rating: 4,
    text: "Very efficient and trustworthy. Highly recommend their mobile cleaning service.",
  },
  {
    name: "Nandi Khumalo",
    date: "Apr 08, 2024",
    location: "Bloemfontein",
    rating: 5,
    text: "Convenient and affordable. I love that they come to my office to clean my car.",
  },
];

const ReviewCarousel = () => {
  const settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 2,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 5000,
    arrows: true,
    responsive: [
      {
        breakpoint: 768,
        settings: {
          slidesToShow: 1,
        },
      },
    ],
  };

  return (
    <section className="reviews-section">
      <div className="section-container">
        <h2 className="carousel-heading">Customer Reviews</h2>
        <p className="section-subheading">
          See what our customers are saying about our services
        </p>

        <Slider {...settings} className="reviews-grid">
          {reviews.map(({ name, date, location, rating, text }, index) => (
            <div key={index} className="review-card">
              <div className="review-header">
                <img
                  src={butterfly}
                  alt="Profile"
                  className="review-profile-photo"
                />
                <div className="reviewer-info">
                  <strong className="reviewer-name">{name}</strong>
                  <div className="review-meta">
                    <span className="review-date">{date}</span> |{" "}
                    <span className="review-location">{location}</span>
                  </div>
                </div>
                <img
                  src={googleLogo}
                  alt="Google Logo"
                  className="google-logo"
                />
              </div>

              <div className="review-stars">
                {[...Array(5)].map((_, i) => (
                  <span
                    key={i}
                    className={`star ${i < rating ? "filled" : ""}`}
                  >
                    ★
                  </span>
                ))}
              </div>

              <p className="review-text">"{text}"</p>
            </div>
          ))}
        </Slider>
      </div>
    </section>
  );
};

export default ReviewCarousel;
