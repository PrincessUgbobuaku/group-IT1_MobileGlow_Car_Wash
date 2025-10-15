import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css"; // external CSS file

const Login = () => {
  const navigate = useNavigate();
  const [login, setLogin] = useState({ email: "", password: "" });
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalType, setModalType] = useState("success"); // 'success' or 'error'

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLogin({ ...login, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/mobileglow/Login/authenticate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          emailAddress: login.email,
          password: login.password,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        setModalMessage(data.message);
        setModalType("success");
        setShowModal(true);
        localStorage.setItem("userEmail", login.email);
        localStorage.setItem("userRoleDescription", data.role_description);
        localStorage.setItem("authToken", data.token);
        localStorage.setItem("userId", data.user_id || "1");

        // Navigate after a short delay to show the modal
        setTimeout(() => {
          setShowModal(false);
          if (data.role_description === "EMPLOYEE") {
            navigate("/LandingEmployee");
          } else {
            navigate("/LandingCustomer");
          }
        }, 2000);
      } else {
        const error = await response.text();
        try {
          const errorData = JSON.parse(error);
          setModalMessage(errorData.message);
        } catch {
          setModalMessage(error);
        }
        setModalType("error");
        setShowModal(true);
        // Auto-hide error modal after 3 seconds
        setTimeout(() => setShowModal(false), 3000);
      }
    } catch (error) {
      console.error("Error logging in:", error);
      alert("Something went wrong!");
    }
  };

  return (
    <div className="login-wrapper">
      {/* Left Section */}
      <div className="welcome-section">
        <div className="logo">
          <span style={{ fontWeight: 600 }}>Mobile</span> <span style={{ fontStyle: 'italic' }}>Car Wash</span>
        </div>
        <div className="welcome-text">
          <h1>Hello, welcome!</h1>
          <p>
            Experience convenience at your fingertips. Login to schedule, manage, 
            and track your car wash with ease.
          </p>
        </div>
        <div className="illustration">
          <img
            src="https://images.unsplash.com/photo-1607860108855-78658b11f80b?auto=format&fit=crop&w=700&q=80"
            alt="Car wash illustration"
          />
        </div>
      </div>

      {/* Right Section */}
      <div className="login-section">
        <form onSubmit={handleSubmit} className="login-form">
          <h2>Sign In</h2>
          <p>Enter your account details to continue</p>

          <div className="form-group">
            <label>Email address</label>
            <input
              type="email"
              name="email"
              value={login.email}
              onChange={handleChange}
              placeholder="name@mail.com"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={login.password}
              onChange={handleChange}
              placeholder="Enter your password"
              required
            />
          </div>

          <div className="form-options">
            <label>
              <input type="checkbox" /> Remember me
            </label>
            <a href="#">Forgot password?</a>
          </div>

          <div className="buttons">
            <button type="submit" className="btn-login">
              Login
            </button>
            <button type="button" className="btn-signup" onClick={() => navigate('/RoleSelection')}>
              Sign Up
            </button>
          </div>

          <div className="socials">
            <span>Follow us:</span>
            <div className="icons">
              <a href="#"><i className="fab fa-facebook-f"></i></a>
              <a href="#"><i className="fab fa-twitter"></i></a>
              <a href="#"><i className="fab fa-instagram"></i></a>
            </div>
          </div>
        </form>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className={`modal-content ${modalType}`}>
            <p>{modalMessage}</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default Login;
