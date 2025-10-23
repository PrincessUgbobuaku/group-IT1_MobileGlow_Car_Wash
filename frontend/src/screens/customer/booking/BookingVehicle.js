import React, { useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import NavbarCustomer from "../../components/NavbarCustomer";
import Footer from "../../components/Footer";
import "./BookingVehicle.css";

const BookingVehicle = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime = null,
    serviceIds = [],
  } = location.state || {};

  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicleId, setSelectedVehicleId] = useState("");
  const [hasConflict, setHasConflict] = useState(false);

  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  // ✅ Fetch vehicles for this logged-in customer
  useEffect(() => {
    if (!userId) return;

    fetch(`http://localhost:8080/mobileglow/api/vehicle/customer/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to fetch vehicles");
        return response.json();
      })
      .then((data) => setVehicles(Array.isArray(data) ? data : []))
      .catch((error) => console.error("Error fetching vehicles:", error));
  }, [userId, token]);

  // ✅ Check for booking conflict
  const handleVehicleChange = async (event) => {
    const vehicleId = event.target.value;
    setSelectedVehicleId(vehicleId);
    setHasConflict(false);

    if (!vehicleId || !selectedDateTime) return;

    try {
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
        )}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) throw new Error("Failed to check booking conflict");
      const conflict = await response.json();
      setHasConflict(conflict);

      if (conflict) {
        alert("⚠️ This vehicle is already booked at the selected time.");
      }
    } catch (error) {
      console.error("Error checking conflict:", error);
    }
  };

  const selectedVehicle = vehicles.find(
    (v) => String(v.vehicleID) === selectedVehicleId
  );

  // ✅ Continue to Confirm page
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

  // ✅ Add Vehicle Button — Keeps current state
  const handleAddVehicle = () => {
    navigate("/vehicles", {
      state: { cart, totalPrice, selectedDateTime, serviceIds },
    });
  };

  return (
    <>
      <NavbarCustomer />

      <div className="booking-vehicle-page">
        <div className="booking-vehicle-container">
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
            {/* LEFT SIDE: Vehicle Selection */}
            <div className="left-panel">
              <div className="vehicle-selection">
                <label>Select your vehicle:</label>
                <select
                  onChange={handleVehicleChange}
                  value={selectedVehicleId}
                >
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

                <button
                  className="add-vehicle-btn"
                  onClick={() =>
                    navigate("/vehicles", {
                      state: { cart, totalPrice, selectedDateTime, serviceIds },
                    })
                  }
                >
                  ➕ Add Vehicle
                </button>

                {hasConflict && (
                  <p className="conflict-message">
                    ⚠️ This vehicle is already booked at the selected time.
                  </p>
                )}
              </div>
            </div>

            {/* RIGHT SIDE: Summary */}
            <div className="right-panel">
              <div className="summary-date-wrapper">
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

                <div className="date-section">
                  <h4>Date & Time</h4>
                  <p>
                    {selectedDateTime
                      ? new Date(selectedDateTime).toLocaleString()
                      : "Not selected"}
                  </p>
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
      </div>

      <Footer />
    </>
  );
};

export default BookingVehicle;
