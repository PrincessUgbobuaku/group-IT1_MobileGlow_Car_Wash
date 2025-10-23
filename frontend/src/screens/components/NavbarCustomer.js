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

  // ✅ Scroll smoothly to "Our Services" section
  const handleServices = () => {
    const section = document.getElementById("our-services");
    if (section) {
      section.scrollIntoView({ behavior: "smooth" });
    }
  };
  const handleAboutUs = () => navigate("/AboutUsCustomer");
  const handleContactUs = () => navigate("/ContactUsCustomer");
  const handleLogoClick = () => navigate("/LandingCustomer");
  return (
    <nav className="customer-navbar">
      <div className="app-content navbar-inner">
        <div className="navbar-left" onClick={handleLogoClick} style={{ cursor: "pointer" }}>
          <img src={logo} alt="Mobile Car Wash Logo" className="logo-img" />
        </div>

        <div className="nav-links">
          <button className="nav-btn" onClick={handleServices}>
            Our Services
          </button>
          <button className="nav-btn" onClick={handleAboutUs}>
            About Us
          </button>
          <button className="nav-btn" onClick={handleContactUs}>
            Contact Us
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