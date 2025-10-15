// Inga
// LandingCustomer.js
import React from "react";
import "./LandingCustomer.css";
import NavbarCustomer from "../components/NavbarCustomer";   // fixed path
import Footer from "../components/Footer";   // fixed path

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
                    <span key={i} className={i < rating ? "star filled" : "star"}>★</span>
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

// Main LandingPublic Component
export default function LandingCustomer() {
    // Use direct paths to images in public folder - NO imports needed
    const features = [
        {
            title: "QUALITY SERVICE",
            description: "Shiny cars. Satisfied drivers. No shortcuts.",
            imageUrl: qualityServiceIcon // Use the imported image
        },
        {
            title: "VARIETY OF LOCATIONS",
            description: "Wherever you roam, we're close to home.",
            imageUrl: locationsIcon // Use the imported image
        },
        {
            title: "OPERATIONAL DURING LOAD SHEDDING",
            description: "When the lights go out, we stay lit.",
            imageUrl: loadSheddingIcon // Use the imported image
        },
        {
            title: "PROFESSIONAL TEAM",
            description: "Shine delivered by the best in the biz.",
            imageUrl: professionalTeamIcon // Use the imported image
        },
    ];


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

    const reviews = [
        { text: "The best booking system I've used! So convenient to have my car cleaned while I'm at work.", author: "Lucy - Cape Town", rating: 5 },
        { text: "Quick, reliable, and convenient service. My car has never looked better!", author: "James - Durban", rating: 4 },
        { text: "Professional and always on time. The interior cleaning service is worth every penny.", author: "Anele - Johannesburg", rating: 5 },
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
                    <h1 className="hero-heading">We bring the shine to you.</h1>
                    <h2 className="hero-subheading">
                        Your car, our care – <span>Anywhere.</span>
                    </h2>

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
                                        top: document.querySelector(".add-vehicle-section").offsetTop,
                                        behavior: "smooth",
                                    });
                                }}>↓
                            </span>

                        </div>

                    </div>
                </div>
            </section>



            {/* Features Section */}
            <section className="features-section">
                <div className="section-container">
                    <div className="features-grid">
                        {features.map((feature, i) => (
                            <Feature
                                key={i}
                                title={feature.title}
                                description={feature.description}
                                image={feature.imageUrl}
                            />
                        ))}
                    </div>
                </div>


            </section>

            {/* Add Vehicle Section */}
            <section className="add-vehicle-section">
                <div className="section-container add-vehicle-container">
                    <h2 className="section-heading">Want to add your vehicle?</h2>
                    <p className="section-subheading">
                        Manage your cars easily by adding them to your profile. This helps us serve you faster during bookings.
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

            {/* Stats Section */}
            <Stats />

            {/* Categories Section */}
            <section id="our-services" className="categories-section">
                <div className="section-container">
                    <h2 className="section-heading">Our Services</h2>
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

                    {/* Book Now Button */}
                    <div className="book-now-container">
                        <button
                            onClick={() => (window.location.href = "/booking")}
                            className="cta-button primary"
                        >
                            BOOK NOW
                        </button>
                    </div>
                </div>
            </section>

            {/* Reviews Section */}
            <section className="reviews-section">
                <div className="section-container">
                    <h2 className="section-heading">Customer Reviews</h2>
                    <p className="section-subheading">See what our customers are saying about our services</p>
                    <div className="reviews-grid">
                        {reviews.map((r, i) => (
                            <ReviewCard key={i} text={r.text} author={r.author} rating={r.rating} />
                        ))}
                    </div>
                </div>
            </section>

            {/* Loyalty Section */}
            <section className="loyalty-section">
                <div className="section-container loyalty-container">
                    <div className="loyalty-content">
                        <h2 className="loyalty-heading">LOYALTY ISN'T CHEAP</h2>
                        <p className="loyalty-text">But we make it worth it. Our loyalty program offers exclusive benefits:</p>
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

