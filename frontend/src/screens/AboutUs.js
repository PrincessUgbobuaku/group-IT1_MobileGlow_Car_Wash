import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './AboutUs.css';
import './components/Footer';

const AboutUs = () => {
    const [visibleSections, setVisibleSections] = useState({});
    const [activeValue, setActiveValue] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        // Animation on scroll
        const handleScroll = () => {
            const sections = document.querySelectorAll('.about-section, .values-section, .team-section');
            const newVisibleSections = {};

            sections.forEach(section => {
                const rect = section.getBoundingClientRect();
                const sectionId = section.id;

                if (rect.top < window.innerHeight - 100) {
                    newVisibleSections[sectionId] = true;
                }
            });

            setVisibleSections(newVisibleSections);
        };

        window.addEventListener('scroll', handleScroll);
        handleScroll(); // Initial check

        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    const toggleValueCard = (index) => {
        setActiveValue(activeValue === index ? null : index);
    };

    const handleContactUs = () => {
        navigate('/contactus');
    };

    const handleBookNow = () => {
        navigate('/signup');
    };

    // Function to handle social media icon clicks
    const handleSocialClick = (platform, profileName = '') => {
        const urls = {
            linkedin: `https://www.linkedin.com/in/${profileName}`,
            twitter: `https://twitter.com/${profileName}`,
            email: `mailto:${profileName}@mobileglow.com`
        };

        if (platform === 'email' && urls[platform]) {
            window.location.href = urls[platform];
        } else if (urls[platform]) {
            window.open(urls[platform], '_blank', 'noopener,noreferrer');
        }
    };

    // Team member data with social media profiles
    const teamMembers = [
        {
            name: 'Abulele Manager',
            role: 'Operations Manager',
            bio: 'With over 15 years in the automotive industry, Abu ensures our operations run smoothly and efficiently.',
            social: {
                linkedin: 'abulele-manager',
                twitter: 'abulele_m',
                email: 'abulele'
            }
        },
        {
            name: 'Princess CEO',
            role: 'Chief Executive Officer',
            bio: 'Princess founded MobileGlow with a vision to revolutionize car care in South Africa.',
            social: {
                linkedin: 'princess-ceo',
                twitter: 'princess_ceo',
                email: 'princess'
            }
        },
        {
            name: 'Kwanda Tech',
            role: 'Technology Director',
            bio: 'Kwanda leads our tech initiatives, ensuring we stay at the forefront of automotive care technology.',
            social: {
                linkedin: 'kwanda-tech',
                twitter: 'kwanda_tech',
                email: 'kwanda'
            }
        },
        {
            name: 'Inga Finance',
            role: 'Finance Manager',
            bio: 'Inga keeps our finances in order and plans for sustainable growth and expansion.',
            social: {
                linkedin: 'inga-finance',
                twitter: 'inga_finance',
                email: 'inga'
            }
        }
    ];

    const tharkirahMember = {
        name: 'Tharkirah Member',
        role: 'Customer Relations Specialist',
        bio: 'Tharkirah ensures our customers receive exceptional service and maintains strong relationships with our valued clients.',
        social: {
            linkedin: 'tharkirah-member',
            twitter: 'tharkirah_crs',
            email: 'tharkirah'
        }
    };

    return (
        <div className="about-us">
            <div className="about-hero">
                <h1>About MobileGlow Car Wash</h1>
                <p>Transforming car care with passion and precision</p>
                <div className="scroll-indicator">
                    <span>Scroll to explore</span>
                    <i className="fas fa-chevron-down"></i>
                </div>
            </div>

            <div className="about-content">
                <div
                    id="story"
                    className={`about-section ${visibleSections.story ? 'visible' : ''}`}
                >
                    <div className="about-text">
                        <h3>Our Story</h3>
                        <p>Founded in 2020, MobileGlow started as a small car wash operation with a big vision: to revolutionize the car care industry through exceptional service, cutting-edge technology, and a commitment to environmental sustainability.</p>
                        <p>Today, we're proud to serve thousands of customers across South Africa with our network of professional car care specialists and state-of-the-art facilities.</p>
                        <div className="stats-container">
                            <div className="stat">
                                <h4>10,000+</h4>
                                <p>Happy Customers</p>
                            </div>
                            <div className="stat">
                                <h4>50+</h4>
                                <p>Professional Staff</p>
                            </div>
                            <div className="stat">
                                <h4>5</h4>
                                <p>Cities Served</p>
                            </div>
                        </div>
                    </div>
                    <div className="about-image">
                        <img src="https://images.unsplash.com/photo-1503376780353-7e6692767b70?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80" alt="Car washing" />
                        <div className="image-overlay">
                            <p>Premium car care services since 2020</p>
                        </div>
                    </div>
                </div>

                <div
                    id="values"
                    className={`values-section ${visibleSections.values ? 'visible' : ''}`}
                >
                    <h2 className="section-title">Our Values</h2>
                    <div className="values-grid">
                        {[
                            {
                                icon: 'fas fa-shield-alt',
                                title: 'Quality',
                                description: 'We never compromise on the quality of our services. Every vehicle is treated with the utmost care and attention to detail.',
                                moreInfo: 'Our team undergoes regular training to maintain the highest standards of service delivery.'
                            },
                            {
                                icon: 'fas fa-leaf',
                                title: 'Sustainability',
                                description: 'We use eco-friendly cleaning products and water-saving techniques to minimize our environmental impact.',
                                moreInfo: 'We recycle 90% of our water and use biodegradable cleaning products exclusively.'
                            },
                            {
                                icon: 'fas fa-users',
                                title: 'Community',
                                description: 'We\'re committed to creating jobs and investing in the local communities where we operate.',
                                moreInfo: 'Each year, we donate 5% of our profits to local community initiatives and youth programs.'
                            }
                        ].map((value, index) => (
                            <div
                                key={index}
                                className={`value-card ${activeValue === index ? 'active' : ''}`}
                                onClick={() => toggleValueCard(index)}
                            >
                                <div className="value-icon">
                                    <i className={value.icon}></i>
                                </div>
                                <h5>{value.title}</h5>
                                <p>{value.description}</p>
                                <div className="value-details">
                                    <p>{value.moreInfo}</p>
                                </div>
                                <div className="value-toggle">
                                    <i className={`fas fa-chevron-${activeValue === index ? 'up' : 'down'}`}></i>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                <div
                    id="team"
                    className={`team-section ${visibleSections.team ? 'visible' : ''}`}
                >
                    <h2 className="section-title">Meet Our Leadership Team</h2>
                    <div className="team-grid">
                        {teamMembers.map((member, index) => (
                            <div key={index} className="team-member">
                                <div className="member-image">
                                    <img
                                        src={`https://ui-avatars.com/api/?name=${member.name.replace(' ', '+')}&size=120&background=4a6cf7&color=fff`}
                                        alt={member.name}
                                    />
                                    <div className="social-links">
                                        <a
                                            href="#"
                                            onClick={(e) => {
                                                e.preventDefault();
                                                handleSocialClick('linkedin', member.social.linkedin);
                                            }}
                                            title="LinkedIn Profile"
                                        >
                                            <i className="fab fa-linkedin"></i>
                                        </a>
                                        <a
                                            href="#"
                                            onClick={(e) => {
                                                e.preventDefault();
                                                handleSocialClick('twitter', member.social.twitter);
                                            }}
                                            title="Twitter Profile"
                                        >
                                            <i className="fab fa-twitter"></i>
                                        </a>
                                        <a
                                            href="#"
                                            onClick={(e) => {
                                                e.preventDefault();
                                                handleSocialClick('email', member.social.email);
                                            }}
                                            title="Send Email"
                                        >
                                            <i className="fas fa-envelope"></i>
                                        </a>
                                    </div>
                                </div>
                                <h5>{member.name}</h5>
                                <p className="role">{member.role}</p>
                                <p className="bio">{member.bio}</p>
                            </div>
                        ))}
                    </div>

                    {/* Tharkirah's card positioned below in center */}
                    <div className="team-grid-centered">
                        <div className="team-member">
                            <div className="member-image">
                                <img
                                    src={`https://ui-avatars.com/api/?name=Tharkirah+Member&size=120&background=4a6cf7&color=fff`}
                                    alt="Tharkirah Member"
                                />
                                <div className="social-links">
                                    <a
                                        href="#"
                                        onClick={(e) => {
                                            e.preventDefault();
                                            handleSocialClick('linkedin', tharkirahMember.social.linkedin);
                                        }}
                                        title="LinkedIn Profile"
                                    >
                                        <i className="fab fa-linkedin"></i>
                                    </a>
                                    <a
                                        href="#"
                                        onClick={(e) => {
                                            e.preventDefault();
                                            handleSocialClick('twitter', tharkirahMember.social.twitter);
                                        }}
                                        title="Twitter Profile"
                                    >
                                        <i className="fab fa-twitter"></i>
                                    </a>
                                    <a
                                        href="#"
                                        onClick={(e) => {
                                            e.preventDefault();
                                            handleSocialClick('email', tharkirahMember.social.email);
                                        }}
                                        title="Send Email"
                                    >
                                        <i className="fas fa-envelope"></i>
                                    </a>
                                </div>
                            </div>
                            <h5>{tharkirahMember.name}</h5>
                            <p className="role">{tharkirahMember.role}</p>
                            <p className="bio">{tharkirahMember.bio}</p>
                        </div>
                    </div>
                </div>

                <div className="cta-section">
                    <h2>Ready to Experience the MobileGlow Difference?</h2>
                    <p>Book our premium car care services today</p>
                    <div className="cta-buttons">
                        <button className="btn btn-primary" onClick={handleBookNow}>Book Now</button>
                        <button className="btn btn-secondary" onClick={handleContactUs}>Contact Us</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AboutUs;