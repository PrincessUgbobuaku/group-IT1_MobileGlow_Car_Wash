import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Booking from './screens/customer/booking/Booking';
import BookingVehicle from "./screens/customer/booking/BookingVehicle";
import BookingTwo from "./screens/customer/booking/BookingTwo";
import ConfirmBookingPage from "./screens/customer/booking/ConfirmBookingPage";
import ProfileManagement from "./screens/ProfileManagement";
import VehiclePage from "./screens/customer/management/VehiclePage";
import CleaningServiceManagement from "./screens/employee/management/CleaningServiceManagement";
import ProfilePage from "./screens/ProfilePage";
import ManageBookings from "./screens/employee/booking//ManageBookings";
import PaymentPage from "./screens/employee/booking/PaymentPage"
function App() {
  return (
    <Router>
      <Routes>

          <Route path="/vehicles" element={<VehiclePage />} />
          <Route path="/cleaning-services/management" element={<CleaningServiceManagement />} />
          <Route path="/profiles" element={<ProfilePage />} />
          <Route path="/profile-management" element={<ProfileManagement />} />
          <Route path="/" element={<Navigate to="/booking" />} />
          <Route path="/booking" element={<Booking />} />
          <Route path="/bookingtwo" element={<BookingTwo />} />
          <Route path="/bookingvehicle" element={<BookingVehicle />} /> {/* Vehicle selection */}
          <Route path="/confirm" element={<ConfirmBookingPage />} />
          <Route path="/manage-bookings" element={<ManageBookings />} />
          <Route path="/payment/:bookingId" element={<PaymentPage />} />

      </Routes>
    </Router>
  );
}

export default App;