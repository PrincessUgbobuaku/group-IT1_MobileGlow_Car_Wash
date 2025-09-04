import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import './BookingTwo.css';

const BookingTwo = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { cart, totalPrice } = location.state;
  const [selectedDateTime, setSelectedDateTime] = useState(null);

  const handleContinue = () => {
    if (!selectedDateTime) {
      alert("Please select both a date and time!");
      return;
    }

    const serviceIds = cart.map(service => ({ cleaningServiceId: service.id }));

    navigate("/bookingvehicle", {
      state: {
        cart,
        totalPrice,
        selectedDateTime,
        serviceIds,
      },
    });
  };

  return (
    <div className="booking-vehicle-container">
      <div className="left-panel">
        <h2 className="booking-page-heading">Select Date and Time</h2>

        <div className="vehicle-selection">
          <label>Select date and time:</label>
          <DatePicker
            selected={selectedDateTime}
            onChange={(date) => setSelectedDateTime(date)}
            showTimeSelect
            timeIntervals={15}
            timeCaption="Time"
            dateFormat="MMMM d, yyyy h:mm aa"
            minDate={new Date()}
            minTime={new Date(new Date().setHours(8, 0, 0, 0))}   // 08:00 AM
            maxTime={new Date(new Date().setHours(17, 0, 0, 0))}  // 05:00 PM
            placeholderText="Click to select date and time"
            className="custom-datepicker"
          />
        </div>

        <button
          className="continue-btn"
          onClick={handleContinue}
          disabled={!selectedDateTime}
        >
          Continue
        </button>
      </div>

      <div className="right-panel">
        <div className="business-info">
          <h3>MobileGlow Car Wash</h3>
          <p>4.9 ⭐ (32)</p>
          <p>Parklands, Cape Town</p>
        </div>

        <div className="summary-section">
          <h4>Selected Services</h4>
          <ul>
            {cart.map((service, i) => (
              <li key={i}>
                {service.serviceName.replace(/_/g, ' ')} - R {service.priceOfService}
              </li>
            ))}
          </ul>

          <h4>Date & Time</h4>
          <p>{selectedDateTime ? new Date(selectedDateTime).toLocaleString() : "Not selected"}</p>

          <div className="total-price">
            <strong>Total:</strong> <span>R {totalPrice}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingTwo;