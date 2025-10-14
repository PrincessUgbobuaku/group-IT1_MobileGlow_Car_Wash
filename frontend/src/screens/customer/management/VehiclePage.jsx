import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './VehiclePage.css';
import NavbarCustomer from "../../components/NavbarCustomer";
import Footer from '../../components/Footer';
import '../../components/Footer.css';

const VehiclePage = () => {
    // ========================================
    // HOOKS & STATE
    // ========================================
    const navigate = useNavigate();
    const [vehicles, setVehicles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [editingVehicle, setEditingVehicle] = useState(null);
    const [showErrorPopup, setShowErrorPopup] = useState(false);
    const [errorDetails, setErrorDetails] = useState({ title: '', message: '', type: 'error' });
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const [deleteTarget, setDeleteTarget] = useState(null);
    const [formData, setFormData] = useState({
        vehicleID: '',
        plateNumber: '',
        make: '',
        colour: '',
        model: '',
        customerId: parseInt(localStorage.getItem('userId') || '1')
    });

    // ========================================
    // API CONFIGURATION
    // ========================================
    const VEHICLE_API = '/api/vehicle';
    const API_BASE_URL = 'http://localhost:8080/mobileglow';

    const getAuthToken = () => {
        return localStorage.getItem('authToken') || localStorage.getItem('token');
    };

    const apiClient = axios.create({
        baseURL: API_BASE_URL,
        headers: {
            'Content-Type': 'application/json',
        }
    });

    apiClient.interceptors.request.use(
        (config) => {
            const token = getAuthToken();
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        },
        (error) => Promise.reject(error)
    );

    // ========================================
    // VEHICLE SERVICE API
    // ========================================
    const vehicleService = {
        getAllVehicles: async () => {
            try {
                const response = await apiClient.get(`${VEHICLE_API}/findAll`);
                return response.data;
            } catch (error) {
                console.error('Error fetching vehicles:', error);
                throw error;
            }
        },

        getVehicleById: async (id) => {
            try {
                const response = await apiClient.get(`${VEHICLE_API}/read/${id}`);
                return response.data;
            } catch (error) {
                console.error(`Error fetching vehicle ${id}:`, error);
                throw error;
            }
        },

        createVehicle: async (vehicleData) => {
            try {
                const response = await apiClient.post(`${VEHICLE_API}/create`, vehicleData);
                return response.data;
            } catch (error) {
                console.error('Error creating vehicle:', error);
                throw error; // Re-throw the error so it can be handled by the calling function
            }
        },

        updateVehicle: async (id, vehicleData) => {
            try {
                console.log(`Updating vehicle ${id} with data:`, vehicleData);
                const response = await apiClient.put(`${VEHICLE_API}/update/${id}`, vehicleData);
                console.log('Update response:', response);
                return response.data;
            } catch (error) {
                console.error(`Error updating vehicle ${id}:`, error);
                throw error;
            }
        },

        deleteVehicle: async (id) => {
            try {
                console.log(`Attempting to delete vehicle with ID: ${id}`);
                const response = await apiClient.delete(`${VEHICLE_API}/delete/${id}`);
                console.log('Delete response:', response);
                return response.status === 204 || response.status === 200;
            } catch (error) {
                console.error(`Error deleting vehicle ${id}:`, error);
                throw error;
            }
        },

        findVehicleByPlateNumber: async (plateNumber) => {
            try {
                const response = await apiClient.get(`${VEHICLE_API}/plate/${plateNumber}`);
                return response.data;
            } catch (error) {
                console.error(`Error finding vehicle by plate ${plateNumber}:`, error);
                throw error;
            }
        },

        findVehiclesByCustomerId: async (customerId) => {
            try {
                const response = await apiClient.get(`${VEHICLE_API}/customer/${customerId}`);
                return response.data;
            } catch (error) {
                console.error(`Error finding vehicles for customer ${customerId}:`, error);
                throw error;
            }
        }
    };

    // ========================================
    // EFFECTS
    // ========================================
    useEffect(() => {
        const token = getAuthToken();
        console.log('Auth token status:', token ? 'Present' : 'Missing');
        
        if (!token) {
            setError('Please log in to access vehicle management. Go to the login page first.');
            setLoading(false);
            return;
        }
        fetchVehicles();
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

    const showDeleteConfirmation = (vehicleId) => {
        setDeleteTarget(vehicleId);
        setShowDeletePopup(true);
    };

    const closeDeletePopup = () => {
        setShowDeletePopup(false);
        setDeleteTarget(null);
    };

    // ========================================
    // API FUNCTIONS
    // ========================================
    const fetchVehicles = async () => {
        try {
            setLoading(true);
            const userId = localStorage.getItem('userId') || '1';
            console.log('Fetching vehicles for user ID:', userId);
            
            const data = await vehicleService.findVehiclesByCustomerId(parseInt(userId));
            console.log('Fetched vehicles for customer:', data);
            console.log('Number of vehicles:', data.length);
            setVehicles(data);
            setError(null);
        } catch (err) {
            console.error('Error fetching vehicles:', err);
            if (err.response?.status === 401) {
                displayErrorPopup(
                    'Authentication Required',
                    'Please log in to access your vehicles. You will be redirected to the login page.',
                    'auth'
                );
            } else if (err.response?.status === 403) {
                displayErrorPopup(
                    'Access Denied',
                    'You do not have permission to access vehicle management. Please contact support.',
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
                    'Failed to Load Vehicles',
                    err.response?.data?.message || 'Unable to fetch your vehicles. Please check your connection and try again.',
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
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            // Frontend duplicate check for new vehicles
            if (!editingVehicle) {
                const isDuplicate = vehicles.some(vehicle =>
                    vehicle.carPlateNumber.toLowerCase() === formData.plateNumber.toLowerCase()
                );

                if (isDuplicate) {
                    displayErrorPopup(
                        'Plate Number Already Exists',
                        'A vehicle with this plate number already exists. Please use a different plate number.',
                        'error'
                    );
                    setLoading(false);
                    return;
                }
            }

            const vehicleData = {
                carPlateNumber: formData.plateNumber,
                carMake: formData.make,
                carColour: formData.colour,
                carModel: formData.model,
                customerId: parseInt(formData.customerId)
            };

            console.log('Submitting vehicle data:', vehicleData);
            console.log('Editing vehicle:', editingVehicle);

            if (editingVehicle) {
                console.log('Updating vehicle with ID:', editingVehicle.vehicleID);
                const result = await vehicleService.updateVehicle(editingVehicle.vehicleID, vehicleData);
                console.log('Update result:', result);
                displaySuccessPopup("Vehicle updated successfully!");
            } else {
                console.log('Creating new vehicle');
                const result = await vehicleService.createVehicle(vehicleData);
                console.log('Create result:', result);
                displaySuccessPopup("Vehicle created successfully!");
            }

            await fetchVehicles();
            resetForm();
        } catch (err) {
            console.error("Failed to save vehicle:", err);
            console.error("Error response:", err.response);
            console.error("Error data:", err.response?.data);
            console.error("Error status:", err.response?.status);
            console.error("Error message:", err.response?.data?.message);
            
            if (err.response?.status === 400) {
                // Check if the error message indicates a duplicate plate number
                const errorMessage = err.response?.data?.message || '';
                if (errorMessage.toLowerCase().includes('plate') || 
                    errorMessage.toLowerCase().includes('already exists') ||
                    errorMessage.toLowerCase().includes('duplicate')) {
                    displayErrorPopup(
                        'Plate Number Already Exists',
                        'A vehicle with this plate number already exists. Please use a different plate number.',
                        'error'
                    );
                } else {
                    displayErrorPopup(
                        'Invalid Vehicle Data',
                        'Please check your vehicle information and try again. Make sure all required fields are filled correctly.',
                        'error'
                    );
                }
            } else if (err.response?.status === 409) {
                displayErrorPopup(
                    'Plate Number Already Exists',
                    'A vehicle with this plate number already exists. Please use a different plate number.',
                    'error'
                );
            } else if (err.response?.status >= 500) {
                displayErrorPopup(
                    'Server Error',
                    'Unable to save your vehicle due to server issues. Please try again later.',
                    'error'
                );
            } else {
                // Check for any error message that might indicate a duplicate plate
                const errorMessage = err.response?.data?.message || err.message || '';
                if (errorMessage.toLowerCase().includes('plate') || 
                    errorMessage.toLowerCase().includes('already exists') ||
                    errorMessage.toLowerCase().includes('duplicate')) {
                    displayErrorPopup(
                        'Plate Number Already Exists',
                        'A vehicle with this plate number already exists. Please use a different plate number.',
                        'error'
                    );
                } else {
                    displayErrorPopup(
                        'Failed to Save Vehicle',
                        errorMessage || 'Unable to save your vehicle. Please try again.',
                        'error'
                    );
                }
            }
        } finally {
            setLoading(false);
        }
    };

    const resetForm = () => {
        const userId = localStorage.getItem('userId') || '1';
        
        setFormData({
            vehicleID: '',
            plateNumber: '',
            make: '',
            colour: '',
            model: '',
            customerId: parseInt(userId)
        });
        setEditingVehicle(null);
        setShowForm(false);
    };

    // ========================================
    // VEHICLE ACTIONS
    // ========================================
    const handleEdit = (vehicle) => {
        console.log('Editing vehicle:', vehicle);
        console.log('Vehicle ID type:', typeof vehicle.vehicleID);
        console.log('Vehicle ID value:', vehicle.vehicleID);
        setEditingVehicle(vehicle);
        
        const userId = localStorage.getItem('userId') || '1';
        
        setFormData({
            vehicleID: vehicle.vehicleID || '',
            plateNumber: vehicle.carPlateNumber || '',
            make: vehicle.carMake || '',
            colour: vehicle.carColour || '',
            model: vehicle.carModel || '',
            customerId: parseInt(userId)
        });
        setShowForm(true);
        setError(null);
    };

    const handleDelete = (id) => {
        console.log('Showing delete confirmation for vehicle ID:', id);
        showDeleteConfirmation(id);
    };

    const confirmDelete = async () => {
        if (!deleteTarget) return;
        
        console.log('Confirming deletion of vehicle with ID:', deleteTarget);
        setLoading(true);
        setError(null);
        closeDeletePopup();
        
        try {
            const result = await vehicleService.deleteVehicle(deleteTarget);
            console.log('Delete result:', result);
            displaySuccessPopup("Vehicle deleted successfully!");
            await fetchVehicles();
        } catch (err) {
            console.error("Failed to delete vehicle:", err);
            
            if (err.response?.status === 404) {
                displayErrorPopup(
                    'Vehicle Not Found',
                    'This vehicle may have already been deleted. Please refresh the page.',
                    'error'
                );
            } else if (err.response?.status === 403) {
                displayErrorPopup(
                    'Cannot Delete Vehicle',
                    'You do not have permission to delete this vehicle.',
                    'error'
                );
            } else if (err.response?.status >= 500) {
                displayErrorPopup(
                    'Server Error',
                    'Unable to delete the vehicle due to server issues. Please try again later.',
                    'error'
                );
            } else {
                displayErrorPopup(
                    'Failed to Delete Vehicle',
                    err.response?.data?.message || 'Unable to delete the vehicle. Please try again.',
                    'error'
                );
            }
        } finally {
            setLoading(false);
        }
    };

    // ========================================
    // RENDER HELPERS
    // ========================================
    const renderLoadingState = () => (
        <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Loading vehicles...</p>
        </div>
    );

    const renderErrorMessage = () => (
        <div className="error-message">
            {error}
            {error.includes('Authentication required') && (
                <div style={{ marginTop: '0.5rem', fontSize: '0.9rem' }}>
                    <a href="/login" style={{ color: 'white', textDecoration: 'underline' }}>
                        Go to Login Page
                    </a>
                </div>
            )}
            <button onClick={() => setError(null)}>×</button>
        </div>
    );


    const renderVehicleCard = (vehicle) => (
        <div 
            className={`vehicle-card ${editingVehicle && editingVehicle.vehicleID === vehicle.vehicleID ? 'editing' : ''}`} 
            key={vehicle.vehicleID}
        >
            <div className="vehicle-info">
                <div className="vehicle-info-item">
                    <span className="vehicle-info-label">Plate Number:</span>
                    <span className="vehicle-info-value">{vehicle.carPlateNumber}</span>
                </div>
                <div className="vehicle-info-item">
                    <span className="vehicle-info-label">Make:</span>
                    <span className="vehicle-info-value">{vehicle.carMake}</span>
                </div>
                <div className="vehicle-info-item">
                    <span className="vehicle-info-label">Model:</span>
                    <span className="vehicle-info-value">{vehicle.carModel}</span>
                </div>
                <div className="vehicle-info-item">
                    <span className="vehicle-info-label">Colour:</span>
                    <span className="vehicle-info-value">{vehicle.carColour}</span>
                </div>
            </div>
            <div className="vehicle-actions">
                <button
                    className="vehicle-page-edit-btn"
                    onClick={() => handleEdit(vehicle)}
                    title="Edit vehicle"
                >
                    Edit
                </button>
                <button
                    className="vehicle-page-delete-btn"
                    onClick={() => handleDelete(vehicle.vehicleID)}
                    title="Delete vehicle"
                >
                    Delete
                </button>
            </div>
        </div>
    );

    const renderDeletePopup = () => (
        <div className="simple-popup-overlay">
            <div className="simple-popup-container">
                <div className="simple-popup-header">
                    <h3>Delete Vehicle</h3>
                </div>
                <div className="simple-popup-content">
                    <p>Are you sure you want to delete this vehicle?</p>
                </div>
                <div className="simple-popup-actions">
                    <button 
                        className="simple-popup-btn simple-popup-no" 
                        onClick={closeDeletePopup}
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

    const renderSuccessPopup = () => (
        <div className="simple-popup-overlay">
            <div className="simple-popup-container">
                <div className="simple-popup-header">
                    <h3>Success!</h3>
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

    const renderVehicleForm = () => (
        <div className="form-sidebar" onClick={resetForm}>
            <div className="form-container" onClick={(e) => e.stopPropagation()}>
                <div className="form-header">
                    <h2>{editingVehicle ? 'Edit Vehicle' : 'Add New Vehicle'}</h2>
                    {editingVehicle && (
                        <div className="editing-indicator">
                            <span className="editing-badge">Editing: {editingVehicle.carPlateNumber}</span>
                        </div>
                    )}
                    <button className="close-btn" onClick={resetForm}>×</button>
                </div>
                <form onSubmit={handleSubmit} className="vehicle-form">
                    <div className="form-group">
                        <label htmlFor="plateNumber">Plate Number *</label>
                        <input
                            type="text"
                            id="plateNumber"
                            name="plateNumber"
                            value={formData.plateNumber}
                            onChange={handleInputChange}
                            required
                            placeholder="Enter plate number"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="make">Make *</label>
                        <input
                            type="text"
                            id="make"
                            name="make"
                            value={formData.make}
                            onChange={handleInputChange}
                            required
                            placeholder="Enter car make"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="model">Model *</label>
                        <input
                            type="text"
                            id="model"
                            name="model"
                            value={formData.model}
                            onChange={handleInputChange}
                            required
                            placeholder="Enter car model"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="colour">Color *</label>
                        <input
                            type="text"
                            id="colour"
                            name="colour"
                            value={formData.colour}
                            onChange={handleInputChange}
                            required
                            placeholder="Enter car color"
                        />
                    </div>

                    <div className="form-actions">
                        <button type="submit" className="btn btn-primary" disabled={loading}>
                            {loading ? 'Saving...' : (editingVehicle ? 'Update Vehicle' : 'Add Vehicle')}
                        </button>
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={resetForm}
                            disabled={loading}
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );

    // ========================================
    // MAIN RENDER
    // ========================================
    if (loading) {
        return renderLoadingState();
    }

    return (
        <div className="vehicle-page">
            <NavbarCustomer />
            
            {/* Header Section */}
            <div className="vehicle-page-header-wrapper">
                <button
                    className="vehicle-page-back-btn"
                    onClick={() => navigate(-1)}
                    title="Go back"
                >
                    ← Back
                </button>
                <div className="vehicle-header-content">
                    <h1>Vehicle Management</h1>
                    <p className="vehicle-page-description">
                        Keep your ride details up to date in one place. Add, edit, or remove vehicles from your profile so booking the right service is quick and hassle-free.
                    </p>
                </div>
                <button
                    className="vehicle-page-add-btn"
                    onClick={() => setShowForm(true)}
                >
                    + Add Vehicle
                </button>
            </div>

            {/* Messages */}
            {error && renderErrorMessage()}

            {/* Main Content */}
            <div className="vehicle-main-content">
                {vehicles.length === 0 ? (
                    <div className="no-data">
                        <p>No vehicles found. Add your first vehicle above.</p>
                    </div>
                ) : (
                    <div className="vehicle-cards-grid">
                        {vehicles.map(renderVehicleCard)}
                    </div>
                )}

                {showForm && renderVehicleForm()}
            </div>
            
            {/* Continue Booking Section */}
            <div className="continue-booking-section">
                <button
                    className="btn continue-booking-btn"
                    onClick={() => navigate('/booking')}
                >
                    Continue Booking
                </button>
            </div>
            
            <Footer />
            
            {/* Error Popup */}
            {showErrorPopup && renderErrorPopup()}
            
            {/* Success Popup */}
            {showSuccessPopup && renderSuccessPopup()}
            
            {/* Delete Confirmation Popup */}
            {showDeletePopup && renderDeletePopup()}
        </div>
    );
};

export default VehiclePage;