import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import './ConfirmBookingPage.css';

function ConfirmBookingPage() {
  const location = useLocation();
  const { cart = [], totalPrice = 0, selectedDate, selectedTime, selectedVehicle } = location.state || {};

  const [washAttendant, setWashAttendant] = useState(null);
  const [loadingAttendant, setLoadingAttendant] = useState(true);
  const [attendantError, setAttendantError] = useState(null);

  // Fetch random wash attendant on mount
  useEffect(() => {
    setLoadingAttendant(true);
    fetch('http://localhost:8080/mobileglow/wash-attendants/random')
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch wash attendant');
        }
        return response.json();
      })
      .then(data => {
        setWashAttendant(data);
        setLoadingAttendant(false);
      })
      .catch(error => {
        console.error('Error fetching wash attendant:', error);
        setAttendantError(error.message);
        setLoadingAttendant(false);
      });
  }, []);

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
            <textarea placeholder="Include comments or requests about your booking" />
          </div>
        </div>

        {/* RIGHT PANEL */}
        <div className="confirm-summary">
          <h3>MobileGlow Car Wash</h3>
          <p className="location">Parklands, Cape Town</p>
          {selectedDate && <p><strong>{selectedDate.toLocaleDateString()}</strong></p>}
          {selectedDate && <p>{selectedTime} ({selectedDate.toLocaleTimeString()})</p>}

          <hr />

          <div className="vehicle-info">
            <p><strong>Vehicle:</strong> {selectedVehicle}</p>
          </div>

          {/* Wash Attendant Section */}
          <div className="attendant-info">
            <h4>Assigned Wash Attendant</h4>
            {loadingAttendant && <p>Loading wash attendant...</p>}
            {attendantError && <p className="error">Error: {attendantError}</p>}
            {washAttendant && !loadingAttendant && !attendantError && (
              <>
                <p><strong>ID:</strong> #{washAttendant.userId}</p>
                <p><strong>Shift Hours:</strong> {washAttendant.shiftHours} hrs</p>
                <p><strong>Type:</strong> {washAttendant.employeeType}</p>
              </>
            )}
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