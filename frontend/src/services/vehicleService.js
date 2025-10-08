import { apiClient } from './api';

const VEHICLE_API = '/api/vehicle';

export const vehicleService = {
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
            const response = await apiClient.get(`${VEHICLE_API}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching vehicle ${id}:`, error);
            throw error;
        }
    },

    // Create new vehicle
    createVehicle: async (vehicleData) => {
        try {
            console.log('Creating vehicle with data:', vehicleData);
            const response = await apiClient.post(`${VEHICLE_API}/create`, vehicleData);
            console.log('Create response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error creating vehicle:', error);
            throw error;
        }
    },

    // Update vehicle
    updateVehicle: async (id, vehicleData) => {
        try {
            console.log('Updating vehicle with ID:', id, 'Data:', vehicleData);
            console.log('ID type:', typeof id);
            console.log('Full URL:', `${VEHICLE_API}/update/${id}`);
            const response = await apiClient.put(`${VEHICLE_API}/update/${id}`, vehicleData);
            console.log('Update response status:', response.status);
            console.log('Update response data:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error updating vehicle:', error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            throw error;
        }
    },

    // Delete vehicle
    deleteVehicle: async (id) => {
        try {
            console.log('Deleting vehicle with ID:', id);
            console.log('ID type:', typeof id);
            console.log('Full URL:', `${VEHICLE_API}/delete/${id}`);
            const response = await apiClient.delete(`${VEHICLE_API}/delete/${id}`);
            console.log('Delete response status:', response.status);
            console.log('Delete response data:', response.data);
            return response.status === 204; // Success if no content
        } catch (error) {
            console.error(`Error deleting vehicle ${id}:`, error);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
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
