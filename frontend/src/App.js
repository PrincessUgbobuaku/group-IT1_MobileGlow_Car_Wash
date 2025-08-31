import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Booking from "./screens/Booking";
import BookingVehicle from "./screens/BookingVehicle"; // Ensure this is imported correctly
import BookingTwo from "./screens/BookingTwo"; // Ensure this is imported correctly
import ConfirmBookingPage from "./screens/ConfirmBookingPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/booking" />} />
        <Route path="/booking" element={<Booking />} />
        <Route path="/bookingtwo" element={<BookingTwo />} />
        <Route path="/bookingvehicle" element={<BookingVehicle />} /> {/* Vehicle selection */}
        <Route path="/confirm" element={<ConfirmBookingPage />} />
      </Routes>
    </Router>
  );
}

export default App;