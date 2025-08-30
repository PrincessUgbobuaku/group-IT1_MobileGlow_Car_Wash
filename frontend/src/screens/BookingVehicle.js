import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import './BookingVehicle.css';

const BookingVehicle = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { cart, totalPrice, selectedDate, selectedTime } = location.state || {}; // Get cart and total price from previous page
  const [vehicles, setVehicles] = useState([]); // Initialize as empty array
  const [selectedVehicle, setSelectedVehicle] = useState(""); // Store selected vehicle ID (ensure it's an empty string)

  const userId = 51;  // Example: Get this dynamically based on logged-in user

  useEffect(() => {
    // Fetch vehicles for the logged-in user
    fetch(`http://localhost:8080/mobileglow/api/vehicle?customerId=${userId}`)  // Adjust endpoint
      .then((response) => response.json())
      .then((data) => {
        console.log("Fetched vehicles:", data);  // Debugging log to check the data
        if (Array.isArray(data) && data.length > 0) {
          setVehicles(data);  // Store vehicles if they are returned correctly
        } else {
          setVehicles([]);  // Ensure an empty array if no vehicles exist
        }
      })
      .catch((error) => {
        console.error('Error fetching vehicles:', error);
      });
  }, [userId]);

  const handleVehicleChange = (event) => {
    setSelectedVehicle(event.target.value); // Update selected vehicle when the user selects one
    console.log("Selected Vehicle:", event.target.value); // Debugging log to check the selected vehicle ID
  };

  const handleContinue = () => {
    if (!selectedVehicle) {
      alert("Please select a vehicle!");
    } else {
      // Pass the selected vehicle to the confirm page along with other details
      navigate("/confirm", {
        state: {
          cart,
          totalPrice,
          selectedDate,
          selectedTime,
          selectedVehicle,  // Add the selected vehicle to the state
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
                  key={vehicle.vehicleid}
                  value={vehicle.vehicleid}>
                  {vehicle.car_make} {vehicle.car_model} ({vehicle.car_colour}) - {vehicle.car_plate_number}
                </option>
              ))
            ) : (
              <option disabled>No vehicles available</option> // Display when no vehicles are available
            )}
          </select>
        </div>

        <button className="continue-btn" onClick={handleContinue} disabled={!selectedVehicle}>
          Continue
        </button>
      </div>

      <div className="right-panel">
        <div className="business-info">
          <h3>Blush and Buff</h3>
          <p>4.9 ⭐ (32)</p>
          <p>Parklands, Cape Town</p>
        </div>
      </div>
    </div>
  );
};

export default BookingVehicle;