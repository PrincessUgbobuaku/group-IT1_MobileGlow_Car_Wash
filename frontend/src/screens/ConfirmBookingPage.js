import './ConfirmBookingPage.css';

function ConfirmBookingPage() {
  const booking = {
    service: "Gel Polish Application on Natural Hands",
    professional: "Lynn",
    date: "Monday, 25 August",
    time: "09:00–10:00",
    price: 275,
    duration: "1 hr"
  };

  return (
    <div className="confirm-container">
      <div className="breadcrumb">
        Services &gt; Professional &gt; Time &gt; <strong>Confirm</strong>
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
            <h3>Discount code</h3>
            <div className="discount-row">
              <input type="text" placeholder="Enter discount code" />
              <button className="apply-btn">Apply</button>
            </div>
          </div>

          <div className="form-section">
            <h3>Booking notes</h3>
            <textarea
              placeholder="Include comments or requests about your booking"
            />
          </div>
        </div>

        {/* RIGHT PANEL */}
        <div className="confirm-summary">
          <h3>Blush and Buff</h3>
          <p className="location">Intercare Blaauwberg, Cnr Link Rd & Park Dr...</p>
          <div className="date-time">
            <p><strong>{booking.date}</strong></p>
            <p>{booking.time} ({booking.duration} duration)</p>
          </div>

          <hr />

          <p>{booking.service}</p>
          <p>{booking.duration} with {booking.professional}</p>

          <div className="price-row">
            <span>Subtotal</span>
            <span>R {booking.price}</span>
          </div>

          <hr />

          <div className="total">
            <div>
              <div>Total</div>
              <div className="pay-now">Pay now</div>
              <div className="pay-at-venue">Pay at venue</div>
            </div>
            <div className="total-price">R {booking.price}</div>
          </div>

          <button className="confirm-btn">Confirm</button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmBookingPage;