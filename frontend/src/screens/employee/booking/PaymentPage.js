import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./PaymentPage.css";
import NavbarEmployee from "../../../screens/components/NavbarEmployee"; // Adjust path if needed
import Footer from "../../../screens/components/Footer"; // Adjust path if needed

const PaymentPage = () => {
  const { bookingId } = useParams();
  const navigate = useNavigate();

  const [booking, setBooking] = useState(null);
  const [paymentAmount, setPaymentAmount] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("CASH"); // Default to CASH
  const [paymentStatus, setPaymentStatus] = useState("PAID");
  const [tipAdd, setTipAdd] = useState(false);

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

  useEffect(() => {
    if (!booking) return;
    const baseCost = Number(booking.bookingCost);
    const amountWithTip = tipAdd ? baseCost * 1.1 : baseCost;
    setPaymentAmount(amountWithTip.toFixed(2));
  }, [tipAdd, booking]);

  const handlePayment = async () => {
    try {
      const payload = {
        paymentAmount: Number(paymentAmount),
        paymentMethod, // CASH or CARD from select
        paymentStatus,
        booking: { bookingId: parseInt(bookingId, 10) },
      };

      console.log("🔎 Final payment method before submit:", paymentMethod);
      console.log("❓ Sending payment payload:", payload);

      const bookingUpdateRes = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/${bookingId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            ...booking,
            tipAdd,
            status: paymentStatus,
          }),
        }
      );

      console.log(
        "Booking update response:",
        bookingUpdateRes.status,
        await bookingUpdateRes.text()
      );

      if (!bookingUpdateRes.ok) {
        throw new Error("Booking update failed");
      }

      const paymentRes = await fetch(
        `http://localhost:8080/mobileglow/api/payments`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        }
      );

      console.log("Payment response status:", paymentRes.status);
      const paymentText = await paymentRes.text();
      console.log("Payment response body/text:", paymentText);

      if (!paymentRes.ok) {
        throw new Error(
          `Payment failed: ${paymentRes.status} - ${paymentText}`
        );
      }

      alert("Payment successful!");
      navigate("/manage-bookings");
    } catch (err) {
      console.error("Payment failed error:", err.message);
      alert(`Payment failed: ${err.message}`);
    }
  };

  if (!booking) return <div>Loading booking info...</div>;

  return (
    <div>
      <NavbarEmployee />

      <div className="payment-page">
        <h2>Payment for Booking</h2>

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
            <option value="CASH">CASH</option>
            <option value="CARD">CARD</option>
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
      <Footer />
    </div>
  );
};

export default PaymentPage;
