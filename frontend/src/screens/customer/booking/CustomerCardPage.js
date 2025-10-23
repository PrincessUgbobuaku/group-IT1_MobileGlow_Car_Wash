import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import NavbarCustomer from "../../../screens/components/NavbarCustomer";
import Footer from "../../components/Footer";
import "./CustomerCard.css";

const CustomerCardsPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // 🧾 Recover booking data (from confirm page or session)
  const storedState = sessionStorage.getItem("confirmBookingState");
  const bookingState =
    location.state && Object.keys(location.state).length > 0
      ? location.state
      : storedState
      ? JSON.parse(storedState)
      : null;

  // Debugging info
  console.log("📦 Booking State:", bookingState);

  // 💳 Card data
  const [cards, setCards] = useState([]);
  const [cardDetails, setCardDetails] = useState({
    cardNumber: "",
    cardHolderName: "",
    cvv: "",
    expiryDate: "",
  });
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);

  const token = localStorage.getItem("authToken");
  const userId = localStorage.getItem("userId");

  // ✅ Fetch user's saved cards
  const fetchCards = async () => {
    if (!userId) return;

    try {
      setLoading(true);
      const res = await fetch(
        `http://localhost:8080/mobileglow/api/cards/customer/${userId}`,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (res.ok) {
        const data = await res.json();
        setCards(Array.isArray(data) ? data : []);
      } else {
        console.error("❌ Failed to fetch cards:", res.status);
      }
    } catch (error) {
      console.error("🚨 Error fetching cards:", error);
    } finally {
      setLoading(false);
    }
  };

  // Fetch cards on mount
  useEffect(() => {
    fetchCards();
  }, []);

  // ✅ Save new card
  const saveCard = async () => {
    if (!userId) {
      alert("User not found. Please log in again.");
      return;
    }

    const payload = {
      ...cardDetails,
      customer: { userId },
    };

    try {
      setLoading(true);
      const res = await fetch("http://localhost:8080/mobileglow/api/cards", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (res.ok) {
        alert("💳 Card saved successfully!");
        setShowModal(false);
        setCardDetails({
          cardNumber: "",
          cardHolderName: "",
          cvv: "",
          expiryDate: "",
        });
        fetchCards();
      } else {
        alert("❌ Failed to save card. Please try again.");
      }
    } catch (error) {
      console.error("🚨 Error saving card:", error);
      alert("An error occurred while saving the card.");
    } finally {
      setLoading(false);
    }
  };

  // ✅ Back to Confirm Page (with restored booking details)
  const handleBackToConfirm = () => {
    if (bookingState) {
      console.log("🔁 Returning to Confirm with booking:", bookingState);
      navigate("/confirm", { state: bookingState });
    } else {
      console.warn("⚠️ No booking state found — redirecting to booking start");
      navigate("/booking");
    }
  };

  return (
    <>
      <NavbarCustomer />
      <div className="customer-cards-page">
        <h2>My Saved Cards</h2>

        <button onClick={() => setShowModal(true)}>Add New Card</button>

        {loading && <p>Loading cards...</p>}

        <ul>
          {cards.length > 0 ? (
            cards.map((card) => (
              <li key={card.cardId}>
                {card.cardHolderName} •••• {card.cardNumber.slice(-4)} (Exp{" "}
                {card.expiryDate})
              </li>
            ))
          ) : (
            <p>No saved cards found.</p>
          )}
        </ul>

        {/* 🔙 Back to Confirm Button */}
        <div style={{ marginTop: "20px" }}>
          <button className="btn back-btn" onClick={handleBackToConfirm}>
            ⬅️ Back to Confirm Page
          </button>
        </div>

        {/* ➕ Add Card Modal */}
        {showModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <h3>Add New Card</h3>

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
                  setCardDetails({
                    ...cardDetails,
                    cardHolderName: e.target.value,
                  })
                }
              />

              <input
                type="text"
                placeholder="CVV"
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
                <button onClick={saveCard} disabled={loading}>
                  {loading ? "Saving..." : "Save Card"}
                </button>
                <button onClick={() => setShowModal(false)}>Cancel</button>
              </div>
            </div>
          </div>
        )}
      </div>
      <Footer />
    </>
  );
};

export default CustomerCardsPage;