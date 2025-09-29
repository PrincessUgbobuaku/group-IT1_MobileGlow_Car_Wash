import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./PaymentPage.css";

const PaymentPage = () => {
  const { bookingId } = useParams();
  const navigate = useNavigate();

  const [booking, setBooking] = useState(null);
  const [paymentAmount, setPaymentAmount] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("DEBIT");
  const [paymentStatus, setPaymentStatus] = useState("PAID");
  const [tipAdd, setTipAdd] = useState(false);

  // Fetch booking info initially
  useEffect(() => {
    const fetchBooking = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/mobileglow/api/bookings/${bookingId}`
        );
        const data = await res.json();
        setBooking(data);
        setTipAdd(data.tipAdd);
      } catch (err) {
        console.error("Error fetching booking:", err);
      }
    };

    fetchBooking();
  }, [bookingId]);

  // Recalculate payment amount whenever booking or tipAdd changes
  useEffect(() => {
    if (!booking) return;
    const baseCost = Number(booking.bookingCost);
    const amountWithTip = tipAdd ? baseCost * 1.1 : baseCost;
    setPaymentAmount(amountWithTip.toFixed(2));
  }, [tipAdd, booking]);

  const handlePayment = async () => {
    try {
      // 1. Update booking tipAdd + status
      const updatedBooking = {
        ...booking,
        tipAdd,
        status: paymentStatus, // include status update
      };

      const bookingUpdateRes = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/${bookingId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(updatedBooking),
        }
      );

      if (!bookingUpdateRes.ok) {
        const errorText = await bookingUpdateRes.text();
        throw new Error(
          `Booking update failed: ${bookingUpdateRes.status} - ${errorText}`
        );
      }

      // 2. Create new payment
      const paymentRes = await fetch(
        `http://localhost:8080/mobileglow/api/payments`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            paymentAmount: Number(paymentAmount),
            paymentMethod,
            paymentStatus,
            booking: { bookingId: parseInt(bookingId) },
          }),
        }
      );

      if (!paymentRes.ok) {
        const errorText = await paymentRes.text();
        throw new Error(`Payment failed: ${paymentRes.status} - ${errorText}`);
      }

      alert("Payment successful!");
      navigate("/manage-bookings");
    } catch (err) {
      console.error("Payment failed:", err.message);
      alert(`Payment failed: ${err.message}`);
    }
  };

  if (!booking) return <div>Loading booking info...</div>;

  return (
    <div className="payment-page">
      <h2>Payment for Booking #{bookingId}</h2>

      <div className="form-group">
        <label>Vehicle:</label>
        <p>
          {booking.vehicle?.carMake} {booking.vehicle?.carModel}
        </p>
      </div>

      <div className="form-group">
        <label>Total Cost:</label>
        <p>{booking.bookingCost}</p>
      </div>

      <div className="form-group">
        <label>Tip (10%)?</label>
        <input
          type="checkbox"
          checked={tipAdd}
          onChange={(e) => setTipAdd(e.target.checked)}
        />
      </div>

      <div className="form-group">
        <label>Payment Method:</label>
        <select
          value={paymentMethod}
          onChange={(e) => setPaymentMethod(e.target.value)}
        >
          <option value="DEBIT">DEBIT</option>
          <option value="CREDIT">CREDIT</option>
          <option value="CASH">CASH</option>
        </select>
      </div>

      <div className="form-group">
        <label>Payment Status:</label>
        <select
          value={paymentStatus}
          onChange={(e) => setPaymentStatus(e.target.value)}
        >
          <option value="PAID">PAID</option>
          <option value="PENDING">PENDING</option>
        </select>
      </div>

      <div className="form-group">
        <label>Amount:</label>
        <input type="number" value={paymentAmount} readOnly />
      </div>

      <button onClick={handlePayment} className="submit-payment">
        Confirm Payment
      </button>
    </div>
  );
};

export default PaymentPage;
