import React, { useEffect, useState } from "react";
import "./ManageBookings.css"; // Make sure this exists and has the styles below
import { useNavigate } from "react-router-dom";

const ManageBookings = () => {
  const navigate = useNavigate();

  const [bookings, setBookings] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [editedBooking, setEditedBooking] = useState({});
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [paymentStatusMap, setPaymentStatusMap] = useState({});

  const toggleDropdown = (id) => {
    setActiveDropdown((prev) => (prev === id ? null : id));
  };

  const fetchBookings = async () => {
    try {
      const res = await fetch("http://localhost:8080/mobileglow/api/bookings");
      const data = await res.json();
      setBookings(data);

      // 🔁 Fetch payment status for each booking
      const statusMap = {};

      for (const booking of data) {
        const res = await fetch(
          `http://localhost:8080/mobileglow/api/bookings/${booking.bookingId}/payment-status`
        );
        const isPaid = await res.json(); // should return true or false
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

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h2>My booking dashboard</h2>
        <div className="dashboard-toolbar">
          <button className="toolbar-btn delete">🗑 Delete</button>
          <button className="toolbar-btn filter">🔍 Filters</button>
          <input type="text" placeholder="Search" className="search-input" />
        </div>
      </div>

      <div className="booking-table">
        {/* Table Headers */}
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
          {/* <div className="col actions">Options</div> */}
        </div>

        {/* Table Rows */}
        {bookings.map((booking) => {
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
              const oldDateTime = new Date(editedBooking.bookingDateTime);
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
            paymentStatusMap[booking.bookingId] ? "paid" : "not-paid"
          }`}
        >
          {paymentStatusMap[booking.bookingId] ? "PAID" : "NOT PAID"}
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
                <button onClick={() => handleEdit(booking)}>Edit</button>
                <button onClick={() => handleDelete(booking.bookingId)}>
                  Delete
                </button>
                <button onClick={() => navigate(`/payment/${booking.bookingId}`)}>
                  Pay
                </button>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
})}
      </div>
    </div>
  );
};

export default ManageBookings;

// import React, { useEffect, useState } from "react";
// import "./ManageBookings.css";

// const ManageBookings = () => {
//   const [bookings, setBookings] = useState([]);
//   const [editingId, setEditingId] = useState(null);
//   const [editedBooking, setEditedBooking] = useState({});

//   const fetchBookings = async () => {
//     try {
//       const res = await fetch("http://localhost:8080/mobileglow/api/bookings");
//       const data = await res.json();
//       setBookings(data);
//     } catch (err) {
//       console.error("Failed to fetch bookings:", err);
//     }
//   };

//   useEffect(() => {
//     fetchBookings();
//   }, []);

//   const handleEdit = (booking) => {
//     setEditingId(booking.bookingId);
//     setEditedBooking({ ...booking });
//   };

//   const handleCancel = () => {
//     setEditingId(null);
//     setEditedBooking({});
//   };

//   const handleInputChange = (e, key) => {
//     const value =
//       e.target.type === "checkbox" ? e.target.checked : e.target.value;
//     setEditedBooking((prev) => ({
//       ...prev,
//       [key]: value,
//     }));
//   };

//   const handleUpdate = async () => {
//     try {
//       const updatedBooking = {
//         ...editedBooking,
//         bookingId: editingId,
//       };

//       const res = await fetch(
//         `http://localhost:8081/mobileglow/api/bookings/${editingId}`,
//         {
//           method: "PUT",
//           headers: {
//             "Content-Type": "application/json",
//           },
//           body: JSON.stringify(updatedBooking),
//         }
//       );

//       if (!res.ok) throw new Error("Failed to update booking");

//       await fetchBookings();
//       handleCancel();
//     } catch (err) {
//       alert(err.message);
//     }
//   };

//   const handleDelete = async (bookingId) => {
//     if (!window.confirm("Are you sure you want to delete this booking?"))
//       return;

//     try {
//       const res = await fetch(
//         `http://localhost:8081/mobileglow/api/bookings/${bookingId}`,
//         {
//           method: "DELETE",
//         }
//       );

//       if (!res.ok) throw new Error("Failed to delete booking");

//       await fetchBookings();
//     } catch (err) {
//       alert(err.message);
//     }
//   };

//   return (
//     <div className="manage-bookings-container">
//       <h2>Manage Bookings</h2>

//       <table className="bookings-table">
//         <thead>
//           <tr>
//             <th>ID</th>
//             <th>Date & Time</th>
//             <th>Booking Cost</th>
//             <th>Tip Added</th>
//             <th>Vehicle</th>
//             <th>Attendant</th>
//             <th>Actions</th>
//           </tr>
//         </thead>

//         <tbody>
//           {bookings.map((booking) => {
//             const isEditing = booking.bookingId === editingId;

//             return (
//               <tr key={booking.bookingId}>
//                 <td>{booking.bookingId}</td>

//                 <td>
//                   {isEditing ? (
//                     <input
//                       type="datetime-local"
//                       value={new Date(editedBooking.bookingDateTime)
//                         .toISOString()
//                         .slice(0, 16)}
//                       onChange={(e) => handleInputChange(e, "bookingDateTime")}
//                     />
//                   ) : (
//                     new Date(booking.bookingDateTime).toLocaleString()
//                   )}
//                 </td>

//                 <td>
//                   {isEditing ? (
//                     <input
//                       type="number"
//                       value={editedBooking.bookingCost || ""}
//                       onChange={(e) => handleInputChange(e, "bookingCost")}
//                     />
//                   ) : (
//                     booking.bookingCost
//                   )}
//                 </td>

//                 <td>
//                   {isEditing ? (
//                     <input
//                       type="checkbox"
//                       checked={!!editedBooking.tipAdd}
//                       onChange={(e) => handleInputChange(e, "tipAdd")}
//                     />
//                   ) : booking.tipAdd ? (
//                     "Yes"
//                   ) : (
//                     "No"
//                   )}
//                 </td>

//                 <td>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={editedBooking.vehicle?.carMake || ""}
//                       onChange={(e) =>
//                         setEditedBooking((prev) => ({
//                           ...prev,
//                           vehicle: {
//                             ...prev.vehicle,
//                             carMake: e.target.value,
//                           },
//                         }))
//                       }
//                     />
//                   ) : (
//                     `${booking.vehicle?.carMake || ""} ${
//                       booking.vehicle?.carModel || ""
//                     }`
//                   )}
//                 </td>

//                 <td>
//                   {isEditing ? (
//                     <input
//                       type="text"
//                       value={editedBooking.washAttendant?.userName || ""}
//                       onChange={(e) =>
//                         setEditedBooking((prev) => ({
//                           ...prev,
//                           washAttendant: {
//                             ...prev.washAttendant,
//                             userName: e.target.value,
//                           },
//                         }))
//                       }
//                     />
//                   ) : (
//                     `${booking.washAttendant?.userName || ""} ${
//                       booking.washAttendant?.userSurname || ""
//                     }`
//                   )}
//                 </td>

//                 <td className="action-buttons">
//                   {isEditing ? (
//                     <>
//                       <button onClick={handleUpdate} className="save-btn">
//                         Save
//                       </button>
//                       <button onClick={handleCancel} className="cancel-btn">
//                         Cancel
//                       </button>
//                     </>
//                   ) : (
//                     <>
//                       <button
//                         onClick={() => handleEdit(booking)}
//                         className="edit-btn"
//                       >
//                         Edit
//                       </button>
//                       <button
//                         onClick={() => handleDelete(booking.bookingId)}
//                         className="delete-btn"
//                       >
//                         Delete
//                       </button>
//                       <button
//                         onClick={() =>
//                           (window.location.href = `/payment/${booking.bookingId}`)
//                         }
//                         className="pay-btn"
//                       >
//                         Pay
//                       </button>
//                     </>
//                   )}
//                 </td>
//               </tr>
//             );
//           })}
//         </tbody>
//       </table>
//     </div>
//   );
// };

// export default ManageBookings;
