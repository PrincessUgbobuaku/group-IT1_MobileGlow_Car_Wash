import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./NavbarCustomer.css";
import logo from "../../assets/logo.jpg";

const NavbarCustomer = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const handleLogout = () => {
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRoleDescription");
    localStorage.removeItem("authToken");
    localStorage.removeItem("userId");
    navigate("/login");
  };

  const userEmail = localStorage.getItem("userEmail");
  const userInitial = userEmail ? userEmail.charAt(0).toUpperCase() : "P";

  return (
    <nav className="customer-navbar">
      <div className="app-content navbar-inner">
        <div className="navbar-left">
          <img src={logo} alt="Mobile Car Wash Logo" className="logo-img" />
          <a href = "LandingCustomer" className="logo-img"></a>
        </div>

        <div className="nav-links">
          <a href="#our-services" className="nav-btn">
            Our Services
          </a>
          <a href="/about" className="nav-btn">
            About Us
          </a>
          <a href="/Contact Us" className="nav-btn">
            Contact Us
          </a>

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
            <a href="/profiles">Profile</a>
          </li>
          <li>
            <a href="/vehicles">Vehicle Page</a>
          </li>
          <li>
            <a href="/bookinghistory">Booking History</a>
          </li>

          {/* ✅ New link for CustomerCardsPage */}
          <li>
            <a href="/my-cards">My Cards</a>
          </li>

          <li>
            <a href="/password-reset">Change Password</a>
          </li>
          <li>
            <a href="/deactivate-account">Deactivate Account</a>
          </li>
          <li>
            <button onClick={handleLogout} className="logout-btn">
              Log Out
            </button>
          </li>
        </ul>
      </div>

      {/* Overlay */}
      {isMenuOpen && <div className="overlay" onClick={toggleMenu}></div>}
    </nav>
  );
};

export default NavbarCustomer;