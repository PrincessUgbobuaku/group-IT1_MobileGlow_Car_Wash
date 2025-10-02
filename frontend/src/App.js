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
import PaymentPage from "./screens/employee/booking/PaymentPage";
import LandingPublic  from "./screens/LandingPublic";
import Navbar from "./screens/components/Navbar";
import Footer from "./screens/components/Footer";
import SignUp from "./screens/auth/SignUp";
import RoleSelection from "./screens/auth/RoleSelection";
import Login from "./screens/auth/Login";
import AddressDetails from "./screens/auth/AddressDetails";
import LandingCustomer from "./screens/customer/LandingCustomer";
import NavbarCustomer from "./screens/components/NavbarCustomer";
import NavbarEmployee from "./screens/components/NavbarEmployee";
import LandingEmployee from "./screens/employee/LandingEmployee";


function App()  {
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
          <Route path="/landingpublic" element={<LandingPublic />} />
          <Route path="/login" element={<Login />} />
          <Route path="/Navbar" element={<Navbar />} />
          <Route path="/Footer" element={<Footer />} />
          <Route path="/SignUp" element={<SignUp />} />
          <Route path="/RoleSelection" element={<RoleSelection />} />
          <Route path="/AddressDetails" element={<AddressDetails />} />
          <Route path="/LandingCustomer" element={<LandingCustomer />} />
          <Route path="/NavbarCustomer" element={<NavbarCustomer />} />
          <Route path="/NavbarEmployee" element={<NavbarEmployee />} />
          <Route path="/LandingEmployee" element={<LandingEmployee />} />

      </Routes>
    </Router>
  );
}

export default App;