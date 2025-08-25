import React from 'react';

function Payment() {
  return (
    <div>
      <h2>Payment Screen</h2>
      <form>
        <label>
          Amount:
          <input type="number" name="amount" />
        </label>
        <br />
        <label>
          Payment Method:
          <input type="text" name="method" />
        </label>
        <br />
        <button type="submit">Submit Payment</button>
      </form>
    </div>
  );
}

export default Payment;