import api from './api';

const VEHICLE_API = '/api/vehicle';

export const vehicleService = {
    // Get all vehicles (simple version without customer details)
    getSimpleVehicles: async () => {
        try {
            const response = await api.get(`${VEHICLE_API}/simple`);
            return response.data;
        } catch (error) {
            console.error('Error fetching simple vehicles:', error);
            throw error;
        }
    },

    // Get all vehicles
    getAllVehicles: async () => {
        try {
            const response = await api.get(`${VEHICLE_API}/findAll`);
            return response.data;
        } catch (error) {
            console.error('Error fetching vehicles:', error);
            throw error;
        }
    },

    // Get vehicle by ID
    getVehicleById: async (id) => {
        try {
            const response = await api.get(`${VEHICLE_API}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching vehicle ${id}:`, error);
            throw error;
        }
    },

    // Create new vehicle
    createVehicle: async (vehicleData) => {
        try {
            const response = await api.post(`${VEHICLE_API}/create`, vehicleData);
            return response.data;
        } catch (error) {
            console.error('Error creating vehicle:', error);
            throw error;
        }
    },

    // Update vehicle
    updateVehicle: async (id, vehicleData) => {
        try {
            const response = await api.put(`${VEHICLE_API}/update/${id}`, vehicleData);
            return response.data;
        } catch (error) {
            console.error(`Error updating vehicle ${id}:`, error);
            throw error;
        }
    },

    // Delete vehicle
    deleteVehicle: async (id) => {
        try {
            const response = await api.delete(`${VEHICLE_API}/delete/${id}`);
            return response.status === 204; // Success if no content
        } catch (error) {
            console.error(`Error deleting vehicle ${id}:`, error);
            throw error;
        }
    },

    // Find vehicle by plate number
    findVehicleByPlateNumber: async (plateNumber) => {
        try {
            const response = await api.get(`${VEHICLE_API}/plate/${plateNumber}`);
            return response.data;
        } catch (error) {
            console.error(`Error finding vehicle by plate ${plateNumber}:`, error);
            throw error;
        }
    },

    // Find vehicles by customer ID
    findVehiclesByCustomerId: async (customerId) => {
        try {
            const response = await api.get(`${VEHICLE_API}/customer/${customerId}`);
            return response.data;
        } catch (error) {
            console.error(`Error finding vehicles for customer ${customerId}:`, error);
            throw error;
        }
    }
};
