// src/screens/components/Navbar.js
import React, { useState } from "react";
import "./NavbarCustomer.css";

const NavbarCustomer = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    return (
        <nav className="navbar">
            <div className="logo">Mobile Car Wash</div>

            <div className="nav-links">
                <a href="/about" className="nav-btn">About Us</a>
                <a href="/Contact Us" className="nav-btn">Contact Us</a>

                <div className="dropdown">
                    <button className="menu-btn" onClick={toggleMenu}>
                        Menu ☰
                    </button>
                    {isOpen && (
                        <ul className="dropdown-menu">
                            <li><a href="/Profile Customer">Profile</a></li>
                            <li><a href="/vehicle page">Vehicle Page</a></li>
                            <li><a href="/Booking History">Booking History</a></li>
                            <li><a href="/login">Log Out</a></li>
                        </ul>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default NavbarCustomer;
