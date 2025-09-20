// import React, { useState, useEffect } from 'react';
// import { cleaningServiceService } from '../services/cleaningService';
// import './CleaningServiceManagement.css';
//
// const CleaningServiceManagement = () => {
//     const [services, setServices] = useState([]);
//     const [loading, setLoading] = useState(false);
//     const [error, setError] = useState(null);
//     const [showForm, setShowForm] = useState(false);
//     const [editingService, setEditingService] = useState(null);
//     const [validationPopup, setValidationPopup] = useState({ show: false, message: '' });
//     const [formData, setFormData] = useState({
//         serviceName: '',
//         category: '',
//         priceOfService: '',
//         duration: ''
//     });
//
//     useEffect(() => {
//         fetchServices();
//     }, []);
//
//     const fetchServices = async () => {
//         setLoading(true);
//         try {
//             const data = await cleaningServiceService.getAllCleaningServices();
//             setServices(data);
//             setError(null);
//         } catch (err) {
//             setError('Failed to fetch services');
//             console.error('Error:', err);
//         } finally {
//             setLoading(false);
//         }
//     };
//
//     const handleInputChange = (e) => {
//         const { name, value, type, checked } = e.target;
//         setFormData(prev => ({
//             ...prev,
//             [name]: type === 'checkbox' ? checked : value
//         }));
//     };
//
//     const resetForm = () => {
//         setFormData({
//             serviceName: '',
//             category: '',
//             priceOfService: '',
//             duration: ''
//         });
//         setEditingService(null);
//         setShowForm(false);
//     };
//
//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         setLoading(true);
//
//         try {
//             // Frontend duplicate check
//             if (!editingService) {
//                 const isDuplicate = services.some(service =>
//                     service.serviceName.toLowerCase() === formData.serviceName.toLowerCase()
//                 );
//
//                 if (isDuplicate) {
//                     setValidationPopup({
//                         show: true,
//                         message: 'Service already exists with this name'
//                     });
//                     resetForm();
//                     setLoading(false);
//                     return;
//                 }
//             }
//
//             // Convert string values to proper types for backend
//             const processedData = {
//                 ...formData,
//                 priceOfService: parseFloat(formData.priceOfService),
//                 duration: parseFloat(formData.duration)
//             };
//
//             if (editingService) {
//                 // Update existing service
//                 await cleaningServiceService.updateCleaningService(editingService.cleaningServiceID, processedData);
//                 setError(null);
//                 resetForm();
//                 fetchServices(); // Refresh the list
//             } else {
//                 // Create new service
//                 const result = await cleaningServiceService.createCleaningService(processedData);
//
//                 if (result.success) {
//                     setError(null);
//                     resetForm();
//                     fetchServices(); // Refresh the list
//                 } else if (result.error === 'DUPLICATE') {
//                     // Show duplicate error popup
//                     setValidationPopup({ show: true, message: result.message });
//                     resetForm();
//                 } else {
//                     setError('Failed to create service');
//                 }
//             }
//         } catch (err) {
//             setError(editingService ? 'Failed to update service' : 'Failed to create service');
//             console.error('Error:', err);
//         } finally {
//             setLoading(false);
//         }
//     };
//
//     const handleEdit = (service) => {
//         setEditingService(service);
//         setFormData({
//             serviceName: service.serviceName || '',
//             category: service.category || '',
//             priceOfService: service.priceOfService || '',
//             duration: service.duration || ''
//         });
//         setShowForm(true);
//     };
//
//     const handleDelete = async (id) => {
//         if (window.confirm('Are you sure you want to delete this service?')) {
//             setLoading(true);
//             try {
//                 await cleaningServiceService.deleteCleaningService(id);
//                 setError(null);
//                 fetchServices(); // Refresh the list
//             } catch (err) {
//                 setError('Failed to delete service');
//                 console.error('Error:', err);
//             } finally {
//                 setLoading(false);
//             }
//         }
//     };
//
//     const toggleAvailability = async (service) => {
//         // This functionality is not supported by the backend domain model
//         // The isAvailable field doesn't exist in the CleaningService entity
//         console.log('Availability toggle not supported by backend');
//     };
//
//     if (loading && services.length === 0) {
//         return (
//             <div className="loading-container">
//                 <div className="loading-spinner"></div>
//                 <p>Loading services...</p>
//             </div>
//         );
//     }
//
//     return (
//         <div className="cleaning-service-management">
//             <div className="header">
//                 <h1>Cleaning Service Management</h1>
//                 <p>Manage all car cleaning services</p>
//                 <div className="admin-links">
//                     <a href="/profile-management" className="admin-link">👥 Profile Management</a>
//                     <a href="/vehicles" className="admin-link">🚗 Vehicle Management</a>
//                 </div>
//             </div>
//
//             {error && (
//                 <div className="error-message">
//                     {error}
//                     <button onClick={() => setError(null)}>×</button>
//                 </div>
//             )}
//
//             <div className="actions-bar">
//                 <button
//                     className="btn btn-primary"
//                     onClick={() => setShowForm(true)}
//                 >
//                     + Add New Service
//                 </button>
//             </div>
//
//             {showForm && (
//                 <div className="form-overlay">
//                     <div className="form-container">
//                         <div className="form-header">
//                             <h2>{editingService ? 'Edit Service' : 'Add New Service'}</h2>
//                             <button className="close-btn" onClick={resetForm}>×</button>
//                         </div>
//
//                         {/* Validation Popup */}
//                         {validationPopup.show && (
//                             <div className="validation-popup">
//                                 <div className="validation-content">
//                                     <div className="validation-icon">⚠️</div>
//                                     <p className="validation-message">{validationPopup.message}</p>
//                                     <button
//                                         className="btn btn-primary"
//                                         onClick={() => setValidationPopup({ show: false, message: '' })}
//                                     >
//                                         OK
//                                     </button>
//                                 </div>
//                             </div>
//                         )}
//
//                         <form onSubmit={handleSubmit} className="service-form">
//                             <div className="form-group">
//                                 <label htmlFor="serviceName">Service Name *</label>
//                                 <input
//                                     type="text"
//                                     id="serviceName"
//                                     name="serviceName"
//                                     value={formData.serviceName}
//                                     onChange={handleInputChange}
//                                     required
//                                     placeholder="e.g., Exterior Wash & Wax"
//                                 />
//                             </div>
//
//                             <div className="form-group">
//                                 <label htmlFor="category">Category *</label>
//                                 <input
//                                     type="text"
//                                     id="category"
//                                     name="category"
//                                     value={formData.category}
//                                     onChange={handleInputChange}
//                                     required
//                                     placeholder="e.g., Exterior, Interior, Wax, etc."
//                                 />
//                             </div>
//
//                             <div className="form-row">
//                                 <div className="form-group">
//                                     <label htmlFor="priceOfService">Price (R) *</label>
//                                     <input
//                                         type="number"
//                                         id="priceOfService"
//                                         name="priceOfService"
//                                         value={formData.priceOfService}
//                                         onChange={handleInputChange}
//                                         required
//                                         min="0"
//                                         step="0.01"
//                                         placeholder="150.00"
//                                     />
//                                 </div>
//
//                                 <div className="form-group">
//                                     <label htmlFor="duration">Duration (hours) *</label>
//                                     <input
//                                         type="number"
//                                         id="duration"
//                                         name="duration"
//                                         value={formData.duration}
//                                         onChange={handleInputChange}
//                                         required
//                                         min="0.5"
//                                         step="0.5"
//                                         placeholder="1.5"
//                                     />
//                                 </div>
//                             </div>
//
//                             <div className="form-actions">
//                                 <button type="button" className="btn btn-secondary" onClick={resetForm}>
//                                     Cancel
//                                 </button>
//                                 <button type="submit" className="btn btn-primary" disabled={loading}>
//                                     {loading ? 'Saving...' : (editingService ? 'Update Service' : 'Create Service')}
//                                 </button>
//                             </div>
//                         </form>
//                     </div>
//                 </div>
//             )}
//
//             <div className="services-grid">
//                 {services.map(service => (
//                     <div className="service-card" key={service.cleaningServiceID}>
//                         <div className="service-header">
//                             <h3>{service.serviceName}</h3>
//                         </div>
//
//                         <div className="service-details">
//                             <div className="service-info">
//                                 <div className="info-item">
//                                     <span className="label">Category:</span>
//                                     <span className="value">{service.category || 'N/A'}</span>
//                                 </div>
//                                 <div className="info-item">
//                                     <span className="label">Price:</span>
//                                     <span className="value">R{service.priceOfService}</span>
//                                 </div>
//                                 <div className="info-item">
//                                     <span className="label">Duration:</span>
//                                     <span className="value">{service.duration} hours</span>
//                                 </div>
//                                 <div className="info-item">
//                                     <span className="label">ID:</span>
//                                     <span className="value">{service.cleaningServiceID}</span>
//                                 </div>
//                             </div>
//                         </div>
//
//                         <div className="service-actions">
//                             <button
//                                 className="btn btn-outline"
//                                 onClick={() => handleEdit(service)}
//                             >
//                                 Edit
//                             </button>
//                             <button
//                                 className="btn btn-danger"
//                                 onClick={() => handleDelete(service.cleaningServiceID)}
//                             >
//                                 Delete
//                             </button>
//                         </div>
//                     </div>
//                 ))}
//             </div>
//
//             {services.length === 0 && !loading && (
//                 <div className="empty-state">
//                     <div className="empty-icon">🚗</div>
//                     <h3>No services found</h3>
//                     <p>Create your first cleaning service to get started</p>
//                     <button className="btn btn-primary" onClick={() => setShowForm(true)}>
//                         Add First Service
//                     </button>
//                 </div>
//             )}
//         </div>
//     );
// };
//
// export default CleaningServiceManagement;
