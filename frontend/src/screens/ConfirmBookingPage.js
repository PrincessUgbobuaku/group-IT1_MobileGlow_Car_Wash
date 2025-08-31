import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './ConfirmBookingPage.css';

function ConfirmBookingPage() {
  const location = useLocation();
  const navigate = useNavigate();

  // Destructure state passed via navigation
  const {
    cart = [],
    totalPrice = 0,
    selectedDateTime,
    selectedVehicle
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
    fetch('http://localhost:8080/mobileglow/wash-attendants/random')
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch wash attendant');
        return response.json();
      })
      .then(data => {
        setWashAttendant(data);
        setLoadingAttendant(false);
      })
      .catch(error => {
        setAttendantError(error.message);
        setLoadingAttendant(false);
      });
  }, []);

  useEffect(() => {
    fetch('http://localhost:8080/mobileglow/api/customers/51')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch customer');
        return res.json();
      })
      .then(data => {
        setCustomer(data);
      })
      .catch(error => {
        setCustomerError(error.message);
      });
  }, []);

  useEffect(() => {
    if (customer?.address?.addressID) {
      fetch(`http://localhost:8080/api/address/read/${customer.address.addressID}`)
        .then(res => {
          if (!res.ok) throw new Error('Failed to fetch address');
          return res.json();
        })
        .then(setAddress)
        .catch(error => {
          setAddressError(error.message);
        });
    }
  }, [customer]);

  // Function to save booking to backend
  const saveBooking = async () => {
    if (!washAttendant || !selectedVehicle || !selectedDateTime) {
      alert('Missing booking information');
      return;
    }

    try {
      const payload = {
        cleaningServices: cart.map(service => ({
          cleaningServiceID: service.id || service.cleaningServiceID
        })),
        vehicle: { vehicleID: selectedVehicle.vehicleID },
        washAttendant: { userId: washAttendant.userId || washAttendant.userID || washAttendant.id },
        bookingDateTime: selectedDateTime,
        tipAdd: false
      };

      console.log('Booking payload:', JSON.stringify(payload, null, 2));

      const res = await fetch('http://localhost:8080/mobileglow/api/bookings', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      console.log('Response status:', res.status);
      if (!res.ok) {
        const errorText = await res.text();
        console.error('Response error text:', errorText);
        throw new Error(errorText);
      }

      const result = await res.json();
      console.log('Booking saved:', result);

      setShowPopup(true);
    } catch (err) {
      console.error('Error in saveBooking:', err);
      alert('Failed to save booking: ' + err.message);
    }
  };

  return (
    <div className="confirm-container">
      <div className="breadcrumb">
        Services &gt; Professional &gt; Time &gt; Vehicle &gt; <strong>Confirm</strong>
      </div>

      <h1>Review and confirm</h1>

      <div className="confirm-content">
        {/* LEFT PANEL */}
        <div className="confirm-left">
          <div className="form-section">
            <h3>Payment method</h3>
            <button className="payment-button">
              <span role="img" aria-label="store">🏪</span> Pay at venue
            </button>
          </div>

          <div className="form-section">
            <h3>Booking notes</h3>
            <textarea placeholder="Include comments or requests about your booking" />
          </div>
        </div>

        {/* RIGHT PANEL */}
        <div className="confirm-summary">
          <h3>MobileGlow Car Wash</h3>
          <p className="location">Parklands, Cape Town</p>

          {/* Display selected datetime */}
          {selectedDateTime && (
            <>
              <p><strong>{new Date(selectedDateTime).toLocaleDateString()}</strong></p>
              <p>{new Date(selectedDateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</p>
            </>
          )}

          <hr />

          <div className="vehicle-info">
            <p><strong>Vehicle:</strong> {selectedVehicle ? `${selectedVehicle.carMake} ${selectedVehicle.carModel}` : "N/A"}</p>
          </div>

          {/* Wash Attendant */}
          <div className="attendant-info">
            <h4>Assigned Wash Attendant</h4>
            {loadingAttendant && <p>Loading wash attendant...</p>}
            {attendantError && <p className="error">Error: {attendantError}</p>}
            {washAttendant && !loadingAttendant && (
              <div className="attendant-details">
                <img
                  src="https://via.placeholder.com/80x80.png?text=Attendant"
                  alt="Wash Attendant"
                  className="attendant-avatar"
                />
                <p><strong>{washAttendant.userName} {washAttendant.userSurname}</strong></p>
              </div>
            )}
          </div>

          {/* Customer Address */}
          {customer && customer.address && (
            <div className="customer-info">
              <h4>Your Address</h4>
              <p>
                {customer.address.streetNumber} {customer.address.streetName}, {customer.address.city}, {customer.address.postalCode}
              </p>
              <button
                onClick={() => navigate('/edit-address')}
                className="edit-address-btn"
              >
                Edit
              </button>
            </div>
          )}
          {customerError && <p className="error">Error: {customerError}</p>}
          {addressError && <p className="error">Error: {addressError}</p>}

          {/* Services */}
          {cart.map(service => (
            <div key={service.id} className="service-info">
              <p>{service.serviceName} - R {service.priceOfService}</p>
            </div>
          ))}

          <div className="price-row">
            <span>Subtotal</span>
            <span>R {totalPrice}</span>
          </div>

          <hr />

          <div className="total">
            <div>
              <div>Total</div>
              <div className="pay-now">Pay now</div>
              <div className="pay-at-venue">Pay at venue</div>
            </div>
            <div className="total-price">R {totalPrice}</div>
          </div>

          {/* Update button to call saveBooking */}
          <button className="confirm-btn" onClick={saveBooking}>Confirm</button>
        </div>
      </div>

      {/* Popup */}
      {showPopup && washAttendant && (
        <div className="popup-overlay">
          <div className="popup">
            <span className="green-tick">✅</span>
            <h2>Booking Confirmed!</h2>
            <p>
              Wash Attendant <strong>{washAttendant.userName} {washAttendant.userSurname}</strong> will be helping you.
            </p>
            <button onClick={() => setShowPopup(false)}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default ConfirmBookingPage;