import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import emailjs from "emailjs-com";
import "./ConfirmBookingPage.css";

function ConfirmBookingPage() {
  const location = useLocation();
  const navigate = useNavigate();

  // ✅ Booking data (passed from previous pages)
  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime,
    selectedVehicle,
    serviceIds,
    selectedService = null,
    otherDetails = "",
  } = location.state || {};

  // 🧾 Booking state
  const [washAttendant, setWashAttendant] = useState(null);
  const [loadingAttendant, setLoadingAttendant] = useState(true);
  const [attendantError, setAttendantError] = useState(null);

  // 👤 Customer info
  const [customer, setCustomer] = useState(null);
  const [customerError, setCustomerError] = useState(null);
  const [address, setAddress] = useState(null);
  const [addressError, setAddressError] = useState(null);

  // 💳 Card info
  const [cards, setCards] = useState([]);
  const [selectedCardId, setSelectedCardId] = useState("");
  const [cardError, setCardError] = useState(null);

  // 📧 Email
  const [customerEmail, setCustomerEmail] = useState("");

  // 💰 Payment
  const [paymentOption, setPaymentOption] = useState("PREPAID");

  // ✅ Popup
  const [showPopup, setShowPopup] = useState(false);

  // 🧍 Fetch wash attendant
  useEffect(() => {
    fetch("http://localhost:8080/mobileglow/wash-attendants/random")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch wash attendant");
        return res.json();
      })
      .then((data) => {
        setWashAttendant(data);
        setLoadingAttendant(false);
      })
      .catch((error) => {
        setAttendantError(error.message);
        setLoadingAttendant(false);
      });
  }, []);

  // 👤 Fetch customer
  useEffect(() => {
    const userId = localStorage.getItem("userId") || "5";
    fetch(`http://localhost:8080/mobileglow/api/customers/read/${userId}`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch customer");
        return res.json();
      })
      .then((data) => setCustomer(data))
      .catch((error) => setCustomerError(error.message));
  }, []);

  // 🏠 Fetch address
  useEffect(() => {
    if (customer?.address?.addressID) {
      fetch(
        `http://localhost:8080/mobileglow/api/address/read/${customer.address.addressID}`
      )
        .then((res) => {
          if (!res.ok) throw new Error("Failed to fetch address");
          return res.json();
        })
        .then(setAddress)
        .catch((error) => setAddressError(error.message));
    }
  }, [customer]);

  // 💳 Fetch cards
  useEffect(() => {
    const userId = localStorage.getItem("userId") || "5";
    fetch(`http://localhost:8080/mobileglow/api/cards/customer/${userId}`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch cards");
        return res.json();
      })
      .then((data) => setCards(Array.isArray(data) ? data : []))
      .catch((err) => setCardError(err.message));
  }, []);

  // 📧 Fetch email
  useEffect(() => {
    const userId = localStorage.getItem("userId") || "5";
    fetch(`http://localhost:8080/mobileglow/Login/byUser/${userId}`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch login email");
        return res.json();
      })
      .then((data) => {
        if (data?.emailAddress) setCustomerEmail(data.emailAddress);
      })
      .catch((err) => console.error("❌ Login email fetch error:", err));
  }, []);

  // 📨 EmailJS setup
  const EMAILJS_CONFIG = {
    SERVICE_ID: "service_w4p7dmi",
    TEMPLATE_ID: "template_j6sgwxj",
    PUBLIC_KEY: "skFnK8AxbQ2kQ_6CI",
  };

  const sendBookingConfirmation = (bookingData) => {
    if (!customerEmail) return;

    const emailParams = {
      to_name: `${customer?.userName || ""} ${
        customer?.userSurname || ""
      }`.trim(),
      email: customerEmail,
      booking_date: new Date(selectedDateTime).toLocaleDateString(),
      booking_time: new Date(selectedDateTime).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      }),
      vehicle: `${selectedVehicle?.carMake || ""} ${
        selectedVehicle?.carModel || ""
      }`.trim(),
      total_price: `R ${totalPrice || "0.00"}`,
      attendant_name: washAttendant
        ? `${washAttendant.userName} ${washAttendant.userSurname}`
        : "Assigned at service time",
      services: cart.map((s) => s.serviceName).join(", "),
    };

    emailjs
      .send(
        EMAILJS_CONFIG.SERVICE_ID,
        EMAILJS_CONFIG.TEMPLATE_ID,
        emailParams,
        EMAILJS_CONFIG.PUBLIC_KEY
      )
      .then(() => console.log("✅ Booking confirmation email sent"))
      .catch((err) => console.error("❌ Email failed:", err));
  };

  // 💾 Save booking
  const saveBooking = async () => {
    if (!washAttendant || !selectedVehicle || !selectedDateTime) {
      alert("Missing booking information");
      return;
    }

    if (paymentOption === "PREPAID") {
      if (cards.length === 0) {
        alert("You must add a payment card before confirming your booking.");
        return;
      }
      if (!selectedCardId) {
        alert("Please select a card before confirming your booking.");
        return;
      }
    }

    const cleaningServicesPayload =
      serviceIds && serviceIds.length > 0
        ? serviceIds
        : cart.map((service) => ({ cleaningServiceId: service.id }));

    if (typeof selectedDateTime !== "string") {
      alert("Invalid booking date/time format.");
      return;
    }

    const payload = {
      cleaningServices: cleaningServicesPayload,
      vehicle: { vehicleID: selectedVehicle.vehicleID },
      washAttendant: {
        userId:
          washAttendant.userId || washAttendant.userID || washAttendant.id,
      },
      bookingDateTime: selectedDateTime,
      card: paymentOption === "PREPAID" ? { cardId: selectedCardId } : null,
      tipAdd: false,
      paymentOption,
      paymentStatus: paymentOption === "PREPAID" ? "PAID" : "PENDING",
    };

    try {
      const res = await fetch("http://localhost:8080/mobileglow/api/bookings", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error(await res.text());
      const result = await res.json();
      console.log("✅ Booking saved:", result);

      if (paymentOption === "PREPAID") {
        try {
          const paymentPayload = {
            booking: { bookingId: result.bookingId },
            paymentAmount: totalPrice,
            paymentMethod: "CARD",
            paymentStatus: "PAID",
            card: { cardId: selectedCardId },
          };

          const payRes = await fetch(
            "http://localhost:8080/mobileglow/api/payments",
            {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(paymentPayload),
            }
          );

          if (!payRes.ok) throw new Error(await payRes.text());
          console.log("💰 Payment successfully recorded in database");
        } catch (payErr) {
          console.error("❌ Failed to record payment:", payErr);
        }
      }

      setShowPopup(true);
      sendBookingConfirmation(result);
    } catch (err) {
      alert("Failed to save booking: " + err.message);
    }
  };

  return (
    <div className="confirm-container app-content">
      <div className="breadcrumb">
        <a href="/" className="breadcrumb-link">Home</a>
        <span className="dot">•</span>
        <a href="/booking" className="breadcrumb-link">Select a service</a>
        <span className="dot">•</span>
        <strong>Review & Confirm</strong>
      </div>

      <h1>Review and Confirm</h1>

      <div className="confirm-content">
        {/* LEFT PANEL */}
        <div className="confirm-left">
          <div className="confirm-form-section">
            <h3>Payment Method</h3>
            <div className="payment-option">
              <label>
                <input
                  type="radio"
                  name="paymentOption"
                  value="PREPAID"
                  checked={paymentOption === "PREPAID"}
                  onChange={() => setPaymentOption("PREPAID")}
                />
                Pay Now (Secure Online Payment)
              </label>
              <label>
                <input
                  type="radio"
                  name="paymentOption"
                  value="ON_SITE"
                  checked={paymentOption === "ON_SITE"}
                  onChange={() => setPaymentOption("ON_SITE")}
                />
                Pay at Premises
              </label>
            </div>

            {/* 💳 Card Section */}
            {paymentOption === "PREPAID" && (
              <>
                {cardError ? (
                  <p className="error-text">Error loading card info</p>
                ) : cards.length > 0 ? (
                  <div className="payment-section">
                    <label>Select a saved card:</label>
                    <select
                      value={selectedCardId}
                      onChange={(e) => setSelectedCardId(e.target.value)}
                      className="card-select"
                    >
                      <option value="">-- Select Card --</option>
                      {cards.map((card) => (
                        <option key={card.cardId} value={card.cardId}>
                          {card.cardHolderName} •••• {card.cardNumber.slice(-4)} (Exp {card.expiryDate})
                        </option>
                      ))}
                    </select>
                    <p style={{ marginTop: "8px" }}>
                      <button
                        className="add-card-link"
                        onClick={() =>
                          navigate("/my-cards", {
                            state: {
                              cart,
                              totalPrice,
                              selectedDateTime,
                              selectedVehicle,
                              serviceIds,
                              selectedService,
                              otherDetails,
                            },
                          })
                        }
                      >
                        ➕ Manage Payment Methods
                      </button>
                    </p>
                  </div>
                ) : (
                  <div className="add-card-section">
                    <p>No saved cards found.</p>
                    <button
                      onClick={() =>
                        navigate("/my-cards", {
                          state: {
                            cart,
                            totalPrice,
                            selectedDateTime,
                            selectedVehicle,
                            serviceIds,
                            selectedService,
                            otherDetails,
                          },
                        })
                      }
                    >
                      Manage Payment Methods
                    </button>
                  </div>
                )}
              </>
            )}
          </div>

          <div className="confirmation-extra-info">
            <div className="cancellation-policy">
              <h3>Cancellation Policy</h3>
              <p>Please cancel within <strong>24 hours</strong> of your appointment.</p>
            </div>
            <div className="important-info">
              <p>24-hour cancellation policy.</p>
              <p>
                You may be held accountable for expenses incurred for changes
                less than 24 hours prior to your booking time.
              </p>
              <p>This is a therapeutic service — no lewd behavior tolerated.</p>
            </div>
          </div>
        </div>

        {/* RIGHT PANEL */}
        <div className="confirm-summary">
          <h3>MobileGlow Car Wash</h3>
          <p className="location">Parklands, Cape Town</p>

          {selectedDateTime && (
            <>
              <p>
                <strong>{new Date(selectedDateTime).toLocaleDateString()}</strong>
              </p>
              <p>
                {new Date(selectedDateTime).toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </p>
            </>
          )}

          <hr />

          <div className="vehicle-info">
            <p>
              <strong>Vehicle:</strong>{" "}
              {selectedVehicle
                ? `${selectedVehicle.carMake} ${selectedVehicle.carModel}`
                : "N/A"}
            </p>
          </div>

          {washAttendant && !loadingAttendant && (
            <div className="attendant-info">
              <p>
                <strong>Wash Attendant:</strong> {washAttendant.userName}{" "}
                {washAttendant.userSurname}
              </p>
            </div>
          )}

          <div className="services-list">
            <h4>Services</h4>
            {cart.map((service) => (
              <div key={service.id} className="service-info">
                <p>
                  {service.serviceName} - R {service.priceOfService}
                </p>
              </div>
            ))}
          </div>

          <div className="price-row">
            <span>Subtotal</span>
            <span>R {totalPrice}</span>
          </div>

          <hr />

          <div className="total">
            <div>
              <div>Total</div>
              <div className="pay-at-venue">
                {paymentOption === "PREPAID"
                  ? "Pay with Card"
                  : "Pay at Premises"}
              </div>
            </div>
            <div className="total-price">R {totalPrice}</div>
          </div>

          <button
            className={`confirm-btn ${showPopup ? "confirm-btn-booked" : ""}`}
            onClick={saveBooking}
            disabled={showPopup}
          >
            {showPopup ? "Booking Made!" : "Confirm Booking"}
          </button>
        </div>
      </div>

      {showPopup && washAttendant && (
        <div className="popup-overlay">
          <div className="popup">
            <span className="green-tick">✅</span>
            <h2>Booking Confirmed!</h2>
            <p>
              Wash Attendant{" "}
              <strong>
                {washAttendant.userName} {washAttendant.userSurname}
              </strong>{" "}
              will be helping you.
            </p>
            <button onClick={() => setShowPopup(false)}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default ConfirmBookingPage;