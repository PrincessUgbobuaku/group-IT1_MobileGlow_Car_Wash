import api, { apiClient } from './api';

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
            let data = customerData;
            let headers = {};
            if (customerData instanceof FormData) {
                // If FormData, create new FormData with 'customer' as JSON part
                const formData = new FormData();
                const customerObj = {};
                for (const [key, value] of customerData.entries()) {
                    if (key === 'imageFile') {
                        formData.append('imageFile', value);
                    } else {
                // Flatten keys like contact.phoneNumber to nested object
                const keys = key.split('.');
                let obj = customerObj;
                for (let i = 0; i < keys.length - 1; i++) {
                    if (!obj[keys[i]]) obj[keys[i]] = {};
                    obj = obj[keys[i]];
                }
                // Handle null values properly
                const finalValue = value === "null" || value === "" ? null : value;
                obj[keys[keys.length - 1]] = finalValue;
                    }
                }
                formData.append('customer', new Blob([JSON.stringify(customerObj)], { type: 'application/json' }));
                data = formData;
                headers = { 'Content-Type': 'multipart/form-data' };
            }
            const response = await apiClient.put(`${CUSTOMER_API}/${id}`, data, { headers });
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
