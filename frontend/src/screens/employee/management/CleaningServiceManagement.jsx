import React, { useState, useEffect } from 'react';
import { apiClient } from '../../../services/api';
import './CleaningServiceManagement.css';
import NavbarEmployee from "../../components/NavbarEmployee";

// Cleaning Service API functions
const CLEANING_SERVICE_API = '/api/cleaningservice';

const cleaningServiceService = {
    // Get all cleaning services
    getAllCleaningServices: async () => {
        try {
            console.log('Fetching all cleaning services...');
            const response = await apiClient.get(`${CLEANING_SERVICE_API}/getAll`);
            console.log('Cleaning services fetched:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error fetching cleaning services:', error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            throw error;
        }
    },

    // Get cleaning service by ID
    getCleaningServiceById: async (id) => {
        try {
            console.log(`Fetching cleaning service with ID: ${id}`);
            const response = await apiClient.get(`${CLEANING_SERVICE_API}/read/${id}`);
            console.log('Cleaning service fetched:', response.data);
            return response.data;
        } catch (error) {
            console.error(`Error fetching cleaning service ${id}:`, error);
            throw error;
        }
    },

    // Create new cleaning service
    createCleaningService: async (serviceData) => {
        try {
            console.log('Creating cleaning service with data:', serviceData);
            const response = await apiClient.post(`${CLEANING_SERVICE_API}/create`, serviceData);
            console.log('Cleaning service created:', response.data);
            return { success: true, data: response.data };
        } catch (error) {
            console.error('Error creating cleaning service:', error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            if (error.response?.status === 400 && error.response?.data?.message?.includes('already exists')) {
                return { success: false, error: 'DUPLICATE', message: 'Service already exists with this name' };
            }
            throw error;
        }
    },

    // Update cleaning service
    updateCleaningService: async (id, serviceData) => {
        try {
            console.log(`Updating cleaning service with ID: ${id}`, serviceData);
            const response = await apiClient.put(`${CLEANING_SERVICE_API}/update/${id}`, serviceData);
            console.log('Cleaning service updated:', response.data);
            return response.data;
        } catch (error) {
            console.error(`Error updating cleaning service ${id}:`, error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            throw error;
        }
    },

    // Delete cleaning service
    deleteCleaningService: async (id) => {
        try {
            console.log(`Deleting cleaning service with ID: ${id}`);
            const response = await apiClient.delete(`${CLEANING_SERVICE_API}/delete/${id}`);
            console.log('Delete response status:', response.status);
            return response.status === 204; // Success if no content
        } catch (error) {
            console.error(`Error deleting cleaning service ${id}:`, error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            throw error;
        }
    }
};

const CleaningServiceManagement = () => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [editingService, setEditingService] = useState(null);
    const [validationPopup, setValidationPopup] = useState({ show: false, message: '' });
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedServices, setSelectedServices] = useState(new Set());
    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const [deleteTarget, setDeleteTarget] = useState(null);
    const [deleteType, setDeleteType] = useState('single'); // 'single' or 'bulk'
    const [serviceDescriptions, setServiceDescriptions] = useState(new Map()); // Local storage for descriptions
    const [formData, setFormData] = useState({
        serviceName: '',
        category: '',
        priceOfService: '',
        duration: '',
        description: ''
    });

    // Authentication helper function
    const getAuthToken = () => {
        return localStorage.getItem('authToken') || localStorage.getItem('token');
    };

    useEffect(() => {
        // Check if user is authenticated
        const token = getAuthToken();
        console.log('Auth token status:', token ? 'Present' : 'Missing');
        
        if (!token) {
            setError('Authentication required. Please log in first.');
            return;
        }
        
        fetchServices();
    }, []);

    const fetchServices = async () => {
        setLoading(true);
        try {
            const data = await cleaningServiceService.getAllCleaningServices();
            setServices(data);
            setError(null);
        } catch (err) {
            console.error('Error fetching services:', err);
            if (err.response?.status === 401) {
                setError('Authentication failed. Please log in again.');
            } else {
                setError(err.response?.data?.message || 'Failed to fetch services. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const resetForm = () => {
        setFormData({
            serviceName: '',
            category: '',
            priceOfService: '',
            duration: '',
            description: ''
        });
        setEditingService(null);
        setShowForm(false);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            // Frontend duplicate check
            if (!editingService) {
                const isDuplicate = services.some(service =>
                    service.serviceName.toLowerCase() === formData.serviceName.toLowerCase()
                );

                if (isDuplicate) {
                    setValidationPopup({
                        show: true,
                        message: 'Service already exists with this name'
                    });
                    resetForm();
                    setLoading(false);
                    return;
                }
            }

            // Convert string values to proper types for backend
            const { description, ...backendData } = formData; // Exclude description from backend data
            const processedData = {
                ...backendData,
                priceOfService: parseFloat(formData.priceOfService),
                duration: parseFloat(formData.duration)
            };

            // Validate duration is within limits
            if (processedData.duration < 0.5 || processedData.duration > 5) {
                setValidationPopup({
                    show: true,
                    message: 'Duration must be between 0.5 and 5 hours'
                });
                setLoading(false);
                return;
            }

            if (editingService) {
                // Update existing service
                await cleaningServiceService.updateCleaningService(editingService.cleaningServiceId, processedData);

                // Save description locally
                if (description.trim()) {
                    setServiceDescriptions(prev => new Map(prev).set(editingService.cleaningServiceId, description));
                }

                setError(null);
                resetForm();
                fetchServices(); // Refresh the list
            } else {
                // Create new service
                const result = await cleaningServiceService.createCleaningService(processedData);

                if (result.success) {
                    // Save description locally for the new service
                    if (description.trim() && result.data?.cleaningServiceId) {
                        setServiceDescriptions(prev => new Map(prev).set(result.data.cleaningServiceId, description));
                    }

                    setError(null);
                    resetForm();
                    fetchServices(); // Refresh the list
                } else if (result.error === 'DUPLICATE') {
                    // Show duplicate error popup
                    setValidationPopup({ show: true, message: result.message });
                    resetForm();
                } else {
                    setError('Failed to create service');
                }
            }
        } catch (err) {
            console.error('Error in handleSubmit:', err);
            if (err.response?.status === 401) {
                setError('Authentication failed. Please log in again.');
            } else {
                setError(err.response?.data?.message || (editingService ? 'Failed to update service' : 'Failed to create service'));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (service) => {
        setEditingService(service);
        const savedDescription = serviceDescriptions.get(service.cleaningServiceId);
        setFormData({
            serviceName: service.serviceName || '',
            category: service.category || '',
            priceOfService: service.priceOfService || '',
            duration: service.duration || '',
            description: savedDescription || 'A thorough top-to-bottom cleaning service that includes...'
        });
        setShowForm(true);
    };

    const handleDelete = async (id) => {
        setDeleteTarget(id);
        setDeleteType('single');
        setShowDeletePopup(true);
    };

    const confirmDelete = async () => {
        setLoading(true);
        try {
            if (deleteType === 'single') {
                await cleaningServiceService.deleteCleaningService(deleteTarget);
            } else if (deleteType === 'bulk') {
                const deletePromises = Array.from(selectedServices).map(id =>
                    cleaningServiceService.deleteCleaningService(id)
                );
                await Promise.all(deletePromises);
                setSelectedServices(new Set());
            }
            setError(null);
            fetchServices(); // Refresh the list
        } catch (err) {
            console.error('Error in confirmDelete:', err);
            if (err.response?.status === 401) {
                setError('Authentication failed. Please log in again.');
            } else {
                setError(err.response?.data?.message || (deleteType === 'single' ? 'Failed to delete service' : 'Failed to delete some services'));
            }
        } finally {
            setLoading(false);
            setShowDeletePopup(false);
            setDeleteTarget(null);
        }
    };

    // const toggleAvailability = async (service) => {
    //     // This functionality is not supported by the backend domain model
    //     // The isAvailable field doesn't exist in the CleaningService entity
    //     console.log('Availability toggle not supported by backend');
    // };

    // Search functionality
    const filteredServices = services.filter(service =>
        service.serviceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        service.category.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Handle search input
    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    // Handle checkbox selection
    const handleServiceSelect = (serviceId, checked) => {
        const newSelected = new Set(selectedServices);
        if (checked) {
            newSelected.add(serviceId);
        } else {
            newSelected.delete(serviceId);
        }
        setSelectedServices(newSelected);
    };

    // Handle select all checkbox
    const handleSelectAll = (checked) => {
        const newSelected = new Set(selectedServices);

        if (checked) {
            // Add all filtered services to selection
            filteredServices.forEach(service => {
                newSelected.add(service.cleaningServiceId);
            });
        } else {
            // Remove all filtered services from selection
            filteredServices.forEach(service => {
                newSelected.delete(service.cleaningServiceId);
            });
        }

        setSelectedServices(newSelected);
    };

    // Bulk delete selected services
    const handleBulkDelete = async () => {
        if (selectedServices.size === 0) {
            setError('Please select services to delete');
            return;
        }

        setDeleteTarget(selectedServices.size);
        setDeleteType('bulk');
        setShowDeletePopup(true);
    };


    // Handle service actions (edit/delete from table)
    const handleServiceAction = (service, action) => {
        if (action === 'edit') {
            handleEdit(service);
        } else if (action === 'delete') {
            handleDelete(service.cleaningServiceId);
        }
    };

    if (loading && services.length === 0) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading services...</p>
            </div>
        );
    }

    return (
        <div className="cleaning-service-management">
            <NavbarEmployee/>
            <div className="header">
                <h1>Manage Services</h1>
                <p>Update and manage your cleaning services</p>
            </div>

            {error && (
                <div className="error-message">
                    {error}
                    <button onClick={() => setError(null)}>×</button>
                </div>
            )}

            <div className="actions-bar">
                <div className="action-left">
                </div>
                <div className="action-right">
                    <div className="search-container">
                        <div className="search-bar">
                            <input
                                type="text"
                                placeholder="Search services..."
                                value={searchTerm}
                                onChange={handleSearchChange}
                            />
                        </div>
                        <button className="search-icon-btn" title="Search">
                            🔍
                        </button>
                    </div>
                </div>
            </div>

            {showForm && (
                <div className="form-overlay">
                    <div className="form-container">
                        <div className="form-header">
                            <h2>{editingService ? 'Edit Service' : 'Add New Service'}</h2>
                            <button className="close-btn" onClick={resetForm}>×</button>
                        </div>


                        <form onSubmit={handleSubmit} className="service-form">
                            <div className="form-group">
                                <label htmlFor="serviceName">Service Name *</label>
                                <input
                                    type="text"
                                    id="serviceName"
                                    name="serviceName"
                                    value={formData.serviceName}
                                    onChange={handleInputChange}
                                    required
                                    placeholder="e.g., Exterior Wash & Wax"
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="category">Category *</label>
                                <input
                                    type="text"
                                    id="category"
                                    name="category"
                                    value={formData.category}
                                    onChange={handleInputChange}
                                    required
                                    placeholder="e.g., Exterior, Interior, Wax, etc."
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="description">Description</label>
                                <textarea
                                    id="description"
                                    name="description"
                                    value={formData.description}
                                    onChange={handleInputChange}
                                    placeholder="Describe what this service includes..."
                                    rows="3"
                                    className="form-textarea"
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="priceOfService">Price (R) *</label>
                                    <input
                                        type="number"
                                        id="priceOfService"
                                        name="priceOfService"
                                        value={formData.priceOfService}
                                        onChange={handleInputChange}
                                        required
                                        min="0"
                                        step="0.01"
                                        placeholder="150.00"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="duration">Duration (hours) *</label>
                                    <input
                                        type="number"
                                        id="duration"
                                        name="duration"
                                        value={formData.duration}
                                        onChange={handleInputChange}
                                        required
                                        min="0.5"
                                        max="5"
                                        step="0.5"
                                        placeholder="1.5"
                                    />
                                </div>
                            </div>

                            <div className="form-actions">
                                <button type="button" className="btn btn-secondary" onClick={resetForm}>
                                    Cancel
                                </button>
                                <button type="submit" className="btn btn-primary" disabled={loading}>
                                    {loading ? 'Saving...' : (editingService ? 'Update Service' : 'Create Service')}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="services-table-container">
                <table className="services-table">
                    <thead>
                    <tr>
                        <th>
                            <input
                                type="checkbox"
                                className="service-checkbox"
                                id="select-all-checkbox"
                                checked={filteredServices.length > 0 && filteredServices.every(service => selectedServices.has(service.cleaningServiceId))}
                                onChange={(e) => {
                                    e.stopPropagation();
                                    handleSelectAll(e.target.checked);
                                }}
                            />
                        </th>
                        <th>Name of service ↓</th>
                        <th>Category ↓</th>
                        <th>Description of service ↓</th>
                        <th>Duration ↓</th>
                        <th>Price of service ↓</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredServices.map(service => (
                        <tr key={service.cleaningServiceId}>
                            <td className="checkbox-cell">
                                <input
                                    type="checkbox"
                                    className="service-checkbox"
                                    checked={selectedServices.has(service.cleaningServiceId)}
                                    onChange={(e) => {
                                        e.stopPropagation();
                                        handleServiceSelect(service.cleaningServiceId, e.target.checked);
                                    }}
                                />
                            </td>
                            <td className="service-name">{service.serviceName}</td>
                            <td className="service-category">{service.category || 'N/A'}</td>
                            <td className="service-description">
                                {serviceDescriptions.get(service.cleaningServiceId) || 'A thorough top-to-bottom cleaning service that includes...'}
                            </td>
                            <td className="service-duration">{service.duration} hours</td>
                            <td className="service-price">R {service.priceOfService}</td>
                            <td className="service-actions">
                                <button
                                    className="btn btn-edit"
                                    onClick={() => handleServiceAction(service, 'edit')}
                                    title="Edit service"
                                >
                                    Edit
                                </button>
                                <button
                                    className="btn btn-delete"
                                    onClick={() => handleServiceAction(service, 'delete')}
                                    title="Delete service"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {/* Create New Service Button */}
            <div className="create-service-section">
                <button 
                    className="btn btn-primary" 
                    onClick={() => setShowForm(true)}
                    disabled={loading}
                >
                    Create New Service
                </button>
            </div>


            {/* Delete Confirmation Popup */}
            {showDeletePopup && (
                <div className="popup-overlay">
                    <div className="popup-container">
                        <div className="popup-header">
                            <h3>Delete Service</h3>
                            <button
                                className="close-btn"
                                onClick={() => setShowDeletePopup(false)}
                            >
                                ×
                            </button>
                        </div>
                        <div className="popup-content">
                            <p>
                                {deleteType === 'single'
                                    ? 'Are you sure you want to delete this service?'
                                    : `Are you sure you want to delete ${deleteTarget} selected service(s)?`
                                }
                            </p>
                        </div>
                        <div className="popup-actions">
                            <button
                                className="btn btn-secondary"
                                onClick={() => setShowDeletePopup(false)}
                                disabled={loading}
                            >
                                Cancel
                            </button>
                            <button
                                className="btn btn-danger"
                                onClick={confirmDelete}
                                disabled={loading}
                            >
                                {loading ? 'Deleting...' : 'Delete'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Validation Popup */}
            {validationPopup.show && (
                <div className="validation-popup">
                    <div className="validation-content">
                        <p className="validation-message">{validationPopup.message}</p>
                        <button
                            className="btn btn-primary"
                            onClick={() => setValidationPopup({ show: false, message: '' })}
                        >
                            OK
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CleaningServiceManagement;
