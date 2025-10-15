import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import "./BookingVehicle.css";

const BookingVehicle = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // Only use state from location
  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime = null,
    serviceIds = [],
  } = location.state || {};

  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicleId, setSelectedVehicleId] = useState("");

  // const userId = 5; // Replace with dynamic value if needed
  const userId = localStorage.getItem("userId"); // Dynamic
  console.log("User ID from localStorage:", userId);

  const [hasConflict, setHasConflict] = useState(false);

  // useEffect(() => {
  //   fetch(`http://localhost:8080/mobileglow/api/vehicle/customer/${userId}`)
  //     .then((response) => response.json())
  //     .then((data) => {
  //       setVehicles(Array.isArray(data) ? data : []);
  //     })
  //     .catch((error) => console.error("Error fetching vehicles:", error));
  // }, [userId]);

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    console.log(
      "🚀 [BookingVehicle] Fetched userId from localStorage:",
      userId
    );
    console.log("🔑 [BookingVehicle] Fetched token from localStorage:", token);

    fetch(`http://localhost:8080/mobileglow/api/vehicle/customer/${userId}`)
      .then((response) => {
        console.log(
          "📡 [BookingVehicle] Fetch response status:",
          response.status
        );
        if (!response.ok) {
          throw new Error("❌ Failed to fetch vehicles from backend");
        }
        return response.json();
      })
      .then((data) => {
        console.log("✅ [BookingVehicle] Vehicles fetched from API:", data);
        setVehicles(Array.isArray(data) ? data : []);
      })
      .catch((error) => {
        console.error("🚨 [BookingVehicle] Error fetching vehicles:", error);
      });
  }, [userId]);

  const handleVehicleChange = async (event) => {
    const vehicleId = event.target.value;
    setSelectedVehicleId(vehicleId);
    setHasConflict(false); // Reset conflict

    if (!vehicleId || !selectedDateTime) return;

    try {
      // Fix ISO formatting issue
      const dateObj = new Date(selectedDateTime);
      const formattedDateTime = `${dateObj.getFullYear()}-${String(
        dateObj.getMonth() + 1
      ).padStart(2, "0")}-${String(dateObj.getDate()).padStart(
        2,
        "0"
      )}T${String(dateObj.getHours()).padStart(2, "0")}:${String(
        dateObj.getMinutes()
      ).padStart(2, "0")}:00`;

      const response = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/check-conflict?vehicleId=${vehicleId}&bookingDateTime=${encodeURIComponent(
          formattedDateTime
        )}`
      );

      if (!response.ok) {
        throw new Error("Failed to check booking conflict.");
      }

      const conflict = await response.json();
      setHasConflict(conflict);

      if (conflict) {
        alert("⚠️ This vehicle is already booked at the selected time.");
      }
    } catch (error) {
      console.error("Error checking conflict:", error);
      alert("An error occurred while checking vehicle booking availability.");
    }
  };

  const selectedVehicle = vehicles.find(
    (v) => String(v.vehicleID) === selectedVehicleId
  );

  const handleContinue = () => {
    if (!selectedVehicleId || !selectedVehicle) {
      alert("Please select a vehicle!");
      return;
    }

    navigate("/confirm", {
      state: {
        cart,
        totalPrice,
        selectedDateTime,
        selectedVehicle,
        serviceIds,
      },
    });
  };

  return (
    <div className="booking-vehicle-container app-content">
      <div className="breadcrumb">
        <Link to="/" className="breadcrumb-link">
          Home
        </Link>
        <span className="dot">•</span>

        <Link
          to="/booking"
          className="breadcrumb-link"
          state={{ cart, totalPrice }}
        >
          Select a service
        </Link>
        <span className="dot">•</span>

        <Link
          to="/bookingtwo"
          className="breadcrumb-link"
          state={{ cart, totalPrice, selectedDateTime, serviceIds }}
        >
          Select a date and time
        </Link>
        <span className="dot">•</span>

        <strong>Select vehicle</strong>
      </div>

      <h2 className="booking-page-heading">Select Vehicle</h2>

      <div className="left-right-panel-container">
        <div className="left-panel">
          <div className="vehicle-selection">
            <label>Select your vehicle:</label>
            <select onChange={handleVehicleChange} value={selectedVehicleId}>
              <option value="">Select Vehicle</option>
              {vehicles.length > 0 ? (
                vehicles.map((vehicle) => (
                  <option
                    key={vehicle.vehicleID}
                    value={String(vehicle.vehicleID)}
                  >
                    {vehicle.carMake} {vehicle.carModel}
                  </option>
                ))
              ) : (
                <option disabled>No vehicles available</option>
              )}
            </select>
            {/* <-- Add conflict message here */}
            {hasConflict && (
              <p style={{ color: "red", marginTop: "0.5rem" }}>
                ⚠️ This vehicle is already booked at the selected time. Please
                choose another.
              </p>
            )}
          </div>
        </div>

        <div className="right-panel">
          <div className="vehicle-business-info">
            <h3>MobileGlow Car Wash</h3>
            <p>4.9 ⭐ (32)</p>
            <p>Parklands, Cape Town</p>
          </div>

          <div className="summary-section">
            <h4>Selected Services</h4>
            <ul>
              {cart.map((item, index) => (
                <li key={index}>
                  {item.serviceName.replace(/_/g, " ")} - R{" "}
                  {item.priceOfService}
                </li>
              ))}
            </ul>

            <h4>Date & Time</h4>
            <p>
              {selectedDateTime
                ? new Date(selectedDateTime).toLocaleString()
                : "Not selected"}
            </p>

            <h4>Vehicle</h4>
            <p>
              {selectedVehicle
                ? `${selectedVehicle.carMake} ${selectedVehicle.carModel}`
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
              disabled={!selectedVehicleId || hasConflict}
            >
              Continue
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingVehicle;
