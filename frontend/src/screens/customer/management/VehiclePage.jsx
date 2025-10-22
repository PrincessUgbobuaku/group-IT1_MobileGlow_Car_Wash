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

    // Vehicle makes and models data
    const vehicleMakes = [
        'Toyota', 'Honda', 'Ford', 'Chevrolet', 'BMW', 'Mercedes-Benz', 'Audi', 'Volkswagen',
        'Nissan', 'Hyundai', 'Kia', 'Mazda', 'Subaru', 'Lexus', 'Infiniti', 'Acura',
        'Volvo', 'Jaguar', 'Land Rover', 'Porsche', 'Tesla', 'Genesis', 'Lincoln', 'Cadillac',
        'Buick', 'Chrysler', 'Dodge', 'Jeep', 'Ram', 'GMC', 'Mitsubishi', 'Suzuki',
        'Isuzu', 'Fiat', 'Alfa Romeo', 'Maserati', 'Bentley', 'Rolls-Royce', 'Aston Martin',
        'McLaren', 'Ferrari', 'Lamborghini', 'Bugatti', 'Koenigsegg', 'Pagani'
    ];

    const vehicleModels = {
        'Toyota': ['Camry', 'Corolla', 'RAV4', 'Highlander', 'Prius', 'Avalon', 'Sienna', 'Tacoma', 'Tundra', '4Runner', 'Sequoia', 'Land Cruiser', 'Yaris', 'C-HR', 'Venza', 'RunX'],
        'Honda': ['Civic', 'Accord', 'CR-V', 'Pilot', 'HR-V', 'Passport', 'Ridgeline', 'Insight', 'Fit', 'Odyssey', 'Element', 'S2000', 'NSX'],
        'Ford': ['F-150', 'Escape', 'Explorer', 'Expedition', 'Mustang', 'Focus', 'Fusion', 'Edge', 'Bronco', 'Ranger', 'Transit', 'EcoSport', 'Flex'],
        'Chevrolet': ['Silverado', 'Equinox', 'Traverse', 'Tahoe', 'Suburban', 'Camaro', 'Corvette', 'Malibu', 'Cruze', 'Sonic', 'Spark', 'Trax', 'Blazer'],
        'BMW': ['3 Series', '5 Series', '7 Series', 'X1', 'X3', 'X5', 'X7', 'Z4', 'i3', 'i8', 'M2', 'M3', 'M4', 'M5', 'M8'],
        'Mercedes-Benz': ['C-Class', 'E-Class', 'S-Class', 'A-Class', 'CLA', 'CLS', 'GLA', 'GLC', 'GLE', 'GLS', 'G-Class', 'SL', 'AMG GT', 'Sprinter'],
        'Audi': ['A3', 'A4', 'A6', 'A8', 'Q3', 'Q5', 'Q7', 'Q8', 'TT', 'R8', 'e-tron', 'RS3', 'RS4', 'RS5', 'RS6', 'RS7'],
        'Volkswagen': ['Jetta', 'Passat', 'Golf', 'Tiguan', 'Atlas', 'Beetle', 'CC', 'Arteon', 'ID.4', 'Touareg', 'Golf R', 'GTI'],
        'Nissan': ['Altima', 'Sentra', 'Maxima', 'Rogue', 'Murano', 'Pathfinder', 'Armada', 'Frontier', 'Titan', '370Z', 'GT-R', 'Leaf', 'Versa'],
        'Hyundai': ['Elantra', 'Sonata', 'Tucson', 'Santa Fe', 'Palisade', 'Kona', 'Venue', 'Veloster', 'Genesis', 'Ioniq', 'Nexo'],
        'Kia': ['Forte', 'Optima', 'Sorento', 'Telluride', 'Sportage', 'Soul', 'Stinger', 'Niro', 'Cadenza', 'Sedona', 'Rio'],
        'Mazda': ['Mazda3', 'Mazda6', 'CX-3', 'CX-5', 'CX-9', 'MX-5 Miata', 'CX-30', 'Mazda2', 'RX-7', 'RX-8'],
        'Subaru': ['Impreza', 'Legacy', 'Outback', 'Forester', 'Ascent', 'WRX', 'BRZ', 'Crosstrek', 'Tribeca', 'Baja'],
        'Lexus': ['ES', 'IS', 'GS', 'LS', 'NX', 'RX', 'GX', 'LX', 'LC', 'RC', 'CT', 'HS', 'SC'],
        'Infiniti': ['Q50', 'Q60', 'Q70', 'QX50', 'QX60', 'QX80', 'G37', 'M37', 'FX35', 'EX35', 'JX35'],
        'Acura': ['ILX', 'TLX', 'RLX', 'RDX', 'MDX', 'NSX', 'TSX', 'TL', 'RL', 'RSX', 'Integra', 'Legend'],
        'Volvo': ['S60', 'S90', 'V60', 'V90', 'XC40', 'XC60', 'XC90', 'C30', 'C70', 'S40', 'S80', 'V40', 'V70'],
        'Jaguar': ['XE', 'XF', 'XJ', 'F-PACE', 'E-PACE', 'I-PACE', 'F-TYPE', 'XK', 'S-TYPE', 'X-TYPE'],
        'Land Rover': ['Range Rover', 'Range Rover Sport', 'Range Rover Evoque', 'Range Rover Velar', 'Discovery', 'Discovery Sport', 'Defender'],
        'Porsche': ['911', 'Cayenne', 'Macan', 'Panamera', 'Taycan', 'Boxster', 'Cayman', '718', '918 Spyder', 'Carrera GT'],
        'Tesla': ['Model S', 'Model 3', 'Model X', 'Model Y', 'Roadster', 'Cybertruck', 'Semi'],
        'Genesis': ['G70', 'G80', 'G90', 'GV70', 'GV80', 'Coupe'],
        'Lincoln': ['Continental', 'MKZ', 'MKX', 'MKC', 'Navigator', 'Aviator', 'Corsair', 'Town Car'],
        'Cadillac': ['ATS', 'CTS', 'XTS', 'CT6', 'XT4', 'XT5', 'XT6', 'Escalade', 'SRX', 'XLR'],
        'Buick': ['Encore', 'Envision', 'Enclave', 'LaCrosse', 'Regal', 'Verano', 'Cascada', 'Century'],
        'Chrysler': ['300', 'Pacifica', 'Voyager', 'Town & Country', 'Sebring', 'Crossfire', 'PT Cruiser'],
        'Dodge': ['Challenger', 'Charger', 'Durango', 'Journey', 'Grand Caravan', 'Dart', 'Avenger', 'Caliber'],
        'Jeep': ['Wrangler', 'Grand Cherokee', 'Cherokee', 'Compass', 'Renegade', 'Gladiator', 'Commander', 'Liberty'],
        'Ram': ['1500', '2500', '3500', 'ProMaster', 'ProMaster City', 'Dakota'],
        'GMC': ['Sierra', 'Canyon', 'Acadia', 'Terrain', 'Yukon', 'Savana', 'Envoy', 'Denali'],
        'Mitsubishi': ['Outlander', 'Eclipse Cross', 'Mirage', 'Lancer', 'Galant', 'Endeavor', 'Montero', 'Diamante'],
        'Suzuki': ['Swift', 'SX4', 'Grand Vitara', 'Kizashi', 'Equator', 'XL7', 'Aerio', 'Forenza'],
        'Isuzu': ['D-Max', 'MU-X', 'Trooper', 'Rodeo', 'Ascender', 'i-Series', 'Axiom'],
        'Fiat': ['500', '500L', '500X', 'Panda', 'Tipo', 'Doblo', 'Ducato', '124 Spider'],
        'Alfa Romeo': ['Giulia', 'Stelvio', '4C', 'Spider', 'GTV', '156', '147', '159'],
        'Maserati': ['Ghibli', 'Quattroporte', 'Levante', 'GranTurismo', 'GranCabrio', 'MC20'],
        'Bentley': ['Continental', 'Flying Spur', 'Bentayga', 'Mulsanne', 'Azure', 'Arnage'],
        'Rolls-Royce': ['Phantom', 'Ghost', 'Wraith', 'Dawn', 'Cullinan', 'Silver Shadow'],
        'Aston Martin': ['DB11', 'Vantage', 'DBS', 'Rapide', 'Vanquish', 'DB9', 'V8 Vantage'],
        'McLaren': ['720S', '570S', '600LT', 'GT', 'Senna', 'P1', '650S', '540C'],
        'Ferrari': ['488', 'F8', 'SF90', 'Roma', 'Portofino', '812', 'LaFerrari', 'California'],
        'Lamborghini': ['Huracán', 'Aventador', 'Urus', 'Gallardo', 'Murciélago', 'Countach'],
        'Bugatti': ['Chiron', 'Veyron', 'Divo', 'Centodieci', 'La Voiture Noire'],
        'Koenigsegg': ['Regera', 'Jesko', 'Gemera', 'CCX', 'CCR', 'Agera'],
        'Pagani': ['Huayra', 'Zonda', 'Utopia', 'Imola', 'C10']
    };

    // Vehicle colors data
    const vehicleColors = [
        'White', 'Black', 'Silver', 'Gray', 'Red', 'Blue', 'Green', 'Brown', 'Beige', 'Gold',
        'Yellow', 'Orange', 'Purple', 'Pink', 'Maroon', 'Navy', 'Turquoise', 'Cream', 'Tan',
        'Champagne', 'Pearl White', 'Metallic Black', 'Metallic Silver', 'Metallic Gray',
        'Metallic Blue', 'Metallic Red', 'Metallic Green', 'Metallic Brown', 'Metallic Gold',
        'Matte Black', 'Matte Gray', 'Matte Blue', 'Carbon Black', 'Jet Black', 'Arctic White',
        'Alpine White', 'Mineral White', 'Space Gray', 'Midnight Blue', 'Deep Blue', 'Royal Blue',
        'Electric Blue', 'Bright Red', 'Crimson Red', 'Fire Red', 'Racing Green', 'Forest Green',
        'Emerald Green', 'British Racing Green', 'Champagne Gold', 'Rose Gold', 'Copper',
        'Bronze', 'Gunmetal', 'Titanium', 'Platinum', 'Pearl', 'Iridescent', 'Chrome'
    ];

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
        setFormData(prev => {
            const newData = {
                ...prev,
                [name]: value
            };
            
            // If make changes, reset model
            if (name === 'make') {
                newData.model = '';
            }
            
            return newData;
        });
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


    const renderVehicleTable = () => (
        <div className="em-table-container">
            <div className="em-table-header">
                <h2>Vehicle List</h2>
                <div className="em-table-actions">
                    <button
                        className="em-secondary-btn"
                        onClick={fetchVehicles}
                    >
                        Refresh List
                    </button>
                </div>
            </div>

            <div className="table-wrapper">
                <table className="em-table">
                    <thead>
                        <tr>
                            <th>Plate Number</th>
                            <th>Make</th>
                            <th>Model</th>
                            <th>Color</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {vehicles.length === 0 ? (
                            <tr>
                                <td colSpan="5" style={{ textAlign: "center", padding: "2rem", color: "#6b7280" }}>
                                    No vehicles found. Add your first vehicle above.
                                </td>
                            </tr>
                        ) : (
                            vehicles.map(vehicle => (
                                <tr key={vehicle.vehicleID} className={editingVehicle && editingVehicle.vehicleID === vehicle.vehicleID ? 'editing' : ''}>
                                    <td>
                                        <div className="vehicle-plate">
                                            <strong>{vehicle.carPlateNumber}</strong>
                                        </div>
                                    </td>
                                    <td>
                                        <span className="vehicle-make">{vehicle.carMake || 'N/A'}</span>
                                    </td>
                                    <td>
                                        <div className="vehicle-model">{vehicle.carModel || 'N/A'}</div>
                                    </td>
                                    <td>
                                        <div className="vehicle-color">{vehicle.carColour || 'N/A'}</div>
                                    </td>
                                    <td>
                                        <div className="vehicle-actions">
                                            <button
                                                className="edit-btn"
                                                onClick={() => handleEdit(vehicle)}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                className="delete-btn"
                                                onClick={() => handleDelete(vehicle.vehicleID)}
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
                        <select
                            id="make"
                            name="make"
                            value={formData.make}
                            onChange={handleInputChange}
                            required
                            className="form-select"
                        >
                            <option value="">Select a make...</option>
                            {vehicleMakes.map(make => (
                                <option key={make} value={make}>
                                    {make}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="model">Model *</label>
                        <select
                            id="model"
                            name="model"
                            value={formData.model}
                            onChange={handleInputChange}
                            required
                            className="form-select"
                            disabled={!formData.make}
                        >
                            <option value="">Select a model...</option>
                            {formData.make && vehicleModels[formData.make] && vehicleModels[formData.make].map(model => (
                                <option key={model} value={model}>
                                    {model}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="colour">Color *</label>
                        <select
                            id="colour"
                            name="colour"
                            value={formData.colour}
                            onChange={handleInputChange}
                            required
                            className="form-select"
                        >
                            <option value="">Select a color...</option>
                            {vehicleColors.map(color => (
                                <option key={color} value={color}>
                                    {color}
                                </option>
                            ))}
                        </select>
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
                {renderVehicleTable()}
                {showForm && renderVehicleForm()}
            </div>
            
            {/* Continue Booking Section */}
            <div className="continue-booking-section">
                <button
                    className="btn continue-booking-btn"
                    onClick={() => navigate('/bookingvehicle')}
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