import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./ConfirmBookingPage.css";

function ConfirmBookingPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime,
    selectedVehicle,
    serviceIds,
  } = location.state || {};

  const [washAttendant, setWashAttendant] = useState(null);
  const [loadingAttendant, setLoadingAttendant] = useState(true);
  const [attendantError, setAttendantError] = useState(null);

  const [customer, setCustomer] = useState(null);
  const [customerError, setCustomerError] = useState(null);

  const [address, setAddress] = useState(null);
  const [addressError, setAddressError] = useState(null);

  const [showPopup, setShowPopup] = useState(false);

  useEffect(() => {
    fetch("http://localhost:8080/mobileglow/wash-attendants/random")
      .then((response) => {
        if (!response.ok) throw new Error("Failed to fetch wash attendant");
        return response.json();
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

  useEffect(() => {
    fetch("http://localhost:8080/mobileglow/api/customers/read/5")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch customer");
        return res.json();
      })
      .then((data) => setCustomer(data))
      .catch((error) => setCustomerError(error.message));
  }, []);

  useEffect(() => {
    if (customer?.address?.addressID) {
      fetch(
        `http://localhost:8080/api/address/read/${customer.address.addressID}`
      )
        .then((res) => {
          if (!res.ok) throw new Error("Failed to fetch address");
          return res.json();
        })
        .then(setAddress)
        .catch((error) => setAddressError(error.message));
    }
  }, [customer]);

  const saveBooking = async () => {
    if (!washAttendant || !selectedVehicle || !selectedDateTime) {
      alert("Missing booking information");
      return;
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
      bookingDateTime: selectedDateTime, // send local datetime string as-is
      tipAdd: false,
    };

    try {
      const res = await fetch("http://localhost:8080/mobileglow/api/bookings", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText);
      }

      const result = await res.json();
      console.log("Booking saved:", result);

      // ✅ Clean up sessionStorage
      sessionStorage.removeItem("bookingData");

      // ✅ Show confirmation popup
      setShowPopup(true);
    } catch (err) {
      alert("Failed to save booking: " + err.message);
    }
  };

  return (
    <div className="confirm-container">
      <div className="breadcrumb">
        <a href="/" className="breadcrumb-link">
          Home
        </a>
        <span className="dot">•</span>
        <a href="/booking" className="breadcrumb-link">
          Select a service
        </a>
        <span className="dot">•</span>
        <a
          href="/bookingtwo"
          className="breadcrumb-link"
          onClick={(e) => {
            e.preventDefault();
            navigate("/bookingtwo", {
              state: {
                cart,
                totalPrice,
                serviceIds,
              },
            });
          }}
        >
          Select a date and time
        </a>
        <span className="dot">•</span>
        <a
          href="/bookingvehicle"
          className="breadcrumb-link"
          onClick={(e) => {
            e.preventDefault();
            navigate("/bookingvehicle", {
              state: {
                cart,
                totalPrice,
                selectedDateTime,
                serviceIds,
              },
            });
          }}
        >
          Select vehicle
        </a>
        <span className="dot">•</span>
        <strong>Review & Confirm</strong>
      </div>
      <h1>Review and confirm</h1>

      <div className="confirm-content">
        {/* LEFT PANEL */}
        <div className="confirm-left">
          <div className="form-section">
            <h3>Payment method</h3>
            <button className="payment-button">
              <span role="img" aria-label="store">
                🏪
              </span>{" "}
              Pay at venue
            </button>
          </div>

          <div className="confirmation-extra-info">
            <div className="cancellation-policy">
              <h3>Cancellation Policy</h3>
              <p>
                Please cancel within <strong>24 hours</strong> of your
                appointment.
              </p>
            </div>
            <div className="important-info">
              <p>24-hour cancellation policy.</p>
              <p>
                You may be held accountable for expenses incurred for changes
                less than 24 hours prior to your booking time.
              </p>
              <p>
                This is a Therapeutic service and no lewd behavior will be
                tolerated.
              </p>
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
                <strong>
                  {new Date(selectedDateTime).toLocaleDateString()}
                </strong>
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

          {/* Vehicle Info */}
          <div className="vehicle-info">
            <p>
              <strong>Vehicle:</strong>{" "}
              {selectedVehicle
                ? `${selectedVehicle.carMake} ${selectedVehicle.carModel}`
                : "N/A"}
            </p>
          </div>

          {/* Wash Attendant Info */}
          {washAttendant && !loadingAttendant && (
            <div className="attendant-info">
              <p>
                <strong>Wash Attendant:</strong> {washAttendant.userName}{" "}
                {washAttendant.userSurname}
              </p>
            </div>
          )}

          {/* Services List */}
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

          {customerError && <p className="error">Error: {customerError}</p>}
          {addressError && <p className="error">Error: {addressError}</p>}

          <div className="price-row">
            <span>Subtotal</span>
            <span>R {totalPrice}</span>
          </div>

          <hr />

          <div className="total">
            <div>
              <div>Total</div>
              <div className="pay-at-venue">Pay at venue</div>
            </div>
            <div className="total-price">R {totalPrice}</div>
          </div>

          <button className="confirm-btn" onClick={saveBooking}>
            Confirm
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
