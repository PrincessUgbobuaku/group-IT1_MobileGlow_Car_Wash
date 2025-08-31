import api from './api';

const CUSTOMER_API = '/api/customers';

export const profileService = {
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
            return response.data;
        } catch (error) {
            console.error('Error creating customer:', error);
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
            console.error(`Error finding customers by surname ${surname}:`, error);
            throw error;
        }
    },

    // Find active customers
    findActiveCustomers: async () => {
        try {
            const response = await api.get(`${CUSTOMER_API}/active`);
            return response.data;
        } catch (error) {
            console.error('Error finding active customers:', error);
            throw error;
        }
    }
};
