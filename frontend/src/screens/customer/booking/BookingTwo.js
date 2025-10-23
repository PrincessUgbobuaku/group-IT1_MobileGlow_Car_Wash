import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate, Link } from "react-router-dom";
import NavbarCustomer from "../../components/NavbarCustomer";
import Footer from "../../components/Footer";
import "./BookingTwo.css";

const BookingTwo = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const savedBookingData =
    JSON.parse(sessionStorage.getItem("bookingData")) || {};
  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime = null,
    serviceIds = [],
  } = location.state || savedBookingData;

  const initialDateTime = selectedDateTime ? new Date(selectedDateTime) : null;

  const [selectedDate, setSelectedDate] = useState(
    initialDateTime
      ? new Date(
          initialDateTime.getFullYear(),
          initialDateTime.getMonth(),
          initialDateTime.getDate()
        )
      : null
  );

  const [selectedTime, setSelectedTime] = useState(
    initialDateTime
      ? new Date(
          1970,
          0,
          1,
          initialDateTime.getHours(),
          initialDateTime.getMinutes(),
          initialDateTime.getSeconds()
        )
      : null
  );

  const [unavailableTimes, setUnavailableTimes] = useState([]);
  const [visibleMonth, setVisibleMonth] = useState("");
  const scrollRef = useRef(null);
  const INITIAL_BUFFER = 30;
  const [days, setDays] = useState([]);
  const latestDateRef = useRef(null);

  // Load initial days
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

    setVisibleMonth(
      today.toLocaleDateString("en-US", { month: "long", year: "numeric" })
    );

    setTimeout(() => {
      if (scrollRef.current) {
        const child = scrollRef.current.children[0];
        if (child) {
          child.scrollIntoView({ behavior: "auto", inline: "start" });
        }
      }
    }, 50);
  }, []);

  // Fetch unavailable times
  useEffect(() => {
    if (!selectedDate) {
      setUnavailableTimes([]);
      return;
    }

    const fetchUnavailableTimes = async () => {
      const formattedDate = selectedDate.toLocaleDateString("en-CA");
      try {
        const response = await fetch(
          `http://localhost:8080/mobileglow/api/bookings/unavailable-timeslots?date=${formattedDate}`
        );
        if (!response.ok) throw new Error("Failed to fetch unavailable slots");

        const data = await response.json();
        const times = data.map((timeStr) => {
          const [hour, minute] = timeStr.split(":").map(Number);
          const dateObj = new Date(selectedDate);
          dateObj.setHours(hour, minute, 0, 0);
          return dateObj;
        });
        setUnavailableTimes(times);
      } catch (error) {
        console.error("Fetch error:", error);
        setUnavailableTimes([]);
      }
    };

    fetchUnavailableTimes();
  }, [selectedDate]);

  // Infinite scroll days
  const extendDays = () => {
    setDays((prevDays) => {
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
    const containerRect = scrollRef.current.getBoundingClientRect();
    const firstVisible = children.find((child) => {
      const rect = child.getBoundingClientRect();
      return rect.left >= containerRect.left;
    });

    if (firstVisible?.dataset?.date) {
      const date = new Date(firstVisible.dataset.date);
      const monthStr = date.toLocaleDateString("en-US", {
        month: "long",
        year: "numeric",
      });
      setVisibleMonth(monthStr);
    }
  };

  const generateTimeSlots = () => {
    const slots = [];
    if (!selectedDate) return slots;
    for (let h = 8; h <= 17; h++) {
      for (let m = 0; m < 60; m += 15) {
        const t = new Date(selectedDate);
        t.setHours(h, m, 0, 0);
        slots.push(t);
      }
    }
    return slots;
  };

  const timeSlots = generateTimeSlots();

  const formatTime = (date) =>
    date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });

  const handleContinue = () => {
    if (!selectedDate || !selectedTime) {
      alert("Please select both a date and time!");
      return;
    }

    const year = selectedDate.getFullYear();
    const month = String(selectedDate.getMonth() + 1).padStart(2, "0");
    const day = String(selectedDate.getDate()).padStart(2, "0");
    const hours = String(selectedTime.getHours()).padStart(2, "0");
    const minutes = String(selectedTime.getMinutes()).padStart(2, "0");

    const localDateTimeStr = `${year}-${month}-${day}T${hours}:${minutes}:00`;

    const serviceIds = cart.map((s) => ({ cleaningServiceId: s.id }));

    sessionStorage.setItem(
      "bookingData",
      JSON.stringify({
        cart,
        totalPrice,
        selectedDateTime: localDateTimeStr,
        serviceIds,
      })
    );

    navigate("/bookingvehicle", {
      state: {
        cart,
        totalPrice,
        selectedDateTime: localDateTimeStr,
        serviceIds,
      },
    });
  };

  return (
    <>
      {/* Full-width sticky header */}
      <NavbarCustomer />

      <div className="booking-two-infinite-container app-content">
        <div className="breadcrumb">
          <Link to="/" className="breadcrumb-link">
            Home
          </Link>
          <span className="booking-breadcrumb-dot">•</span>
          <Link
            to="/booking"
            className="breadcrumb-link"
            state={{ cart, totalPrice }}
          >
            Select a service
          </Link>
          <span className="booking-breadcrumb-dot">•</span>
          <strong>Select a date and time</strong>
        </div>

        <h2 className="booking-page-heading">Select Date & Time</h2>

        <div className="panel-container">
          {/* Left Panel */}
          <div className="left-panel">
            <div className="visible-month-header">{visibleMonth}</div>

            <div
              className="date-scroll-container"
              ref={scrollRef}
              onScroll={onScroll}
            >
              {days.map((day, idx) => {
                const isSelected =
                  selectedDate &&
                  day.toDateString() === selectedDate.toDateString();
                return (
                  <div
                    key={idx}
                    data-date={day.toISOString()}
                    className={`date-item ${isSelected ? "selected" : ""}`}
                    onClick={() => {
                      setSelectedDate(day);
                      setSelectedTime(null);
                    }}
                  >
                    <div className="day-short">
                      {day.toLocaleDateString("en-US", { weekday: "short" })}
                    </div>
                    <div className="date-num">{day.getDate()}</div>
                  </div>
                );
              })}
            </div>

            <div className="time-selector-vertical">
              <label>Select a time:</label>
              {!selectedDate ? (
                <div className="no-date-selected">
                  <p>Please select a date to view available times.</p>
                </div>
              ) : (
                <div className="time-list">
                  {timeSlots.length > 0 ? (
                    timeSlots.map((time, idx) => {
                      const isSelected =
                        selectedTime &&
                        time.getHours() === selectedTime.getHours() &&
                        time.getMinutes() === selectedTime.getMinutes();
                      const isUnavailable = unavailableTimes.some(
                        (unavailable) => unavailable.getTime() === time.getTime()
                      );

                      return (
                        <div
                          key={idx}
                          className={`time-block ${
                            isSelected ? "selected" : ""
                          } ${isUnavailable ? "disabled" : ""}`}
                          onClick={() => {
                            if (!isUnavailable) setSelectedTime(time);
                          }}
                        >
                          {formatTime(time)}
                          {isUnavailable && (
                            <span className="fully-booked-label">
                              Fully booked
                            </span>
                          )}
                        </div>
                      );
                    })
                  ) : (
                    <p>No available times for this date.</p>
                  )}
                </div>
              )}
            </div>
          </div>

          {/* Right Panel */}
          <div className="right-panel">
            <div className="date-time-business-info">
              <h3>MobileGlow Car Wash</h3>
              <p>4.9 ⭐ (32)</p>
              <p>Parklands, Cape Town</p>
            </div>

            <div className="summary-section">
              <h4>Selected Services</h4>
              <ul>
                {cart.map((s, i) => (
                  <li key={i}>
                    {s.serviceName.replace(/_/g, " ")} — R {s.priceOfService}
                  </li>
                ))}
              </ul>

              <h4>Date & Time</h4>
              <p>
                {selectedDate && selectedTime
                  ? `${selectedDate.toDateString()} at ${formatTime(
                      selectedTime
                    )}`
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

      {/* Full-width footer */}
      <Footer />
    </>
  );
};

export default BookingTwo;