import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Payment from "./screens/Payment";
import Booking from "./screens/Booking"; // ⬅️ Import the Booking component
import BookingTwo from "./screens/BookingTwo"; // ⬅️ Import the Booking component
import ConfirmBookingPage from "./screens/ConfirmBookingPage"; // ⬅️ Import the Booking component



function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/payment" />} />
        <Route path="/payment" element={<Payment />} />
        <Route path="/booking" element={<Booking />} /> {/* ⬅️ Add this line */}
        <Route path="/bookingtwo" element={<BookingTwo />} /> {/* ⬅️ Add this line */}
        <Route path="/confirm" element={<ConfirmBookingPage />} /> {/* ⬅️ Add this line */}


      </Routes>
    </Router>
  );
}

export default App;
