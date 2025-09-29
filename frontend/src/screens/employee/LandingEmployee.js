//inga
// src/screens/employee/LandingEmployee.js
import React from "react";
import NavbarEmployee from "../components/NavbarEmployee";
import Footer from "../components/Footer";
import "./LandingEmployee.css"; // reuse same CSS for consistent look

import bookingsIcon from "../../assets/bookings.png";
import employeesIcon from "../../assets/employees.png";
import servicesIcon from "../../assets/services.png";
import performanceIcon from "../../assets/performance.png";


const LandingEmployee = () => {
    return (
        <div className="landing-page">
            {/* Navbar */}
            <NavbarEmployee />

            {/* Hero Section */}
            <section className="hero-section">
                <div className="hero-content">
                    <h1 className="hero-heading">Welcome to Your Employee Dashboard</h1>
                    {/*<p className="hero-subheading">*/}
                    {/*    Here you can: Manage <span>bookings</span>, update <span>services</span>, and*/}
                    {/*    track your <span>performance</span>.*/}
                    {/*</p>*/}
                </div>
            </section>

            {/* Features Section */}
            <section className="features-section">
                <div className="section-container">
                    <h2 className="section-heading">What You Can Do</h2>
                    <p className="section-subheading">
                        Access powerful tools to manage your work efficiently.
                    </p>

                    <div className="features-grid">
                        <div className="feature">
                            <div className="feature-icon">
                                <img src={bookingsIcon} alt="View Bookings" />
                            </div>
                            <h3 className="feature-title">View Bookings</h3>
                            <p className="feature-description">
                                See upcoming and past bookings at a glance to stay organized.
                            </p>
                        </div>
                        <div className="feature">
                            <div className="feature-icon">
                                <img src={servicesIcon} alt="Update Services" />
                            </div>
                            <h3 className="feature-title">Update Services</h3>
                            <p className="feature-description">
                                Easily edit the services you offer, including prices and availability.
                            </p>
                        </div>
                        <div className="feature">
                            <div className="feature-icon">
                                <img src={performanceIcon} alt="Performance Tracking" />
                            </div>
                            <h3 className="feature-title">Performance Tracking</h3>
                            <p className="feature-description">
                                Monitor your performance, customer ratings, and growth over time.
                            </p>
                        </div>
                        <div className="feature">
                            <div className="feature-icon">
                                <img src={employeesIcon} alt="Manage Profile" />
                            </div>
                            <h3 className="feature-title">Manage Profile</h3>
                            <p className="feature-description">
                                Update your details, profile picture, and contact information.
                            </p>
                        </div>
                    </div>

                </div>
            </section>

            {/* Stats Section */}
            <section className="stats-section">
                <div className="stats-container">
                    <div className="stat">
                        <div className="stat-number">1,250+</div>
                        <div className="stat-label">Bookings Completed</div>
                    </div>
                    <div className="stat">
                        <div className="stat-number">98%</div>
                        <div className="stat-label">Customer Satisfaction</div>
                    </div>
                    <div className="stat">
                        <div className="stat-number">50+</div>
                        <div className="stat-label">Active Employees</div>
                    </div>
                </div>
            </section>

            {/*/!* Call to Action *!/*/}
            {/*<div className="book-now-container">*/}
            {/*    <button*/}
            {/*        className="cta-button primary"*/}
            {/*        onClick={() => (window.location.href = "/login")}*/}
            {/*    >*/}
            {/*        Go to Dashboard*/}
            {/*    </button>*/}
            {/*</div>*/}

            {/* Footer */}
            <Footer />
        </div>
    );
};

export default LandingEmployee;
