import { useState } from "react";
import { useNavigate } from "react-router-dom";
import './BookingTwo.css';

function BookingTwo() {
  const navigate = useNavigate();
  const [selectedDate, setSelectedDate] = useState("2025-08-23");

  const fullyBookedDates = ["2025-08-23", "2025-08-24"];
  const dates = [
    "2025-08-23",
    "2025-08-24",
    "2025-08-25",
    "2025-08-26",
    "2025-08-27",
    "2025-08-28",
    "2025-08-29",
  ];

  const availableTimes = [
    "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30"
  ];

  const handleContinue = () => {
    navigate("/confirm", { state: { date: selectedDate } });
  };

  const isFullyBooked = fullyBookedDates.includes(selectedDate);

  const selectedDateObj = new Date(selectedDate);
  const monthName = selectedDateObj.toLocaleString("en-US", { month: "long", year: "numeric" });

  return (
    <div className="booking-container">
      {/* Left Section */}
      <div className="booking-calendar">
        <h1>Select time</h1>
        <div className="month-label">{monthName}</div>

        <div className="date-grid">
          {dates.map((date) => {
            const d = new Date(date);
            const day = d.toLocaleDateString("en-US", { weekday: "short" });
            const dayNum = d.getDate();
            return (
              <button
                key={date}
                onClick={() => setSelectedDate(date)}
                className={`date-btn ${
                  selectedDate === date ? "selected" : ""
                }`}
                disabled={fullyBookedDates.includes(date)}
              >
                <div>{dayNum}</div>
                <div>{day}</div>
              </button>
            );
          })}
        </div>

        {isFullyBooked ? (
          <div className="info-card">
            <p>Lynn is fully booked on this date</p>
            <p>Available from Mon, 25 Aug</p>
            <div style={{ display: "flex", gap: "10px", marginTop: "10px" }}>
              <button className="outline-btn">Go to next available date</button>
              <button className="outline-btn">Join waitlist</button>
            </div>
          </div>
        ) : (
          <>
            <div className="info-card">
              <p>Available slots for {selectedDate}</p>
              <p>Choose your time at confirmation</p>
            </div>
            <div className="time-grid">
              {availableTimes.map((time) => (
                <button key={time} className="time-btn">{time}</button>
              ))}
            </div>
          </>
        )}
      </div>

      {/* Right Section */}
      <div className="booking-summary">
        <h3>Blush and Buff</h3>
        <p>Gel Polish Application on Natural Hands</p>
        <p>1 hr with Lynn</p>
        <div style={{ marginTop: "20px", fontWeight: "bold" }}>
          Total: <span style={{ float: "right" }}>R 275</span>
        </div>
        <button
          className="continue-btn"
          disabled={isFullyBooked}
          onClick={handleContinue}
        >
          Continue
        </button>
      </div>
    </div>
  );
}

export default BookingTwo;