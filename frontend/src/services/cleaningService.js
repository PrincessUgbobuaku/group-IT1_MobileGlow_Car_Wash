import api from './api';

const CLEANING_SERVICE_API = '/api/cleaningservice';

export const cleaningServiceService = {
    // Get all cleaning services
    getAllCleaningServices: async () => {
        try {
            const response = await api.get(`${CLEANING_SERVICE_API}/getAll`);
            return response.data;
        } catch (error) {
            console.error('Error fetching cleaning services:', error);
            throw error;
        }
    },

    // Get cleaning service by ID
    getCleaningServiceById: async (id) => {
        try {
            const response = await api.get(`${CLEANING_SERVICE_API}/read/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching cleaning service ${id}:`, error);
            throw error;
        }
    },

    // Create new cleaning service
    createCleaningService: async (serviceData) => {
        try {
            const response = await api.post(`${CLEANING_SERVICE_API}/create`, serviceData);
            return { success: true, data: response.data };
        } catch (error) {
            if (error.response?.status === 400 && error.response?.data?.message?.includes('already exists')) {
                return { success: false, error: 'DUPLICATE', message: 'Service already exists with this name' };
            }
            throw error;
        }
    },

    // Update cleaning service
    updateCleaningService: async (id, serviceData) => {
        try {
            const response = await api.put(`${CLEANING_SERVICE_API}/update/${id}`, serviceData);
            return response.data;
        } catch (error) {
            console.error(`Error updating cleaning service ${id}:`, error);
            throw error;
        }
    },

    // Delete cleaning service
    deleteCleaningService: async (id) => {
        try {
            const response = await api.delete(`${CLEANING_SERVICE_API}/delete/${id}`);
            return response.status === 204; // Success if no content
        } catch (error) {
            console.error(`Error deleting cleaning service ${id}:`, error);
            throw error;
        }
    }
};
