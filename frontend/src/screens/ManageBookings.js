import React, { useEffect, useState } from 'react';
import './ManageBookings.css';

const ManageBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [editedBooking, setEditedBooking] = useState({});

  const fetchBookings = async () => {
    try {
      const res = await fetch('http://localhost:8081/mobileglow/api/bookings');
      const data = await res.json();
      setBookings(data);
    } catch (err) {
      console.error('Failed to fetch bookings:', err);
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
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
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

      const res = await fetch(`http://localhost:8081/mobileglow/api/bookings/${editingId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedBooking),
      });

      if (!res.ok) throw new Error('Failed to update booking');

      await fetchBookings();
      handleCancel();
    } catch (err) {
      alert(err.message);
    }
  };

  const handleDelete = async (bookingId) => {
    if (!window.confirm('Are you sure you want to delete this booking?')) return;

    try {
      const res = await fetch(`http://localhost:8081/mobileglow/api/bookings/${bookingId}`, {
        method: 'DELETE',
      });

      if (!res.ok) throw new Error('Failed to delete booking');

      await fetchBookings();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="manage-bookings-container">
      <h2>Manage Bookings</h2>

      <table className="bookings-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Date & Time</th>
            <th>Booking Cost</th>
            <th>Tip Added</th>
            <th>Vehicle</th>
            <th>Attendant</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {bookings.map((booking) => {
            const isEditing = booking.bookingId === editingId;

            return (
              <tr key={booking.bookingId}>
                <td>{booking.bookingId}</td>

                <td>
                  {isEditing ? (
                    <input
                      type="datetime-local"
                      value={new Date(editedBooking.bookingDateTime).toISOString().slice(0, 16)}
                      onChange={(e) => handleInputChange(e, 'bookingDateTime')}
                    />
                  ) : (
                    new Date(booking.bookingDateTime).toLocaleString()
                  )}
                </td>

                <td>
                  {isEditing ? (
                    <input
                      type="number"
                      value={editedBooking.bookingCost || ''}
                      onChange={(e) => handleInputChange(e, 'bookingCost')}
                    />
                  ) : (
                    booking.bookingCost
                  )}
                </td>

                <td>
                  {isEditing ? (
                    <input
                      type="checkbox"
                      checked={!!editedBooking.tipAdd}
                      onChange={(e) => handleInputChange(e, 'tipAdd')}
                    />
                  ) : (
                    booking.tipAdd ? 'Yes' : 'No'
                  )}
                </td>

                <td>
                  {isEditing ? (
                    <input
                      type="text"
                      value={editedBooking.vehicle?.carMake || ''}
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
                  ) : (
                    `${booking.vehicle?.carMake || ''} ${booking.vehicle?.carModel || ''}`
                  )}
                </td>

                <td>
                  {isEditing ? (
                    <input
                      type="text"
                      value={editedBooking.washAttendant?.userName || ''}
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
                  ) : (
                    `${booking.washAttendant?.userName || ''} ${booking.washAttendant?.userSurname || ''}`
                  )}
                </td>

                <td className="action-buttons">
                  {isEditing ? (
                    <>
                      <button onClick={handleUpdate} className="save-btn">Save</button>
                      <button onClick={handleCancel} className="cancel-btn">Cancel</button>
                    </>
                  ) : (
                    <>
                      <button onClick={() => handleEdit(booking)} className="edit-btn">Edit</button>
                      <button onClick={() => handleDelete(booking.bookingId)} className="delete-btn">Delete</button>
                      <button
                        onClick={() => window.location.href = `/payment/${booking.bookingId}`}
                        className="pay-btn"
                      >
                        Pay
                      </button>
                    </>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default ManageBookings;