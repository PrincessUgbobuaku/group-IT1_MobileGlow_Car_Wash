// src/screens/components/Navbar.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./NavbarCustomer.css";

const NavbarCustomer = () => {
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate();

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    const handleLogout = () => {
        localStorage.removeItem('userEmail');
        localStorage.removeItem('userRoleDescription');
        // Add any other session items to clear
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="logo">Mobile Car Wash</div>

            <div className="nav-links">
                <a href="/aboutus" className="nav-btn">About Us</a>
                <a href="/ContactUs" className="nav-btn">Contact Us</a>

                <div className="dropdown">
                    <button className="menu-btn" onClick={toggleMenu}>
                        Menu ☰
                    </button>
                    {isOpen && (
                        <ul className="dropdown-menu">
                            <li><a href="/profiles">Profile</a></li>
                            <li><a href="/vehicles">Vehicle Page</a></li>
                            <li><a href="/Booking History">Booking History</a></li>
                            <li><a href="/password-reset">Change Password</a></li>
                            <li><a href="/deactivate-account">Deactivate Account</a></li>
                            <li><button onClick={handleLogout} className="logout-btn">Log Out</button></li>
                        </ul>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default NavbarCustomer;
