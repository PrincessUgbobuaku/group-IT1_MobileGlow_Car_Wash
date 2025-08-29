import React, { useState } from 'react';
import './Booking.css';
import { ToggleButton, ToggleButtonGroup } from '@mui/material';


const Booking = () => {

  const categories = ['Exterior Wash', 'Interior Cleaning', 'Full Detailing', 'Protection Services'];

  const services = [
    { id: 1, name: 'Gel Polish Application on Natural Hands', duration: '1 hr', price: 275, category: 'Featured' },
    { id: 2, name: 'Soak Off', duration: '15 mins – 35 mins', price: 60, category: 'Nail Enhancement' },
    { id: 3, name: 'Acrylic', duration: '10 mins – 1 hr, 30 mins', price: 100, category: 'Nail Enhancement' },
    { id: 4, name: 'Brows', duration: '20 mins', price: 120, category: 'Henna' },
  ];

  const [selectedCategory, setSelectedCategory] = useState('Featured');

    const handleCategoryChange = (event, newCategory) => {
      if (newCategory !== null) setSelectedCategory(newCategory);
    };

  const [cart, setCart] = useState([]);

  const addToCart = (service) => {
    if (!cart.includes(service)) {
      setCart([...cart, service]);
    }
  };

  const removeFromCart = (serviceId) => {
    setCart(cart.filter(item => item.id !== serviceId));
  };

  const totalPrice = cart.reduce((sum, item) => sum + item.price, 0);

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
          {services.map(service => (
            <div key={service.id} className="service-card">
              <div>
                <h4>{service.name}</h4>
                <p className="duration">{service.duration}</p>
                <p className="price">R {service.price}</p>
              </div>
              <button className="add-btn" onClick={() => addToCart(service)}>+</button>
            </div>
          ))}
        </div>
      </div>

      <div className="right-panel">
        <div className="business-info">
          <h3>Blush and Buff</h3>
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
                  {item.name} - R {item.price}
                  <button className="remove-btn" onClick={() => removeFromCart(item.id)}>x</button>
                </li>
              ))}
            </ul>
          )}
          <div className="total">
            <strong>Total:</strong> R {totalPrice || '0'}
          </div>
          <button className="continue-btn" disabled={cart.length === 0}>Continue</button>
        </div>
      </div>
    </div>
  );
};

export default Booking;