import React, { useState, useEffect } from "react";
import "./Booking.css";
import { ToggleButton, ToggleButtonGroup } from "@mui/material";
import { useNavigate, Link } from "react-router-dom";
import { FaStar, FaTrash } from "react-icons/fa";
import NavbarCustomer from "../../components/NavbarCustomer";
import Footer from "../../components/Footer";

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
      .then((res) => res.json())
      .then((data) => setServices(data))
      .catch((err) => console.error("Error fetching services:", err));
  }, []);

  const handleCategoryChange = (event, newCategory) => {
    if (newCategory) setSelectedCategory(newCategory);
  };

  const filteredServices = Array.isArray(services)
    ? services.filter(
        (s) =>
          s.category?.trim().toLowerCase() ===
          selectedCategory.trim().toLowerCase()
      )
    : [];

  const addToCart = (service) => {
    const normalized = { ...service, id: service.cleaningServiceId };
    if (!cart.some((item) => item.id === normalized.id)) {
      setCart([...cart, normalized]);
    }
  };

  const removeFromCart = (id) => setCart(cart.filter((item) => item.id !== id));

  const totalPrice = cart.reduce((sum, item) => sum + item.priceOfService, 0);

  const handleContinue = () => {
    if (cart.length === 0) return;
    const serviceIds = cart.map((s) => ({ cleaningServiceId: s.id }));
    const state = { cart, totalPrice, serviceIds };
    sessionStorage.setItem("bookingData", JSON.stringify(state));
    navigate("/bookingtwo", { state });
  };

  return (
    <>
      <NavbarCustomer />
      <main className="bookingpage-wrapper">
        {/* Header */}
        <section className="bookingpage-header">
          <div className="bookingpage-breadcrumb">
            <Link to="/" className="bookingpage-link">
              Home
            </Link>
            <span className="bookingpage-separator">›</span>
            <strong>Select a Service</strong>
          </div>

          <h1 className="bookingpage-title">Choose Your Wash</h1>
          <div className="bookingpage-info">
            <span className="bookingpage-rating">
              <FaStar className="bookingpage-star" /> 4.8 (288)
            </span>
            <span>•</span>
            <span>Open until 17:00</span>
            <span>•</span>
            <span>Cape Town</span>
          </div>

          {/* Optional banner area */}
          <div className="bookingpage-banner">
            <p>
              Select from our premium range of wash and detailing services —
              designed to make your car look showroom new.
            </p>
          </div>
        </section>

        {/* Panels */}
        <section className="bookingpage-panels">
          {/* Left */}
          <div className="bookingpage-left">
            <ToggleButtonGroup
              value={selectedCategory}
              exclusive
              onChange={handleCategoryChange}
              className="bookingpage-categories"
            >
              {categories.map((cat) => (
                <ToggleButton
                  key={cat}
                  value={cat}
                  className="bookingpage-category"
                >
                  {cat}
                </ToggleButton>
              ))}
            </ToggleButtonGroup>

            <div className="bookingpage-service-list">
              {filteredServices.length === 0 ? (
                <p>No services available for this category</p>
              ) : (
                filteredServices.map((service) => (
                  <div
                    key={service.cleaningServiceId}
                    className="bookingpage-service-card"
                  >
                    <div>
                      <h5>{service.serviceName.replace(/_/g, " ")}</h5>
                      <p className="bookingpage-duration">
                        Duration: {service.duration}h
                      </p>
                      <p className="bookingpage-price">
                        R {service.priceOfService}
                      </p>
                    </div>
                    <button
                      className={`bookingpage-add-btn ${
                        cart.some((i) => i.id === service.cleaningServiceId)
                          ? "bookingpage-added"
                          : ""
                      }`}
                      onClick={() => {
                        cart.some((i) => i.id === service.cleaningServiceId)
                          ? removeFromCart(service.cleaningServiceId)
                          : addToCart(service);
                      }}
                    >
                      {cart.some((i) => i.id === service.cleaningServiceId)
                        ? "✓ Selected"
                        : "Add Service"}
                    </button>
                  </div>
                ))
              )}
            </div>
          </div>

          {/* Right */}
          <aside className="bookingpage-right">
            <h3>🧽 Your Selection</h3>
            {cart.length === 0 ? (
              <p className="bookingpage-empty">No services selected yet</p>
            ) : (
              <ul>
                {cart.map((item) => (
                  <li key={item.id} className="bookingpage-cart-item">
                    <span>
                      {item.serviceName.replace(/_/g, " ")} – R{" "}
                      {item.priceOfService}
                    </span>
                    <button
                      onClick={() => removeFromCart(item.id)}
                      className="bookingpage-remove-btn"
                    >
                      <FaTrash />
                    </button>
                  </li>
                ))}
              </ul>
            )}

            <div className="bookingpage-total">
              <strong>Total:</strong> R {totalPrice.toFixed(2)}
            </div>

            <button
              onClick={handleContinue}
              disabled={cart.length === 0}
              className="bookingpage-continue-btn"
            >
              Continue
            </button>
          </aside>
        </section>
      </main>

      <Footer />
    </>
  );
};

export default Booking;