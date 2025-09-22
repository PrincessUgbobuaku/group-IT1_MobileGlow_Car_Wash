import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate, Link } from "react-router-dom";
import './BookingTwo.css';

const BookingTwo = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // ✅ Fallback to sessionStorage if location.state is null
  const savedBookingData = JSON.parse(sessionStorage.getItem("bookingData")) || {};
  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime = null,
    serviceIds = [],
  } = location.state || savedBookingData;

  const [selectedDate, setSelectedDate] = useState(
    selectedDateTime ? new Date(selectedDateTime) : null
  );
  const [selectedTime, setSelectedTime] = useState(
    selectedDateTime ? new Date(selectedDateTime) : null
  );

  const [visibleMonth, setVisibleMonth] = useState('');
  const scrollRef = useRef(null);
  const INITIAL_BUFFER = 30;
  const [days, setDays] = useState([]);
  const latestDateRef = useRef(null);

  useEffect(() => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const generated = [];
    for (let offset = 0; offset <= INITIAL_BUFFER; offset++) {
      const d = new Date(today);
      d.setDate(today.getDate() + offset);
      generated.push(d);
    }

    setDays(generated);
    latestDateRef.current = generated[generated.length - 1];

    setVisibleMonth(today.toLocaleDateString("en-US", { month: "long", year: "numeric" }));

    setTimeout(() => {
      if (scrollRef.current) {
        const child = scrollRef.current.children[0];
        if (child) {
          child.scrollIntoView({ behavior: "auto", inline: "start" });
        }
      }
    }, 50);
  }, []);

  const extendDays = () => {
    setDays(prevDays => {
      const newDays = [];
      for (let i = 1; i <= 15; i++) {
        const nextDay = new Date(latestDateRef.current);
        nextDay.setDate(nextDay.getDate() + i);
        newDays.push(nextDay);
      }
      latestDateRef.current = newDays[newDays.length - 1];
      return [...prevDays, ...newDays];
    });
  };

  const onScroll = () => {
    if (!scrollRef.current) return;

    const { scrollLeft, scrollWidth, clientWidth } = scrollRef.current;

    if (scrollLeft + clientWidth >= scrollWidth - 100) {
      extendDays();
    }

    const children = Array.from(scrollRef.current.children);
    const firstVisible = children.find(child => {
      const rect = child.getBoundingClientRect();
      const containerRect = scrollRef.current.getBoundingClientRect();
      return rect.left >= containerRect.left;
    });

    if (firstVisible?.dataset?.date) {
      const date = new Date(firstVisible.dataset.date);
      const monthStr = date.toLocaleDateString("en-US", {
        month: "long",
        year: "numeric"
      });
      setVisibleMonth(monthStr);
    }
  };

  const generateTimeSlots = () => {
    const slots = [];
    const startHour = 8;
    const endHour = 17;
    for (let h = startHour; h <= endHour; h++) {
      for (let m = 0; m < 60; m += 15) {
        const t = new Date();
        t.setHours(h, m, 0, 0);
        slots.push(t);
      }
    }
    return slots;
  };

  const timeSlots = generateTimeSlots();

  const formatTime = (date) =>
    date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

  const handleContinue = () => {
    if (!selectedDate || !selectedTime) {
      alert("Please select both a date and time!");
      return;
    }
    const dateTime = new Date(
      selectedDate.getFullYear(),
      selectedDate.getMonth(),
      selectedDate.getDate(),
      selectedTime.getHours(),
      selectedTime.getMinutes()
    );
    const serviceIds = cart.map(s => ({ cleaningServiceId: s.id }));

    // ✅ Save state to sessionStorage
    sessionStorage.setItem("bookingData", JSON.stringify({
      cart,
      totalPrice,
      selectedDateTime: dateTime,
      serviceIds,
    }));

    navigate("/bookingvehicle", {
      state: {
        cart,
        totalPrice,
        selectedDateTime: dateTime,
        serviceIds,
      }
    });
  };

  return (
    <div className="booking-two-infinite-container">
      {/* ✅ Breadcrumb Navigation */}
      <div className="breadcrumb">
        <Link to="/" className="breadcrumb-link">Home</Link>
        <span className="dot">•</span>
        <Link
          to="/booking"
          className="breadcrumb-link"
          state={{ cart, totalPrice }}
        >
          Select a service
        </Link>
        <span className="dot">•</span>
        <strong>Select a date and time</strong>
      </div>

      <h2 className="booking-page-heading">Select Date & Time</h2>
      <div className="panel-container">
        <div className="left-panel">
          <div className="visible-month-header">{visibleMonth}</div>

          <div className="date-scroll-container" ref={scrollRef} onScroll={onScroll}>
            {days.map((day, idx) => {
              const isSelected =
                selectedDate && day.toDateString() === selectedDate.toDateString();
              return (
                <div
                  key={idx}
                  data-date={day.toISOString()}
                  className={`date-item ${isSelected ? "selected" : ""}`}
                  onClick={() => setSelectedDate(day)}
                >
                  <div className="day-short">
                    {day.toLocaleDateString("en-US", { weekday: "short" })}
                  </div>
                  <div className="date-num">
                    {day.getDate()}
                  </div>
                </div>
              );
            })}
          </div>

          {selectedDate && (
            <div className="time-selector-vertical">
              <label>Select a time:</label>
              <div className="time-list">
                {timeSlots.map((time, idx) => {
                  const isSelected =
                    selectedTime &&
                    time.getHours() === selectedTime.getHours() &&
                    time.getMinutes() === selectedTime.getMinutes();
                  return (
                    <div
                      key={idx}
                      className={`time-block ${isSelected ? "selected" : ""}`}
                      onClick={() => setSelectedTime(time)}
                    >
                      {formatTime(time)}
                    </div>
                  );
                })}
              </div>
            </div>
          )}
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
              {cart.map((s, i) => (
                <li key={i}>
                  {s.serviceName.replace(/_/g, ' ')} — R {s.priceOfService}
                </li>
              ))}
            </ul>

            <h4>Date & Time</h4>
            <p>
              {selectedDate && selectedTime
                ? `${selectedDate.toDateString()} at ${formatTime(selectedTime)}`
                : "Not selected"}
            </p>

            <div className="total-price">
              <strong>Total:</strong> R {totalPrice}
            </div>
          </div>

          <div className="right-panel-continue">
            <button
              className="continue-btn"
              onClick={handleContinue}
              disabled={!selectedDate || !selectedTime}
            >
              Continue
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingTwo;