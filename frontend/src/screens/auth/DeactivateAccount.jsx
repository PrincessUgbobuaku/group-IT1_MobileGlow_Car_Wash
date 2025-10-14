import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./DeactivateAccount.css";

const DeactivateAccount = () => {
  const [showPopup, setShowPopup] = useState(false);
  const navigate = useNavigate();

  // Get user role from localStorage
  const userRole = localStorage.getItem('userRoleDescription');

  const handleDeleteClick = () => {
    setShowPopup(true);
  };

  const handleCancel = () => {
    setShowPopup(false);
  };

  const handleConfirm = async () => {
    setShowPopup(false);

    const userEmail = localStorage.getItem('userEmail');
    const userRole = localStorage.getItem('userRoleDescription');

    if (!userEmail || !userRole) {
      alert('User information not found. Please log in again.');
      navigate('/login');
      return;
    }

    let endpoint;
    if (userRole === 'CLIENT') {
      endpoint = 'http://localhost:8080/mobileglow/api/customers/update';
    } else if (userRole === 'EMPLOYEE') {
      // Need to determine specific employee type
      const employeeType = localStorage.getItem('employeeType'); // Assuming this is stored
      if (employeeType === 'Manager') {
        endpoint = 'http://localhost:8080/mobileglow/Manager/update';
      } else if (employeeType === 'Accountant') {
        endpoint = 'http://localhost:8080/mobileglow/Accountant/update';
      } else if (employeeType === 'WashAttendant') {
        endpoint = 'http://localhost:8080/mobileglow/wash-attendants/update';
      } else {
        alert('Invalid employee type.');
        return;
      }
    } else {
      alert('Invalid user role.');
      return;
    }

    try {
      const response = await fetch(endpoint, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          emailAddress: userEmail,
          isActive: false
        }),
      });

      if (response.ok) {
        alert('Your account has been deactivated.');
        // Clear localStorage and navigate to login
        localStorage.removeItem('userEmail');
        localStorage.removeItem('userRoleDescription');
        navigate('/login');
      } else {
        alert('Failed to deactivate account. Please try again.');
      }
    } catch (error) {
      console.error('Error deactivating account:', error);
      alert('Something went wrong!');
    }
  };

  const handleGoBack = () => {
    if (userRole === 'CLIENT') {
      navigate('/LandingCustomer');
    } else if (userRole === 'EMPLOYEE') {
      navigate('/LandingEmployee');
    } else {
      navigate('/'); // fallback to public landing
    }
  };

  return (
    <div className="deactivate-container">
      <div className="delete-card">
        <h2>Deactivate account</h2>
        <p className="delete-heading">
          Are you sure you want to deactivate your account?
        </p>
        <p className="delete-text">
          Once you deactivate your account, it cannot be undone. All your data will
          be permanently erased from this app including your profile
          information, preferences, saved content, and any activity history.
        </p>
        <p className="delete-text">
          We’re sad to see you go, but we understand that sometimes it’s
          necessary. Please take a moment to consider the consequences before
          proceeding.
        </p>

        <div className="button-group">
          <button className="delete-btn" onClick={handleDeleteClick}>
            Deactivate account
          </button>
          <button className="go-back-btn" onClick={handleGoBack}>Go back</button>
        </div>
      </div>

      {showPopup && (
        <div className="popup-overlay">
          <div className="popup-box">
            <h3>Are you sure you want to continue?</h3>
            <p>This action cannot be undone. Are you sure you want to continue?</p>
            <div className="popup-buttons">
              <button className="cancel-btn" onClick={handleCancel}>
                Cancel
              </button>
              <button className="confirm-btn" onClick={handleConfirm}>
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DeactivateAccount;
