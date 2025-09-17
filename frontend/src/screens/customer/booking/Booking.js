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
    fetch('http://localhost:8080/mobileglow/api/cleaningservice/getAll')
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

  const filteredServices = Array.isArray(services)
    ? services.filter(
        (service) => service.category?.trim().toLowerCase() === selectedCategory.trim().toLowerCase()
      )
    : [];

  const addToCart = (service) => {
    const normalizedService = {
      ...service,
      id: service.cleaningServiceId
    };

    console.log("Trying to add service to cart:", normalizedService);

    if (!cart.some(item => item.id === normalizedService.id)) {
      const newCart = [...cart, normalizedService];
      setCart(newCart);
      console.log("Updated cart after addition:", newCart);
    } else {
      console.log("Service already in cart:", normalizedService);
    }
  };

  const removeFromCart = (serviceId) => {
    setCart(cart.filter(item => item.id !== serviceId));
  };

  const totalPrice = cart.reduce((sum, item) => sum + item.priceOfService, 0);
  console.log("Current cart contents:", cart);

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
                <div className="service-info">
                  <h4>{service.serviceName.replace(/_/g, ' ')}</h4>
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
              {cart.map((item, index) => {
                console.log(`Rendering cart item - id: ${item.id}, name: ${item.serviceName}`);
                return (
                  <li key={item.id || `${item.serviceName}-${index}`}>
                    {item.serviceName.replace(/_/g, ' ')} - R {item.priceOfService}
                    <button className="remove-btn" onClick={() => removeFromCart(item.id)}>x</button>
                  </li>
                );
              })}
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