import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import './BookingVehicle.css';

const BookingVehicle = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { cart, totalPrice, selectedDateTime, serviceIds } = location.state || {};
  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicleId, setSelectedVehicleId] = useState("");

  const userId = 5; // example

  useEffect(() => {
    fetch(`http://localhost:8080/mobileglow/api/vehicle/customer/${userId}`)
      .then((response) => response.json())
      .then((data) => {
        setVehicles(Array.isArray(data) ? data : []);
      })
      .catch((error) => console.error('Error fetching vehicles:', error));
  }, [userId]);

  const handleVehicleChange = (event) => {
    setSelectedVehicleId(event.target.value);
  };

  const handleContinue = () => {
    if (!selectedVehicleId) {
      alert("Please select a vehicle!");
      return;
    }

    const selectedVehicle = vehicles.find(v => String(v.vehicleID) === selectedVehicleId);

    if (!selectedVehicle) {
      alert("Selected vehicle not found!");
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

  const selectedVehicle = vehicles.find(v => String(v.vehicleID) === selectedVehicleId);

  return (
    <div className="booking-vehicle-container">
    <h2 className="booking-page-heading">Select Vehicle</h2>
    <div className="left-right-panel-container">


      <div className="left-panel">
        <div className="vehicle-selection">
          <label>Select your vehicle:</label>
          <select onChange={handleVehicleChange} value={selectedVehicleId}>
            <option value="">Select Vehicle</option>
            {vehicles.length > 0 ? (
              vehicles.map(vehicle => (
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
        </div>
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
            {cart.map((item, index) => (
              <li key={index}>
                {item.serviceName.replace(/_/g, ' ')} - R {item.priceOfService}
              </li>
            ))}
          </ul>

          <h4>Date & Time</h4>
          <p>{selectedDateTime ? new Date(selectedDateTime).toLocaleString() : "Not selected"}</p>

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
            disabled={!selectedVehicleId}
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