
//api.js is a helper file that sets up how the app talks to the server. 
//It adds the login token to requests so the server knows the user is logged in and allows updates.

import axios from 'axios';

// Base URL for the Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/mobileglow';

// Create axios instance with default configuration
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor for logging and auth
api.interceptors.request.use(
    (config) => {
        console.log('API Request:', config.method?.toUpperCase(), config.url);
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor for error handling
api.interceptors.response.use(
    (response) => {
        console.log('API Response:', response.status, response.config.url);
        return response;
    },
    (error) => {
        console.error('API Error:', error.response?.status, error.response?.data);
        return Promise.reject(error);
    }
);

export default api;
