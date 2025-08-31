import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import './BookingVehicle.css';

const BookingVehicle = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { cart, totalPrice, selectedDate, selectedTime } = location.state || {};
  const [vehicles, setVehicles] = useState([]);
  const [selectedVehicle, setSelectedVehicle] = useState("");

  const userId = 51; // Example: get this dynamically for production

  useEffect(() => {
    fetch(`http://localhost:8080/mobileglow/api/vehicle/customer/${userId}`)
      .then((response) => response.json())
      .then((data) => {
        console.log("Fetched vehicles:", data);
        console.log("First vehicle object:", data[0]);
        setVehicles(Array.isArray(data) ? data : []);
      })
      .catch((error) => {
        console.error('Error fetching vehicles:', error);
      });
  }, [userId]);

  const handleVehicleChange = (event) => {
    setSelectedVehicle(event.target.value);
    console.log("Selected Vehicle:", event.target.value);
  };

  const handleContinue = () => {
    if (!selectedVehicle) {
      alert("Please select a vehicle!");
    } else {
      navigate("/confirm", {
        state: {
          cart,
          totalPrice,
          selectedDate,
          selectedTime,
          selectedVehicle,
        }
      });
    }
  };

  return (
    <div className="booking-vehicle-container">
      <div className="left-panel">
        <button className="back-button">←</button>
        <h2 className="booking-page-heading">Select Vehicle for Cleaning</h2>

        <div className="vehicle-selection">
          <label>Select your vehicle:</label>
          <select onChange={handleVehicleChange} value={selectedVehicle}>
            <option value="">Select Vehicle</option>
            {vehicles.length > 0 ? (
              vehicles.map(vehicle => (
                <option
                  key={vehicle.vehicleID}
                  value={vehicle.vehicleid}
                >
                  {vehicle.carMake} {vehicle.carModel}
                </option>
              ))
            ) : (
              <option disabled>No vehicles available</option>
            )}
          </select>
        </div>

        <button
          className="continue-btn"
          onClick={handleContinue}
          disabled={!selectedVehicle}
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
      </div>

      {/*
      // DEBUG VIEW – Uncomment if needed for troubleshooting
      <div className="vehicle-debug">
        <h4>Debug: Vehicles List</h4>
        <ul>
          {vehicles.map(vehicle => (
            <li key={vehicle.vehicleID}>
              ID: {vehicle.vehicleID}, Model: {vehicle.carModel}, Make: {vehicle.carMake}, Plate: {vehicle.carPlateNumber}, Colour: {vehicle.carColour}
            </li>
          ))}
        </ul>
      </div>
      */}
    </div>
  );
};

export default BookingVehicle;