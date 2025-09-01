import React, { useState, useEffect } from 'react';
import { cleaningServiceService } from '../services/cleaningService';
import LoadingSpinner from '../components/LoadingSpinner';
import './CleaningServicePage.css';

const CleaningServicePage = () => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedService, setSelectedService] = useState(null);
    const [filterCategory, setFilterCategory] = useState('all');

    useEffect(() => {
        fetchServices();
    }, []);

    const fetchServices = async () => {
        try {
            setLoading(true);
            const data = await cleaningServiceService.getAllCleaningServices();
            setServices(data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch cleaning services');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleServiceSelect = (service) => {
        setSelectedService(service);
    };

    const handleBookService = (service) => {
        // TODO: Implement booking functionality
        alert(`Booking ${service.serviceName.replace(/_/g, ' ')} for R${service.priceOfService.toFixed(2)}`);
    };

    const filteredServices = filterCategory === 'all'
        ? services
        : services.filter(service => service.category === filterCategory);

    const categories = ['all', ...new Set(services.map(s => s.category).filter(Boolean))];

    if (loading) {
        return <LoadingSpinner text="Loading cleaning services..." />;
    }

    return (
        <div className="cleaning-service-page">
            <div className="page-header">
                <div className="header-content">
                    <h1>Car Wash Services</h1>
                    <p>Choose from our range of professional cleaning services</p>
                </div>

                <div className="header-actions">
                    <a href="/cleaning-services/management" className="admin-link">
                        🛠️ Manage Services
                    </a>
                </div>

                {/* Category Filter */}
                <div className="category-filter">
                    <label>Filter by Category:</label>
                    <select
                        value={filterCategory}
                        onChange={(e) => setFilterCategory(e.target.value)}
                        className="filter-select"
                    >
                        {categories.map(category => (
                            <option key={category} value={category}>
                                {category === 'all' ? 'All Categories' : category}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError(null)}>×</button>
                </div>
            )}

            {/* Services Grid */}
            <div className="services-container">
                <div className="services-grid">
                    {filteredServices.map((service) => (
                        <div
                            key={service.cleaningServiceID}
                            className={`service-card ${selectedService?.cleaningServiceID === service.cleaningServiceID ? 'selected' : ''}`}
                        >
                            <div className="service-header">
                                <div className="service-icon">🧽</div>
                                <div className="service-badge">
                                    {service.category || 'Standard'}
                                </div>
                            </div>

                            <div className="service-content">
                                <h3>{service.serviceName.replace(/_/g, ' ')}</h3>
                                <p className="service-description">
                                    Professional {service.serviceName.replace(/_/g, ' ').toLowerCase()} service
                                </p>

                                <div className="service-details">
                                    <div className="detail-item">
                                        <span className="detail-label">Duration:</span>
                                        <span className="detail-value">{service.duration} hours</span>
                                    </div>
                                    {service.category && (
                                        <div className="detail-item">
                                            <span className="detail-label">Category:</span>
                                            <span className="detail-value">{service.category}</span>
                                        </div>
                                    )}
                                </div>

                                <div className="service-price">
                                    <span className="price-amount">R {service.priceOfService.toFixed(2)}</span>
                                    <span className="price-label">per service</span>
                                </div>
                            </div>

                            <div className="service-actions">
                                <button
                                    className="btn btn-outline"
                                    onClick={() => handleServiceSelect(service)}
                                >
                                    View Details
                                </button>
                                <button
                                    className="btn btn-primary"
                                    onClick={() => handleBookService(service)}
                                >
                                    Book Now
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* Selected Service Details Modal */}
            {selectedService && (
                <div className="service-modal-overlay" onClick={() => setSelectedService(null)}>
                    <div className="service-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>{selectedService.serviceName.replace(/_/g, ' ')}</h2>
                            <button
                                className="modal-close"
                                onClick={() => setSelectedService(null)}
                            >
                                ×
                            </button>
                        </div>

                        <div className="modal-content">
                            <div className="service-info-modal">
                                <div className="info-row">
                                    <span className="label">Duration:</span>
                                    <span className="value">{selectedService.duration} hours</span>
                                </div>
                                <div className="info-row">
                                    <span className="label">Price:</span>
                                    <span className="value">R {selectedService.priceOfService.toFixed(2)}</span>
                                </div>
                                {selectedService.category && (
                                    <div className="info-row">
                                        <span className="label">Category:</span>
                                        <span className="value">{selectedService.category}</span>
                                    </div>
                                )}
                            </div>

                            <div className="modal-actions">
                                <button
                                    className="btn btn-primary"
                                    onClick={() => handleBookService(selectedService)}
                                >
                                    Book This Service
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CleaningServicePage;
