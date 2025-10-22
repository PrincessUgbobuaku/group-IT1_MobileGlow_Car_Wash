// Inga
// LandingCustomer.js
import "./LandingCustomer.css";
import NavbarCustomer from "../components/NavbarCustomer"; // fixed path
import Footer from "../components/Footer"; // fixed path
import { useScrollReveal } from "../../hooks/useScrollReveal"; // adjust path
import butterfly from "../../assets/about-us.png";
import React, { useState, useEffect, useRef } from "react";


import FULL_WASH_IMAGE from "../../assets/full-wash.png";
import DETAILING_IMAGE from "../../assets/detailing.png";
import WAXING_IMAGE from "../../assets/waxing.png";
import INTERIOR_IMAGE from "../../assets/interior.png";
import HERO_IMAGE from "../../assets/hero-carwash.jpg";


import ReviewCarousel from "../components/ReviewCarousel";
import POLISH_IMAGE from "../../assets/polish.png";
import ENGINE_IMAGE from "../../assets/engine-wash.jpg";
import ABOUT_IMAGE from "../../assets/about-us.png";
import MISSION_IMAGE from "../../assets/mission-bg.jpg";

// CategoryCard Component - UPDATED
const CategoryCard = ({ title, description, imageUrl }) => {
    return (
        <div className="category-card">
            <div className="category-image-container">
                <img src={imageUrl} alt={title} className="category-image" />
                <div className="category-overlay">
                    <div className="category-content">
                        <h3 className="category-title">{title}</h3>
                        <p className="category-description">{description}</p>
                    </div>
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

// Zoom Section Component for About and Mission
const ZoomSection = ({ image, title, content, isRight = false }) => {
    const imageRef = useRef(null);
    const contentRef = useRef(null);

    useEffect(() => {
        const handleScroll = () => {
            if (imageRef.current && contentRef.current) {
                const element = imageRef.current;
                const contentElement = contentRef.current;
                const rect = element.getBoundingClientRect();
                const windowHeight = window.innerHeight;

                // Start zoom effect when element enters viewport
                if (rect.top < windowHeight && rect.bottom > 0) {
                    const progress = 1 - (rect.top / windowHeight);
                    const zoomLevel = 1 + (progress * 0.2); // 20% zoom max
                    const opacity = Math.min(1, progress * 2);

                    element.style.transform = `scale(${zoomLevel})`;
                    contentElement.style.opacity = opacity;
                    contentElement.style.transform = `translateY(${(1 - progress) * 50}px)`;
                }
            }
        };

        window.addEventListener('scroll', handleScroll);
        handleScroll(); // Initial check

        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    return (
        <section className={`zoom-section ${isRight ? 'right' : 'left'}`}>
            <div className="zoom-container">
                <div className="zoom-image-container" ref={imageRef}>
                    <img src={image} alt={title} className="zoom-image" />
                </div>
                <div className="zoom-content" ref={contentRef}>
                    <h1 className="zoom-title">{title}</h1>
                    <div className="zoom-text">
                        {content}
                    </div>
                </div>
            </div>
        </section>
    );
};

const WhyChooseUsRow = ({ image, isRight, heading, children }) => {
  const [headingRef, headingStyle] = useScrollReveal(isRight ? "right" : "left");

  return (
      <ZoomSection
          image={image}
          isRight={isRight}
          content={
            <div className="text-column">
              <h3 ref={headingRef} style={headingStyle}>
                {heading}
              </h3>
              {children}
            </div>
          }
      />
  );
};


// Main LandingPublic Component
export default function LandingCustomer() {

    const categories = [
        {
            title: "Full Wash",
            description: "Complete exterior and interior cleaning including tires and windows",
            imageUrl: FULL_WASH_IMAGE
        },
        {
            title: "Polish",
            description: "Restore your car's shine with our premium polishing service",
            imageUrl: POLISH_IMAGE
        },
        {
            title: "Waxing",
            description: "Protective wax coating to keep your car looking new",
            imageUrl: WAXING_IMAGE
        },
        {
            title: "Interior Cleaning",
            description: "Deep cleaning of seats, carpets, dashboard and all interior surfaces",
            imageUrl: INTERIOR_IMAGE
        },
        {
            title: "Engine Wash",
            description: "Thorough cleaning of your engine bay to prevent corrosion",
            imageUrl: ENGINE_IMAGE
        },
        {
            title: "Detailing",
            description: "Premium interior and exterior detailing for that showroom finish",
            imageUrl: DETAILING_IMAGE
        },
    ];

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
            Your car, our care – Anywhere.
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


        {/* Our services */}
        <div className="business-services">
            <div className="service-card-background-container">
                <h1 className="business-section-heading">Business Services</h1>

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


      <section
        className="parallax-section"
        style={{ backgroundImage: `url(${FULL_WASH_IMAGE})` }}
      >
      </section>




        {/* Categories Section - UPDATED WITH NAVY BACKGROUND */}
        <section id="our-services" className="categories-section">
            <div className="section-container">
                <h1 className="section-heading">Our Services</h1>
                <p className="section-subheading">
                    Professional car care services delivered to your doorstep at your convenience
                </p>
                <div className="categories-grid">
                    {categories.map((c, i) => (
                        <CategoryCard
                            key={i}
                            title={c.title}
                            description={c.description}
                            imageUrl={c.imageUrl}
                        />
                    ))}
                </div>
                <div className="book-now-container">
                    <button
                        onClick={() => (window.location.href = "/login")}
                        className="cta-button primary"
                    >
                        BOOK NOW
                    </button>
                </div>
            </div>
        </section>

      {/* Add Vehicle Section */}
      <section className="add-vehicle-section">
        <div className="section-container add-vehicle-container">
          <h1 className="section-heading">Want to add your vehicle?</h1>
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

        {/* About Us Section with Zoom Effect */}
        <ZoomSection
            image={ABOUT_IMAGE}
            title="About Us"
            content={
                <div className="about-content">
                    <h2>Our Values</h2>
                    <p>
                        At Mobile Glow Car Wash, we provide a premium mobile car detailing service that focuses on
                        delivering convenience and luxury to your doorstep. Our team is dedicated to ensuring
                        the highest quality standards in car care, with a meticulous attention to detail.
                        Experience the ultimate in professional car detailing services with us.
                    </p>
                </div>
            }
            isRight={false}
        />

        {/* Mission Section with Zoom Effect */}
        <ZoomSection
            image={MISSION_IMAGE}
            title="Our Mission"
            content={
                <div className="mission-content">
                    <p>
                        At Mobile Glow Car Wash, we are dedicated to providing a mobile car detailing service that
                        brings convenience, luxury, and exceptional care right to your doorstep. Our focus on
                        quality ensures that your vehicle receives the best treatment possible, leaving it
                        looking pristine and well-maintained.
                    </p>
                </div>
            }
            isRight={true}
        />



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
