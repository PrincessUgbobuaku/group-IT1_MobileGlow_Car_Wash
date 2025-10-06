// import React, { useState, useEffect } from 'react';

// const ManagerProfile = () => {
//     const initialManager = {
//         userName: '',
//         userSurname: '',
//         isActive: false,
//         roleDescription: '',
//         employeeType: '',
//         hireDate: '',
//         login: {
//             emailAddress: '',
//             password: ''
//         }
//     };

//     const [manager, setManager] = useState(initialManager);
//     const [isEditing, setIsEditing] = useState(false);
//     const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
//     const [loading, setLoading] = useState(true);
//     const [statusMessage, setStatusMessage] = useState('');

//     // Save original state for cancel
//     const [originalManager, setOriginalManager] = useState(initialManager);

//     useEffect(() => {
//         const fetchManagerData = async () => {
//             const userEmail = localStorage.getItem('userEmail');
//             if (!userEmail) {
//                 setStatusMessage('No user logged in');
//                 setLoading(false);
//                 return;
//             }

//             try {
//                 const response = await fetch('http://localhost:8080/mobileglow/Manager/getAllManagers');
//                 if (response.ok) {
//                     const managers = await response.json();
//                     const loggedInManager = managers.find(m => m.login.emailAddress === userEmail);
//                     if (loggedInManager) {
//                         setManager(loggedInManager);
//                         setOriginalManager(loggedInManager);
//                     } else {
//                         setStatusMessage('Manager data not found');
//                     }
//                 } else {
//                     setStatusMessage('Failed to fetch manager data');
//                 }
//             } catch (error) {
//                 console.error('Error fetching manager data:', error);
//                 setStatusMessage('Something went wrong while fetching data');
//             } finally {
//                 setLoading(false);
//             }
//         };

//         fetchManagerData();
//     }, []);

//     const handleChange = (e) => {
//         const { name, value } = e.target;
//         setManager({ ...manager, [name]: value });
//     };

//     const handleLoginChange = (e) => {
//         const { name, value } = e.target;
//         setManager({
//             ...manager,
//             login: {
//                 ...manager.login,
//                 [name]: value
//             }
//         });
//     };

//     const handleEdit = () => {
//         setOriginalManager(manager); // Save current data for cancel
//         setIsEditing(true);
//     };

//     const handleUpdate = async () => {
//         try {
//             const response = await fetch('http://localhost:8080/mobileglow/Manager/update', {
//                 method: 'PUT',
//                 headers: { 'Content-Type': 'application/json' },
//                 body: JSON.stringify(manager)
//             });

//             if (response.ok) {
//                 const updatedManager = await response.json();
//                 setManager(updatedManager);
//                 setOriginalManager(updatedManager);
//                 setIsEditing(false);
//                 setStatusMessage(' Profile updated successfully!');
//             } else {
//                 setStatusMessage(' Failed to update profile');
//             }
//         } catch (error) {
//             console.error('Error updating manager:', error);
//             setStatusMessage(' Something went wrong while updating');
//         }
//     };

//     const handleCancel = () => {
//         setManager(originalManager);
//         setIsEditing(false);
//     };

//     const handleDelete = () => setShowDeleteConfirm(true);

//     const confirmDelete = async () => {
//         try {
//             const response = await fetch(`http://localhost:8080/mobileglow/Manager/delete/${manager.userId}`, {
//                 method: 'DELETE'
//             });

//             if (response.ok) {
//                 setStatusMessage(' Profile deleted successfully!');
//                 setTimeout(() => (window.location.href = '/login'), 2000);
//             } else {
//                 setStatusMessage(' Failed to delete profile');
//             }
//         } catch (error) {
//             console.error('Error deleting manager:', error);
//             setStatusMessage(' Something went wrong while deleting');
//         }
//         setShowDeleteConfirm(false);
//     };

//     const cancelDelete = () => setShowDeleteConfirm(false);

//     if (loading) {
//         return (
//             <div className="loading-container">
//                 <div className="spinner"></div>
//                 <h2>Loading Manager Profile...</h2>
//             </div>
//         );
//     }

//     return (
//         <>
//             <div className="profile-container">
//                 <h2>Manager Profile</h2>
//                 {statusMessage && <div className="status-message">{statusMessage}</div>}

//                 <form className="profile-form" onSubmit={(e) => e.preventDefault()}>
//                     {[
//                         { label: 'First Name', name: 'userName', placeholder: 'Enter first name' },
//                         { label: 'Last Name', name: 'userSurname', placeholder: 'Enter last name' },
//                         { label: 'Employee Type', name: 'employeeType', disabled: true },
//                         { label: 'Hire Date', name: 'hireDate', type: 'date' },
//                         { label: 'Role Description', name: 'roleDescription', disabled: true }
//                     ].map(({ label, name, placeholder, disabled, type = 'text' }) => (
//                         <div className="form-group" key={name}>
//                             <label>{label}</label>
//                             <input
//                                 type={type}
//                                 name={name}
//                                 value={manager[name]}
//                                 onChange={handleChange}
//                                 disabled={!isEditing || disabled}
//                                 placeholder={placeholder}
//                             />
//                         </div>
//                     ))}

//                     <h3>Login Details</h3>
//                     <div className="form-group">
//                         <label>Email</label>
//                         <input
//                             type="email"
//                             name="emailAddress"
//                             value={manager.login.emailAddress}
//                             onChange={handleLoginChange}
//                             disabled={!isEditing}
//                         />
//                     </div>

//                     <div className="form-group">
//                         <label>Password</label>
//                         <input
//                             type="password"
//                             name="password"
//                             value={manager.login.password}
//                             onChange={handleLoginChange}
//                             disabled={!isEditing}
//                         />
//                     </div>

//                     <div className="button-group">
//                         {!isEditing ? (
//                             <>
//                                 <button type="button" className="primary-btn" onClick={handleEdit}>Edit</button>
//                                 <button type="button" className="danger-btn" onClick={handleDelete}>Delete</button>
//                             </>
//                         ) : (
//                             <>
//                                 <button type="button" className="primary-btn" onClick={handleUpdate}>Update</button>
//                                 <button type="button" className="secondary-btn" onClick={handleCancel}>Cancel</button>
//                             </>
//                         )}
//                     </div>
//                 </form>
//             </div>

//             {showDeleteConfirm && (
//                 <div className="modal-overlay">
//                     <div className="modal">
//                         <h4>Are you sure you want to delete this profile?</h4>
//                         <div className="modal-buttons">
//                             <button className="danger-btn" onClick={confirmDelete}>Yes, Delete</button>
//                             <button className="secondary-btn" onClick={cancelDelete}>Cancel</button>
//                         </div>
//                     </div>
//                 </div>
//             )}

//             <style>{`
//         /* Same structure, but with modern UI enhancements */
//         body { background: #f9f9f9; }
//         .profile-container {
//           max-width: 550px;
//           margin: 50px auto;
//           padding: 35px;
//           border-radius: 15px;
//           background: #fff;
//           box-shadow: 0 12px 25px rgba(0,0,0,0.1);
//         }
//         h2, h3 {
//           text-align: center;
//           color: #333;
//         }
//         .status-message {
//           text-align: center;
//           margin-bottom: 15px;
//           font-weight: 500;
//           color: #00796b;
//         }
//         .form-group label { font-weight: 500; margin-bottom: 6px; }
//         input {
//           width: 100%;
//           padding: 12px;
//           border-radius: 8px;
//           border: 1px solid #ccc;
//           margin-bottom: 18px;
//         }
//         input[disabled] {
//           background: #f1f1f1;
//         }
//         .button-group { display: flex; gap: 10px; }
//         .primary-btn {
//           background: #00796b;
//           color: white;
//           border: none;
//           padding: 12px;
//           border-radius: 8px;
//           cursor: pointer;
//         }
//         .danger-btn {
//           background: #d9534f;
//           color: white;
//           border: none;
//           padding: 12px;
//           border-radius: 8px;
//           cursor: pointer;
//         }
//         .secondary-btn {
//           background: #ccc;
//           color: #000;
//           border: none;
//           padding: 12px;
//           border-radius: 8px;
//           cursor: pointer;
//         }
//         .modal-overlay {
//           position: fixed;
//           top: 0; left: 0;
//           width: 100%; height: 100%;
//           background: rgba(0,0,0,0.5);
//           display: flex; align-items: center; justify-content: center;
//         }
//         .modal {
//           background: #fff;
//           padding: 25px;
//           border-radius: 10px;
//           text-align: center;
//           animation: fadeIn 0.3s ease-in-out;
//         }
//         @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
//       `}</style>
//         </>
//     );
// };

// export default ManagerProfile;






// // import React, { useState, useEffect } from 'react';
// // const ManagerProfile = () => {
// //   const initialManager = {
// //     userName: '',
// //     userSurname: '',
// //     isActive: false,
// //     roleDescription: '',
// //     employeeType: '',
// //     hireDate: '',
// //     login: {
// //       emailAddress: '',
// //       password: ''
// //     }
// //   };

// //   const [manager, setManager] = useState(initialManager);
// //   const [isEditing, setIsEditing] = useState(false);
// //   const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
// //   const [loading, setLoading] = useState(true);

// //   // Save original state for cancel
// //   const [originalManager, setOriginalManager] = useState(initialManager);

// //   useEffect(() => {
// //     const fetchManagerData = async () => {
// //       const userEmail = localStorage.getItem('userEmail');
// //       if (!userEmail) {
// //         alert('No user logged in');
// //         return;
// //       }

// //       try {
// //         const response = await fetch('http://localhost:8080/mobileglow/Manager/getAllManagers');
// //         if (response.ok) {
// //           const managers = await response.json();
// //           const loggedInManager = managers.find(m => m.login.emailAddress === userEmail);
// //           if (loggedInManager) {
// //             setManager(loggedInManager);
// //             setOriginalManager(loggedInManager);
// //           } else {
// //             alert('Manager data not found');
// //           }
// //         } else {
// //           alert('Failed to fetch manager data');
// //         }
// //       } catch (error) {
// //         console.error('Error fetching manager data:', error);
// //         alert('Something went wrong while fetching data');
// //       } finally {
// //         setLoading(false);
// //       }
// //     };

// //     fetchManagerData();
// //   }, []);

// //   const handleChange = (e) => {
// //     const { name, value } = e.target;
// //     setManager({ ...manager, [name]: value });
// //   };

// //   const handleLoginChange = (e) => {
// //     const { name, value } = e.target;
// //     setManager({
// //       ...manager,
// //       login: {
// //         ...manager.login,
// //         [name]: value
// //       }
// //     });
// //   };

// //   const handleEdit = () => {
// //     setOriginalManager(manager); // Save current data for cancel
// //     setIsEditing(true);
// //   };

// //   const handleUpdate = async () => {
// //     try {
// //       const response = await fetch('http://localhost:8080/mobileglow/Manager/update', {
// //         method: 'PUT',
// //         headers: { 'Content-Type': 'application/json' },
// //         body: JSON.stringify(manager)
// //       });

// //       if (response.ok) {
// //         const updatedManager = await response.json();
// //         setManager(updatedManager);
// //         setOriginalManager(updatedManager);
// //         setIsEditing(false);
// //         alert('Profile updated successfully!');
// //       } else {
// //         alert('Failed to update profile');
// //       }
// //     } catch (error) {
// //       console.error('Error updating manager:', error);
// //       alert('Something went wrong while updating');
// //     }
// //   };

// //   const handleCancel = () => {
// //     setManager(originalManager); // Reset to original values
// //     setIsEditing(false);
// //     console.log('Edit canceled, data reverted');
// //   };

// //   const handleDelete = () => {
// //     setShowDeleteConfirm(true);
// //   };

// //   const confirmDelete = async () => {
// //     try {
// //       const response = await fetch(`http://localhost:8080/mobileglow/Manager/delete/${manager.userId}`, {
// //         method: 'DELETE'
// //       });

// //       if (response.ok) {
// //         alert('Profile deleted successfully!');
// //         // Redirect to login or home page
// //         window.location.href = '/login';
// //       } else {
// //         alert('Failed to delete profile');
// //       }
// //     } catch (error) {
// //       console.error('Error deleting manager:', error);
// //       alert('Something went wrong while deleting');
// //     }
// //     setShowDeleteConfirm(false);
// //   };

// //   const cancelDelete = () => {
// //     setShowDeleteConfirm(false);
// //   };

// //   if (loading) {
// //     return (
// //       <div className="loading-container">
// //         <h2>Loading Manager Profile...</h2>
// //       </div>
// //     );
// //   }

// //   return (
// //     <>
// //       <div className="profile-container">
// //         <h2>Manager Profile</h2>
// //         <form className="profile-form" onSubmit={(e) => e.preventDefault()}>
// //           <div className="form-group">
// //             <label>Firstname</label>
// //             <input
// //               type="text"
// //               name="userName"
// //               value={manager.userName}
// //               onChange={handleChange}
// //               disabled={!isEditing}
// //               placeholder="Enter first name"
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Lastname</label>
// //             <input
// //               type="text"
// //               name="userSurname"
// //               value={manager.userSurname}
// //               onChange={handleChange}
// //               disabled={!isEditing}
// //               placeholder="Enter last name"
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Employee Type</label>
// //             <input
// //               type="text"
// //               name="employeeType"
// //               value={manager.employeeType}
// //               disabled
// //               placeholder="Manager"
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Hire Date</label>
// //             <input
// //               type="date"
// //               name="hireDate"
// //               value={manager.hireDate}
// //               onChange={handleChange}
// //               disabled={!isEditing}
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Role Description</label>
// //             <input
// //               type="text"
// //               name="roleDescription"
// //               value={manager.roleDescription}
// //               disabled
// //             />
// //           </div>

// //           <h3>Login Details</h3>
// //           <div className="form-group">
// //             <label>Email</label>
// //             <input
// //               type="email"
// //               name="emailAddress"
// //               value={manager.login.emailAddress}
// //               onChange={handleLoginChange}
// //               disabled={!isEditing}
// //               placeholder="Enter email address"
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Password</label>
// //             <input
// //               type="password"
// //               name="password"
// //               value={manager.login.password}
// //               onChange={handleLoginChange}
// //               disabled={!isEditing}
// //               placeholder="Enter password"
// //             />
// //           </div>

// //           <div className="button-group">
// //             {!isEditing ? (
// //               <>
// //                 <button type="button" className="edit-btn" onClick={handleEdit}>
// //                   Edit
// //                 </button>
// //                 <button type="button" className="delete-btn" onClick={handleDelete}>
// //                   Delete
// //                 </button>
// //               </>
// //             ) : (
// //               <>
// //                 <button type="button" className="update-btn" onClick={handleUpdate}>
// //                   Update
// //                 </button>
// //                 <button type="button" className="cancel-btn" onClick={handleCancel}>
// //                   Cancel
// //                 </button>
// //               </>
// //             )}
// //           </div>
// //         </form>
// //       </div>

// //       {showDeleteConfirm && (
// //         <div className="modal-overlay">
// //           <div className="modal">
// //             <h4>Are you sure you want to delete this profile?</h4>
// //             <div className="modal-buttons">
// //               <button className="confirm-delete" onClick={confirmDelete}>
// //                 Yes, Delete
// //               </button>
// //               <button className="cancel-delete" onClick={cancelDelete}>
// //                 Cancel
// //               </button>
// //             </div>
// //           </div>
// //         </div>
// //       )}

// //       <style>{`
// //         .loading-container {
// //           display: flex;
// //           justify-content: center;
// //           align-items: center;
// //           height: 50vh;
// //           font-family: 'Segoe UI', Arial, sans-serif;
// //         }

// //         .loading-container h2 {
// //           color: #333;
// //         }

// //         .profile-container {
// //           max-width: 550px;
// //           margin: 50px auto;
// //           padding: 35px;
// //           border-radius: 15px;
// //           background: #fff;
// //           box-shadow: 0 12px 25px rgba(0,0,0,0.15);
// //           font-family: 'Segoe UI', Arial, sans-serif;
// //         }

// //         .profile-container h2, .profile-container h3 {
// //           text-align: center;
// //           margin-bottom: 25px;
// //           font-weight: 600;
// //         }

// //         .profile-form .form-group {
// //           display: flex;
// //           flex-direction: column;
// //           margin-bottom: 18px;
// //         }

// //         .profile-form label {
// //           margin-bottom: 6px;
// //           font-weight: 500;
// //         }

// //         .profile-form input[type="text"],
// //         .profile-form input[type="email"],
// //         .profile-form input[type="password"],
// //         .profile-form input[type="date"] {
// //           padding: 12px;
// //           border-radius: 8px;
// //           border: 1px solid #ccc;
// //           font-size: 14px;
// //           transition: all 0.2s ease;
// //         }

// //         .profile-form input:focus {
// //           outline: none;
// //           border: 1px solid #000;
// //         }

// //         .profile-form input[disabled] {
// //           background: #f9f9f9;
// //           cursor: not-allowed;
// //         }

// //         .button-group {
// //           display: flex;
// //           justify-content: space-between;
// //           margin-top: 25px;
// //           gap: 10px;
// //         }

// //         .edit-btn,
// //         .update-btn,
// //         .cancel-btn,
// //         .delete-btn {
// //           flex: 1;
// //           padding: 12px 20px;
// //           border: none;
// //           border-radius: 10px;
// //           font-size: 15px;
// //           font-weight: 600;
// //           cursor: pointer;
// //           transition: background 0.2s ease;
// //         }

// //         .edit-btn,
// //         .update-btn {
// //           background: #000;
// //           color: #fff;
// //         }

// //         .edit-btn:hover,
// //         .update-btn:hover {
// //           background: #333;
// //         }

// //         .cancel-btn {
// //           background: #ccc;
// //           color: #000;
// //         }

// //         .cancel-btn:hover {
// //           background: #bbb;
// //         }

// //         .delete-btn {
// //           background: #fff;
// //           border: 1px solid #000;
// //           color: #000;
// //         }

// //         .delete-btn:hover {
// //           background: #f2f2f2;
// //         }

// //         /* Modal Styles */
// //         .modal-overlay {
// //           position: fixed;
// //           top: 0;
// //           left: 0;
// //           width: 100%;
// //           height: 100%;
// //           background: rgba(0,0,0,0.5);
// //           display: flex;
// //           align-items: center;
// //           justify-content: center;
// //         }

// //         .modal {
// //           background: #fff;
// //           padding: 25px;
// //           border-radius: 10px;
// //           text-align: center;
// //           max-width: 400px;
// //           width: 90%;
// //         }

// //         .modal-buttons {
// //           display: flex;
// //           justify-content: center;
// //           gap: 15px;
// //           margin-top: 20px;
// //         }

// //         .confirm-delete {
// //           background: #d9534f;
// //           color: white;
// //           padding: 10px 18px;
// //           border: none;
// //           border-radius: 8px;
// //           cursor: pointer;
// //         }

// //         .confirm-delete:hover {
// //           background: #c9302c;
// //         }

// //         .cancel-delete {
// //           background: #ccc;
// //           color: #000;
// //           padding: 10px 18px;
// //           border: none;
// //           border-radius: 8px;
// //           cursor: pointer;
// //         }

// //         .cancel-delete:hover {
// //           background: #bbb;
// //         }

// //         @media (max-width: 480px) {
// //           .button-group {
// //             flex-direction: column;
// //           }
// //         }
// //       `}</style>
// //     </>
// //   );
// // };

// // export default ManagerProfile;
