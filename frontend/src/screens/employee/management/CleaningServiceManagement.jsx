import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiClient } from '../../../services/api';
import './CleaningServiceManagement.css';
import NavbarEmployee from "../../components/NavbarEmployee";
import Footer from '../../components/Footer';
import '../../components/Footer.css';

// ========================================
// API CONFIGURATION
// ========================================
const CLEANING_SERVICE_API = '/api/cleaningservice';

const cleaningServiceService = {
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

    deleteCleaningService: async (id) => {
        try {
            console.log(`Deleting cleaning service with ID: ${id}`);
            const response = await apiClient.delete(`${CLEANING_SERVICE_API}/delete/${id}`);
            console.log('Delete response status:', response.status);
            return response.status === 204;
        } catch (error) {
            console.error(`Error deleting cleaning service ${id}:`, error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            throw error;
        }
    }
};

const CleaningServiceManagement = () => {
    // ========================================
    // HOOKS & STATE
    // ========================================
    const navigate = useNavigate();
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
    const [deleteType, setDeleteType] = useState('single');
    const [serviceDescriptions, setServiceDescriptions] = useState(new Map());
    const [showErrorPopup, setShowErrorPopup] = useState(false);
    const [errorDetails, setErrorDetails] = useState({ title: '', message: '', type: 'error' });
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [formData, setFormData] = useState({
        serviceName: '',
        category: '',
        newCategory: '',
        priceOfService: '',
        duration: '',
        description: ''
    });

    // ========================================
    // AUTHENTICATION
    // ========================================
    const getAuthToken = () => {
        return localStorage.getItem('authToken') || localStorage.getItem('token');
    };

    // ========================================
    // EFFECTS
    // ========================================
    useEffect(() => {
        const token = getAuthToken();
        console.log('Auth token status:', token ? 'Present' : 'Missing');
        
        if (!token) {
            setError('Authentication required. Please log in first.');
            return;
        }
        
        fetchServices();
    }, []);

    // ========================================
    // ERROR HANDLING
    // ========================================
    const displayErrorPopup = (title, message, type = 'error') => {
        setErrorDetails({ title, message, type });
        setShowErrorPopup(true);
        setError(null); // Clear inline error
    };

    const closeErrorPopup = () => {
        setShowErrorPopup(false);
        setErrorDetails({ title: '', message: '', type: 'error' });
    };

    const displaySuccessPopup = (message) => {
        setSuccessMessage(message);
        setShowSuccessPopup(true);
    };

    const closeSuccessPopup = () => {
        setShowSuccessPopup(false);
        setSuccessMessage('');
    };

    // ========================================
    // API FUNCTIONS
    // ========================================
    const fetchServices = async () => {
        setLoading(true);
        try {
            const data = await cleaningServiceService.getAllCleaningServices();
            setServices(data);
            setError(null);
        } catch (err) {
            console.error('Error fetching services:', err);
            if (err.response?.status === 401) {
                displayErrorPopup(
                    'Authentication Required',
                    'Please log in to access service management. You will be redirected to the login page.',
                    'auth'
                );
            } else if (err.response?.status === 403) {
                displayErrorPopup(
                    'Access Denied',
                    'You do not have permission to access service management. Please contact support.',
                    'error'
                );
            } else if (err.response?.status >= 500) {
                displayErrorPopup(
                    'Server Error',
                    'Our servers are experiencing issues. Please try again later or contact support if the problem persists.',
                    'error'
                );
            } else {
                displayErrorPopup(
                    'Failed to Load Services',
                    err.response?.data?.message || 'Unable to fetch services. Please check your connection and try again.',
                    'error'
                );
            }
        } finally {
            setLoading(false);
        }
    };

    // ========================================
    // FORM HANDLERS
    // ========================================
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
            newCategory: '',
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
            const { description, newCategory, ...backendData } = formData;
            const processedData = {
                ...backendData,
                category: formData.category === 'new' ? formData.newCategory : formData.category,
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
                fetchServices();
                displaySuccessPopup('Service updated successfully!');
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
                    fetchServices();
                    displaySuccessPopup('Service created successfully!');
                } else if (result.error === 'DUPLICATE') {
                    setValidationPopup({ show: true, message: result.message });
                    resetForm();
                } else {
                    setError('Failed to create service');
                }
            }
        } catch (err) {
            console.error('Error in handleSubmit:', err);
            if (err.response?.status === 400) {
                displayErrorPopup(
                    'Invalid Service Data',
                    err.response?.data?.message || 'Please check your service information and try again. Make sure all required fields are filled correctly.',
                    'error'
                );
            } else if (err.response?.status === 409) {
                displayErrorPopup(
                    'Service Already Exists',
                    'A service with this name already exists. Please use a different name.',
                    'error'
                );
            } else if (err.response?.status === 401) {
                displayErrorPopup(
                    'Authentication Required',
                    'Please log in to save services. You will be redirected to the login page.',
                    'auth'
                );
            } else if (err.response?.status >= 500) {
                displayErrorPopup(
                    'Server Error',
                    'Unable to save your service due to server issues. Please try again later.',
                    'error'
                );
            } else {
                displayErrorPopup(
                    'Failed to Save Service',
                    err.response?.data?.message || (editingService ? 'Unable to update the service. Please try again.' : 'Unable to create the service. Please try again.'),
                    'error'
                );
            }
        } finally {
            setLoading(false);
        }
    };

    // ========================================
    // SERVICE ACTIONS
    // ========================================
    const handleEdit = (service) => {
        setEditingService(service);
        const savedDescription = serviceDescriptions.get(service.cleaningServiceId);
        setFormData({
            serviceName: service.serviceName || '',
            category: service.category || '',
            newCategory: '',
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
                displaySuccessPopup('Service deleted successfully!');
            } else if (deleteType === 'bulk') {
                const deletePromises = Array.from(selectedServices).map(id =>
                    cleaningServiceService.deleteCleaningService(id)
                );
                await Promise.all(deletePromises);
                setSelectedServices(new Set());
                displaySuccessPopup(`${selectedServices.size} services deleted successfully!`);
            }
            setError(null);
            fetchServices();
        } catch (err) {
            console.error('Error in confirmDelete:', err);
            if (err.response?.status === 404) {
                displayErrorPopup(
                    'Service Not Found',
                    'This service may have already been deleted. Please refresh the page.',
                    'error'
                );
            } else if (err.response?.status === 403) {
                displayErrorPopup(
                    'Cannot Delete Service',
                    'You do not have permission to delete this service.',
                    'error'
                );
            } else if (err.response?.status === 401) {
                displayErrorPopup(
                    'Authentication Required',
                    'Please log in to delete services. You will be redirected to the login page.',
                    'auth'
                );
            } else if (err.response?.status >= 500) {
                displayErrorPopup(
                    'Server Error',
                    'Unable to delete the service due to server issues. Please try again later.',
                    'error'
                );
            } else {
                displayErrorPopup(
                    'Failed to Delete Service',
                    err.response?.data?.message || (deleteType === 'single' ? 'Unable to delete the service. Please try again.' : 'Unable to delete some services. Please try again.'),
                    'error'
                );
            }
        } finally {
            setLoading(false);
            setShowDeletePopup(false);
            setDeleteTarget(null);
        }
    };

    const handleServiceAction = (service, action) => {
        if (action === 'edit') {
            handleEdit(service);
        } else if (action === 'delete') {
            handleDelete(service.cleaningServiceId);
        }
    };

    // ========================================
    // SEARCH & FILTER FUNCTIONS
    // ========================================
    const getUniqueCategories = () => {
        return services
            .map(service => service.category)
            .filter(category => category && category.trim() !== '')
            .filter((category, index, self) => self.indexOf(category) === index)
            .sort();
    };

    const filteredServices = services.filter(service =>
        service.serviceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (service.category && service.category.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    // ========================================
    // RENDER HELPERS
    // ========================================
    const renderLoadingState = () => (
        <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading services...</p>
        </div>
    );

    const renderErrorMessage = () => (
        <div className="error-message">
            {error}
            <button onClick={() => setError(null)}>×</button>
        </div>
    );

    const renderServiceForm = () => (
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
                                <select
                                    id="category"
                                    name="category"
                                    value={formData.category}
                                    onChange={handleInputChange}
                                    required
                                    className="form-select"
                                >
                                    <option value="">Select a category...</option>
                                    <option value="EXTERIOR WASH">EXTERIOR WASH</option>
                                    <option value="INTERIOR CARE">INTERIOR CARE</option>
                                    <option value="FULL DETAILING">FULL DETAILING</option>
                                    <option value="PROTECTION SERVICES">PROTECTION SERVICES</option>
                                    {getUniqueCategories().filter(cat => 
                                        !['EXTERIOR WASH', 'INTERIOR CARE', 'FULL DETAILING', 'PROTECTION SERVICES'].includes(cat)
                                    ).map(category => (
                                        <option key={category} value={category}>
                                            {category}
                                        </option>
                                    ))}
                                    <option value="new">+ Add New Category</option>
                                </select>
                        {formData.category === 'new' && (
                            <input
                                type="text"
                                name="newCategory"
                                value={formData.newCategory || ''}
                                onChange={handleInputChange}
                                placeholder="Enter new category name..."
                                className="form-input"
                                style={{ marginTop: '8px' }}
                            />
                        )}
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
                        <button type="submit" className="btn btn-primary" disabled={loading}>
                            {loading ? 'Saving...' : (editingService ? 'Update Service' : 'Create Service')}
                        </button>
                        <button type="button" className="btn btn-secondary" onClick={resetForm}>
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );

    const renderServiceTable = () => (
        <div className="em-table-container">
            <div className="em-table-header">
                <h2>Service List</h2>
                <div className="em-table-actions">
                    <button
                        className="em-secondary-btn"
                        onClick={fetchServices}
                    >
                        Refresh List
                    </button>
                </div>
            </div>

            <div className="table-wrapper">
                <table className="em-table">
                    <thead>
                        <tr>
                            <th>Service Name</th>
                            <th>Category</th>
                            <th>Description</th>
                            <th>Duration</th>
                            <th>Price</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredServices.length === 0 ? (
                            <tr>
                                <td colSpan="6" style={{ textAlign: "center", padding: "2rem", color: "#6b7280" }}>
                                    No services match your search.
                                </td>
                            </tr>
                        ) : (
                            filteredServices.map(service => (
                                <tr key={service.cleaningServiceId}>
                                    <td>
                                        <div className="service-name">
                                            <strong>{service.serviceName}</strong>
                                        </div>
                                    </td>
                                    <td>
                                        <span className="service-category">{service.category || 'N/A'}</span>
                                    </td>
                                    <td>
                                        <div className="service-description">
                                            {serviceDescriptions.get(service.cleaningServiceId) || 'A thorough top-to-bottom cleaning service that includes...'}
                                        </div>
                                    </td>
                                    <td>
                                        <div className="service-duration">{service.duration} hours</div>
                                    </td>
                                    <td>
                                        <div className="service-price">R {service.priceOfService}</div>
                                    </td>
                                    <td>
                                        <div className="service-actions">
                                            <button
                                                className="edit-btn"
                                                onClick={() => handleServiceAction(service, 'edit')}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                className="delete-btn"
                                                onClick={() => handleServiceAction(service, 'delete')}
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );

    const renderDeletePopup = () => (
        <div className="simple-popup-overlay">
            <div className="simple-popup-container">
                <div className="simple-popup-header">
                    <h3>Delete Service</h3>
                </div>
                <div className="simple-popup-content">
                    <p>
                        {deleteType === 'single'
                            ? 'Are you sure you want to delete this service?'
                            : `Are you sure you want to delete ${deleteTarget} selected service(s)?`
                        }
                    </p>
                </div>
                <div className="simple-popup-actions">
                    <button
                        className="simple-popup-btn simple-popup-no"
                        onClick={() => setShowDeletePopup(false)}
                        disabled={loading}
                    >
                        No
                    </button>
                    <button
                        className="simple-popup-btn simple-popup-yes"
                        onClick={confirmDelete}
                        disabled={loading}
                    >
                        {loading ? 'Deleting...' : 'Yes'}
                    </button>
                </div>
            </div>
        </div>
    );

    const renderErrorPopup = () => (
        <div className="simple-popup-overlay">
            <div className="simple-popup-container">
                <div className="simple-popup-header">
                    <h3>{errorDetails.title}</h3>
                </div>
                <div className="simple-popup-content">
                    <p>{errorDetails.message}</p>
                </div>
                <div className="simple-popup-actions">
                    {errorDetails.type === 'auth' ? (
                        <button 
                            className="simple-popup-btn simple-popup-yes" 
                            onClick={() => {
                                closeErrorPopup();
                                navigate('/login');
                            }}
                        >
                            Go to Login
                        </button>
                    ) : (
                        <button className="simple-popup-btn simple-popup-yes" onClick={closeErrorPopup}>
                            OK
                        </button>
                    )}
                </div>
            </div>
        </div>
    );

    const renderSuccessPopup = () => (
        <div className="simple-popup-overlay">
            <div className="simple-popup-container">
                <div className="simple-popup-header">
                    <h3>Success</h3>
                </div>
                <div className="simple-popup-content">
                    <p>{successMessage}</p>
                </div>
                <div className="simple-popup-actions">
                    <button className="simple-popup-btn simple-popup-yes" onClick={closeSuccessPopup}>
                        OK
                    </button>
                </div>
            </div>
        </div>
    );

    const renderValidationPopup = () => (
        <div className="simple-popup-overlay">
            <div className="simple-popup-container">
                <div className="simple-popup-header">
                    <h3>Validation Error</h3>
                </div>
                <div className="simple-popup-content">
                    <p>{validationPopup.message}</p>
                </div>
                <div className="simple-popup-actions">
                    <button
                        className="simple-popup-btn simple-popup-yes"
                        onClick={() => setValidationPopup({ show: false, message: '' })}
                    >
                        OK
                    </button>
                </div>
            </div>
        </div>
    );

    // ========================================
    // MAIN RENDER
    // ========================================
    if (loading && services.length === 0) {
        return renderLoadingState();
    }

    return (
        <div className="cleaning-service-management">
            <NavbarEmployee/>
            <div className="main-content">
                {/* Header Section */}
                <div className="cleaning-page-header-container">
                    <button
                        className="cleaning-page-back-btn"
                        onClick={() => navigate(-1)}
                        title="Go back"
                    >
                        ← Back
                    </button>
                    <div className="cleaning-header-content">
                        <h1>Manage Services</h1>
                        <p>Update and manage your cleaning services</p>
                    </div>
                    <button
                        className="cleaning-page-create-btn"
                        onClick={() => setShowForm(true)}
                        disabled={loading}
                    >
                        + Create New Service
                    </button>
                </div>

                {/* Error Message */}
                {error && renderErrorMessage()}

                {/* Search Bar */}
                <div className="search-bar-container">
                    <input
                        type="text"
                        placeholder="Search services..."
                        value={searchTerm}
                        onChange={handleSearchChange}
                        className="search-input"
                    />
                </div>

                {/* Service Form Modal */}
                {showForm && renderServiceForm()}

                {/* Service Table */}
                {renderServiceTable()}

                {/* Delete Confirmation Popup */}
                {showDeletePopup && renderDeletePopup()}

                {/* Validation Popup */}
                {validationPopup.show && renderValidationPopup()}
            </div>
            <Footer />
            
            {/* Error Popup */}
            {showErrorPopup && renderErrorPopup()}

            {/* Success Popup */}
            {showSuccessPopup && renderSuccessPopup()}
        </div>
    );
};

export default CleaningServiceManagement;