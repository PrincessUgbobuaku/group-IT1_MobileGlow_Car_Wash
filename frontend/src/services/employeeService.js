// src/services/employeeService.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/mobileglow';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const employeeService = {
    // Get all employees from all endpoints
    getAllEmployees: async () => {
        try {
            const [managers, accountants, washAttendants] = await Promise.all([
                api.get('/Manager/getAllManagers').then(res => res.data.map(emp => ({ ...emp, type: 'Manager' }))),
                api.get('/Accountant/getAllAccountants').then(res => res.data.map(emp => ({ ...emp, type: 'Accountant' }))),
                api.get('/WashAttendant/getAllWashAttendants').then(res => res.data.map(emp => ({ ...emp, type: 'Wash Attendant' })))
            ]);

            return [...managers, ...accountants, ...washAttendants];
        } catch (error) {
            console.error('Failed to fetch employees:', error);
            throw new Error('Failed to fetch employees');
        }
    },

    // Create employee based on type
    createEmployee: async (employeeData) => {
        const endpoint = getEndpointByType(employeeData.type, 'create');
        return api.post(endpoint, employeeData);
    },

    // Get single employee
    getEmployee: async (id, type) => {
        const endpoint = getEndpointByType(type, 'read');
        return api.get(`${endpoint}/${id}`);
    },

    // Update employee
    updateEmployee: async (employeeData) => {
        const endpoint = getEndpointByType(employeeData.type, 'update');
        return api.put(endpoint, employeeData);
    },

    // Delete employee
    deleteEmployee: async (id, type) => {
        const endpoint = getEndpointByType(type, 'delete');
        return api.delete(`${endpoint}/${id}`);
    }
};

const getEndpointByType = (type, operation) => {
    // Remove spaces from type for URL (e.g., "Wash Attendant" -> "WashAttendant")
    const cleanType = type.replace(' ', '');

    switch (operation) {
        case 'create':
            return `/${cleanType}/create`;
        case 'read':
            return `/${cleanType}/read`;
        case 'update':
            return `/${cleanType}/update`;
        case 'delete':
            return `/${cleanType}/delete`;
        case 'getAll':
            return `/${cleanType}/getAll${cleanType}s`;
        default:
            return `/${cleanType}`;
    }
};

// Simple explicit service methods
export const employeeServiceSimple = {
    // Manager endpoints
    getAllManagers: () => api.get('/Manager/getAllManagers'),
    getManager: (id) => api.get(`/Manager/read/${id}`),
    createManager: (manager) => api.post('/Manager/create', manager),
    updateManager: (manager) => api.put('/Manager/update', manager),
    deleteManager: (id) => api.delete(`/Manager/delete/${id}`),

    // Accountant endpoints
    getAllAccountants: () => api.get('/Accountant/getAllAccountants'),
    getAccountant: (id) => api.get(`/Accountant/read/${id}`),
    createAccountant: (accountant) => api.post('/Accountant/create', accountant),
    updateAccountant: (accountant) => api.put('/Accountant/update', accountant),
    deleteAccountant: (id) => api.delete(`/Accountant/delete/${id}`),

    // Wash Attendant endpoints
    getAllWashAttendants: () => api.get('/WashAttendant/getAllWashAttendants'),
    getWashAttendant: (id) => api.get(`/WashAttendant/read/${id}`),
    createWashAttendant: (washAttendant) => api.post('/WashAttendant/create', washAttendant),
    updateWashAttendant: (washAttendant) => api.put('/WashAttendant/update', washAttendant),
    deleteWashAttendant: (id) => api.delete(`/WashAttendant/delete/${id}`),
};

// Add error interceptor for better debugging
api.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error('API Error:', error.response?.data || error.message);
        return Promise.reject(error);
    }
);

export default api;