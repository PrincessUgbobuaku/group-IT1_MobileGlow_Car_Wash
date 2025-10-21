import React, { useState, useEffect, useRef } from 'react';
import Navbar from '../../components/Navbar';
import './FullDetailing.css';
import Footer from '../../components/Footer';

import waxing from '../../../assets/waxing.png';
import hero from '../../../assets/hero-carwash.jpg';
import interior from '../../../assets/interior.png';
import engine from '../../../assets/engine.png';

// Custom hook to handle scroll-based animation
const useScrollAnimation = () => {
    const refs = useRef([]);

    useEffect(() => {
        const observer = new IntersectionObserver(
            entries => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('visible');
                    }
                });
            },
            {
                threshold: 0.1,
            }
        );

        refs.current.forEach(ref => {
            if (ref) observer.observe(ref);
        });

        return () => {
            refs.current.forEach(ref => {
                if (ref) observer.unobserve(ref);
            });
        };
    }, []);

    return refs;
};

function FullDetailing() {
    const [activeIndex, setActiveIndex] = useState(null);
    const serviceRefs = useScrollAnimation();
    const faqRefs = useScrollAnimation();

    const toggleAccordion = (index) => {
        setActiveIndex(activeIndex === index ? null : index);
    };

    const services = [
        {
            title: 'Exterior Polishing & Waxing',
            description:
                'A thorough hand wash, wheel clean, and dry to remove dirt, grime, and road salt. Perfect for regular vehicle maintenance.',
            image: waxing,
        },
        {
            title: 'Interior Deep Cleaning',
            description:
                'Protect and enhance your car’s finish with our premium wax and polish. Adds shine and shields paint from environmental wear.',
            image: interior,
        },
        {
            title: 'Engine Bay Cleaning',
            description:
                'Deep clean and dress your tires and wheels for a showroom-ready appearance that lasts.',
            image: engine,
        },

    ];


    const faqs = [
        {
            question: 'How long does an exterior wash take?',
            answer: 'Most washes take between 20-30 minutes depending on the vehicle size.',
        },
        {
            question: 'Do you use eco-friendly products?',
            answer: 'Yes! We use biodegradable soaps and water-saving techniques.',
        },
        {
            question: 'Can I schedule recurring exterior washes?',
            answer: 'Absolutely! We offer weekly, bi-weekly, or monthly plans.',
        },
        {
            question: 'Do I need to be present during the wash?',
            answer: 'Not always — if access is arranged, we can clean while you’re away.',
        },
    ];

    return (
        <>
            <Navbar />

            <section
                className="exterior-hero"
                style={{
                    backgroundImage: `url(${hero})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    padding: '120px 20px',
                    color: 'white',
                    textAlign: 'center',
                    textShadow: '0 2px 6px rgba(0,0,0,0.7)',
                }}
            >
                <h1>Full Detailing Services</h1>
                <p>Keep your vehicles spotless and professional with our expertly crafted wash options.</p>
            </section>

            <section className="service-section app-content">
                {services.map((service, idx) => (
                    <div
                        key={idx}
                        className={`service-block ${idx % 2 !== 0 ? 'reverse' : ''} animate-up`}
                        ref={(el) => (serviceRefs.current[idx] = el)}
                    >
                        <div className="service-image">
                            <img src={service.image} alt={service.title} />
                        </div>
                        <div className="protection-services-description">
                            <h2>{service.title}</h2>
                            <p>{service.description}</p>
                        </div>
                    </div>
                ))}
            </section>

            <section className="book-now-container">
                <div className="book-now-container">
                    <button
                        onClick={() => (window.location.href = "/login")}
                        className="cta-button primary"
                    >
                        BOOK NOW
                    </button>
                </div>
            </section>

            <section className="faq-section app-content">
                <h2>Frequently Asked Questions About Exterior Wash</h2>
                <div className="accordion">
                    {faqs.map((faq, idx) => (
                        <div
                            key={idx}
                            className="accordion-item animate-up"
                            ref={(el) => (faqRefs.current[idx] = el)}
                        >
                            <button
                                className={`accordion-title ${activeIndex === idx ? 'active' : ''}`}
                                onClick={() => toggleAccordion(idx)}
                                aria-expanded={activeIndex === idx}
                                aria-controls={`faq-content-${idx}`}
                                id={`faq-title-${idx}`}
                            >
                                {faq.question}
                            </button>
                            <div
                                id={`faq-content-${idx}`}
                                role="region"
                                aria-labelledby={`faq-title-${idx}`}
                                className={`accordion-content ${activeIndex === idx ? 'open' : ''}`}
                            >
                                <p>{faq.answer}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </section>
            <Footer />
        </>
    );
}

export default FullDetailing;