import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import emailjs from 'emailjs-com';
import './ContactUs.css';
import Footer from"./components/Footer";

const ContactUs = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        subject: '',
        message: ''
    });
    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitSuccess, setSubmitSuccess] = useState(false);
    const [visible, setVisible] = useState(false);
    const [activeFaqIndex, setActiveFaqIndex] = useState(null);
    const navigate = useNavigate();

    const EMAILJS_CONFIG = {
        SERVICE_ID: 'service_t8xcg8k',
        TEMPLATE_ID: 'template_8s0ryoj',
        USER_ID: 'sqpo7Bji9gBBnZBTW'
    };

    // Social media links - replace with your actual social media URLs
    const socialLinks = {
        facebook: 'https://www.facebook.com/yourpage',
        instagram: 'https://www.instagram.com/yourprofile',
        twitter: 'https://www.twitter.com/yourprofile',
        linkedin: 'https://www.linkedin.com/company/yourcompany'
    };

    const faqData = [
        {
            question: "What services do you offer?",
            answer: "We offer comprehensive mobile car detailing services including exterior washing, waxing, interior cleaning, vacuuming, leather conditioning, and specialized treatments for headlights and windows."
        },
        {
            question: "How long does a typical service take?",
            answer: "Basic wash takes 30-45 minutes, full exterior detail takes 2-3 hours, and complete interior & exterior packages take 3-4 hours depending on vehicle size and condition."
        },
        {
            question: "Do you use eco-friendly products?",
            answer: "Yes! We prioritize environmentally friendly cleaning products that are biodegradable and safe for your vehicle and the environment."
        },
        {
            question: "Can I schedule recurring appointments?",
            answer: "Absolutely! We offer flexible scheduling options including one-time, weekly, bi-weekly, and monthly maintenance plans to keep your vehicle looking its best."
        },
        {
            question: "What areas do you serve?",
            answer: "We currently serve the greater Johannesburg area. Contact us to confirm if we cover your specific location."
        },
        {
            question: "What payment methods do you accept?",
            answer: "We accept cash, credit/debit cards, EFT payments, and mobile payment solutions like Zapper and SnapScan."
        }
    ];

    useEffect(() => {
        setVisible(true);
        if (submitSuccess) {
            const timer = setTimeout(() => {
                setSubmitSuccess(false);
            }, 5000);
            return () => clearTimeout(timer);
        }
    }, [submitSuccess]);

    const validateForm = () => {
        const newErrors = {};

        if (!formData.name.trim()) {
            newErrors.name = 'Name is required';
        }

        if (!formData.email.trim()) {
            newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Email is invalid';
        }

        if (!formData.subject.trim()) {
            newErrors.subject = 'Subject is required';
        }

        if (!formData.message.trim()) {
            newErrors.message = 'Message is required';
        } else if (formData.message.length > 500) {
            newErrors.message = 'Message must be less than 500 characters';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });

        // Clear error when user starts typing
        if (errors[name]) {
            setErrors({ ...errors, [name]: '' });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            // Add shake animation to form on error
            const form = document.querySelector('.form-card');
            form.classList.add('shake');
            setTimeout(() => form.classList.remove('shake'), 500);
            return;
        }

        setIsSubmitting(true);

        try {
            // Send email using EmailJS
            await emailjs.send(
                EMAILJS_CONFIG.SERVICE_ID,
                EMAILJS_CONFIG.TEMPLATE_ID,
                {
                    from_name: formData.name,
                    from_email: formData.email,
                    subject: formData.subject,
                    message: formData.message,
                    to_email: 'waterlilykid.123@gmail.com',
                    reply_to: formData.email,
                    timestamp: new Date().toLocaleString()
                },
                EMAILJS_CONFIG.USER_ID
            );

            setIsSubmitting(false);
            setSubmitSuccess(true);
            setFormData({ name: '', email: '', subject: '', message: '' });

        } catch (error) {
            console.error('Failed to send email:', error);
            setIsSubmitting(false);
            // Handle error state
            setErrors({ submit: 'Failed to send message. Please try again later.' });
        }
    };

    const handleBackToHome = () => {
        navigate('/');
    };

    const toggleFaq = (index) => {
        setActiveFaqIndex(activeFaqIndex === index ? null : index);
    };

    const handleSocialClick = (platform) => {
        // You can add tracking or analytics here if needed
        console.log(`Navigating to ${platform}`);
    };

    return (
        <div className={`contact-us ${visible ? 'visible' : ''}`}>
            {/* Modern minimalistic header */}
            <div className="contact-header">
                <button className="back-button" onClick={handleBackToHome}>
                    ← Back to Home
                </button>
                <h1>Contact Us</h1>
                <p>Get in touch with our team</p>
            </div>

            <div className="contact-container">
                {/* Contact form section */}
                <div className="contact-form-section">
                    <div className="form-card">
                        <h3>Send us a message</h3>
                        <p className="form-subtitle">We'll get back to you as soon as possible</p>

                        {submitSuccess && (
                            <div className="success-message">
                                <div className="success-icon">✓</div>
                                <div>
                                    <h4>Message sent successfully</h4>
                                    <p>Thank you for your message! We will get back to you soon.</p>
                                </div>
                            </div>
                        )}

                        {errors.submit && (
                            <div className="submit-error">
                                {errors.submit}
                            </div>
                        )}

                        <form onSubmit={handleSubmit} noValidate>
                            <div className="form-grid">
                                <div className="form-group">
                                    <label htmlFor="name">Full Name</label>
                                    <input
                                        type="text"
                                        id="name"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleInputChange}
                                        className={errors.name ? 'error' : ''}
                                        placeholder="Enter your full name"
                                        required
                                    />
                                    {errors.name && <span className="error-message">{errors.name}</span>}
                                </div>

                                <div className="form-group">
                                    <label htmlFor="email">Email Address</label>
                                    <input
                                        type="email"
                                        id="email"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleInputChange}
                                        className={errors.email ? 'error' : ''}
                                        placeholder="Enter your email address"
                                        required
                                    />
                                    {errors.email && <span className="error-message">{errors.email}</span>}
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="subject">Subject</label>
                                <input
                                    type="text"
                                    id="subject"
                                    name="subject"
                                    value={formData.subject}
                                    onChange={handleInputChange}
                                    className={errors.subject ? 'error' : ''}
                                    placeholder="What is this regarding?"
                                    required
                                />
                                {errors.subject && <span className="error-message">{errors.subject}</span>}
                            </div>

                            <div className="form-group">
                                <label htmlFor="message">Message</label>
                                <textarea
                                    id="message"
                                    name="message"
                                    value={formData.message}
                                    onChange={handleInputChange}
                                    className={errors.message ? 'error' : ''}
                                    rows="4"
                                    placeholder="Type your message here..."
                                    required
                                ></textarea>
                                {errors.message && <span className="error-message">{errors.message}</span>}
                                <div className={`char-counter ${formData.message.length > 500 ? 'error' : ''}`}>
                                    {formData.message.length}/500
                                </div>
                            </div>

                            <button
                                type="submit"
                                className={`submit-button ${isSubmitting ? 'loading' : ''}`}
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? (
                                    <>
                                        <div className="spinner"></div>
                                        Sending...
                                    </>
                                ) : (
                                    'Send Message'
                                )}
                            </button>
                        </form>
                    </div>
                </div>

                {/* Contact information section */}
                <div className="contact-info-section">
                    <div className="info-card">
                        <div className="info-icon">📍</div>
                        <h4>Visit Us</h4>
                        <p>123 Car Care Street<br />Johannesburg, 2000<br />South Africa</p>
                        <a href="#" className="map-link">
                            View on map
                        </a>
                    </div>

                    <div className="info-card">
                        <div className="info-icon">📞</div>
                        <h4>Call Us</h4>
                        <p>+27 11 123 4567<br />Mon - Fri, 8:00 AM - 6:00 PM</p>
                        <a href="tel:+27111234567" className="contact-link">
                            Call now
                        </a>
                    </div>

                    <div className="info-card">
                        <div className="info-icon">✉️</div>
                        <h4>Email Us</h4>
                        <p>waterlilykid.123@gmail.com<br />support@mobileglow.com</p>
                        <a href="mailto:waterlilykid.123@gmail.com" className="contact-link">
                            Send email
                        </a>
                    </div>

                    <div className="social-section">
                        <h5>Follow Us</h5>
                        <div className="social-links">
                            <a
                                href={socialLinks.facebook}
                                target="_blank"
                                rel="noopener noreferrer"
                                aria-label="Facebook"
                                onClick={() => handleSocialClick('facebook')}
                            >
                                <i className="fab fa-facebook-f"></i>
                            </a>
                            <a
                                href={socialLinks.instagram}
                                target="_blank"
                                rel="noopener noreferrer"
                                aria-label="Instagram"
                                onClick={() => handleSocialClick('instagram')}
                            >
                                <i className="fab fa-instagram"></i>
                            </a>
                            <a
                                href={socialLinks.twitter}
                                target="_blank"
                                rel="noopener noreferrer"
                                aria-label="Twitter"
                                onClick={() => handleSocialClick('twitter')}
                            >
                                <i className="fab fa-twitter"></i>
                            </a>
                            <a
                                href={socialLinks.linkedin}
                                target="_blank"
                                rel="noopener noreferrer"
                                aria-label="LinkedIn"
                                onClick={() => handleSocialClick('linkedin')}
                            >
                                <i className="fab fa-linkedin-in"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            {/* FAQ Section */}
            <div className="faq-section">
                <div className="faq-container">
                    <div className="faq-header">
                        <h2>Frequently Asked Questions</h2>
                        <p>Find quick answers to common questions about our services</p>
                    </div>

                    <div className="faq-list">
                        {faqData.map((faq, index) => (
                            <div
                                key={index}
                                className={`faq-item ${activeFaqIndex === index ? 'active' : ''}`}
                            >
                                <div
                                    className="faq-question"
                                    onClick={() => toggleFaq(index)}
                                >
                                    <span>{faq.question}</span>
                                    <span className="faq-icon">
                                        {activeFaqIndex === index ? '−' : '+'}
                                    </span>
                                </div>
                                <div className="faq-answer">
                                    <p>{faq.answer}</p>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="faq-cta">
                        <p>Still have questions? We're here to help!</p>
                        <button
                            className="cta-button"
                            onClick={() => document.querySelector('.contact-form-section').scrollIntoView({ behavior: 'smooth' })}
                        >
                            Contact Us Now
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ContactUs;