// src/services/employeeService.js
import api from './api';

export const employeeService = {
    // Get all employees from all endpoints
    getAllEmployees: async () => {
        try {
            const [managers, accountants, washAttendants] = await Promise.all([
                api.get('/Manager/getAllManagers').then(res => res.data.map(emp => ({ ...emp, type: 'Manager' }))),
                api.get('/Accountant/getAllAccountants').then(res => res.data.map(emp => ({ ...emp, type: 'Accountant' }))),
                api.get('/wash-attendants/getAllWashAttendants').then(res => res.data.map(emp => ({ ...emp, type: 'Wash Attendant' })))
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
        const endpoint = getEndpointByType(employeeData.employeeType || employeeData.type, 'update');
        return api.put(`${endpoint}/${employeeData.userId}`, employeeData);
    },

    // Delete employee
    deleteEmployee: async (id, type) => {
        const endpoint = getEndpointByType(type, 'delete');
        return api.delete(`${endpoint}/${id}`);
    }
};

const getEndpointByType = (type, operation) => {
    let base;
    if (type === 'WashAttendant' || type === 'Wash Attendant') {
        base = 'wash-attendants';
    } else {
        base = type;
    }

    switch (operation) {
        case 'create':
            return `/${base}/create`;
        case 'read':
            return `/${base}/read`;
        case 'update':
            return `/${base}/update`;
        case 'delete':
            return `/${base}/delete`;
        case 'getAll':
            return `/${base}/getAll${base.replace('-', '')}s`;
        default:
            return `/${base}`;
    }
};

// Simple explicit service methods
export const employeeServiceSimple = {
    // Manager endpoints
    getAllManagers: () => api.get('/Manager/getAllManagers'),
    getManager: (id) => api.get(`/Manager/read/${id}`),
    createManager: (manager) => api.post('/Manager/create', manager),
    updateManager: (manager) => api.put(`/Manager/update/${manager.userId}`, manager),
    deleteManager: (id) => api.delete(`/Manager/delete/${id}`),

    // Accountant endpoints
    getAllAccountants: () => api.get('/Accountant/getAllAccountants'),
    getAccountant: (id) => api.get(`/Accountant/read/${id}`),
    createAccountant: (accountant) => api.post('/Accountant/create', accountant),
    updateAccountant: (accountant) => api.put(`/Accountant/update/${accountant.userId}`, accountant),
    deleteAccountant: (id) => api.delete(`/Accountant/delete/${id}`),

    // Wash Attendant endpoints
    getAllWashAttendants: () => api.get('/wash-attendants/getAllWashAttendants'),
    getWashAttendant: (id) => api.get(`/wash-attendants/read/${id}`),
    createWashAttendant: (washAttendant) => api.post('/wash-attendants/create', washAttendant),
    updateWashAttendant: (washAttendant) => api.put(`/wash-attendants/update/${washAttendant.userId}`, washAttendant),
    deleteWashAttendant: (id) => api.delete(`/wash-attendants/delete/${id}`),
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