// components/Footer.js
import React from "react";
import "./Footer.css"; // optional if you want a separate CSS

const Footer = () => {
    return (
        <footer className="footer">
            <div className="footer-container">
                <div className="footer-section">
                    <h3 className="footer-heading">Mobile Car Wash</h3>
                    <p className="footer-text">Professional mobile car cleaning services at your doorstep.</p>
                </div>

                <div className="footer-section">
                    <h4 className="footer-subheading">For Customers</h4>
                    <ul className="footer-links">
                        <li><a href="/login">Log in or sign up</a></li>
                        <li><a href="/download">Download the app</a></li>
                        <li><a href="/help">Help and support</a></li>
                    </ul>
                </div>

                <div className="footer-section">
                    <h4 className="footer-subheading">For Businesses</h4>
                    <ul className="footer-links">
                        <li><a href="/business">List your business</a></li>
                        <li><a href="/partnership">Partnership opportunities</a></li>
                    </ul>
                </div>

                <div className="footer-section">
                    <h4 className="footer-subheading">Connect With Us</h4>
                    <div className="social-icons">
                        <a href="https://facebook.com" aria-label="Facebook">FB</a>
                        <a href="https://twitter.com" aria-label="Twitter">TW</a>
                        <a href="https://instagram.com" aria-label="Instagram">IG</a>
                    </div>
                </div>
            </div>

            <div className="footer-bottom">
                <p>© {new Date().getFullYear()} Mobile Car Wash. All rights reserved.</p>
                <div className="legal-links">
                    <a href="/privacy">Privacy Policy</a>
                    <a href="/terms">Terms of Service</a>
                    <a href="/cookies">Cookie Policy</a>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
