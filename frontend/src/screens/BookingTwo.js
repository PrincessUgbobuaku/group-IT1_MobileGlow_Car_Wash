import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import './BookingTwo.css';

const BookingTwo = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { cart, totalPrice } = location.state; // Get cart and total price from the previous page
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [selectedTime, setSelectedTime] = useState(null); // Initialize as null, so no time is selected by default

  const availableTimes = [
    "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30"
  ];

  // Handle time selection
  const handleTimeChange = (time) => {
    setSelectedTime(time); // This will update the selectedTime state when a time is clicked
  };

  // Handle continue action
  const handleContinue = () => {
    // Ensure selectedTime and selectedDate are set before navigating
    if (!selectedTime || !selectedDate) {
      alert("Please select both a time and a date!");
      return;
    }

    // Proceed to the next page
    navigate("/bookingvehicle", {
      state: {
        cart,
        totalPrice,
        selectedDate,
        selectedTime, // Pass selected time to the vehicle selection page
      }
    });
  };

  return (
    <div className="booking-two-container">
      <div className="left-panel">
        <h1>Select Date and Time</h1>
        <div className="calendar">
          <DatePicker
            selected={selectedDate}
            onChange={(date) => setSelectedDate(date)} // Set the selected date
            showTimeSelect
            dateFormat="Pp"
            minDate={new Date()} // Ensure the user can't pick a past date
          />
        </div>

        <div className="time-selector">
          <h2>Select Time</h2>
          <div className="time-buttons">
            {availableTimes.map(time => (
              <button
                key={time}
                onClick={() => handleTimeChange(time)} // Update selectedTime when clicked
                className={`time-btn ${selectedTime === time ? "selected" : ""}`}
              >
                {time}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="right-panel">
        <h3>Blush and Buff</h3>
        <p>Gel Polish Application on Natural Hands</p>
        <p>1 hr with Lynn</p>
        <div className="total-price">
          <strong>Total:</strong> <span>R {totalPrice}</span>
        </div>
        <button className="continue-btn" onClick={handleContinue}>Continue</button>
      </div>
    </div>
  );
};

export default BookingTwo;