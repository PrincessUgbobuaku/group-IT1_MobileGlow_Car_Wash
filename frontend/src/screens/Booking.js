// ...imports remain unchanged
import React, { useState, useEffect } from 'react';
import './Booking.css';
import { ToggleButton, ToggleButtonGroup } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const Booking = () => {
  const categories = ['Exterior Wash', 'Interior Care', 'Full Detailing', 'Protection Services'];
  const [selectedCategory, setSelectedCategory] = useState('Exterior Wash');
  const [services, setServices] = useState([]);
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/mobileglow/api/cleaningservice')
      .then((response) => response.json())
      .then((data) => {
        console.log('Fetched Services:', data);
        setServices(data);
      })
      .catch((error) => console.error('Error fetching services:', error));
  }, []);

  const handleCategoryChange = (event, newCategory) => {
    if (newCategory !== null) setSelectedCategory(newCategory);
  };

const filteredServices = services.filter(
  (service) => service.category?.trim().toLowerCase() === selectedCategory.trim().toLowerCase()
);

  const addToCart = (service) => {
    if (!cart.includes(service)) {
      setCart([...cart, service]);
    }
  };

  const removeFromCart = (serviceId) => {
    setCart(cart.filter(item => item.id !== serviceId));
  };

  const totalPrice = cart.reduce((sum, item) => sum + item.priceOfService, 0);

  const handleContinue = () => {
    navigate("/bookingtwo", {
      state: {
        cart: cart,
        totalPrice: totalPrice,
      },
    });
  };

  console.log("Selected category:", selectedCategory);
  console.log("All services from API:", services);

  return (
    <div className="booking-layout">
      <div className="left-panel">
        <button className="back-button">←</button>
        <h2 className="booking-page-heading">Services</h2>

        <ToggleButtonGroup
          value={selectedCategory}
          exclusive
          onChange={handleCategoryChange}
          aria-label="Category"
          className="category-toggle-group"
        >
          {categories.map((cat) => (
            <ToggleButton
              key={cat}
              value={cat}
              className="category-toggle-button"
            >
              {cat}
            </ToggleButton>
          ))}
        </ToggleButtonGroup>

        <div className="service-list">
          {filteredServices.length === 0 ? (
            <p>No services available for this category</p>
          ) : (
            filteredServices.map(service => (
              <div key={service.id} className="service-card">
                <div>
                  {/* 🔁 Replace underscores in service name */}
                  <h4>{service.serviceName.replace(/_/g, ' ')}</h4>

                  {/* 🕒 Add 'Duration:' label */}
                  <p className="duration">Duration: {service.duration} <strong>hours</strong></p>

                  <p className="price">R {service.priceOfService}</p>
                </div>
                <button className="add-btn" onClick={() => addToCart(service)}>+</button>
              </div>
            ))
          )}
        </div>
      </div>

      <div className="right-panel">
        <div className="business-info">
          <h3>MobileGlow Car Wash</h3>
          <p>4.9 ⭐ (32)</p>
          <p>Parklands, Cape Town</p>
        </div>
        <div className="cart">
          <h4>Selected Services</h4>
          {cart.length === 0 ? (
            <p className="empty-cart">No services selected</p>
          ) : (
            <ul>
              {cart.map(item => (
                <li key={item.id}>
                  {/* Replace underscores in cart items as well */}
                  {item.serviceName.replace(/_/g, ' ')} - R {item.priceOfService}
                  <button className="remove-btn" onClick={() => removeFromCart(item.id)}>x</button>
                </li>
              ))}
            </ul>
          )}
          <div className="total">
            <strong>Total:</strong> R {totalPrice || '0'}
          </div>
          <button className="continue-btn" disabled={cart.length === 0} onClick={handleContinue}>Continue</button>
        </div>
      </div>
    </div>
  );
};

export default Booking;