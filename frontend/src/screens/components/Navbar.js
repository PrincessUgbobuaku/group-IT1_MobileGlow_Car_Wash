// src/screens/components/Navbar.js
import React, { useState } from "react";
import "./Navbar.css";

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    return (
        <nav className="navbar">
            <div className="logo">Mobile Car Wash</div>

            <div className="nav-links">
                <a href="/signup" className="nav-btn">Sign Up</a>
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
