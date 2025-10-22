import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ProfilePage.css';

const ProfilePage = () => {
  const [client, setClient] = useState({
    userName: '',
    userSurname: '',
    contact: { phoneNumber: '' },
    address: { streetNumber: '', streetName: '', city: '', postalCode: '' },
    login: { emailAddress: '' }
  });
  const [loading, setLoading] = useState(true);
  const [statusMessage, setStatusMessage] = useState('');
  const [messageType, setMessageType] = useState('error');
  const [imageUrl, setImageUrl] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const navigate = useNavigate();

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
          const token = localStorage.getItem('authToken');
          const response = await fetch('http://localhost:8080/mobileglow/api/customers', {
            headers: {
              'Authorization': token ? `Bearer ${token}` : '',
            },
          });
          if (response.ok) {
            users = await response.json();
          } else {
            setStatusMessage('Failed to fetch user data');
            setMessageType('error');
            return;
          }
        } else {
          const endpointTypes = [
            { url: 'http://localhost:8080/mobileglow/Manager/getAllManagers', type: 'Manager' },
            { url: 'http://localhost:8080/mobileglow/Accountant/getAllAccountants', type: 'Accountant' },
            { url: 'http://localhost:8080/mobileglow/wash-attendants/getAllWashAttendants', type: 'WashAttendant' }
          ];
          for (const { url, type } of endpointTypes) {
            try {
              const response = await fetch(url);
              if (response.ok) {
                const data = await response.json();
                users = users.concat(data.map(u => ({ ...u, employeeType: type })));
              }
            } catch (error) {
              console.error('Error fetching from', url, error);
            }
          }
        }
        // const loggedInUser = users.find(u => u.login.emailAddress === userEmail);
        const loggedInUser = users.find(u => u?.login?.emailAddress === userEmail);

        if (loggedInUser) {
          setClient(loggedInUser);
          // Fetch image if available
          if (loggedInUser.imageName) {
            let imageEndpoint = '';
            if (userRole === 'CLIENT') {
              imageEndpoint = `http://localhost:8080/mobileglow/api/customers/image/${loggedInUser.userId}`;
            } else {
              const type = loggedInUser.employeeType;
              if (type === 'Manager') {
                imageEndpoint = `http://localhost:8080/mobileglow/Manager/image/${loggedInUser.userId}`;
              } else if (type === 'Accountant') {
                imageEndpoint = `http://localhost:8080/mobileglow/Accountant/image/${loggedInUser.userId}`;
              } else if (type === 'WashAttendant') {
                imageEndpoint = `http://localhost:8080/mobileglow/wash-attendants/image/${loggedInUser.userId}`;
              }
            }
            if (imageEndpoint) {
              fetch(imageEndpoint)
                .then(response => response.blob())
                .then(blob => {
                  const url = URL.createObjectURL(blob);
                  setImageUrl(url);
                })
                .catch(error => console.error('Error fetching image:', error));
            }
          }
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

  const userRole = localStorage.getItem('userRoleDescription');

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner"></div>
        <h2>Loading Profile...</h2>
      </div>
    );
  }

  const initials = client.userName ? client.userName.charAt(0).toUpperCase() : '?';

  return (
    <div className="profile-wrapper">
      <button
        onClick={() => navigate(userRole === 'CLIENT' ? '/LandingCustomer' : '/LandingEmployee')}
        className="back-btn"
      >
        ←
      </button>

      <h2 className="profile-title">Profile</h2>
      {statusMessage && <div className={`${messageType}-message`}>{statusMessage}</div>}

      <div className="profile-container">
        {/* LEFT SIDE - PROFILE CARD */}
        <div className="profile-card">
          <div className="avatar-section">
            {imageUrl ? (
              <img src={imageUrl} alt="Profile" className="avatar-image" onClick={() => setIsModalOpen(true)} />
            ) : (
              <div className="avatar-circle">{initials}</div>
            )}
            <button className="edit-link" onClick={() => navigate(userRole === 'CLIENT' ? '/EditCustomerProfile' : '/EditEmployeeProfile')}>
              Edit
            </button>
          </div>

          <h3 className="profile-name">
            {client.userName} {client.userSurname}
          </h3>

          <div className="profile-info">
            <div className="info-item">
              <span className="info-label">First name</span>
              <span>{client.userName}</span>
            </div>
            <div className="info-item">
              <span className="info-label">Last name</span>
              <span>{client.userSurname}</span>
            </div>
            {userRole === 'EMPLOYEE' && (
              <>
                <div className="info-item">
                  <span className="info-label">Employee Type</span>
                  <span>{client.employeeType || '—'}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Hire Date</span>
                  <span>{client.hireDate || '—'}</span>
                </div>
              </>
            )}
            {userRole === 'CLIENT' && (
              <div className="info-item">
                <span className="info-label">Date of Birth</span>
                <span>{client.customerDOB || '—'}</span>
              </div>
            )}
            <div className="info-item">
              <span className="info-label">Mobile number</span>
              <span>{client.contact.phoneNumber || '—'}</span>
            </div>
            <div className="info-item">
              <span className="info-label">Email</span>
              <span>{client.login.emailAddress}</span>
            </div>
          </div>
        </div>

        {/* RIGHT SIDE - ADDRESS CARD */}
        <div className="address-card">
          <h3>My addresses</h3>
          <div className="address-list">
            <div className="address-item">
              <div className="icon home-icon">🏠</div>
              <div>
                <strong>Home</strong>
                <p>
                  {client.address.streetNumber
                    ? `${client.address.streetNumber} ${client.address.streetName}, ${client.address.city}, ${client.address.postalCode}`
                    : 'Add a home address'}
                </p>
              </div>
            </div>

            <div className="address-item">
              <div className="icon work-icon">💼</div>
              <div>
                <strong>Work</strong>
                <p>Add a work address</p>
              </div>
            </div>
          </div>

          <button className="add-btn">+ Add</button>
        </div>
      </div>

      {/* Modal for full-size image */}
      {isModalOpen && (
        <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <img src={imageUrl} alt="Full Profile" className="full-image" />
            <button className="close-btn" onClick={() => setIsModalOpen(false)}>×</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
