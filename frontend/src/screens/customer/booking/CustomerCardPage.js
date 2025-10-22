import React, { useEffect, useState } from "react";
import "./CustomerCard.css";
import NavbarCustomer from "../../../screens/components/NavbarCustomer";
import Footer from "../../components/Footer";

const CustomerCardsPage = () => {
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
    if (!userId) {
      console.error("❌ No userId found in localStorage");
      return;
    }

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

  useEffect(() => {
    fetchCards();
  }, []);

  return (
    <>
      {/* ✅ Navbar stays on top */}
      <NavbarCustomer />

      <div className="customer-cards-page">
        <h2>My Saved Cards</h2>

        <button onClick={() => setShowModal(true)}>Add New Card</button>

        {loading && <p>Loading...</p>}

        <ul>
          {cards.length > 0 ? (
            cards.map((c) => (
              <li key={c.cardId}>
                {c.cardHolderName} •••• {c.cardNumber.slice(-4)} (Exp{" "}
                {c.expiryDate})
              </li>
            ))
          ) : (
            <p>No saved cards yet.</p>
          )}
        </ul>

        {showModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <h3>Add Card</h3>

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
                placeholder="Expiry"
                value={cardDetails.expiryDate}
                onChange={(e) =>
                  setCardDetails({ ...cardDetails, expiryDate: e.target.value })
                }
              />

              <button onClick={saveCard} disabled={loading}>
                {loading ? "Saving..." : "Save Card"}
              </button>

              <button onClick={() => setShowModal(false)}>Cancel</button>
            </div>
          </div>
        )}
      </div>
      <Footer />
    </>
  );
};

export default CustomerCardsPage;