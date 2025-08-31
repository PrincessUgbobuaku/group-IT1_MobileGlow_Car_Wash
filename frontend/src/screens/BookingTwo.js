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

    navigate("/bookingvehicle", {
      state: {
        cart,
        totalPrice,
        selectedDateTime, // Now this is a full date-time object
      }
    });
  };

  return (
    <div className="booking-two-container">
      <div className="left-panel">
        <h1>Select Date and Time</h1>
        <div className="calendar">
          <DatePicker
            selected={selectedDateTime}
            onChange={(date) => setSelectedDateTime(date)}
            showTimeSelect
            timeIntervals={15} // e.g., 15-minute intervals
            timeCaption="Time"
            dateFormat="MMMM d, yyyy h:mm aa"
            minDate={new Date()}
            placeholderText="Click to select date and time"
            className="custom-datepicker"
          />
        </div>
      </div>

      <div className="right-panel">
        <h3>MobileGlow Car Wash</h3>
        <p>Gel Polish Application on Natural Hands</p>
        <p>1 hr with Lynn</p>
        <div className="total-price">
          <strong>Total:</strong> <span>R {totalPrice}</span>
        </div>
        <button className="continue-btn" onClick={handleContinue}>
          Continue
        </button>
      </div>
    </div>
  );
};

export default BookingTwo;