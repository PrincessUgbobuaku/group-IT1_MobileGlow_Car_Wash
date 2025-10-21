// src/screens/components/Navbar.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Navbar.css";
import logo from "../../assets/logo.jpg";

const Navbar = () => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const navigate = useNavigate();

    const toggleMenu = () => setIsMenuOpen(!isMenuOpen);

    const handleAboutUs = () => navigate("/about");
    const handleContactUs = () => navigate("/contact-us");
    const handleServices = () => navigate("#our-services");
    const handleSignUp = () => navigate("/roleselection");
    const handleLogIn = () => navigate("/login");

    return (
        <nav className="navbar">
            <div className="app-content navbar-inner">
                <div className="navbar-left">
                    <img src={logo} alt="Mobile Car Wash Logo" className="logo-img" />
                    {/*<span className="logo-text">Mobile Car Wash</span>*/}
                </div>

                <div className="nav-links">
                    <button className="nav-btn" onClick={handleServices}>
                        Our Services
                    </button>
                    <button className="nav-btn" onClick={handleSignUp}>
                        Sign Up
                    </button>
                    <button className="nav-btn" onClick={handleLogIn}>
                        Log In
                    </button>

                    {/* Hamburger/X Button */}
                    <button
                        className={`hamburger ${isMenuOpen ? "open" : ""}`}
                        onClick={toggleMenu}
                    >
                        <span></span>
                        <span></span>
                    </button>
                </div>
            </div>

            {/* Slide-In Side Menu */}
            <div className={`side-menu ${isMenuOpen ? "open" : ""}`}>
                <ul>
                    <li>
                        <button onClick={handleAboutUs}>About Us</button>
                    </li>
                    <li>
                        <button onClick={handleContactUs}>Contact Us</button>
                    </li>
                </ul>
            </div>

            {/* Overlay */}
            {isMenuOpen && <div className="overlay" onClick={toggleMenu}></div>}
        </nav>
    );
};

export default Navbar;
