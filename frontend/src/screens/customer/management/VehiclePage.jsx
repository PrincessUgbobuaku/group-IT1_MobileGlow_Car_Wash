// import React, { useState, useEffect } from 'react';
// import { vehicleService } from '../services/vehicleService';
// import LoadingSpinner from '../components/LoadingSpinner';
// import './VehiclePage.css';
//
// const VehiclePage = () => {
//     const [vehicles, setVehicles] = useState([]);
//     const [loading, setLoading] = useState(true);
//     const [error, setError] = useState(null);
//     const [showForm, setShowForm] = useState(false);
//     const [editingVehicle, setEditingVehicle] = useState(null);
//     const [formData, setFormData] = useState({
//         vehicleID: '12',
//         plateNumber: '',
//         make: '',
//         colour: '',
//         model: '',
//         customer: { userId: 1 } // Default customer ID for demo
//     });
//
//     useEffect(() => {
//         fetchVehicles();
//     }, []);
//
//     const fetchVehicles = async () => {
//         try {
//             setLoading(true);
//             const data = await vehicleService.getSimpleVehicles();
//             console.log('Fetched vehicles:', data);
//             console.log('Number of vehicles:', data.length);
//             setVehicles(data);
//             setError(null);
//         } catch (err) {
//             setError('Failed to fetch vehicles');
//             console.error(err);
//         } finally {
//             setLoading(false);
//         }
//     };
//
//     const handleInputChange = (e) => {
//         const { name, value } = e.target;
//         if (name === 'customerId') {
//             setFormData(prev => ({
//                 ...prev,
//                 customer: { userId: parseInt(value) }
//             }));
//         } else {
//             setFormData(prev => ({
//                 ...prev,
//                 [name]: value
//             }));
//         }
//     };
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         // Transform form data to match backend field names
//         const vehicleData = {
//             carPlateNumber: formData.plateNumber,
//             carMake: formData.make,
//             carColour: formData.colour,
//             carModel: formData.model,
//             customerId: formData.customer.userId
//         };
//
//         // Decide if it's an update or create request
//         const request = editingVehicle
//             ? vehicleService.updateVehicle(editingVehicle.vehicleID, vehicleData)
//             : vehicleService.createVehicle(vehicleData);
//
//         request
//             .then(() => {
//                 console.log("Vehicle saved successfully.");
//                 // After saving, fetch all vehicles
//                 console.log("About to fetch vehicles...");
//                 return fetchVehicles();
//             })
//             .then(() => {
//                 console.log("Vehicles fetched, resetting form...");
//                 // Reset form after successful save
//                 resetForm();
//             })
//             .catch((err) => {
//                 console.error("Failed to save vehicle:", err);
//                 setError("Failed to save vehicle.");
//             });
//     };
//
//
//     const handleEdit = (vehicle) => {
//         setEditingVehicle(vehicle);
//         setFormData({
//             vehicleID: vehicle.vehicleID || '',
//             plateNumber: vehicle.carPlateNumber || '',
//             make: vehicle.carMake || '',
//             colour: vehicle.carColour || '',
//             model: vehicle.carModel || '',
//             customer: vehicle.customer || { userId: 1 }
//         });
//         setShowForm(true);
//     };
//
//     const handleDelete = async (id) => {
//         if (window.confirm('Are you sure you want to delete this vehicle?')) {
//             try {
//                 await vehicleService.deleteVehicle(id);
//                 await fetchVehicles();
//             } catch (err) {
//                 setError('Failed to delete vehicle');
//                 console.error(err);
//             }
//         }
//     };
//
//     const resetForm = () => {
//         setFormData({
//             vehicleID: '',
//             plateNumber: '',
//             make: '',
//             colour: '',
//             model: '',
//             customer: { userId: 1 }
//         });
//         setEditingVehicle(null);
//         setShowForm(false);
//     };
//
//     if (loading) {
//         return <LoadingSpinner text="Loading vehicles..." />;
//     }
//
//     return (
//         <div className="vehicle-page">
//             <div className="page-header">
//                 <h1>Vehicle Management</h1>
//                 <button
//                     className="btn btn-primary"
//                     onClick={() => setShowForm(true)}
//                 >
//                     Add New Vehicle
//                 </button>
//             </div>
//
//             {error && (
//                 <div className="error-message">
//                     {error}
//                     <button onClick={() => setError(null)}>×</button>
//                 </div>
//             )}
//
//             {showForm && (
//                 <div className="form-container">
//                     <h2>{editingVehicle ? 'Edit Vehicle' : 'Add New Vehicle'}</h2>
//                     <form onSubmit={handleSubmit} className="vehicle-form">
//                         <div className="form-group">
//                             <label htmlFor="plateNumber">Plate Number *</label>
//                             <input
//                                 type="text"
//                                 id="plateNumber"
//                                 name="plateNumber"
//                                 value={formData.plateNumber}
//                                 onChange={handleInputChange}
//                                 required
//                                 placeholder="Enter plate number"
//                             />
//                         </div>
//
//                         <div className="form-group">
//                             <label htmlFor="make">Make *</label>
//                             <input
//                                 type="text"
//                                 id="make"
//                                 name="make"
//                                 value={formData.make}
//                                 onChange={handleInputChange}
//                                 required
//                                 placeholder="Enter car make"
//                             />
//                         </div>
//
//                         <div className="form-group">
//                             <label htmlFor="model">Model *</label>
//                             <input
//                                 type="text"
//                                 id="model"
//                                 name="model"
//                                 value={formData.model}
//                                 onChange={handleInputChange}
//                                 required
//                                 placeholder="Enter car model"
//                             />
//                         </div>
//
//                         <div className="form-group">
//                             <label htmlFor="colour">Color *</label>
//                             <input
//                                 type="text"
//                                 id="colour"
//                                 name="colour"
//                                 value={formData.colour}
//                                 onChange={handleInputChange}
//                                 required
//                                 placeholder="Enter car color"
//                             />
//                         </div>
//
//                         <div className="form-group">
//                             <label htmlFor="customerId">Customer ID *</label>
//                             <input
//                                 type="number"
//                                 id="customerId"
//                                 name="customerId"
//                                 value={formData.customer.userId}
//                                 onChange={handleInputChange}
//                                 required
//                                 placeholder="Enter customer ID"
//                             />
//                         </div>
//
//                         <div className="form-actions">
//                             <button type="submit" className="btn btn-primary">
//                                 {editingVehicle ? 'Update Vehicle' : 'Add Vehicle'}
//                             </button>
//                             <button
//                                 type="button"
//                                 className="btn btn-secondary"
//                                 onClick={resetForm}
//                             >
//                                 Cancel
//                             </button>
//                         </div>
//                     </form>
//                 </div>
//             )}
//
//             <div className="vehicles-list">
//                 <h2>Vehicles ({vehicles.length})</h2>
//                 {vehicles.length === 0 ? (
//                     <p className="no-data">No vehicles found. Add your first vehicle above.</p>
//                 ) : (
//                     <div className="table-container">
//                         <table className="vehicles-table">
//                             <thead>
//                             <tr>
//                                 <th>ID</th>
//                                 <th>Plate Number</th>
//                                 <th>Make</th>
//                                 <th>Model</th>
//                                 <th>Color</th>
//                                 <th>Customer ID</th>
//                                 <th>Actions</th>
//                             </tr>
//                             </thead>
//                             <tbody>
//                             {vehicles.map((vehicle) => (
//                                 <tr key={vehicle.vehicleID}>
//                                     <td>{vehicle.vehicleID}</td>
//                                     <td>{vehicle.carPlateNumber}</td>
//                                     <td>{vehicle.carMake}</td>
//                                     <td>{vehicle.carModel}</td>
//                                     <td>{vehicle.carColour}</td>
//                                     <td>{vehicle.customer?.userId || 'N/A'}</td>
//                                     <td>
//                                         <button
//                                             className="btn btn-small btn-secondary"
//                                             onClick={() => handleEdit(vehicle)}
//                                         >
//                                             Edit
//                                         </button>
//                                         <button
//                                             className="btn btn-small btn-danger"
//                                             onClick={() => handleDelete(vehicle.vehicleID)}
//                                         >
//                                             Delete
//                                         </button>
//                                     </td>
//                                 </tr>
//                             ))}
//                             </tbody>
//                         </table>
//                     </div>
//                 )}
//             </div>
//         </div>
//     );
// };
//
// export default VehiclePage;
