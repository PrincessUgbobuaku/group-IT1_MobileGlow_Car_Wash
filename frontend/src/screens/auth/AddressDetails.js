import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const styles = {
    pageContainer: {
        display: 'flex',
        height: '100vh',
        fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    },
    leftContainer: {
        flex: 1,
        padding: '20px 30px 40px 30px',
        display: 'flex',
        alignItems: 'flex-start',
        justifyContent: 'center',
        overflowY: 'auto',
        maxHeight: 'calc(100vh - 70px)', // Adjust based on navbar height
    },
    rightContainer: {
        flex: 1,
        overflow: 'hidden',
    },
    image: {
        width: '100%',
        height: '100%',
        objectFit: 'cover',
    },
};

const AddressForm = () => {
    const navigate = useNavigate();
    const location = useLocation();
    // Get manager data passed from ManagerForm
    const managerData = location.state?.manager || null;

    const [address, setAddress] = useState({
        streetNumber: '',
        streetName: '',
        city: '',
        postalCode: '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setAddress({ ...address, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!managerData) {
            alert('Manager data is missing. Please complete the previous form first.');
            navigate('/manager-form');
            return;
        }

        // Combine manager and address data for final submission
        const fullData = {
            userName: managerData.userName,
            userSurname: managerData.userSurname,
            isActive: managerData.isActive,
            roleDescription: managerData.employeeType === 'washAttendent' ? 'employee' : managerData.roleDescription,
            employeeType: managerData.employeeType,
            contact: {
                phoneNumber: managerData.contact?.phoneNumber || ''
            },
            address: {
                streetNumber: address.streetNumber,
                streetName: address.streetName,
                city: address.city,
                postalCode: address.postalCode,
            },
            login: {
                emailAddress: managerData.login?.emailAddress || '',
                password: managerData.login?.password || ''
            }
        };

        try {
            // Assuming backend endpoint to create washAttendat with address is the same as before
            const response = await fetch('http://localhost:8080/mobileglow/washAttendant/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(fullData),
            });

            if (response.ok) {
                alert('Account created successfully!');
                navigate('/login');
            } else {
                alert('Failed to create account.');
            }
        } catch (error) {
            console.error('Error creating account:', error);
            alert('Something went wrong!');
        }
    };

    return (
        <>
            <div style={styles.pageContainer}>
                <div style={styles.leftContainer}>
                    <form
                        onSubmit={handleSubmit}
                        className="address-form"
                        style={{
                            width: '100%',
                            maxWidth: 550,
                            padding: 30,
                            background: '#ffffff',
                            borderRadius: 15,
                            boxShadow: '0 8px 25px rgba(0,0,0,0.08)',
                        }}
                    >
                        <h2 style={{ textAlign: 'center', marginBottom: 20 }}>Enter Address Details</h2>

                        <div className="form-group">
                            <label>Street Number</label>
                            <input
                                type="text"
                                name="streetNumber"
                                value={address.streetNumber}
                                onChange={handleChange}
                                placeholder="Enter street number"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Street Name</label>
                            <input
                                type="text"
                                name="streetName"
                                value={address.streetName}
                                onChange={handleChange}
                                placeholder="Enter street name"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>City</label>
                            <input
                                type="text"
                                name="city"
                                value={address.city}
                                onChange={handleChange}
                                placeholder="Enter city"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Postal Code</label>
                            <input
                                type="text"
                                name="postalCode"
                                value={address.postalCode}
                                onChange={handleChange}
                                placeholder="Enter postal code"
                                required
                            />
                        </div>

                        <div className="button-group" style={{ marginTop: 20 }}>
                            <button type="submit" className="submit-btn">
                                Create Account
                            </button>
                        </div>
                    </form>
                </div>
                <div style={styles.rightContainer}>
                    <img
                        src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80"
                        alt="Car wash"
                        style={styles.image}
                    />
                </div>
            </div>

            <style>{`
        h2 {
          text-align: center;
          color: #333;
          margin-bottom: 20px;
        }

        .address-form .form-group {
          display: flex;
          flex-direction: column;
          margin-bottom: 18px;
        }

        .address-form label {
          margin-bottom: 6px;
          font-weight: 600;
          color: #444;
        }

        .address-form input {
          padding: 12px;
          border-radius: 8px;
          border: 1px solid #ccc;
          font-size: 14px;
          transition: all 0.3s ease;
        }

        .address-form input:focus {
          border: 1px solid #4CAF50;
          box-shadow: 0 0 6px rgba(76,175,80,0.2);
          outline: none;
        }

        .submit-btn {
          flex: 1;
          background: #4CAF50;
          color: white;
          padding: 14px;
          font-size: 16px;
          font-weight: 600;
          border: none;
          border-radius: 10px;
          cursor: pointer;
          transition: background 0.3s ease;
        }

        .submit-btn:hover {
          background: #45a049;
        }

        .button-group {
          display: flex;
          gap: 10px;
          margin-top: 20px;
        }

        @media (max-width: 480px) {
          .button-group {
            flex-direction: column;
          }
        }
      `}</style>
        </>
    );
};

export default AddressForm;
