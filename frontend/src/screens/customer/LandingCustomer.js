// Inga
// LandingCustomer.js
import "./LandingCustomer.css";
import NavbarCustomer from "../components/NavbarCustomer"; // fixed path
import Footer from "../components/Footer"; // fixed path
import butterfly from "../../assets/butterfly.png";
import React, { useState, useEffect, useRef } from "react";

// Import images correctly
import qualityServiceIcon from "../../assets/quality-service.png";
import locationsIcon from "../../assets/variety-of-locations.png";
import loadSheddingIcon from "../../assets/operational-during-load-shedding.png";
import professionalTeamIcon from "../../assets/professional-team.png";

import POLISH_IMAGE from "../../assets/polish.png";
import FULL_WASH_IMAGE from "../../assets/full-wash.png";
import DETAILING_IMAGE from "../../assets/detailing.png";
import ENGINE_IMAGE from "../../assets/engine.png";
import WAXING_IMAGE from "../../assets/waxing.png";
import INTERIOR_IMAGE from "../../assets/interior.png";
import HERO_IMAGE from "../../assets/hero-carwash.jpg";

import ReviewCarousel from "../components/ReviewCarousel";

// Feature Component
const Feature = ({ title, description, image }) => {
  return (
    <div className="feature">
      <div className="feature-icon">
        <img src={image} alt={title} className="feature-image" />
      </div>
      <h3 className="feature-title">{title}</h3>
      <p className="feature-description">{description}</p>
    </div>
  );
};

// CategoryCard Component
const CategoryCard = ({ title, description, imageUrl }) => {
  return (
    <div className="category-card">
      <img src={imageUrl} alt={title} className="feature-image" />
      <h3 className="category-title">{title}</h3>
      <p className="category-description">{description}</p>
    </div>
  );
};

// ReviewCard Component
const ReviewCard = ({ text, author, rating }) => {
  return (
    <div className="review-card">
      <div className="review-stars">
        {[...Array(5)].map((_, i) => (
          <span key={i} className={i < rating ? "star filled" : "star"}>
            ★
          </span>
        ))}
      </div>
      <p className="review-text">"{text}"</p>
      <p className="review-author">- {author}</p>
    </div>
  );
};

// Stats Component
const Stats = () => {
  return (
    <div className="stats-section">
      <div className="stats-container">
        <div className="stat">
          <h3 className="stat-number">1</h3>
          <p className="stat-label">SA Provinces Reached</p>
        </div>
        <div className="stat">
          <h3 className="stat-number">3</h3>
          <p className="stat-label">Site Locations</p>
        </div>
      </div>
    </div>
  );
};

const CountUp = ({ end, start = 0, duration = 2000 }) => {
  const [count, setCount] = useState(start);
  const ref = useRef(null);
  const started = useRef(false);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting && !started.current) {
          started.current = true;
          let startTime = null;

          const step = (timestamp) => {
            if (!startTime) startTime = timestamp;
            const progress = timestamp - startTime;
            const progressRatio = Math.min(progress / duration, 1);
            const currentCount = Math.floor(
              progressRatio * (end - start) + start
            );
            setCount(currentCount);
            if (progress < duration) {
              requestAnimationFrame(step);
            } else {
              setCount(end);
            }
          };

          requestAnimationFrame(step);
        }
      },
      { threshold: 0.2 }
    );

    const currentRef = ref.current;
    if (currentRef) observer.observe(currentRef);

    return () => {
      if (currentRef) observer.unobserve(currentRef);
    };
  }, [end, start, duration]);

  return (
    <h1 ref={ref} className="stat-number">
      {count}+
    </h1>
  );
};

// Main LandingPublic Component
export default function LandingCustomer() {
  // Use direct paths to images in public folder - NO imports needed
  const features = [
    {
      title: "QUALITY SERVICE",
      description: "Shiny cars. Satisfied drivers. No shortcuts.",
      imageUrl: qualityServiceIcon, // Use the imported image
    },
    {
      title: "VARIETY OF LOCATIONS",
      description: "Wherever you roam, we're close to home.",
      imageUrl: locationsIcon, // Use the imported image
    },
    {
      title: "OPERATIONAL DURING LOAD SHEDDING",
      description: "When the lights go out, we stay lit.",
      imageUrl: loadSheddingIcon, // Use the imported image
    },
    {
      title: "PROFESSIONAL TEAM",
      description: "Shine delivered by the best in the biz.",
      imageUrl: professionalTeamIcon, // Use the imported image
    },
  ];

  const categories = [
    {
      title: "Full Wash",
      description:
        "Complete exterior and interior cleaning including tires and windows",
      imageUrl: FULL_WASH_IMAGE,
    },
    {
      title: "Polish",
      description:
        "Restore your car's shine with our premium polishing service",
      imageUrl: POLISH_IMAGE,
    },
    {
      title: "Waxing",
      description: "Protective wax coating to keep your car looking new",
      imageUrl: WAXING_IMAGE,
    },
    {
      title: "Interior Cleaning",
      description:
        "Deep cleaning of seats, carpets, dashboard and all interior surfaces",
      imageUrl: INTERIOR_IMAGE,
    },
    {
      title: "Engine Wash",
      description: "Thorough cleaning of your engine bay to prevent corrosion",
      imageUrl: ENGINE_IMAGE,
    },
    {
      title: "Detailing",
      description:
        "Premium interior and exterior detailing for that showroom finish",
      imageUrl: DETAILING_IMAGE,
    },
  ];

  //   const reviews = [
  //     {
  //       text: "The best booking system I've used! So convenient to have my car cleaned while I'm at work.",
  //       author: "Lucy - Cape Town",
  //       rating: 5,
  //     },
  //     {
  //       text: "Quick, reliable, and convenient service. My car has never looked better!",
  //       author: "James - Durban",
  //       rating: 4,
  //     },
  //     {
  //       text: "Professional and always on time. The interior cleaning service is worth every penny.",
  //       author: "Anele - Johannesburg",
  //       rating: 5,
  //     },
  //   ];

  return (
    <div className="landing-page">
      <NavbarCustomer />

      {/* Hero Section */}
      <section
        className="hero-section"
        style={{ backgroundImage: `url(${HERO_IMAGE})` }}
      >
        <div className="hero-overlay"></div>

        <div className="hero-content">
          <h1 className="hero-heading">MobileGlow Car Wash</h1>
          <h3 className="hero-subheading">
            Your car, our care – <span>Anywhere.</span>
          </h3>

          <div className="book-now-container">
            <button
              onClick={() => (window.location.href = "/booking")}
              className="cta-button primary"
            >
              BOOK NOW
            </button>
            <div className="scroll-down-hint">
              <p>Add vehicle below</p>
              <span
                className="down-arrow"
                onClick={() => {
                  window.scrollTo({
                    top: document.querySelector(".add-vehicle-section")
                      .offsetTop,
                    behavior: "smooth",
                  });
                }}
              >
                ↓
              </span>
            </div>
          </div>
        </div>
      </section>

      {/* About Us Section */}
      <div className="about-us-section app-content">
        <div className="about-us-text">
          <h1 className="about-us-heading">About Us</h1>
          <p className="about-us-description">
            At Mobile Car Wash, we believe your car deserves the best care, no
            matter where you are. Our mission is to provide top-notch car
            cleaning services that come to you, whether you're at home, work, or
            anywhere in between. With a commitment to quality, convenience, and
            customer satisfaction, we make car care effortless and accessible.
          </p>
          <p className="about-us-description">
            At Mobile Car Wash, we believe your car deserves the best care, no
            matter where you are. Our mission is to provide top-notch car
            cleaning services that come to you, whether you're at home, work, or
            anywhere in between. With a commitment to quality, convenience, and
            customer satisfaction, we make car care effortless and accessible.
          </p>
          <button>Learn more about us</button>
        </div>

        <div className="about-us-image">
          <img src={butterfly} alt="About Us" className="about-us-img" />
        </div>
      </div>

      <section
        className="parallax-section"
        style={{ backgroundImage: `url(${butterfly})` }}
      >
        <div className="parallax-content">
          <h1>Grace in Motion</h1>
          <p>Just like a butterfly, let your brand take flight.</p>
        </div>
      </section>

      {/*What we wash section */}
      <div className="what-we-wash app-content">
        <div className="what-we-wash-text">
          <h1> No Car Left Dirty.</h1>
          <p>
            We clean all types of vehicles — from everyday sedans to luxury
            cars, SUVs, and work trucks. Our mobile service brings professional
            care right to your doorstep, saving you time and effort. Using
            eco-friendly products and precision techniques, we ensure every
            detail shines. Whether it's a quick wash or full detailing, quality
            is always our priority. At MobileGlow, we treat every car like it’s
            our own.
          </p>
        </div>
        <section className="wash-cards-section">
          <div
            className="wash-card"
            style={{
              backgroundImage: `url(${butterfly})`,
            }}
          >
            <div className="card-text">Sedans</div>
          </div>
          <div
            className="wash-card"
            style={{
              backgroundImage: `url(${butterfly})`,
            }}
          >
            <div className="card-text">SUVs</div>
          </div>
          <div
            className="wash-card"
            style={{
              backgroundImage: `url(${butterfly})`,
            }}
          >
            <div className="card-text">Motorcycles</div>
          </div>
        </section>
      </div>

      {/* Our services */}
      <div className="business-services">
        <div className="service-card-background-container">
          <h1 className="business-section-heading">Business Services</h1>
          <p>
            We offer the best in mobile car care — from full washes and interior
            detailing to corporate packages and fleet management. Our
            professional team delivers premium services with convenience,
            quality, and a spotless finish every time.
          </p>

          <div className="service-card-grid app-content">
            <div
              className="service-card with-bg"
              style={{ backgroundImage: `url(${butterfly})` }}
            >
              <div className="card-overlay">
                <h3>Exterior Wash</h3>
                <p>
                  Keep your business fleet spotless and professional-looking
                  with our bulk cleaning service.
                </p>
                <button
                  onClick={() => (window.location.href = "/exterior-wash")}
                >
                  Learn more
                </button>
              </div>
            </div>

            <div
              className="service-card with-bg"
              style={{ backgroundImage: `url(${butterfly})` }}
            >
              <div className="card-overlay">
                <h3>Interior Care</h3>
                <p>
                  Offer your employees a sparkling perk with our customizable
                  corporate wash plans.
                </p>
                <button
                  onClick={() => (window.location.href = "/corporate-packages")}
                >
                  Learn More
                </button>
              </div>
            </div>

            <div
              className="service-card with-bg"
              style={{ backgroundImage: `url(${butterfly})` }}
            >
              <div className="card-overlay">
                <h3>Full Detailing</h3>
                <p>
                  Partner with us to offer our mobile services at your location
                  and share in the shine.
                </p>
                <button
                  onClick={() => (window.location.href = "/partner-with-us")}
                >
                  Learn More
                </button>
              </div>
            </div>

            <div
              className="service-card with-bg"
              style={{ backgroundImage: `url(${butterfly})` }}
            >
              <div className="card-overlay">
                <h3>Protection Services</h3>
                <p>
                  Looking to start your own car wash business? Explore our
                  franchise options today.
                </p>
                <button onClick={() => (window.location.href = "/franchise")}>
                  Learn More
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Add Vehicle Section */}
      <section className="add-vehicle-section">
        <div className="section-container add-vehicle-container">
          <h2 className="section-heading">Want to add your vehicle?</h2>
          <p className="section-subheading">
            Manage your cars easily by adding them to your profile. This helps
            us serve you faster during bookings.
          </p>
          <div className="add-vehicle-btn-container">
            <button
              className="cta-button primary"
              onClick={() => (window.location.href = "/vehicles")}
            >
              Add Vehicle
            </button>
          </div>
        </div>
      </section>

      {/* Why choose us section */}
      <section className="why-choose-us-section app-content">
        <h1 className="section-heading">Why Choose Mobile Car Wash?</h1>
        <div className="row first-row">
          <div className="text-column">
            <h3>Convenience at Your Doorstep</h3>
            <p>
              Life is busy, and finding time to visit a car wash often feels
              like a luxury. That’s why we bring our expert car cleaning
              services directly to you, wherever you are — at home, at work, or
              even during your busy errands. Our fully equipped mobile team
              arrives on time and handles every detail, so you never have to
              interrupt your schedule or stand in line.
            </p>
            <p>
              This convenience means you get a spotless, professionally cleaned
              vehicle without the hassle or stress. We understand your time is
              valuable, and our service is designed to fit seamlessly into your
              lifestyle. Whether you have a packed day ahead or just want to
              relax while we work, we make car care effortless and efficient.
            </p>
          </div>
          <div className="image-column">
            <img src={butterfly} alt="Convenience" />
          </div>
        </div>

        <div className="row second-row">
          <div className="image-column">
            <img src={butterfly} alt="Eco-Friendly" />
          </div>
          <div className="text-column">
            <h3>Eco-Friendly and Reliable</h3>
            <p>
              At Mobile Car Wash, we’re committed to protecting the environment
              while delivering top-quality service. Our cleaning products are
              carefully selected to be eco-friendly, biodegradable, and free
              from harsh chemicals that could harm plants, animals, or
              waterways. Beyond just being green, our techniques ensure your car
              receives a deep, thorough clean that lasts. We take pride in
              employing highly trained professionals who pay close attention to
              every detail, providing consistent, reliable results you can count
              on. Choosing us means you’re not only maintaining your vehicle’s
              appearance but also supporting a cleaner planet. We believe
              sustainability and outstanding service go hand-in-hand, and we
              strive to make every wash both effective and environmentally
              responsible.
            </p>
          </div>
        </div>
      </section>

      {/* stats banner */}
      <section className="stats-banner">
        <div className="stats-container">
          <div className="stat">
            <CountUp end={1000} start={990} duration={2000} />
            <p className="stat-label">Cars Washed</p>
          </div>
          <div className="stat">
            <CountUp end={50} start={45} duration={2000} />
            <p className="stat-label">Locations Served</p>
          </div>
        </div>
      </section>

      {/* Google reviews */}
      <div>
        <h2 className="carousel-heading"></h2>
        <ReviewCarousel />
      </div>

      {/* Loyalty Section */}
      <section className="loyalty-section">
        <div className="section-container loyalty-container">
          <div className="loyalty-content">
            <h2 className="loyalty-heading">LOYALTY ISN'T CHEAP</h2>
            <p className="loyalty-text">
              But we make it worth it. Our loyalty program offers exclusive
              benefits:
            </p>
            <ul className="loyalty-benefits">
              <li>Earn points with every service</li>
              <li>Redeem points for discounts</li>
              <li>Priority booking</li>
              <li>Exclusive member-only offers</li>
            </ul>
          </div>
          <div className="loyalty-visual">
            <div className="loyalty-card">
              <div className="loyalty-icon">🎁</div>
              <h3>Loyalty Program</h3>
              <p>Join today and start earning rewards with every service!</p>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
}
