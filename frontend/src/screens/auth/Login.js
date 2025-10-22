import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css"; // external CSS file
import image1 from '../../assets/image-1.png';
import image2 from '../../assets/image-2.png';
import image3 from '../../assets/image-3.png';
import interior from '../../assets/interior.png';

const Login = () => {
  const navigate = useNavigate();
  const [login, setLogin] = useState({ email: "", password: "" });
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalType, setModalType] = useState("success"); // 'success' or 'error'
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const images = [image1, image2, image3, interior];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
    }, 3000); // Change image every 3 seconds

    return () => clearInterval(interval);
  }, [images.length]);

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
          <div className="image-slider" style={{ overflow: 'hidden', width: '100%', height: '100%', position: 'relative', borderRadius: '15px' }}>
            <div
              className="slider-track"
              style={{
                display: 'flex',
                width: `${images.length * 100}%`,
                height: '100%',
                transform: `translateX(-${currentImageIndex * (100 / images.length)}%)`,
                transition: 'transform 0.8s cubic-bezier(0.4, 0, 0.2, 1)'
              }}
            >
              {images.map((image, index) => (
                <div key={index} style={{ width: `${100 / images.length}%`, height: '100%', position: 'relative', flexShrink: 0 }}>
                  <img
                    src={image}
                    alt={`Car wash illustration ${index + 1}`}
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover'
                    }}
                  />
                  <div className="slide-overlay" style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    background: 'linear-gradient(135deg, rgba(0,0,0,0.3) 0%, rgba(0,0,0,0.1) 100%)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}>
                    <div className="slide-content" style={{
                      textAlign: 'center',
                      color: 'white',
                      padding: '20px',
                      borderRadius: '10px',
                      background: 'rgba(255,255,255,0.1)',
                      backdropFilter: 'blur(10px)',
                      border: '1px solid rgba(255,255,255,0.2)'
                    }}>
                      <h3 style={{ margin: '0 0 10px 0', fontSize: '24px', fontWeight: 'bold' }}>
                        {index === 0 && 'Rim Detailing Excellence'}
                        {index === 1 && 'Complete Exterior Bath'}
                        {index === 2 && 'Thorough Wheel Care'}
                        {index === 3 && 'Interior Perfection'}
                      </h3>
                      <p style={{ margin: 0, fontSize: '16px', opacity: 0.9 }}>
                        {index === 0 && 'Precision cleaning for sparkling wheel rims'}
                        {index === 1 && 'Luxurious exterior coverage for deep cleaning'}
                        {index === 2 && 'Expert tire and rim restoration service'}
                        {index === 3 && 'Immaculate interior polishing and care'}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Navigation Arrows */}
            <button
              onClick={() => setCurrentImageIndex((prev) => (prev - 1 + images.length) % images.length)}
              style={{
                position: 'absolute',
                left: '20px',
                top: '50%',
                transform: 'translateY(-50%)',
                background: 'rgba(255,255,255,0.2)',
                border: 'none',
                borderRadius: '50%',
                width: '50px',
                height: '50px',
                color: 'white',
                fontSize: '20px',
                cursor: 'pointer',
                backdropFilter: 'blur(10px)',
                transition: 'all 0.3s ease',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
              onMouseEnter={(e) => e.target.style.background = 'rgba(255,255,255,0.4)'}
              onMouseLeave={(e) => e.target.style.background = 'rgba(255,255,255,0.2)'}
            >
              ‹
            </button>
            <button
              onClick={() => setCurrentImageIndex((prev) => (prev + 1) % images.length)}
              style={{
                position: 'absolute',
                right: '20px',
                top: '50%',
                transform: 'translateY(-50%)',
                background: 'rgba(255,255,255,0.2)',
                border: 'none',
                borderRadius: '50%',
                width: '50px',
                height: '50px',
                color: 'white',
                fontSize: '20px',
                cursor: 'pointer',
                backdropFilter: 'blur(10px)',
                transition: 'all 0.3s ease',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
              onMouseEnter={(e) => e.target.style.background = 'rgba(255,255,255,0.4)'}
              onMouseLeave={(e) => e.target.style.background = 'rgba(255,255,255,0.2)'}
            >
              ›
            </button>
          </div>

          {/* Enhanced Dots */}
          <div className="dots-container" style={{
            position: 'absolute',
            bottom: '30px',
            left: '50%',
            transform: 'translateX(-50%)',
            display: 'flex',
            gap: '15px',
            background: 'rgba(0,0,0,0.3)',
            padding: '10px 20px',
            borderRadius: '25px',
            backdropFilter: 'blur(10px)'
          }}>
            {images.map((_, index) => (
              <span
                key={index}
                onClick={() => setCurrentImageIndex(index)}
                style={{
                  width: currentImageIndex === index ? '16px' : '12px',
                  height: currentImageIndex === index ? '16px' : '12px',
                  borderRadius: '50%',
                  backgroundColor: currentImageIndex === index ? '#fff' : 'rgba(255, 255, 255, 0.5)',
                  cursor: 'pointer',
                  transition: 'all 0.3s ease',
                  border: currentImageIndex === index ? '2px solid rgba(255,255,255,0.8)' : 'none',
                  boxShadow: currentImageIndex === index ? '0 0 10px rgba(255,255,255,0.5)' : 'none'
                }}
              />
            ))}
          </div>
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
