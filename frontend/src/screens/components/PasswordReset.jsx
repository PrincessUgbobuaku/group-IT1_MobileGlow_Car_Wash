import React, { useState } from "react";
import "./PasswordReset.css";

const PasswordReset = () => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [strength, setStrength] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const checkStrength = (value) => {
    if (value.length < 6) return "Very weak";
    if (value.match(/[A-Z]/) && value.match(/[0-9]/) && value.length >= 8)
      return "Strong";
    if (value.length >= 8) return "Medium";
    return "Weak";
  };

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
    setStrength(checkStrength(value));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (password !== confirm) {
      setError("Passwords do not match!");
      return;
    }

    const userEmail = localStorage.getItem('userEmail');
    if (!userEmail) {
      setError("User not logged in!");
      return;
    }

    try {
      const token = localStorage.getItem('authToken');
      const response = await fetch('http://localhost:8080/mobileglow/Login/change-password', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : '',
        },
        body: JSON.stringify({
          email: userEmail,
          currentPassword: currentPassword,
          newPassword: password,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        setSuccess("Password changed successfully!");
        setCurrentPassword("");
        setPassword("");
        setConfirm("");
        setStrength("");
        // Clear stored token and user data, then redirect to login
        localStorage.removeItem('authToken');
        localStorage.removeItem('userEmail');
        localStorage.removeItem('userRoleDescription');
        localStorage.removeItem('userId');
        setTimeout(() => {
          window.location.href = '/login'; // Redirect to login page
        }, 2000); // Delay to show success message
      } else {
        setError(data.message || "Failed to change password");
      }
    } catch (err) {
      setError("An error occurred. Please try again.");
    }
  };

  return (
    <div className="reset-container">
      <div className="reset-box">
        <h2>Change Password</h2>
        <p>Enter your current password and new password.</p>

        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Current Password</label>
            <input
              type="password"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              placeholder="Enter current password"
              required
            />
          </div>

          <div className="form-group">
            <label>New Password</label>
            <input
              type="password"
              value={password}
              onChange={handlePasswordChange}
              placeholder="Enter new password"
              required
            />
            <div className={`strength ${strength.toLowerCase()}`}>
              {strength && <span>{strength}</span>}
            </div>
          </div>

          <div className="form-group">
            <label>Confirm New Password</label>
            <input
              type="password"
              value={confirm}
              onChange={(e) => setConfirm(e.target.value)}
              placeholder="Confirm new password"
              required
            />
          </div>

          <button type="submit" className="btn">
            Change Password
          </button>
        </form>
      </div>
    </div>
  );
};

export default PasswordReset;
