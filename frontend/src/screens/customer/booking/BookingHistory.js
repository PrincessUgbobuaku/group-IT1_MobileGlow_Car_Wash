import React, { useEffect, useState } from "react";
import NavbarCustomer from "../../components/NavbarCustomer";
import Footer from "../../components/Footer";
import "./BookingHistory.css";
import { BsThreeDotsVertical } from "react-icons/bs";

const BookingHistory = () => {
  const [bookings, setBookings] = useState([]);
  const [paymentStatusMap, setPaymentStatusMap] = useState({});
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [loading, setLoading] = useState(true);

  // 👤 Get logged-in customer ID (from JWT or localStorage)
  const userId = localStorage.getItem("userId");

  const fetchBookings = async () => {
    try {
      setLoading(true);
      const res = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/customer/${userId}`
      );
      if (!res.ok) throw new Error("Failed to fetch bookings");

      const data = await res.json();
      setBookings(data);

      // Get payment status for each booking
      const statusMap = {};
      for (const booking of data) {
        const res2 = await fetch(
          `http://localhost:8080/mobileglow/api/bookings/${booking.bookingId}/payment-status`
        );
        const isPaid = await res2.json();
        statusMap[booking.bookingId] = isPaid;
      }
      setPaymentStatusMap(statusMap);
    } catch (err) {
      console.error("❌ Failed to load bookings:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (userId) fetchBookings();
  }, [userId]);

  // Split bookings into upcoming and past
  const now = new Date();
  const upcomingBookings = bookings.filter(
    (b) => new Date(b.bookingDateTime) >= now
  );
  const pastBookings = bookings.filter(
    (b) => new Date(b.bookingDateTime) < now
  );

  const cancelBooking = async (bookingId) => {
    if (!window.confirm("Cancel this booking?")) return;
    try {
      const res = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/${bookingId}/cancel`,
        { method: "PUT", headers: { "Content-Type": "application/json" } }
      );
      if (!res.ok) throw new Error("Failed to cancel booking");
      alert("Booking cancelled successfully!");
      fetchBookings();
    } catch (err) {
      console.error(err);
      alert("Error cancelling booking.");
    }
  };

  if (loading)
    return (
      <div className="customer-bookings-container">
        <NavbarCustomer />
        <main className="cb-content">
          <h2>Loading your bookings...</h2>
        </main>
        <Footer />
      </div>
    );

  return (
    <div className="customer-bookings-container">
      <NavbarCustomer />
      <main className="cb-content app-content">
        <h1>My Bookings</h1>
        <p>View and manage your previous and upcoming car wash bookings.</p>

        {/* UPCOMING BOOKINGS */}
        <section className="cb-section">
          <h2>Upcoming Bookings</h2>
          {upcomingBookings.length === 0 ? (
            <p>No upcoming bookings found.</p>
          ) : (
            <div className="em-table">
              <div className="em-table-header">
                <div>ID</div>
                <div>Date</div>
                <div>Time</div>
                <div>Vehicle</div>
                <div>Attendant</div>
                <div>Status</div>
                <div>Actions</div>
              </div>
              {upcomingBookings.map((booking) => {

              console.log(
                  "Booking:",
                  booking.bookingId,
                  "Cancelled:",
                  booking.cancelled
                ); // 👈 Add this line here

                const dateObj = new Date(booking.bookingDateTime);
                const date = dateObj.toLocaleDateString();
                const time = dateObj.toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                });
                const isPaid = paymentStatusMap[booking.bookingId];
                return (
                  <div
className={`em-table-row ${booking.cancelled ? "cancelled-row" : ""}`}
                    key={booking.bookingId}
                  >
                    <div>{booking.bookingId}</div>
                    <div>{date}</div>
                    <div>{time}</div>
                    <div>
                      {booking.vehicle?.carMake} {booking.vehicle?.carModel}
                    </div>
                    <div>
                      {booking.washAttendant
                        ? `${booking.washAttendant.userName} ${booking.washAttendant.userSurname}`
                        : "Not Assigned"}
                    </div>
                    <div>
                      <span className={`status-tag ${isPaid ? "paid" : "not-paid"}`}>
                        {isPaid ? "PAID" : "NOT PAID"}
                      </span>
                    </div>
                    <div>
                      <div className="dropdown-wrapper">
                        {/* Disable actions if cancelled */}
                        {booking.cancelled === 1 ? (
                          <span style={{ color: "#888" }}>Cancelled</span>
                        ) : (
                          <>
                            <button
                              className="dropdown-toggle"
                              onClick={() =>
                                setActiveDropdown(
                                  activeDropdown === booking.bookingId
                                    ? null
                                    : booking.bookingId
                                )
                              }
                            >
                              <BsThreeDotsVertical size={18} />
                            </button>
                            {activeDropdown === booking.bookingId && (
                              <div className="dropdown-menu">
                                {!isPaid && (
                                  <button
                                    onClick={() =>
                                      (window.location.href = `/payment/${booking.bookingId}`)
                                    }
                                  >
                                    Pay
                                  </button>
                                )}
                                <button onClick={() => cancelBooking(booking.bookingId)}>
                                  Cancel
                                </button>
                              </div>
                            )}
                          </>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </section>

        {/* PAST BOOKINGS */}
        <section className="cb-section">
          <h2>Past Bookings</h2>
          {pastBookings.length === 0 ? (
            <p>No past bookings yet.</p>
          ) : (
            <div className="em-table">
              <div className="em-table-header">
                <div>ID</div>
                <div>Date</div>
                <div>Time</div>
                <div>Vehicle</div>
                <div>Attendant</div>
                <div>Status</div>
              </div>
              {pastBookings.map((booking) => {
                const dateObj = new Date(booking.bookingDateTime);
                const date = dateObj.toLocaleDateString();
                const time = dateObj.toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                });
                const isPaid = paymentStatusMap[booking.bookingId];
                return (
                  <div className="em-table-row" key={booking.bookingId}>
                    <div>{booking.bookingId}</div>
                    <div>{date}</div>
                    <div>{time}</div>
                    <div>
                      {booking.vehicle?.carMake} {booking.vehicle?.carModel}
                    </div>
                    <div>
                      {booking.washAttendant
                        ? `${booking.washAttendant.userName} ${booking.washAttendant.userSurname}`
                        : "Not Assigned"}
                    </div>
                    <div>
                      <span
                        className={`status-tag ${isPaid ? "paid" : "not-paid"}`}
                      >
                        {isPaid ? "PAID" : "NOT PAID"}
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </section>
      </main>
      <Footer />
    </div>
  );
};

export default BookingHistory;