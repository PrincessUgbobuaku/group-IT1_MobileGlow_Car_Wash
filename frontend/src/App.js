import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Booking from "./screens/Booking";
import BookingVehicle from "./screens/BookingVehicle"; // Ensure this is imported correctly
import BookingTwo from "./screens/BookingTwo"; // Ensure this is imported correctly
import ConfirmBookingPage from "./screens/ConfirmBookingPage";
import ProfileManagement from "./screens/ProfileManagement";
import VehiclePage from "./screens/VehiclePage";
import CleaningServicePage from "./screens/CleaningServicePage";
import CleaningServiceManagement from "./screens/CleaningServiceManagement";
import ProfilePage from "./screens/ProfilePage";

function App() {
  return (
    <Router>
      <Routes>

          <Route path="/vehicles" element={<VehiclePage />} />
          <Route path="/cleaning-services" element={<CleaningServicePage />} />
          <Route path="/cleaning-services/management" element={<CleaningServiceManagement />} />
          <Route path="/profiles" element={<ProfilePage />} />
          <Route path="/profile-management" element={<ProfileManagement />} />
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