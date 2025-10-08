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

// Create authenticated axios instance with JWT token
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor for JWT authentication
apiClient.interceptors.request.use(
    (config) => {
        // Get JWT token from localStorage
        const token = localStorage.getItem('authToken') || localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        console.log('API Client Request:', config.method?.toUpperCase(), config.url);
        console.log('Auth token present:', !!token);
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Request interceptor for logging (basic api)
api.interceptors.request.use(
    (config) => {
        console.log('API Request:', config.method?.toUpperCase(), config.url);
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

// Response interceptor for apiClient error handling
apiClient.interceptors.response.use(
    (response) => {
        console.log('API Client Response:', response.status, response.config.url);
        return response;
    },
    (error) => {
        console.error('API Client Error:', error.response?.status, error.response?.data);
        if (error.response?.status === 401) {
            console.error('Authentication failed - token may be invalid or expired');
            // Optionally clear the token and redirect to login
            localStorage.removeItem('authToken');
            localStorage.removeItem('token');
        }
        return Promise.reject(error);
    }
);

export default api;
export { apiClient };
