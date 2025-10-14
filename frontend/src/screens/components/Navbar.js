// src/screens/components/Navbar.js
import React, { useState } from "react";
import "./Navbar.css";
import logo from "../../assets/logo.jpg"; // ✅ make sure path is correct

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => setIsOpen(!isOpen);

    return (
        <nav className="navbar">
            <div className="logo">
                <img src={logo} alt="Mobile Car Wash Logo" className="logo-img" />
                <span className="logo-text">Mobile Car Wash</span>
            </div>

            <div className={`nav-links ${isOpen ? "open" : ""}`}>
                {/* 🔹 Add 'Our Services' link */}
                <a href="#our-services" className="nav-btn">Our Services</a>

                <a href="/roleselection" className="nav-btn">Sign Up</a>
                <a href="/login" className="nav-btn">Log In</a>

                <div className="dropdown">
                    <button className="menu-btn" onClick={toggleMenu}>
                        Menu ☰
                    </button>
                    {isOpen && (
                        <ul className="dropdown-menu">
                            <li><a href="/about">About Us</a></li>
                            <li><a href="/contact">Contact Us</a></li>
                        </ul>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
