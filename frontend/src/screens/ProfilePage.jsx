import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ProfilePage.css';

const ProfilePage = () => {
  const initialManager = {
    userName: '',
    userSurname: '',
    isActive: false,
    roleDescription: '',
    employeeType: '',
    hireDate: '',
    contact: {
      phoneNumber: ''
    },
    address: {
      streetNumber: '',
      streetName: '',
      city: '',
      postalCode: ''
    },
    login: {
      emailAddress: '',
      password: ''
    }
  };

  const [manager, setManager] = useState(initialManager);
  const [isEditing, setIsEditing] = useState(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [statusMessage, setStatusMessage] = useState('');
  const [messageType, setMessageType] = useState('error');

  // Save original state for cancel
  const [originalManager, setOriginalManager] = useState(initialManager);

  useEffect(() => {
    const fetchUserData = async () => {
      const userEmail = localStorage.getItem('userEmail');
      const userRole = localStorage.getItem('userRoleDescription');
      if (!userEmail) {
        setStatusMessage('No user logged in');
        setMessageType('error');
        setLoading(false);
        return;
      }

      try {
        let users = [];
        if (userRole === 'CLIENT') {
          const response = await fetch('http://localhost:8080/mobileglow/api/customers');
          if (response.ok) {
            users = await response.json();
          } else {
            setStatusMessage('Failed to fetch user data');
            setMessageType('error');
            return;
          }
        } else {
          // For employees, fetch from all employee endpoints
          const endpoints = [
            'http://localhost:8080/mobileglow/Manager/getAllManagers',
            'http://localhost:8080/mobileglow/Accountant/getAllAccountants',
            'http://localhost:8080/mobileglow/wash-attendants/getAllWashAttendants'
          ];
          for (const endpoint of endpoints) {
            try {
              const response = await fetch(endpoint);
              if (response.ok) {
                const data = await response.json();
                users = users.concat(data);
              }
            } catch (error) {
              console.error('Error fetching from', endpoint, error);
            }
          }
        }
        const loggedInUser = users.find(u => u.login.emailAddress === userEmail);
        if (loggedInUser) {
          setManager(loggedInUser);
          setOriginalManager(loggedInUser);
        } else {
          setStatusMessage('User data not found');
          setMessageType('error');
        }
      } catch (error) {
        console.error('Error fetching user data:', error);
        setStatusMessage('Something went wrong while fetching data');
        setMessageType('error');
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setManager({ ...manager, [name]: value });
  };

  const handleContactChange = (e) => {
    const { name, value } = e.target;
    setManager({
      ...manager,
      contact: {
        ...manager.contact,
        [name]: value
      }
    });
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setManager({
      ...manager,
      address: {
        ...manager.address,
        [name]: value
      }
    });
  };

  const handleLoginChange = (e) => {
    const { name, value } = e.target;
    setManager({
      ...manager,
      login: {
        ...manager.login,
        [name]: value
      }
    });
  };

  const handleEdit = () => {
    setOriginalManager(manager); // Save current data for cancel
    setIsEditing(true);
  };

  const handleUpdate = async () => {
    const userRole = localStorage.getItem('userRoleDescription');
    try {
      let endpoint;
      if (userRole === 'CLIENT') {
        endpoint = `http://localhost:8080/mobileglow/api/customers/${manager.userId}`;
      } else {
        if (manager.employeeType === 'ACCOUNTANT') {
          endpoint = 'http://localhost:8080/mobileglow/Accountant/update';
        } else if (manager.employeeType === 'WashAttendant') {
          endpoint = 'http://localhost:8080/mobileglow/wash-attendants/update';
        } else {
          endpoint = 'http://localhost:8080/mobileglow/Manager/update';
        }
      }
      const token = localStorage.getItem('token');
      const response = await fetch(endpoint, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` })
        },
        body: JSON.stringify(manager)
      });

      if (response.ok) {
        const updatedUser = await response.json();
        setManager(updatedUser);
        setOriginalManager(updatedUser);
        setIsEditing(false);
        setStatusMessage('Profile updated successfully!');
        setMessageType('success');
        setTimeout(() => { setStatusMessage(''); setMessageType('error'); }, 5000);
      } else {
        setStatusMessage('Failed to update profile');
        setMessageType('error');
      }
    } catch (error) {
      console.error('Error updating profile:', error);
      setStatusMessage('Something went wrong while updating');
      setMessageType('error');
    }
  };

  const handleCancel = () => {
    setManager(originalManager);
    setIsEditing(false);
  };

  const handleDelete = () => setShowDeleteConfirm(true);

  const confirmDelete = async () => {
    const userRole = localStorage.getItem('userRoleDescription');
    try {
      let endpoint;
      if (userRole === 'CLIENT') {
        endpoint = `http://localhost:8080/mobileglow/api/customers/${manager.userId}`;
      } else {
        if (manager.employeeType === 'ACCOUNTANT') {
          endpoint = `http://localhost:8080/mobileglow/Accountant/delete/${manager.userId}`;
        } else if (manager.employeeType === 'WashAttendant') {
          endpoint = `http://localhost:8080/mobileglow/wash-attendants/delete/${manager.userId}`;
        } else {
          endpoint = `http://localhost:8080/mobileglow/Manager/delete/${manager.userId}`;
        }
      }
      const token = localStorage.getItem('token');
      const response = await fetch(endpoint, {
        method: 'DELETE',
        headers: {
          ...(token && { 'Authorization': `Bearer ${token}` })
        }
      });

      if (response.ok) {
        setStatusMessage('Profile deleted successfully!');
        setMessageType('success');
        setTimeout(() => { setStatusMessage(''); setMessageType('error'); }, 5000);
        setTimeout(() => (window.location.href = '/login'), 2000);
      } else {
        setStatusMessage('Failed to delete profile');
        setMessageType('error');
      }
    } catch (error) {
      console.error('Error deleting profile:', error);
      setStatusMessage('Something went wrong while deleting');
      setMessageType('error');
    }
    setShowDeleteConfirm(false);
  };

  const cancelDelete = () => setShowDeleteConfirm(false);

  const userRole = localStorage.getItem('userRoleDescription');
  const navigate = useNavigate();

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <h2>Loading Manager Profile...</h2>
      </div>
    );
  }

  return (
    <>
      <div className="profile-page">
        <button
          onClick={() => navigate(userRole === 'CLIENT' ? '/LandingCustomer' : '/LandingEmployee')}
          style={{
            fontSize: 24,
            background: 'none',
            border: 'none',
            cursor: 'pointer',
            marginBottom: '1rem',
          }}
          aria-label="Go back"
        >
          ←
        </button>
        <h2>{manager.userName} {manager.userSurname}'s Profile</h2>
        {statusMessage && <div className={`${messageType}-message`}>{statusMessage}</div>}

        <form className="profile-form" onSubmit={(e) => e.preventDefault()}>
          <div className="form-section">
            <div className="form-row">
              {[
                { label: 'First Name', name: 'userName', placeholder: 'Enter first name' },
                { label: 'Last Name', name: 'userSurname', placeholder: 'Enter last name' },
                { label: 'Employee Type', name: 'employeeType', disabled: true },
                { label: 'Hire Date', name: 'hireDate', type: 'date' },
                { label: 'Role Description', name: 'roleDescription', disabled: true }
              ].filter(item => userRole === 'CLIENT' ? item.name !== 'employeeType' && item.name !== 'hireDate' : true).map(({ label, name, placeholder, disabled, type = 'text' }) => (
                <div className="form-group" key={name}>
                  <label>{label}</label>
                  <input
                    type={type}
                    name={name}
                    value={manager[name]}
                    onChange={handleChange}
                    disabled={!isEditing || disabled}
                    placeholder={placeholder}
                  />
                </div>
              ))}
            </div>
          </div>

          <div className="form-section">
            <h3>Contact Details</h3>
            <div className="form-row">
              <div className="form-group">
                <label>Phone Number</label>
                <input
                  type="tel"
                  name="phoneNumber"
                  value={manager.contact.phoneNumber}
                  onChange={handleContactChange}
                  disabled={!isEditing}
                  placeholder="Enter phone number"
                />
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>Address Details</h3>
            <div className="form-row">
              <div className="form-group">
                <label>Street Number</label>
                <input
                  type="text"
                  name="streetNumber"
                  value={manager.address.streetNumber}
                  onChange={handleAddressChange}
                  disabled={!isEditing}
                  placeholder="Enter street number"
                />
              </div>
              <div className="form-group">
                <label>Street Name</label>
                <input
                  type="text"
                  name="streetName"
                  value={manager.address.streetName}
                  onChange={handleAddressChange}
                  disabled={!isEditing}
                  placeholder="Enter street name"
                />
              </div>
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>City</label>
                <input
                  type="text"
                  name="city"
                  value={manager.address.city}
                  onChange={handleAddressChange}
                  disabled={!isEditing}
                  placeholder="Enter city"
                />
              </div>
              <div className="form-group">
                <label>Postal Code</label>
                <input
                  type="text"
                  name="postalCode"
                  value={manager.address.postalCode}
                  onChange={handleAddressChange}
                  disabled={!isEditing}
                  placeholder="Enter postal code"
                />
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>Login Details</h3>
            <div className="form-row">
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  name="emailAddress"
                  value={manager.login.emailAddress}
                  onChange={handleLoginChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  name="password"
                  value={manager.login.password}
                  onChange={handleLoginChange}
                  disabled={!isEditing}
                />
              </div>
            </div>
          </div>

          <div className="form-actions">
            {!isEditing ? (
              <>
                <button type="button" className="btn btn-primary" onClick={handleEdit}>Edit</button>
                <button type="button" className="btn btn-danger" onClick={handleDelete}>Delete</button>
              </>
            ) : (
              <>
                <button type="button" className="btn btn-primary" onClick={handleUpdate}>Update</button>
                <button type="button" className="btn btn-secondary" onClick={handleCancel}>Cancel</button>
              </>
            )}
          </div>
        </form>
      </div>

      {showDeleteConfirm && (
        <div className="modal-overlay">
          <div className="modal">
            <h4>Are you sure you want to delete this profile?</h4>
            <div className="modal-buttons">
              <button className="danger-btn" onClick={confirmDelete}>Yes, Delete</button>
              <button className="secondary-btn" onClick={cancelDelete}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ProfilePage;
