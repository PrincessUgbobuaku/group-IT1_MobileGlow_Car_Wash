// src/services/employeeService.js
import api, { apiClient } from './api';

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
                api.get('/Manager/getAllManagers').then(res => res.data.map(emp => ({ ...emp, type: 'Manager' }))),
                api.get('/Accountant/getAllAccountants').then(res => res.data.map(emp => ({ ...emp, type: 'Accountant' }))),
                api.get('/wash-attendants/getAllWashAttendants').then(res => res.data.map(emp => ({ ...emp, type: 'Wash Attendant' })))
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
        const endpoint = getEndpointByType(type, 'read');
        return api.get(`${endpoint}/${id}`);
    },

    // Update employee
    updateEmployee: async (employeeData) => {
        const type = employeeData.get('employeeType') || employeeData.get('type');
        const userId = employeeData.get('userId');
        const endpoint = getEndpointByType(type, 'update');
        // Create FormData with 'employee' as JSON part
        const formData = new FormData();
        const employeeObj = {};
        for (const [key, value] of employeeData.entries()) {
            if (key === 'imageFile') {
                formData.append('imageFile', value);
            } else {
                // Flatten keys like contact.phoneNumber to nested object
                const keys = key.split('.');
                let obj = employeeObj;
                for (let i = 0; i < keys.length - 1; i++) {
                    if (!obj[keys[i]]) obj[keys[i]] = {};
                    obj = obj[keys[i]];
                }
                obj[keys[keys.length - 1]] = value;
            }
        }
        formData.append('employee', new Blob([JSON.stringify(employeeObj)], { type: 'application/json' }));
        return apiClient.put(`${endpoint}/${userId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
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

