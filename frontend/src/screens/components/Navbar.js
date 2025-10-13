// src/screens/components/Navbar.js
import React, { useState } from "react";
import "./Navbar.css";

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    const handleAboutUs = () => {
        // Navigate to About Us page
        window.location.href = "/AboutUs";
    };

    const handleContactUs = () => {
        // Navigate to Contact Us page
        window.location.href = "/ContactUs";
    };

    return (
        <nav className="navbar">
            <div className="logo">Mobile Car Wash</div>

            <div className="nav-links">
                <a href="/roleselection" className="nav-btn">Sign Up</a>
                <a href="/login" className="nav-btn">Log In</a>

                <div className="dropdown">
                    <button className="menu-btn" onClick={toggleMenu}>
                        Menu ☰
                    </button>
                    {isOpen && (
                        <ul className="dropdown-menu">
                            <li><button onClick={handleAboutUs}>About Us</button></li>
                            <li><button onClick={handleContactUs}>Contact Us</button></li>
                        </ul>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;