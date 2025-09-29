import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './VehiclePage.css';

// Vehicle Service API functions
const VEHICLE_API = '/api/vehicle';
const API_BASE_URL = 'http://localhost:8080/mobileglow';

const vehicleService = {
    // Get all vehicles (simple version without customer details)
    getSimpleVehicles: async () => {
        try {
            const response = await axios.get(`${API_BASE_URL}${VEHICLE_API}/simple`);
            return response.data;
        } catch (error) {
            console.error('Error fetching simple vehicles:', error);
            throw error;
        }
    },

    // Get all vehicles
    getAllVehicles: async () => {
        try {
            const response = await axios.get(`${API_BASE_URL}${VEHICLE_API}/findAll`);
            return response.data;
        } catch (error) {
            console.error('Error fetching vehicles:', error);
            throw error;
        }
    },

    // Get vehicle by ID
    getVehicleById: async (id) => {
        try {
            const response = await axios.get(`${API_BASE_URL}${VEHICLE_API}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching vehicle ${id}:`, error);
            throw error;
        }
    },

    // Create new vehicle
    createVehicle: async (vehicleData) => {
        try {
            const response = await axios.post(`${API_BASE_URL}${VEHICLE_API}/create`, vehicleData);
            return response.data;
        } catch (error) {
            console.error('Error creating vehicle:', error);
            throw error;
        }
    },

    // Update vehicle
    updateVehicle: async (id, vehicleData) => {
        try {
            const response = await axios.put(`${API_BASE_URL}${VEHICLE_API}/update/${id}`, vehicleData);
            return response.data;
        } catch (error) {
            console.error(`Error updating vehicle ${id}:`, error);
            throw error;
        }
    },

    // Delete vehicle
    deleteVehicle: async (id) => {
        try {
            const response = await axios.delete(`${API_BASE_URL}${VEHICLE_API}/delete/${id}`);
            return response.status === 204; // Success if no content
        } catch (error) {
            console.error(`Error deleting vehicle ${id}:`, error);
            throw error;
        }
    },

    // Find vehicle by plate number
    findVehicleByPlateNumber: async (plateNumber) => {
        try {
            const response = await axios.get(`${API_BASE_URL}${VEHICLE_API}/plate/${plateNumber}`);
            return response.data;
        } catch (error) {
            console.error(`Error finding vehicle by plate ${plateNumber}:`, error);
            throw error;
        }
    },

    // Find vehicles by customer ID
    findVehiclesByCustomerId: async (customerId) => {
        try {
            const response = await axios.get(`${API_BASE_URL}${VEHICLE_API}/customer/${customerId}`);
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
    const [showForm, setShowForm] = useState(false);
    const [editingVehicle, setEditingVehicle] = useState(null);
    const [formData, setFormData] = useState({
        vehicleID: '12',
        plateNumber: '',
        make: '',
        colour: '',
        model: '',
        customer: { userId: 1 } // Default customer ID for demo
    });

    useEffect(() => {
        fetchVehicles();
    }, []);

    const fetchVehicles = async () => {
        try {
            setLoading(true);
            const data = await vehicleService.getSimpleVehicles();
            console.log('Fetched vehicles:', data);
            console.log('Number of vehicles:', data.length);
            setVehicles(data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch vehicles');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (name === 'customerId') {
            setFormData(prev => ({
                ...prev,
                customer: { userId: parseInt(value) }
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                [name]: value
            }));
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // Transform form data to match backend field names
        const vehicleData = {
            carPlateNumber: formData.plateNumber,
            carMake: formData.make,
            carColour: formData.colour,
            carModel: formData.model,
            customerId: formData.customer.userId
        };

        // Decide if it's an update or create request
        const request = editingVehicle
            ? vehicleService.updateVehicle(editingVehicle.vehicleID, vehicleData)
            : vehicleService.createVehicle(vehicleData);

        request
            .then(() => {
                console.log("Vehicle saved successfully.");
                // After saving, fetch all vehicles
                console.log("About to fetch vehicles...");
                return fetchVehicles();
            })
            .then(() => {
                console.log("Vehicles fetched, resetting form...");
                // Reset form after successful save
                resetForm();
            })
            .catch((err) => {
                console.error("Failed to save vehicle:", err);
                setError("Failed to save vehicle.");
            });
    };

    const handleEdit = (vehicle) => {
        setEditingVehicle(vehicle);
        setFormData({
            vehicleID: vehicle.vehicleID || '',
            plateNumber: vehicle.carPlateNumber || '',
            make: vehicle.carMake || '',
            colour: vehicle.carColour || '',
            model: vehicle.carModel || '',
            customer: vehicle.customer || { userId: 1 }
        });
        setShowForm(true);
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this vehicle?')) {
            try {
                await vehicleService.deleteVehicle(id);
                await fetchVehicles();
            } catch (err) {
                setError('Failed to delete vehicle');
                console.error(err);
            }
        }
    };

    const resetForm = () => {
        setFormData({
            vehicleID: '',
            plateNumber: '',
            make: '',
            colour: '',
            model: '',
            customer: { userId: 1 }
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
                    <button onClick={() => setError(null)}>×</button>
                </div>
            )}

            {showForm && (
                <div className="form-container">
                    <h2>{editingVehicle ? 'Edit Vehicle' : 'Add New Vehicle'}</h2>
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

                        <div className="form-group">
                            <label htmlFor="customerId">Customer ID *</label>
                            <input
                                type="number"
                                id="customerId"
                                name="customerId"
                                value={formData.customer.userId}
                                onChange={handleInputChange}
                                required
                                placeholder="Enter customer ID"
                            />
                        </div>

                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary">
                                {editingVehicle ? 'Update Vehicle' : 'Add Vehicle'}
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

            <div className="vehicles-list">
                {vehicles.length === 0 ? (
                    <div className="no-data">
                        <p>No vehicles found. Add your first vehicle above.</p>
                    </div>
                ) : (
                    <div className="vehicles-grid">
                        {vehicles.map((vehicle) => (
                            <div className="vehicle-card" key={vehicle.vehicleID}>
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
                                        className="btn-icon edit"
                                        onClick={() => handleEdit(vehicle)}
                                        title="Edit vehicle"
                                    >
                                        ✏️
                                    </button>
                                    <button
                                        className="btn-icon delete"
                                        onClick={() => handleDelete(vehicle.vehicleID)}
                                        title="Delete vehicle"
                                    >
                                        🗑️
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default VehiclePage;
