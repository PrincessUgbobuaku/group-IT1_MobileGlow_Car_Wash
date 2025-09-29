// src/screens/components/NavbarEmployee.js
import React, { useState } from "react";
import "./NavbarEmployee.css";

const NavbarEmployee = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    return (
        <nav className="navbar">
            <div className="logo">Mobile Car Wash - Employee</div>

            <div className="nav-links">
                {/* Top-level navigation */}
                <a href="/bookings" className="nav-btn">View Bookings</a>
                <a href="/services" className="nav-btn">Update Services</a>
                <a href="/performance" className="nav-btn">Performance</a>
                <a href="/EmployeeManagement" className="nav-btn">Employees</a>

                {/* Dropdown menu */}
                <div className="dropdown">
                    <button className="menu-btn" onClick={toggleMenu}>
                        Menu ☰
                    </button>
                    {isOpen && (
                        <ul className="dropdown-menu">
                            <li><a href="/ProfileEmployee">Profile</a></li>
                        </ul>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default NavbarEmployee;
