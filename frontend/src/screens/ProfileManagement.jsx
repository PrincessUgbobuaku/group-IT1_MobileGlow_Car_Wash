import React, { useState, useEffect } from 'react';
import { customerService } from '../services/customerService';
import './ProfileManagement.css';

const ProfileManagement = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [editingCustomer, setEditingCustomer] = useState(null);
    const [showProfile, setShowProfile] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [filterActive, setFilterActive] = useState('all'); // 'all', 'active', 'inactive'
    const [successMessage, setSuccessMessage] = useState(null);

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        setLoading(true);
        try {
            const data = await customerService.getAllCustomers();
            setCustomers(data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch customers');
            console.error('Error:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (customer) => {
        setEditingCustomer({
            ...customer,
            editData: {
                userName: customer.userName || '',
                userSurname: customer.userSurname || '',
                customerDOB: customer.customerDOB || ''
            }
        });
    };

    const handleCancelEdit = () => {
        setEditingCustomer(null);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setEditingCustomer(prev => ({
            ...prev,
            editData: {
                ...prev.editData,
                [name]: value
            }
        }));
    };

    const handleSaveEdit = async () => {
        if (!editingCustomer) return;

        try {
            const updatedCustomer = await customerService.updateCustomer(
                editingCustomer.userId,
                editingCustomer.editData
            );

            // Update the local state
            setCustomers(prev => prev.map(c =>
                c.userId === editingCustomer.userId ? updatedCustomer : c
            ));

            setEditingCustomer(null);
            setError(null);
            setSuccessMessage('Customer profile updated successfully!');

            // Clear success message after 3 seconds
            setTimeout(() => setSuccessMessage(null), 3000);
        } catch (err) {
            setError('Failed to update customer');
            setSuccessMessage(null);
            console.error('Error:', err);
        }
    };

    const handleToggleStatus = async (customerId) => {
        try {
            const currentCustomer = customers.find(c => c.userId === customerId);
            console.log('Current customer before toggle:', currentCustomer);
            console.log('Current isActive value:', currentCustomer?.isActive);

            console.log('Toggling status for customer:', customerId);
            const updatedCustomer = await customerService.toggleCustomerStatus(customerId);
            console.log('Received updated customer:', updatedCustomer);
            console.log('Updated isActive value:', updatedCustomer?.isActive);

            // Update the local state
            setCustomers(prev => {
                const newCustomers = prev.map(c =>
                    c.userId === customerId ? updatedCustomer : c
                );
                console.log('Updated customers list:', newCustomers);
                return newCustomers;
            });

            // Close edit mode if this customer was being edited
            if (editingCustomer && editingCustomer.userId === customerId) {
                setEditingCustomer(null);
            }

            setError(null);
            setSuccessMessage('Customer status updated successfully!');

            // Clear success message after 3 seconds
            setTimeout(() => setSuccessMessage(null), 3000);
        } catch (err) {
            console.error('Error toggling status:', err);
            setError('Failed to update customer status');
            setSuccessMessage(null);
        }
    };

    const filteredCustomers = customers.filter(customer => {
        const matchesSearch = customer.userName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            customer.userSurname?.toLowerCase().includes(searchTerm.toLowerCase());

        if (filterActive === 'all') return matchesSearch;
        if (filterActive === 'active') return matchesSearch && customer.isActive;
        if (filterActive === 'inactive') return matchesSearch && !customer.isActive;

        return matchesSearch;
    });

    if (loading && customers.length === 0) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading customer profiles...</p>
            </div>
        );
    }

    return (
        <div className="profile-management">
            <div className="header">
                <h1>Profile Management</h1>
                <p>Edit customer profiles and manage account status</p>
            </div>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError(null)}>×</button>
                </div>
            )}

            {successMessage && (
                <div className="success-message">
                    {successMessage}
                    <button onClick={() => setSuccessMessage(null)}>×</button>
                </div>
            )}

            <div className="controls">
                <div className="search-filter">
                    <input
                        type="text"
                        placeholder="Search by name or surname..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="search-input"
                    />

                    <select
                        value={filterActive}
                        onChange={(e) => setFilterActive(e.target.value)}
                        className="filter-select"
                    >
                        <option value="all">All Customers</option>
                        <option value="active">Active Only</option>
                        <option value="inactive">Inactive Only</option>
                    </select>
                </div>
            </div>

            <div className="customers-grid">
                {filteredCustomers.map(customer => (
                    <div className="customer-card" key={customer.userId}>
                        <div className="customer-header">
                            <h3>{customer.userName} {customer.userSurname}</h3>
                            <span className={`status-badge ${customer.isActive ? 'active' : 'inactive'}`}>
                {customer.isActive ? 'Active' : 'Inactive'}
              </span>
                        </div>

                        <div className="customer-details">
                            {editingCustomer && editingCustomer.userId === customer.userId ? (
                                // Edit Mode
                                <div className="edit-form">
                                    <div className="form-group">
                                        <label>First Name:</label>
                                        <input
                                            type="text"
                                            name="userName"
                                            value={editingCustomer.editData.userName}
                                            onChange={handleInputChange}
                                            className="edit-input"
                                        />
                                    </div>
                                    <div className="form-group">
                                        <label>Last Name:</label>
                                        <input
                                            type="text"
                                            name="userSurname"
                                            value={editingCustomer.editData.userSurname}
                                            onChange={handleInputChange}
                                            className="edit-input"
                                        />
                                    </div>
                                    <div className="form-group">
                                        <label>Date of Birth:</label>
                                        <input
                                            type="date"
                                            name="customerDOB"
                                            value={editingCustomer.editData.customerDOB || ''}
                                            onChange={handleInputChange}
                                            className="edit-input"
                                        />
                                    </div>
                                </div>
                            ) : (
                                // View Mode
                                <div className="customer-info">
                                    <div className="info-item">
                                        <span className="label">ID:</span>
                                        <span className="value">{customer.userId}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="label">Role:</span>
                                        <span className="value">{customer.roleDescription || 'N/A'}</span>
                                    </div>
                                    {customer.customerDOB && (
                                        <div className="info-item">
                                            <span className="label">Date of Birth:</span>
                                            <span className="value">{new Date(customer.customerDOB).toLocaleDateString()}</span>
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>

                        <div className="customer-actions">
                            {editingCustomer && editingCustomer.userId === customer.userId ? (
                                // Edit Mode Actions
                                <>
                                    <button
                                        className="btn btn-success"
                                        onClick={handleSaveEdit}
                                    >
                                        Save
                                    </button>
                                    <button
                                        className="btn btn-secondary"
                                        onClick={handleCancelEdit}
                                    >
                                        Cancel
                                    </button>
                                </>
                            ) : (
                                // View Mode Actions
                                <>
                                    <button
                                        className="btn btn-outline"
                                        onClick={() => handleEdit(customer)}
                                    >
                                        Edit Profile
                                    </button>
                                    <button
                                        className={`btn ${customer.isActive ? 'btn-warning' : 'btn-success'}`}
                                        onClick={() => handleToggleStatus(customer.userId)}
                                    >
                                        {customer.isActive ? 'Deactivate' : 'Activate'}
                                    </button>
                                </>
                            )}
                        </div>
                    </div>
                ))}
            </div>

            {filteredCustomers.length === 0 && !loading && (
                <div className="empty-state">
                    <div className="empty-icon">👤</div>
                    <h3>No customers found</h3>
                    <p>{searchTerm ? `No customers match "${searchTerm}"` : 'No customers in the system'}</p>
                </div>
            )}
        </div>
    );
};

export default ProfileManagement;
