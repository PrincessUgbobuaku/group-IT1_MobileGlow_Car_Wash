import React, { useState, useEffect } from 'react';
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

const SignUp = () => {
    const location = useLocation();
    const selectedRole = location.state?.role || 'Customer';

    const [manager, setManager] = useState({
        userName: '',
        userSurname: '',
        customerDOB: '',
        isActive: true,
        roleDescription: selectedRole === 'Employee' ? 'EMPLOYEE' : 'CLIENT',
        employeeType: 'None',
        hireDate: '',
        contact: {
            phoneNumber: ''
        },
        login: {
            emailAddress: '',
            password: ''
        }
    });

    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        // Reset manager state to empty when component mounts or selectedRole changes
        setManager({
            userName: '',
            userSurname: '',
            customerDOB: '',
            isActive: true,
            roleDescription: selectedRole === 'Employee' ? 'EMPLOYEE' : 'CLIENT',
            employeeType: 'None',
            hireDate: '',
            contact: {
                phoneNumber: ''
            },
            login: {
                emailAddress: '',
                password: ''
            }
        });
        setIsEditing(false);
    }, [selectedRole]);
    const navigate = useNavigate();



    const handleChange = (e) => {
        const { name, value } = e.target;
        let updatedManager = { ...manager, [name]: value };
        if (name === 'employeeType' && value === 'washAttendent') {
            updatedManager.roleDescription = 'employee';
        }
        setManager(updatedManager);
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

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Instead of saving here, navigate to address page with manager data
        navigate('/AddressDetails', { state: { manager } });
    };

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this profile?')) {
            try {
                const response = await fetch(
                    `http://localhost:8080/mobileglow/Manager/delete/${manager.userId}`,
                    { method: 'DELETE' }
                );

                if (response.ok) {
                    alert('Profile deleted successfully!');
                    localStorage.clear();
                    navigate('/login');
                } else {
                    alert('Failed to delete profile');
                }
            } catch (error) {
                console.error('Error deleting manager:', error);
                alert('Something went wrong while deleting');
            }
        }
    };

    return (
        <>
            <div style={styles.pageContainer}>
                <div style={styles.leftContainer}>
                    <form onSubmit={handleSubmit} className="manager-form" style={{ width: '100%', maxWidth: 550, padding: 30, background: '#ffffff', borderRadius: 15, boxShadow: '0 8px 25px rgba(0,0,0,0.08)' }}>
                        <h2 style={{ textAlign: 'center', marginBottom: 20 }}>{isEditing ? 'Edit Manager Profile' : 'Create Account'}</h2>
                        <div className="form-group">
                            <label>First Name</label>
                            <input
                                type="text"
                                name="userName"
                                value={manager.userName}
                                onChange={handleChange}
                                placeholder="Enter first name"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Last Name</label>
                            <input
                                type="text"
                                name="userSurname"
                                value={manager.userSurname}
                                onChange={handleChange}
                                placeholder="Enter last name"
                                required
                            />
                        </div>

                        {selectedRole === 'Employee' && (
                            <>
                                <div className="form-group">
                                    <label>Employee Type</label>
                                    <select
                                        name="employeeType"
                                        value={manager.employeeType}
                                        onChange={handleChange}
                                        required
                                    >
                                        <option value="None">None</option>
                                        <option value="Accountant">Accountant</option>
                                        <option value="Manager">Manager</option>
                                        <option value="WashAttendant">WashAttendant</option>
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label>Hire Date</label>
                                    <input
                                        type="date"
                                        name="hireDate"
                                        value={manager.hireDate}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </>
                        )}

                        <div className="form-group">
                            <label>Phone Number</label>
                            <input
                                type="tel"
                                name="phoneNumber"
                                value={manager.contact.phoneNumber}
                                onChange={handleContactChange}
                                placeholder="Enter phone number (e.g., 073...)"
                                required
                            />
                        </div>

                        <div className="form-group" style={{ marginTop: 20 }}>
                            <label>Email</label>
                            <input
                                type="email"
                                name="emailAddress"
                                value={manager.login.emailAddress}
                                onChange={handleLoginChange}
                                placeholder="Enter email address"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Password</label>
                            <input
                                type="password"
                                name="password"
                                value={manager.login.password}
                                onChange={handleLoginChange}
                                placeholder="Enter password"
                                required
                            />
                        </div>

                        <div className="button-group" style={{ marginTop: 20 }}>
                            <button type="submit" className="submit-btn">
                                {isEditing ? 'Update Profile' : 'Continue'}
                            </button>
                            {isEditing && (
                                <button type="button" className="delete-btn" onClick={handleDelete}>
                                    Delete Profile
                                </button>
                            )}
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
        h2, h3 {
          text-align: center;
          color: #333;
          margin-bottom: 20px;
        }

        .manager-form .form-group {
          display: flex;
          flex-direction: column;
          margin-bottom: 18px;
        }

        .manager-form label {
          margin-bottom: 6px;
          font-weight: 600;
          color: #444;
        }

        .manager-form input {
          padding: 12px;
          border-radius: 8px;
          border: 1px solid #ccc;
          font-size: 14px;
          transition: all 0.3s ease;
        }

        .manager-form input:focus {
          border: 1px solid #4CAF50;
          box-shadow: 0 0 6px rgba(76,175,80,0.2);
          outline: none;
        }

        .radio-group {
          display: flex;
          gap: 15px;
          margin-top: 6px;
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

        .delete-btn {
          flex: 1;
          background: #fff;
          color: #d32f2f;
          padding: 14px;
          font-size: 16px;
          font-weight: 600;
          border: 1px solid #d32f2f;
          border-radius: 10px;
          cursor: pointer;
          transition: all 0.3s ease;
        }

        .delete-btn:hover {
          background: #fdecea;
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

export default SignUp;


