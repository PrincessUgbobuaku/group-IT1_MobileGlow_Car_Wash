import api from './api';

const CUSTOMER_API = '/api/customers';

export const customerService = {
    // Get all customers
    getAllCustomers: async () => {
        try {
            const response = await api.get(CUSTOMER_API);
            return response.data;
        } catch (error) {
            console.error('Error fetching customers:', error);
            throw error;
        }
    },

    // Get customer by ID
    getCustomerById: async (id) => {
        try {
            const response = await api.get(`${CUSTOMER_API}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching customer ${id}:`, error);
            throw error;
        }
    },

    // Create new customer
    createCustomer: async (customerData) => {
        try {
            const response = await api.post(CUSTOMER_API, customerData);
            return { success: true, data: response.data };
        } catch (error) {
            throw error;
        }
    },

    // Update customer
    updateCustomer: async (id, customerData) => {
        try {
            const response = await api.put(`${CUSTOMER_API}/${id}`, customerData);
            return response.data;
        } catch (error) {
            console.error(`Error updating customer ${id}:`, error);
            throw error;
        }
    },

    // Delete customer
    deleteCustomer: async (id) => {
        try {
            const response = await api.delete(`${CUSTOMER_API}/${id}`);
            return response.status === 204; // Success if no content
        } catch (error) {
            console.error(`Error deleting customer ${id}:`, error);
            throw error;
        }
    },

    // Find customers by surname
    findCustomersBySurname: async (surname) => {
        try {
            const response = await api.get(`${CUSTOMER_API}/surname/${surname}`);
            return response.data;
        } catch (error) {
            console.error(`Error finding customers with surname ${surname}:`, error);
            throw error;
        }
    },

    // Get active customers
    getActiveCustomers: async () => {
        try {
            const response = await api.get(`${CUSTOMER_API}/active`);
            return response.data;
        } catch (error) {
            console.error('Error fetching active customers:', error);
            throw error;
        }
    },

    // Toggle customer status (activate/deactivate)
    toggleCustomerStatus: async (id) => {
        try {
            console.log('Calling toggle status API for customer:', id);
            const response = await api.put(`${CUSTOMER_API}/${id}/toggle-status`);
            console.log('Toggle status API response:', response);
            return response.data;
        } catch (error) {
            console.error(`Error toggling customer status ${id}:`, error);
            throw error;
        }
    }
};
