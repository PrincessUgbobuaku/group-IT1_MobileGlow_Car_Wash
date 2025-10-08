// src/services/employeeService.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/mobileglow';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// CORRECTED SERVICE - MATCHES YOUR ACTUAL CONTROLLERS
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

    // Wash Attendant endpoints - CORRECTED to match your controller
    getAllWashAttendants: () => api.get('/wash-attendants/getAllWashAttendants'),
    getWashAttendant: (id) => api.get(`/wash-attendants/read/${id}`),
    createWashAttendant: (washAttendant) => api.post('/wash-attendants/create', washAttendant),
    updateWashAttendant: (washAttendant) => api.put('/wash-attendants/update', washAttendant),
    deleteWashAttendant: (id) => api.delete(`/wash-attendants/delete/${id}`),

    // Bonus: Random wash attendant endpoint from your controller
    getRandomWashAttendant: () => api.get('/wash-attendants/random')
};

// Enhanced service with error handling
export const employeeService = {
    // Get all employees from all endpoints
    getAllEmployees: async () => {
        try {
            const [managers, accountants, washAttendants] = await Promise.all([
                employeeServiceSimple.getAllManagers().then(res => res.data.map(emp => ({ ...emp, type: 'Manager' }))),
                employeeServiceSimple.getAllAccountants().then(res => res.data.map(emp => ({ ...emp, type: 'Accountant' }))),
                employeeServiceSimple.getAllWashAttendants().then(res => res.data.map(emp => ({ ...emp, type: 'Wash Attendant' })))
            ]);

            return [...managers, ...accountants, ...washAttendants];
        } catch (error) {
            console.error('Failed to fetch employees:', error);
            throw new Error('Failed to fetch employees: ' + error.message);
        }
    },

    // Generic methods
    createEmployee: async (employeeData) => {
        const type = employeeData.type;
        switch (type) {
            case 'Manager':
                return employeeServiceSimple.createManager(employeeData);
            case 'Accountant':
                return employeeServiceSimple.createAccountant(employeeData);
            case 'Wash Attendant':
                return employeeServiceSimple.createWashAttendant(employeeData);
            default:
                throw new Error(`Unknown employee type: ${type}`);
        }
    },

    getEmployee: async (id, type) => {
        switch (type) {
            case 'Manager':
                return employeeServiceSimple.getManager(id);
            case 'Accountant':
                return employeeServiceSimple.getAccountant(id);
            case 'Wash Attendant':
                return employeeServiceSimple.getWashAttendant(id);
            default:
                throw new Error(`Unknown employee type: ${type}`);
        }
    }
};

// Debugging interceptors
api.interceptors.request.use(
    (config) => {
        console.log(`🚀 ${config.method?.toUpperCase()} to: ${config.baseURL}${config.url}`);
        return config;
    }
);

api.interceptors.response.use(
    (response) => {
        console.log('✅ Response:', response.status, response.data);
        return response;
    },
    (error) => {
        console.error('❌ API Error:');
        console.error('URL:', error.config?.url);
        console.error('Status:', error.response?.status);
        console.error('Data:', error.response?.data);
        console.error('Message:', error.message);

        // Check for CORS error
        if (error.message.includes('Network Error') || error.message.includes('CORS')) {
            console.error('🔴 CORS ERROR: Backend not allowing requests from frontend');
            console.error('💡 Solution: Add @CrossOrigin(origins = "http://localhost:3000") to your controllers');
        }

        return Promise.reject(error);
    }
);

