import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./PaymentPage.css";
import NavbarEmployee from "../../../screens/components/NavbarEmployee";
import Footer from "../../../screens/components/Footer";

const PaymentPage = () => {
  const { bookingId } = useParams();
  const navigate = useNavigate();

  const [booking, setBooking] = useState(null);
  const [customer, setCustomer] = useState(null);
  const [cards, setCards] = useState([]);
  const [selectedCardId, setSelectedCardId] = useState(null);
  const [paymentAmount, setPaymentAmount] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("CASH");
  const [paymentStatus, setPaymentStatus] = useState("PAID");
  const [tipAdd, setTipAdd] = useState(false);
  const [showCardModal, setShowCardModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [cardDetails, setCardDetails] = useState({
    cardNumber: "",
    cardHolderName: "",
    cvv: "",
    expiryDate: "",
  });
  const [message, setMessage] = useState(""); // ✅ success popup

  // --- Fetch booking + linked customer ---
  useEffect(() => {
    const fetchBooking = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/mobileglow/api/bookings/${bookingId}`
        );
        if (!res.ok) throw new Error("Failed to fetch booking");
        const data = await res.json();
        setBooking(data);
        console.log("🧾 [DEBUG] Booking fetched:", data);

        if (data.vehicle?.vehicleID) {
          const vehRes = await fetch(
            `http://localhost:8080/mobileglow/api/vehicle/${data.vehicle.vehicleID}`
          );
          if (vehRes.ok) {
            const vehicleData = await vehRes.json();
            console.log("🚗 [DEBUG] Vehicle full data:", vehicleData);
            if (vehicleData.customer) {
              setCustomer(vehicleData.customer);
              fetchCardsForCustomer(vehicleData.customer.userId);
            }
          }
        }

        setTipAdd(Boolean(data.tipAdd));
      } catch (err) {
        console.error("❌ Error fetching booking:", err);
      }
    };

    const fetchCardsForCustomer = async (custId) => {
      if (!custId) return;
      try {
        const res = await fetch(
          `http://localhost:8080/mobileglow/api/cards/customer/${custId}`
        );
        if (!res.ok) return;
        const data = await res.json();
        console.log("💳 [DEBUG] fetched cards:", data);
        setCards(Array.isArray(data) ? data : []);
        if (data.length > 0)
          setSelectedCardId(data[0].cardId || data[0].cardID);
      } catch (err) {
        console.error("❌ Error fetching cards:", err);
      }
    };

    fetchBooking();
  }, [bookingId]);

  // --- Calculate amount with tip ---
  useEffect(() => {
    if (!booking) return;
    const baseCost = Number(booking.bookingCost || 0);
    const amountWithTip = tipAdd ? baseCost * 1.1 : baseCost;
    setPaymentAmount(amountWithTip.toFixed(2));
  }, [tipAdd, booking]);

  // --- Process payment ---
  const processPayment = async (card = null) => {
    try {
      setLoading(true);
      const payload = {
        paymentAmount: Number(paymentAmount),
        paymentMethod,
        paymentStatus,
        booking: { bookingId: parseInt(bookingId, 10) },
        ...(card ? { card } : {}),
      };

      console.log("📤 [DEBUG] Sending payment payload:", payload);

      const res = await fetch("http://localhost:8080/mobileglow/api/payments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const text = await res.text();
      if (!res.ok) throw new Error(text);

      setMessage("✅ Payment successful!");
      setTimeout(() => navigate("/manage-bookings"), 1200);
    } catch (err) {
      console.error("❌ [ERROR] processPayment:", err);
      alert(`Payment failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirmPayment = async () => {
    if (paymentMethod === "CARD") {
      if (selectedCardId) {
        const cardObj = cards.find(
          (c) => String(c.cardId) === String(selectedCardId)
        );
        if (cardObj) return processPayment(cardObj);
      }
      setShowCardModal(true);
      return;
    }
    await processPayment();
  };

  // --- Save card only ---
  const handleSaveCard = async () => {
    const customerId = customer?.userId;
    if (!customerId) {
      alert("Missing customer info.");
      return;
    }

    const cardPayload = {
      ...cardDetails,
      customer: { userId: customerId },
    };

    try {
      setLoading(true);
      const res = await fetch("http://localhost:8080/mobileglow/api/cards", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cardPayload),
      });

      const text = await res.text();
      if (!res.ok) throw new Error(text);
      const savedCard = JSON.parse(text);

      setCards((prev) => [savedCard, ...prev]);
      setSelectedCardId(savedCard.cardId);
      setShowCardModal(false);
      setCardDetails({ cardNumber: "", cardHolderName: "", cvv: "", expiryDate: "" });
      setMessage("✅ Card saved successfully!");
      setTimeout(() => setMessage(""), 2000);
    } catch (err) {
      alert(`Creating card failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const formatCardLabel = (c) => {
    const num = c.cardNumber;
    const last4 = num ? num.slice(-4) : "----";
    return `${c.cardHolderName} •••• ${last4} (Exp ${c.expiryDate})`;
  };

  if (!booking) return <div className="loading-text">Loading booking info...</div>;

  return (
    <div>
      <NavbarEmployee />
      <div className="payment-page">
        <h2>Payment for Booking</h2>

        <div className="form-group">
          <label>Vehicle:</label>
          <p>{booking.vehicle?.carMake} {booking.vehicle?.carModel}</p>
        </div>

        <div className="form-group">
          <label>Total Cost:</label>
          <p>R {booking.bookingCost}</p>
        </div>

        <div className="form-group">
          <label>Tip (10%)?</label>
          <input type="checkbox" checked={tipAdd} onChange={(e) => setTipAdd(e.target.checked)} />
        </div>

        <div className="form-group">
          <label>Payment Method:</label>
          <select value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
            <option value="CASH">CASH</option>
            <option value="CARD">CARD</option>
          </select>
        </div>

        {paymentMethod === "CARD" && customer && (
          <div className="form-group">
            <label>Saved Cards for {customer.userName}:</label>

            {cards.length > 0 ? (
              <>
                {cards.map((c) => (
                  <div key={c.cardId}>
                    <input
                      type="radio"
                      id={`card-${c.cardId}`}
                      name="savedCard"
                      checked={String(selectedCardId) === String(c.cardId)}
                      onChange={() => setSelectedCardId(c.cardId)}
                    />
                    <label htmlFor={`card-${c.cardId}`}>{formatCardLabel(c)}</label>
                  </div>
                ))}
                <button
                  style={{ marginTop: "0.5rem" }}
                  onClick={() => setShowCardModal(true)}
                >
                  Add another card
                </button>
              </>
            ) : (
              <div>
                <p>No saved cards found for this customer.</p>
                <button onClick={() => setShowCardModal(true)}>Add card</button>
              </div>
            )}
          </div>
        )}

        <div className="form-group">
          <label>Amount:</label>
          <input type="number" value={paymentAmount} readOnly />
        </div>

        <button
          onClick={handleConfirmPayment}
          className="submit-payment"
          disabled={loading}
        >
          {loading ? "Processing..." : "Confirm Payment"}
        </button>
      </div>

      {showCardModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Add Card Details</h3>
            <input
              type="text"
              placeholder="Card Number"
              value={cardDetails.cardNumber}
              onChange={(e) =>
                setCardDetails({ ...cardDetails, cardNumber: e.target.value })
              }
            />
            <input
              type="text"
              placeholder="Card Holder Name"
              value={cardDetails.cardHolderName}
              onChange={(e) =>
                setCardDetails({ ...cardDetails, cardHolderName: e.target.value })
              }
            />
            <input
              type="text"
              placeholder="CVV"
              maxLength="4"
              value={cardDetails.cvv}
              onChange={(e) =>
                setCardDetails({ ...cardDetails, cvv: e.target.value })
              }
            />
            <input
              type="month"
              placeholder="Expiry Date"
              value={cardDetails.expiryDate}
              onChange={(e) =>
                setCardDetails({ ...cardDetails, expiryDate: e.target.value })
              }
            />
            <div className="modal-buttons">
              <button onClick={handleSaveCard} className="confirm-btn" disabled={loading}>
                {loading ? "Saving..." : "Save card details"}
              </button>
              <button onClick={() => setShowCardModal(false)} className="cancel-btn">
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      {message && <div className="toast-message">{message}</div>}

      <Footer />
    </div>
  );
};

export default PaymentPage;