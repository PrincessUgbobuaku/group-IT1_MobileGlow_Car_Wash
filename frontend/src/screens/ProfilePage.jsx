import React, { useState, useEffect } from 'react';
import { profileService } from '../services/profileService';
import LoadingSpinner from '../components/LoadingSpinner';
import './ProfilePage.css';

const ProfilePage = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [formData, setFormData] = useState({
        userName: '',
        userSurname: '',
        isActive: true,
        roleDescription: 'CLIENT',
        customerDOB: '',
        login: {
            loginID: '',
            username: '',
            password: ''
        },
        address: {
            addressID: '',
            streetNumber: '',
            streetName: '',
            suburb: '',
            city: '',
            postalCode: ''
        },
        contact: {
            contactID: '',
            phoneNumber: '',
            emailAddress: ''
        }
    });

    const roleDescriptions = ['CLIENT', 'EMPLOYEE'];

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        try {
            setLoading(true);
            const data = await profileService.getAllCustomers();
            setCustomers(data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch customer accounts');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;

        if (name.includes('.')) {
            const [section, field] = name.split('.');
            setFormData(prev => ({
                ...prev,
                [section]: {
                    ...prev[section],
                    [field]: value
                }
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                [name]: name === 'isActive' ? value === 'true' : value
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingCustomer) {
                await profileService.updateCustomer(editingCustomer.userId, formData);
            } else {
                await profileService.createCustomer(formData);
            }
            fetchCustomers();
            resetForm();
        } catch (err) {
            setError('Failed to save customer account');
            console.error(err);
        }
    };

    const handleEdit = (customer) => {
        setEditingCustomer(customer);
        setFormData({
            userName: customer.userName || '',
            userSurname: customer.userSurname || '',
            isActive: customer.isActive !== undefined ? customer.isActive : true,
            roleDescription: customer.roleDescription || 'CLIENT',
            customerDOB: customer.customerDOB || '',
            login: {
                loginID: customer.login?.loginID || '',
                username: customer.login?.username || '',
                password: customer.login?.password || ''
            },
            address: {
                addressID: customer.address?.addressID || '',
                streetNumber: customer.address?.streetNumber || '',
                streetName: customer.address?.streetName || '',
                suburb: customer.address?.suburb || '',
                city: customer.address?.city || '',
                postalCode: customer.address?.postalCode || ''
            },
            contact: {
                contactID: customer.contact?.contactID || '',
                phoneNumber: customer.contact?.phoneNumber || '',
                emailAddress: customer.contact?.emailAddress || ''
            }
        });
        setShowForm(true);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this customer account? This action cannot be undone.')) {
            try {
                await profileService.deleteCustomer(id);
                fetchCustomers();
            } catch (err) {
                setError('Failed to delete customer account');
                console.error(err);
            }
        }
    };

    const resetForm = () => {
        setFormData({
            userName: '',
            userSurname: '',
            isActive: true,
            roleDescription: 'CLIENT',
            customerDOB: '',
            login: {
                loginID: '',
                username: '',
                password: ''
            },
            address: {
                addressID: '',
                streetNumber: '',
                streetName: '',
                suburb: '',
                city: '',
                postalCode: ''
            },
            contact: {
                contactID: '',
                phoneNumber: '',
                emailAddress: ''
            }
        });
        setEditingCustomer(null);
        setShowForm(false);
    };

    if (loading) {
        return <LoadingSpinner text="Loading customer accounts..." />;
    }

    return (
        <div className="profile-page">
            <div className="page-header">
                <h1>Customer Account Management</h1>
                <button
                    className="btn btn-primary"
                    onClick={() => setShowForm(true)}
                >
                    Create New Account
                </button>
            </div>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError(null)}>×</button>
                </div>
            )}

            {showForm && (
                <div className="form-container">
                    <h2>{editingCustomer ? 'Edit Customer Account' : 'Create New Customer Account'}</h2>
                    <form onSubmit={handleSubmit} className="profile-form">
                        <div className="form-section">
                            <h3>Basic Information</h3>
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="userName">First Name *</label>
                                    <input
                                        type="text"
                                        id="userName"
                                        name="userName"
                                        value={formData.userName}
                                        onChange={handleInputChange}
                                        required
                                        placeholder="Enter first name"
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="userSurname">Last Name *</label>
                                    <input
                                        type="text"
                                        id="userSurname"
                                        name="userSurname"
                                        value={formData.userSurname}
                                        onChange={handleInputChange}
                                        required
                                        placeholder="Enter last name"
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="customerDOB">Date of Birth</label>
                                    <input
                                        type="date"
                                        id="customerDOB"
                                        name="customerDOB"
                                        value={formData.customerDOB}
                                        onChange={handleInputChange}
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="roleDescription">Role</label>
                                    <select
                                        id="roleDescription"
                                        name="roleDescription"
                                        value={formData.roleDescription}
                                        onChange={handleInputChange}
                                    >
                                        {roleDescriptions.map(role => (
                                            <option key={role} value={role}>{role}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="isActive">Account Status</label>
                                <select
                                    id="isActive"
                                    name="isActive"
                                    value={formData.isActive.toString()}
                                    onChange={handleInputChange}
                                >
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
                                </select>
                            </div>
                        </div>

                        <div className="form-section">
                            <h3>Login Information</h3>
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="login.loginID">Login ID</label>
                                    <input
                                        type="text"
                                        id="login.loginID"
                                        name="login.loginID"
                                        value={formData.login.loginID}
                                        onChange={handleInputChange}
                                        placeholder="Enter login ID"
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="login.username">Username</label>
                                    <input
                                        type="text"
                                        id="login.username"
                                        name="login.username"
                                        value={formData.login.username}
                                        onChange={handleInputChange}
                                        placeholder="Enter username"
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="login.password">Password</label>
                                <input
                                    type="password"
                                    id="login.password"
                                    name="login.password"
                                    value={formData.login.password}
                                    onChange={handleInputChange}
                                    placeholder="Enter password"
                                />
                            </div>
                        </div>

                        <div className="form-section">
                            <h3>Contact Information</h3>
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="contact.phoneNumber">Phone Number</label>
                                    <input
                                        type="tel"
                                        id="contact.phoneNumber"
                                        name="contact.phoneNumber"
                                        value={formData.contact.phoneNumber}
                                        onChange={handleInputChange}
                                        placeholder="Enter phone number"
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="contact.emailAddress">Email Address</label>
                                    <input
                                        type="email"
                                        id="contact.emailAddress"
                                        name="contact.emailAddress"
                                        value={formData.contact.emailAddress}
                                        onChange={handleInputChange}
                                        placeholder="Enter email address"
                                    />
                                </div>
                            </div>
                        </div>

                        <div className="form-section">
                            <h3>Address Information</h3>
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="address.streetNumber">Street Number</label>
                                    <input
                                        type="text"
                                        id="address.streetNumber"
                                        name="address.streetNumber"
                                        value={formData.address.streetNumber}
                                        onChange={handleInputChange}
                                        placeholder="Enter street number"
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="address.streetName">Street Name</label>
                                    <input
                                        type="text"
                                        id="address.streetName"
                                        name="address.streetName"
                                        value={formData.address.streetName}
                                        onChange={handleInputChange}
                                        placeholder="Enter street name"
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="address.suburb">Suburb</label>
                                    <input
                                        type="text"
                                        id="address.suburb"
                                        name="address.suburb"
                                        value={formData.address.suburb}
                                        onChange={handleInputChange}
                                        placeholder="Enter suburb"
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="address.city">City</label>
                                    <input
                                        type="text"
                                        id="address.city"
                                        name="address.city"
                                        value={formData.address.city}
                                        onChange={handleInputChange}
                                        placeholder="Enter city"
                                    />
                                </div>
                            </div>

                            <div className="form-group">
                                <label htmlFor="address.postalCode">Postal Code</label>
                                <input
                                    type="text"
                                    id="address.postalCode"
                                    name="address.postalCode"
                                    value={formData.address.postalCode}
                                    onChange={handleInputChange}
                                    placeholder="Enter postal code"
                                />
                            </div>
                        </div>

                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary">
                                {editingCustomer ? 'Update Account' : 'Create Account'}
                            </button>
                            <button
                                type="button"
                                className="btn btn-secondary"
                                onClick={resetForm}
                            >
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            )}

            <div className="profiles-list">
                <h2>Customer Accounts ({customers.length})</h2>
                {customers.length === 0 ? (
                    <p className="no-data">No customer accounts found. Create your first account above.</p>
                ) : (
                    <div className="table-container">
                        <table className="profiles-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Status</th>
                                <th>Role</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>City</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {customers.map((customer) => (
                                <tr key={customer.userId}>
                                    <td>{customer.userId}</td>
                                    <td>{`${customer.userName || ''} ${customer.userSurname || ''}`.trim() || 'N/A'}</td>
                                    <td>
                      <span className={`status ${customer.isActive ? 'active' : 'inactive'}`}>
                        {customer.isActive ? 'Active' : 'Inactive'}
                      </span>
                                    </td>
                                    <td>{customer.roleDescription || 'N/A'}</td>
                                    <td>{customer.contact?.phoneNumber || 'N/A'}</td>
                                    <td>{customer.contact?.emailAddress || 'N/A'}</td>
                                    <td>{customer.address?.city || 'N/A'}</td>
                                    <td>
                                        <button
                                            className="btn btn-small btn-secondary"
                                            onClick={() => handleEdit(customer)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="btn btn-small btn-danger"
                                            onClick={() => handleDelete(customer.userId)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;
