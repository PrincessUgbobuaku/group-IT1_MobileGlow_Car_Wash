import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './VehiclePage.css';
import NavbarCustomer from "../../components/NavbarCustomer";

// Vehicle Service API functions
const VEHICLE_API = '/api/vehicle';
const API_BASE_URL = 'http://localhost:8080/mobileglow';

// Get auth token from localStorage
const getAuthToken = () => {
    return localStorage.getItem('authToken') || localStorage.getItem('token');
};

// Create axios instance with default headers
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

// Add request interceptor to include auth token
apiClient.interceptors.request.use(
    (config) => {
        const token = getAuthToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

const vehicleService = {
    // Get all vehicles (simple version without customer details)
    getSimpleVehicles: async () => {
        try {
            const response = await apiClient.get(`${VEHICLE_API}/simple`);
            return response.data;
        } catch (error) {
            console.error('Error fetching simple vehicles:', error);
            throw error;
        }
    },

    // Get all vehicles
    getAllVehicles: async () => {
        try {
            const response = await apiClient.get(`${VEHICLE_API}/findAll`);
            return response.data;
        } catch (error) {
            console.error('Error fetching vehicles:', error);
            throw error;
        }
    },

    // Get vehicle by ID
    getVehicleById: async (id) => {
        try {
            const response = await apiClient.get(`${VEHICLE_API}/read/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching vehicle ${id}:`, error);
            throw error;
        }
    },

    // Create new vehicle
    createVehicle: async (vehicleData) => {
        try {
            const response = await apiClient.post(`${VEHICLE_API}/create`, vehicleData);
            return response.data;
        } catch (error) {
            console.error('Error creating vehicle:', error);
            throw error;
        }
    },

    // Update vehicle
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

    // Delete vehicle
    deleteVehicle: async (id) => {
        try {
            console.log(`Attempting to delete vehicle with ID: ${id}`);
            const response = await apiClient.delete(`${VEHICLE_API}/delete/${id}`);
            console.log('Delete response:', response);
            return response.status === 204 || response.status === 200; // Success if no content or OK
        } catch (error) {
            console.error(`Error deleting vehicle ${id}:`, error);
            throw error;
        }
    },

    // Find vehicle by plate number
    findVehicleByPlateNumber: async (plateNumber) => {
        try {
            const response = await apiClient.get(`${VEHICLE_API}/plate/${plateNumber}`);
            return response.data;
        } catch (error) {
            console.error(`Error finding vehicle by plate ${plateNumber}:`, error);
            throw error;
        }
    },

    // Find vehicles by customer ID
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

const VehiclePage = () => {
    const [vehicles, setVehicles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [editingVehicle, setEditingVehicle] = useState(null);
    const [formData, setFormData] = useState({
        vehicleID: '',
        plateNumber: '',
        make: '',
        colour: '',
        model: '',
        customerId: parseInt(localStorage.getItem('userId') || '1') // Use logged-in user's ID
    });

    useEffect(() => {
        // Check if user is authenticated
        const token = getAuthToken();
        console.log('Auth token status:', token ? 'Present' : 'Missing');
        
        if (!token) {
            setError('Please log in to access vehicle management. Go to the login page first.');
            setLoading(false);
            return;
        }
        fetchVehicles();
    }, []);

    const fetchVehicles = async () => {
        try {
            setLoading(true);
            
            // Get the current user's ID from localStorage
            const userId = localStorage.getItem('userId') || '1';
            console.log('Fetching vehicles for user ID:', userId);
            
            // Use the customer-specific endpoint instead of the simple endpoint
            const data = await vehicleService.findVehiclesByCustomerId(parseInt(userId));
            console.log('Fetched vehicles for customer:', data);
            console.log('Number of vehicles:', data.length);
            setVehicles(data);
            setError(null);
        } catch (err) {
            console.error('Error fetching vehicles:', err);
            if (err.response?.status === 401) {
                setError('Authentication required. Please log in first.');
            } else {
                setError(err.response?.data?.message || 'Failed to fetch vehicles. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    };

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
        setSuccess(null);

        try {
            // Transform form data to match backend field names
            const vehicleData = {
                carPlateNumber: formData.plateNumber,
                carMake: formData.make,
                carColour: formData.colour,
                carModel: formData.model,
                customerId: parseInt(formData.customerId)
            };

            console.log('Submitting vehicle data:', vehicleData);
            console.log('Editing vehicle:', editingVehicle);

            // Decide if it's an update or create request
            if (editingVehicle) {
                console.log('Updating vehicle with ID:', editingVehicle.vehicleID);
                const result = await vehicleService.updateVehicle(editingVehicle.vehicleID, vehicleData);
                console.log('Update result:', result);
                setSuccess("Vehicle updated successfully!");
            } else {
                console.log('Creating new vehicle');
                const result = await vehicleService.createVehicle(vehicleData);
                console.log('Create result:', result);
                setSuccess("Vehicle created successfully!");
            }

            // Refresh the vehicle list
            await fetchVehicles();
            // Reset form after successful save
            resetForm();
        } catch (err) {
            console.error("Failed to save vehicle:", err);
            console.error("Error response:", err.response);
            console.error("Error data:", err.response?.data);
            setError(err.response?.data?.message || err.message || "Failed to save vehicle. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (vehicle) => {
        console.log('Editing vehicle:', vehicle);
        console.log('Vehicle ID type:', typeof vehicle.vehicleID);
        console.log('Vehicle ID value:', vehicle.vehicleID);
        setEditingVehicle(vehicle);
        
        // Get the current user's ID from localStorage
        const userId = localStorage.getItem('userId') || '1';
        
        setFormData({
            vehicleID: vehicle.vehicleID || '',
            plateNumber: vehicle.carPlateNumber || '',
            make: vehicle.carMake || '',
            colour: vehicle.carColour || '',
            model: vehicle.carModel || '',
            customerId: parseInt(userId) // Use logged-in user's ID
        });
        setShowForm(true);
        setError(null);
        setSuccess(null);
    };

    const handleDelete = async (id) => {
        console.log('Attempting to delete vehicle with ID:', id);
        if (window.confirm('Are you sure you want to delete this vehicle?')) {
            setLoading(true);
            setError(null);
            setSuccess(null);
            try {
                const result = await vehicleService.deleteVehicle(id);
                console.log('Delete result:', result);
                setSuccess("Vehicle deleted successfully!");
                await fetchVehicles();
            } catch (err) {
                console.error("Failed to delete vehicle:", err);
                setError(err.response?.data?.message || 'Failed to delete vehicle. Please try again.');
            } finally {
                setLoading(false);
            }
        }
    };

    const resetForm = () => {
        // Get the current user's ID from localStorage
        const userId = localStorage.getItem('userId') || '1';
        
        setFormData({
            vehicleID: '',
            plateNumber: '',
            make: '',
            colour: '',
            model: '',
            customerId: parseInt(userId) // Use logged-in user's ID
        });
        setEditingVehicle(null);
        setShowForm(false);
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading vehicles...</p>
            </div>
        );
    }

    return (
        <div className="vehicle-page">
            <NavbarCustomer />
            <div className="page-header">
                <h1>Vehicle Management</h1>
                <button
                    className="btn btn-primary"
                    onClick={() => setShowForm(true)}
                >
                    + Add Vehicle
                </button>
            </div>

            <div className="page-subtitle">
                <p>Keep your ride details up to date in one place. Add, edit, or remove vehicles from your profile so booking the right service is quick and hassle-free.</p>
            </div>

            {error && (
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
            )}

            {success && (
                <div className="success-message">
                    {success}
                    <button onClick={() => setSuccess(null)}>×</button>
                </div>
            )}


            <div className="main-content">
                <div className="vehicles-list">
                    {vehicles.length === 0 ? (
                        <div className="no-data">
                            <p>No vehicles found. Add your first vehicle above.</p>
                        </div>
                    ) : (
                        <div className="vehicles-grid">
                            {vehicles.map((vehicle) => (
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
                                            className="btn btn-edit"
                                            onClick={() => handleEdit(vehicle)}
                                            title="Edit vehicle"
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="btn btn-delete"
                                            onClick={() => handleDelete(vehicle.vehicleID)}
                                            title="Delete vehicle"
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {showForm && (
                    <div className="form-sidebar">
                        <div className="form-container">
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
                )}
            </div>
        </div>
    );
};

export default VehiclePage;
