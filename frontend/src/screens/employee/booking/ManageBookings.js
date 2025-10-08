import React, { useEffect, useState } from "react";
import "./ManageBookings.css";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/Navbar";
import Footer from "../../components/Footer";
import "../../components/Footer.css";
import { MdFilterList } from "react-icons/md";
import { FaChevronDown, FaChevronUp } from "react-icons/fa"; // For arrows (if not imported yet)

const ManageBookings = () => {
  const navigate = useNavigate();

  const [bookings, setBookings] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [editedBooking, setEditedBooking] = useState({});
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [paymentStatusMap, setPaymentStatusMap] = useState({});
  const [searchTerm, setSearchTerm] = useState("");

  const [filterStatus, setFilterStatus] = useState("");
  const [filterTip, setFilterTip] = useState("");
  const [filterStartDate, setFilterStartDate] = useState("");
  const [filterEndDate, setFilterEndDate] = useState("");
  const [filterAttendant, setFilterAttendant] = useState("");

  const [showFilters, setShowFilters] = useState(false);

  const toggleDropdown = (id) => {
    setActiveDropdown((prev) => (prev === id ? null : id));
  };

  const fetchBookings = async () => {
    try {
      const res = await fetch("http://localhost:8080/mobileglow/api/bookings");
      const data = await res.json();
      setBookings(data);

      const statusMap = {};
      for (const booking of data) {
        const res2 = await fetch(
          `http://localhost:8080/mobileglow/api/bookings/${booking.bookingId}/payment-status`
        );
        const isPaid = await res2.json();
        statusMap[booking.bookingId] = isPaid;
      }

      setPaymentStatusMap(statusMap);
    } catch (err) {
      console.error("Failed to fetch bookings or payment statuses:", err);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handleEdit = (booking) => {
    setEditingId(booking.bookingId);
    setEditedBooking({ ...booking });
  };

  const handleCancel = () => {
    setEditingId(null);
    setEditedBooking({});
  };

  const handleInputChange = (e, key) => {
    const value =
      e.target.type === "checkbox" ? e.target.checked : e.target.value;
    setEditedBooking((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleUpdate = async () => {
    try {
      const updatedBooking = {
        ...editedBooking,
        bookingId: editingId,
      };

      const res = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/${editingId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(updatedBooking),
        }
      );

      if (!res.ok) throw new Error("Failed to update booking");

      await fetchBookings();
      handleCancel();
    } catch (err) {
      alert(err.message);
    }
  };

  const handleDelete = async (bookingId) => {
    if (!window.confirm("Are you sure you want to delete this booking?"))
      return;

    try {
      const res = await fetch(
        `http://localhost:8080/mobileglow/api/bookings/${bookingId}`,
        {
          method: "DELETE",
        }
      );

      if (!res.ok) throw new Error("Failed to delete booking");

      await fetchBookings();
    } catch (err) {
      alert(err.message);
    }
  };

  const filteredBookings = bookings.filter((booking) => {
    const search = searchTerm.toLowerCase();

    const id = booking.bookingId?.toString().toLowerCase() || "";
    const date = new Date(booking.bookingDateTime)
      .toLocaleDateString()
      .toLowerCase();
    const time = new Date(booking.bookingDateTime)
      .toLocaleTimeString()
      .toLowerCase();
    const carMake = booking.vehicle?.carMake?.toLowerCase() || "";
    const carModel = booking.vehicle?.carModel?.toLowerCase() || "";
    const attendantName = booking.washAttendant?.userName?.toLowerCase() || "";
    const attendantSurname =
      booking.washAttendant?.userSurname?.toLowerCase() || "";

    const matchesSearch =
      id.includes(search) ||
      date.includes(search) ||
      time.includes(search) ||
      carMake.includes(search) ||
      carModel.includes(search) ||
      attendantName.includes(search) ||
      attendantSurname.includes(search);

    if (!matchesSearch) return false;

    const status = paymentStatusMap[booking.bookingId] ? "PAID" : "NOT_PAID";
    if (filterStatus && filterStatus !== status) return false;

    if (filterTip === "true" && !booking.tipAdd) return false;
    if (filterTip === "false" && booking.tipAdd) return false;

    const bookingDate = new Date(booking.bookingDateTime);
    if (filterStartDate && bookingDate < new Date(filterStartDate))
      return false;
    if (filterEndDate && bookingDate > new Date(filterEndDate)) return false;

    const attendantFullName = (
      booking.washAttendant?.userName +
      " " +
      booking.washAttendant?.userSurname
    )
      .toLowerCase()
      .trim();
    if (
      filterAttendant &&
      !attendantFullName.includes(filterAttendant.toLowerCase().trim())
    )
      return false;

    return true;
  });

  return (
    <div className="dashboard-container">
      <Navbar />
      <div className="app-content">
        <div className="dashboard-header">
          <div className="dashboard-title-toolbar">
            <h2 className="dashboard-title">My booking dashboard</h2>

            <div className="dashboard-toolbar">
              <button
                className="toolbar-btn filter"
                onClick={() => setShowFilters((prev) => !prev)}
                aria-expanded={showFilters}
                aria-controls="filters-container"
              >
                <MdFilterList size={20} style={{ marginRight: 8 }} />
                Filter
                {showFilters ? (
                  <FaChevronUp size={14} style={{ marginLeft: 8 }} />
                ) : (
                  <FaChevronDown size={14} style={{ marginLeft: 8 }} />
                )}
              </button>

              <input
                type="text"
                placeholder="Search"
                className="search-input"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>

          {/* ✅ Move filters below the header block */}
          {showFilters && (
            <div
              id="filters-container"
              className={`filters-row ${showFilters ? "show" : "hide"}`}
            >
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="filter-select"
              >
                <option value="">Payment Status (All)</option>
                <option value="PAID">Paid</option>
                <option value="NOT_PAID">Not Paid</option>
              </select>

              <select
                value={filterTip}
                onChange={(e) => setFilterTip(e.target.value)}
                className="filter-select"
              >
                <option value="">Tip Added (All)</option>
                <option value="true">Yes</option>
                <option value="false">No</option>
              </select>

              <input
                type="date"
                value={filterStartDate}
                onChange={(e) => setFilterStartDate(e.target.value)}
                className="filter-date"
              />
              <input
                type="date"
                value={filterEndDate}
                onChange={(e) => setFilterEndDate(e.target.value)}
                className="filter-date"
              />

              <input
                type="text"
                placeholder="Filter by Attendant"
                className="filter-input"
                value={filterAttendant}
                onChange={(e) => setFilterAttendant(e.target.value)}
              />
            </div>
          )}
        </div>

        <div className="booking-table">
          <div className="booking-row booking-header">
            <div className="col checkbox">
              <input type="checkbox" />
            </div>
            <div className="col id">Booking ID ↓</div>
            <div className="col date">Date ↓</div>
            <div className="col time">Time ↓</div>
            <div className="col tip">Tip added ↓</div>
            <div className="col vehicle">Vehicle/s ↓</div>
            <div className="col attendant">Wash Attendant ↓</div>
            <div className="col status">Payment Status</div>
          </div>

          {filteredBookings.length === 0 ? (
            <div className="booking-row">
              <div
                className="col"
                style={{ textAlign: "center", flex: "100%" }}
              >
                No bookings match your search and filters.
              </div>
            </div>
          ) : (
            filteredBookings.map((booking) => {
              const isEditing = booking.bookingId === editingId;
              const dateObj = new Date(booking.bookingDateTime);
              const date = dateObj.toLocaleDateString();
              const time = dateObj.toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              });

              return (
                <div className="booking-row" key={booking.bookingId}>
                  <div className="col checkbox">
                    <input type="checkbox" />
                  </div>

                  <div className="col id">{booking.bookingId}</div>

                  <div className="col date">
                    {isEditing ? (
                      <input
                        type="date"
                        value={new Date(editedBooking.bookingDateTime)
                          .toISOString()
                          .slice(0, 10)}
                        onChange={(e) => {
                          const newDate = e.target.value;
                          const oldDateTime = new Date(
                            editedBooking.bookingDateTime
                          );
                          const updatedDateTime = new Date(newDate);
                          updatedDateTime.setHours(oldDateTime.getHours());
                          updatedDateTime.setMinutes(oldDateTime.getMinutes());
                          setEditedBooking((prev) => ({
                            ...prev,
                            bookingDateTime: updatedDateTime.toISOString(),
                          }));
                        }}
                      />
                    ) : (
                      date
                    )}
                  </div>

                  <div className="col time">
                    {isEditing ? (
                      <input
                        type="time"
                        value={new Date(editedBooking.bookingDateTime)
                          .toISOString()
                          .slice(11, 16)}
                        onChange={(e) => {
                          const [hours, minutes] = e.target.value.split(":");
                          const updatedDateTime = new Date(
                            editedBooking.bookingDateTime
                          );
                          updatedDateTime.setHours(hours);
                          updatedDateTime.setMinutes(minutes);
                          setEditedBooking((prev) => ({
                            ...prev,
                            bookingDateTime: updatedDateTime.toISOString(),
                          }));
                        }}
                      />
                    ) : (
                      time
                    )}
                  </div>

                  <div className="col tip">
                    {isEditing ? (
                      <input
                        type="checkbox"
                        checked={!!editedBooking.tipAdd}
                        onChange={(e) => handleInputChange(e, "tipAdd")}
                      />
                    ) : booking.tipAdd ? (
                      "Yes"
                    ) : (
                      "No"
                    )}
                  </div>

                  <div className="col vehicle">
                    {isEditing ? (
                      <>
                        <input
                          type="text"
                          placeholder="Make"
                          value={editedBooking.vehicle?.carMake || ""}
                          onChange={(e) =>
                            setEditedBooking((prev) => ({
                              ...prev,
                              vehicle: {
                                ...prev.vehicle,
                                carMake: e.target.value,
                              },
                            }))
                          }
                        />
                        <input
                          type="text"
                          placeholder="Model"
                          value={editedBooking.vehicle?.carModel || ""}
                          onChange={(e) =>
                            setEditedBooking((prev) => ({
                              ...prev,
                              vehicle: {
                                ...prev.vehicle,
                                carModel: e.target.value,
                              },
                            }))
                          }
                        />
                      </>
                    ) : (
                      `${booking.vehicle?.carMake || ""}, ${
                        booking.vehicle?.carModel || ""
                      }`
                    )}
                  </div>

                  <div className="col attendant">
                    {isEditing ? (
                      <>
                        <input
                          type="text"
                          placeholder="Name"
                          value={editedBooking.washAttendant?.userName || ""}
                          onChange={(e) =>
                            setEditedBooking((prev) => ({
                              ...prev,
                              washAttendant: {
                                ...prev.washAttendant,
                                userName: e.target.value,
                              },
                            }))
                          }
                        />
                        <input
                          type="text"
                          placeholder="Surname"
                          value={editedBooking.washAttendant?.userSurname || ""}
                          onChange={(e) =>
                            setEditedBooking((prev) => ({
                              ...prev,
                              washAttendant: {
                                ...prev.washAttendant,
                                userSurname: e.target.value,
                              },
                            }))
                          }
                        />
                      </>
                    ) : (
                      `${booking.washAttendant?.userName || ""} ${
                        booking.washAttendant?.userSurname || ""
                      }`
                    )}
                  </div>

                  <div className="col status">
                    <span
                      className={`status-tag ${
                        paymentStatusMap[booking.bookingId]
                          ? "paid"
                          : "not-paid"
                      }`}
                    >
                      {paymentStatusMap[booking.bookingId]
                        ? "PAID"
                        : "NOT PAID"}
                    </span>
                  </div>

                  <div className="col actions">
                    {isEditing ? (
                      <div style={{ display: "flex", gap: "8px" }}>
                        <button className="save-btn" onClick={handleUpdate}>
                          Save
                        </button>
                        <button className="cancel-btn" onClick={handleCancel}>
                          Cancel
                        </button>
                      </div>
                    ) : (
                      <div className="menu-wrapper">
                        <button
                          className="dots-menu-btn"
                          onClick={() => toggleDropdown(booking.bookingId)}
                        >
                          ⋮
                        </button>
                        {activeDropdown === booking.bookingId && (
                          <div className="dropdown-menu">
                            <button onClick={() => handleEdit(booking)}>
                              Edit
                            </button>
                            <button
                              onClick={() => handleDelete(booking.bookingId)}
                            >
                              Delete
                            </button>
                            <button
                              onClick={() =>
                                navigate(`/payment/${booking.bookingId}`)
                              }
                            >
                              Pay
                            </button>
                          </div>
                        )}
                      </div>
                    )}
                  </div>
                </div>
              );
            })
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default ManageBookings;
