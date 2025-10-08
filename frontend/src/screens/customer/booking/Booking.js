import React, { useState, useEffect } from "react";
import "./Booking.css";
import { ToggleButton, ToggleButtonGroup } from "@mui/material";
import { useNavigate, Link } from "react-router-dom";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import bookingImage1 from "../../../assets/booking-interface-tire-clean-water-pressure.png";
import bookingImage2 from "../../../assets/booking-interface-wash-attendant-washing-tire.png";
import { FaStar, FaTrash } from "react-icons/fa";

const Booking = () => {
  const categories = [
    "Exterior Wash",
    "Interior Care",
    "Full Detailing",
    "Protection Services",
  ];
  const [selectedCategory, setSelectedCategory] = useState("Exterior Wash");
  const [services, setServices] = useState([]);
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/mobileglow/api/cleaningservice/getAll")
      .then((response) => response.json())
      .then((data) => {
        console.log("Fetched Services:", data);
        setServices(data);
      })
      .catch((error) => console.error("Error fetching services:", error));
  }, []);

  const handleCategoryChange = (event, newCategory) => {
    if (newCategory) {
      setSelectedCategory(newCategory);
    }
  };

  const filteredServices = Array.isArray(services)
    ? services.filter(
        (service) =>
          service.category?.trim().toLowerCase() ===
          selectedCategory.trim().toLowerCase()
      )
    : [];

  const addToCart = (service) => {
    const normalizedService = {
      ...service,
      id: service.cleaningServiceId,
    };

    if (!cart.some((item) => item.id === normalizedService.id)) {
      const newCart = [...cart, normalizedService];
      setCart(newCart);
    }
  };

  const removeFromCart = (serviceId) => {
    setCart(cart.filter((item) => item.id !== serviceId));
  };

  const totalPrice = cart.reduce((sum, item) => sum + item.priceOfService, 0);

  const handleContinue = () => {
    if (cart.length === 0) return;

    const serviceIds = cart.map((s) => ({
      cleaningServiceId: s.id,
    }));

    const state = {
      cart,
      totalPrice,
      serviceIds,
    };

    sessionStorage.setItem("bookingData", JSON.stringify(state));
    navigate("/bookingtwo", { state });
  };

  // Carousel settings
  const carouselSettings = {
    dots: true,
    infinite: true,
    speed: 600,
    slidesToShow: 2,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 2000,
  };

  const images = [bookingImage1, bookingImage2, bookingImage1, bookingImage2];

  return (
    <div className="booking-layout app-content">
      {/* Header */}
      <div className="booking-header">
        <div className="breadcrumb">
          <Link to="/" className="breadcrumb-link">
            Home
          </Link>
          <span className="dot">•</span>
          <strong>Select a service</strong>
        </div>

        <h1 className="main-title">Select a service</h1>

        <div className="business-info-inline">
          <span className="rating">
            <strong>4.8</strong>
            <span className="stars">
              <FaStar />
              <FaStar />
              <FaStar />
              <FaStar />
              <FaStar />
            </span>
            <span className="rating-count">(288)</span>
          </span>

          <span className="dot">•</span>
          <span className="hours">
            <span className="open-label">Open</span> until 17:00
          </span>

          <span className="dot">•</span>
          <span className="location">Cape Town, Cape Town</span>

          <span className="dot">•</span>
          <a href="#" className="directions-link">
            Get directions
          </a>
        </div>

        {/* ✅ Carousel Section */}
        <div className="carousel-wrapper">
          <Slider {...carouselSettings}>
            {images.map((img, index) => (
              <div key={index}>
                <img
                  src={img}
                  alt={`Car Wash ${index + 1}`}
                  className="carousel-image"
                />
              </div>
            ))}
          </Slider>
        </div>

        <h2 className="services-title">Services</h2>
      </div>

      {/* Main Content */}
      <div className="panel-container">
        {/* Left Panel */}
        <div className="left-panel">
          <ToggleButtonGroup
            value={selectedCategory}
            exclusive
            onChange={handleCategoryChange}
            aria-label="Category"
            className="category-toggle-group"
          >
            {categories.map((cat) => (
              <ToggleButton
                key={cat}
                value={cat}
                className="category-toggle-button"
              >
                {cat}
              </ToggleButton>
            ))}
          </ToggleButtonGroup>

          <div className="service-list">
            {filteredServices.length === 0 ? (
              <p>No services available for this category</p>
            ) : (
              filteredServices.map((service) => (
                <div key={service.id} className="service-card">
                  <div className="service-info">
                    <h4>{service.serviceName.replace(/_/g, " ")}</h4>
                    <p className="duration">
                      Duration: {service.duration} <strong>hours</strong>
                    </p>
                    <p className="price">R {service.priceOfService}</p>
                  </div>

                  <button
                    className={`add-btn ${
                      cart.some((item) => item.id === service.cleaningServiceId)
                        ? "added"
                        : ""
                    }`}
                    onClick={() => {
                      cart.some((item) => item.id === service.cleaningServiceId)
                        ? removeFromCart(service.cleaningServiceId)
                        : addToCart(service);
                    }}
                  >
                    {cart.some((item) => item.id === service.cleaningServiceId)
                      ? "✓ Selected"
                      : "Add Service"}
                  </button>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Right Panel */}
        <div className="right-panel">
          <div className="business-info">
            <h3 className="right-panel-heading">MobileGlow Car Wash</h3>
            <p>4.9 ⭐ (32)</p>
            <p>Parklands, Cape Town</p>
          </div>

          <div className="cart">
            <h4>Selected Services</h4>

            {cart.length === 0 ? (
              <p className="empty-cart">No services selected</p>
            ) : (
              <ul>
                {cart.map((item, index) => (
                  <li
                    key={item.id || `${item.serviceName}-${index}`}
                    className="cart-item"
                  >
                    <span className="cart-service-name">
                      {item.serviceName.replace(/_/g, " ")} - R{" "}
                      {item.priceOfService}
                    </span>
                    <button
                      className="remove-btn"
                      onClick={() => removeFromCart(item.id)}
                    >
                      <FaTrash />
                    </button>
                  </li>
                ))}
              </ul>
            )}

            <div className="total">
              <strong>Total:</strong> R {totalPrice || "0"}
            </div>

            <button
              className="continue-btn"
              disabled={cart.length === 0}
              onClick={handleContinue}
            >
              Continue
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Booking;