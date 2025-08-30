import React from 'react';
import { useLocation } from 'react-router-dom';
import './ConfirmBookingPage.css';

function ConfirmBookingPage() {
  const location = useLocation();
  const { cart, totalPrice, selectedDate, selectedTime, selectedVehicle } = location.state || {}; // Get the passed data

  return (
    <div className="confirm-container">
      <div className="breadcrumb">
        Services &gt; Professional &gt; Time &gt; Vehicle &gt; <strong>Confirm</strong>
      </div>

      <h1>Review and confirm</h1>

      <div className="confirm-content">
        {/* LEFT PANEL */}
        <div className="confirm-left">
          <div className="form-section">
            <h3>Payment method</h3>
            <button className="payment-button">
              <span role="img" aria-label="store">🏪</span> Pay at venue
            </button>
          </div>

          <div className="form-section">
            <h3>Booking notes</h3>
            <textarea
              placeholder="Include comments or requests about your booking"
            />
          </div>
        </div>

        {/* RIGHT PANEL */}
        <div className="confirm-summary">
          <h3>Blush and Buff</h3>
          <p className="location">Parklands, Cape Town</p>
          <p><strong>{selectedDate.toLocaleDateString()}</strong></p>
          <p>{selectedTime} ({selectedDate.toLocaleTimeString()})</p>

          <hr />

          <div className="vehicle-info">
            <p>Vehicle: {selectedVehicle}</p>
          </div>

          {cart.map(service => (
            <div key={service.id} className="service-info">
              <p>{service.serviceName} - R {service.priceOfService}</p>
            </div>
          ))}

          <div className="price-row">
            <span>Subtotal</span>
            <span>R {totalPrice}</span>
          </div>

          <hr />

          <div className="total">
            <div>
              <div>Total</div>
              <div className="pay-now">Pay now</div>
              <div className="pay-at-venue">Pay at venue</div>
            </div>
            <div className="total-price">R {totalPrice}</div>
          </div>

          <button className="confirm-btn">Confirm</button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmBookingPage;