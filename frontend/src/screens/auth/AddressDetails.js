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

const AddressDetails = () => {
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

    const [showModal, setShowModal] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        let updatedValue = value;

        // Validate street number and postal code to allow only numerical values
        if (name === 'streetNumber' || name === 'postalCode') {
            updatedValue = value.replace(/\D/g, ''); // Remove non-numeric characters
        }

        setAddress({ ...address, [name]: updatedValue });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!managerData) {
            alert('Manager data is missing. Please complete the previous form first.');
            navigate('/manager-form');
            return;
        }

        // Show the confirmation modal instead of proceeding directly
        setShowModal(true);
    };

    const handleConfirm = async () => {
        setShowModal(false);

        let endpoint;
        let fullData;
        let successRedirect = '/login';

        if (managerData.roleDescription === 'CLIENT') {
            endpoint = 'http://localhost:8080/mobileglow/api/customers/create';
            fullData = {
                userName: managerData.userName,
                userSurname: managerData.userSurname,
                customerDOB: managerData.customerDOB || "",
                isActive: managerData.isActive,
                roleDescription: 'CLIENT',
        contact: {
            phoneNumber: managerData.contact?.phoneNumber || ""
        },
                address: {
                    streetNumber: address.streetNumber,
                    streetName: address.streetName,
                    city: address.city,
                    postalCode: address.postalCode,
                },
                login: {
                    emailAddress: managerData.login?.emailAddress || "",
                    password: managerData.login?.password || ""
                }
            };
            successRedirect = '/LandingCustomer';
        } else if (managerData.roleDescription === 'EMPLOYEE') {
            if (managerData.employeeType === 'Manager') {
                endpoint = 'http://localhost:8080/mobileglow/Manager/create';
                fullData = {
                    userName: managerData.userName,
                    userSurname: managerData.userSurname,
                    isActive: managerData.isActive,
                    roleDescription: 'EMPLOYEE',
                    employeeType: 'Manager',
                    hireDate: managerData.hireDate,
                    contact: {
                        phoneNumber: managerData.contact?.phoneNumber || ""
                    },
                    address: {
                        streetNumber: address.streetNumber,
                        streetName: address.streetName,
                        city: address.city,
                        postalCode: address.postalCode,
                    },
                    login: {
                        emailAddress: managerData.login?.emailAddress || "",
                        password: managerData.login?.password || ""
                    }
                };
            } else if (managerData.employeeType === 'Accountant') {
                endpoint = 'http://localhost:8080/mobileglow/Accountant/create';
                fullData = {
                    userName: managerData.userName,
                    userSurname: managerData.userSurname,
                    isActive: managerData.isActive,
                    roleDescription: 'EMPLOYEE',
                    employeeType: 'Accountant',
                    hireDate: managerData.hireDate,
                    contact: {
                        phoneNumber: managerData.contact?.phoneNumber || ""
                    },
                    address: {
                        streetNumber: address.streetNumber,
                        streetName: address.streetName,
                        city: address.city,
                        postalCode: address.postalCode,
                    },
                    login: {
                        emailAddress: managerData.login?.emailAddress || "",
                        password: managerData.login?.password || ""
                    }
                };
            } else if (managerData.employeeType === 'WashAttendant') {
                endpoint = 'http://localhost:8080/mobileglow/wash-attendants/create';
                fullData = {
                    userName: managerData.userName,
                    userSurname: managerData.userSurname,
                    isActive: managerData.isActive,
                    roleDescription: 'EMPLOYEE',
                    employeeType: 'WashAttendant',
                    isFullTime: true,
                    shiftHours: 8,
                    contact: {
                        phoneNumber: managerData.contact?.phoneNumber || ""
                    },
                    address: {
                        streetNumber: address.streetNumber,
                        streetName: address.streetName,
                        city: address.city,
                        postalCode: address.postalCode,
                    },
                    login: {
                        emailAddress: managerData.login?.emailAddress || "",
                        password: managerData.login?.password || ""
                    }
                };
            } else {
                // Default to Manager if None or unknown
                endpoint = 'http://localhost:8080/mobileglow/Manager/create';
                fullData = {
                    userName: managerData.userName,
                    userSurname: managerData.userSurname,
                    isActive: managerData.isActive,
                    roleDescription: 'EMPLOYEE',
                    employeeType: 'Manager',
                    hireDate: managerData.hireDate,
                    contact: {
                        phoneNumber: managerData.contact?.phoneNumber || "",
                        emailAddress: managerData.login?.emailAddress || ""
                    },
                    address: {
                        streetNumber: address.streetNumber,
                        streetName: address.streetName,
                        city: address.city,
                        postalCode: address.postalCode,
                    },
                    login: {
                        emailAddress: managerData.login?.emailAddress || "",
                        password: managerData.login?.password || ""
                    }
                };
            }
        } else {
            alert('Invalid role description');
            return;
        }

        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(fullData),
            });

            if (response.ok) {
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

            {/* Modal */}
            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <p>By creating an account you are allowing our terms and conditions</p>
                        <div className="modal-buttons">
                            <button className="confirm-btn" onClick={handleConfirm}>
                                Confirm
                            </button>
                            <button className="cancel-btn" onClick={() => setShowModal(false)}>
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}

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
          background: linear-gradient(90deg, #1976d2, #42a5f5);
          color: #fff;
          padding: 14px;
          font-size: 16px;
          font-weight: 600;
          border: none;
          border-radius: 10px;
          cursor: pointer;
          transition: background 0.3s ease;
        }

        .submit-btn:hover {
          background: linear-gradient(90deg, #1565c0, #1e88e5);
        }

        .button-group {
          display: flex;
          gap: 10px;
          margin-top: 20px;
        }

        .modal-overlay {
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.5);
          display: flex;
          justify-content: center;
          align-items: center;
          z-index: 1000;
        }

        .modal-content {
          padding: 20px 30px;
          border-radius: 10px;
          box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
          text-align: center;
          font-size: 16px;
          font-weight: 500;
          max-width: 400px;
          width: 90%;
          background: #fff;
          color: #333;
        }

        .modal-buttons {
          display: flex;
          gap: 10px;
          justify-content: center;
          margin-top: 20px;
        }

        .confirm-btn {
          background: #fff;
          color: #1976d2;
          padding: 10px 20px;
          font-size: 16px;
          font-weight: 600;
          border: 1px solid #1976d2;
          border-radius: 10px;
          cursor: pointer;
          transition: all 0.3s ease;
        }

        .confirm-btn:hover {
          background: #e3f2fd;
        }

        .cancel-btn {
          background: #fff;
          color: #d32f2f;
          padding: 10px 20px;
          font-size: 16px;
          font-weight: 600;
          border: 1px solid #d32f2f;
          border-radius: 10px;
          cursor: pointer;
          transition: all 0.3s ease;
        }

        .cancel-btn:hover {
          background: #fdecea;
        }

        @media (max-width: 480px) {
          .button-group {
            flex-direction: column;
          }

          .modal-buttons {
            flex-direction: column;
          }
        }
      `}</style>
        </>
    );
};

export default AddressDetails;
