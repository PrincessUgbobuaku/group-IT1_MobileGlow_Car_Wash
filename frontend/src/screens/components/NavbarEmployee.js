// src/screens/components/NavbarEmployee.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./NavbarEmployee.css";

const NavbarEmployee = () => {
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
            <div className="logo">Mobile Car Wash - Employee</div>

            <div className="nav-links">
                {/* Top-level navigation */}
                <a href="/manage-bookings" className="nav-btn">View Bookings</a>
                <a href="/cleaning-services/management" className="nav-btn">Update Services</a>
                <a href="/performance" className="nav-btn">Performance</a>
                <a href="/EmployeeManagement" className="nav-btn">Employees</a>

                {/* Dropdown menu */}
                <div className="dropdown">
                    <button className="menu-btn" onClick={toggleMenu}>
                        Menu ☰
                    </button>
                    {isOpen && (
                        <ul className="dropdown-menu">
                            <li><a href="/profiles">Profile</a></li>
                            <li><a href="/password-reset">Change password</a></li>
                            <li><a href="/deactivate-account">Deactivate Account</a></li>
                            <li><button onClick={handleLogout} className="logout-btn">Log Out</button></li>
                        </ul>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default NavbarEmployee;
