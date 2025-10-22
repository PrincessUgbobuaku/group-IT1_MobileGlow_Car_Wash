// src/screens/employee/LandingEmployee.js
import React from "react";
import NavbarEmployee from "../components/NavbarEmployee";
import Footer from "../components/Footer";
import "./LandingEmployee.css";

import bookingsIcon from "../../assets/bookings.png";
import employeesIcon from "../../assets/employees.png";
import servicesIcon from "../../assets/services.png";
import performanceIcon from "../../assets/performance.png";


import heroCarwash from "../../assets/hero-carwash.jpg";

const LandingEmployee = () => {
    return (
        <div className="employee-landing-page">
            <NavbarEmployee />

            {/* Hero Section */}
            <section
                className="employee-hero-section"
                style={{ backgroundImage: `url(${heroCarwash})` }}
            >
                <div className="employee-hero-overlay"></div>

                {/* ✅ Logo at top-left corner */}
                {/*<img src={logo} alt="Logo" className="employee-hero-logo" />*/}

                {/* Centered content */}
                <div className="employee-hero-content">
                    {/*<img src={logo} alt="Logo" className="employee-hero-logo" />*/}

                    <h1 className="employee-hero-heading">Welcome to Your Employee Dashboard</h1>
                    <p className="employee-hero-subheading">
                        Manage <span>bookings</span>, update <span>services</span>, and track your{" "}
                        <span>performance</span> — all in one place.
                    </p>
                </div>
            </section>

            {/* Features Section */}
            <section className="employee-actions-section">
                <h1 className="section-heading">What You Can Do</h1>
                <div className="employee-actions-grid">
                    <div className="employee-action-card">
                        <div className="feature-icon">
                            <img src={bookingsIcon} alt="View Bookings" />
                        </div>
                        <h3>View Bookings</h3>
                        <p>See upcoming and past bookings at a glance to stay organized.</p>
                    </div>

                    <div className="employee-action-card">
                        <div className="feature-icon">
                            <img src={servicesIcon} alt="Update Services" />
                        </div>
                        <h3>Update Services</h3>
                        <p>Edit your offered services, including prices and availability.</p>
                    </div>

                    <div className="employee-action-card">
                        <div className="feature-icon">
                            <img src={performanceIcon} alt="Performance Tracking" />
                        </div>
                        <h3>Performance Tracking</h3>
                        <p>Monitor your ratings, reviews, and performance trends over time.</p>
                    </div>

                    <div className="employee-action-card">
                        <div className="feature-icon">
                            <img src={employeesIcon} alt="Manage Profile" />
                        </div>
                        <h3>Manage Profile</h3>
                        <p>Update your personal info, profile photo, and contact details.</p>
                    </div>
                </div>
            </section>

            <Footer />
        </div>
    );
};

export default LandingEmployee;
