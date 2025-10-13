// import React, { useState, useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';

// const ManagerForm = () => {
//     const [manager, setManager] = useState({
//         userName: '',
//         userSurname: '',
//         isActive: true,
//         roleDescription: 'EMPLOYEE',
//         employeeType: 'Manager',
//         hireDate: '',
//         login: {
//             emailAddress: '',
//             password: ''
//         }
//     });

//     const [isEditing, setIsEditing] = useState(false);
//     const navigate = useNavigate();

//     useEffect(() => {
//         const userEmail = localStorage.getItem('userEmail');
//         if (userEmail) {
//             const fetchManagerData = async () => {
//                 try {
//                     const response = await fetch('http://localhost:8080/mobileglow/Manager/getAllManagers');
//                     if (response.ok) {
//                         const managers = await response.json();
//                         const loggedInManager = managers.find(m => m.login.emailAddress === userEmail);
//                         if (loggedInManager) {
//                             setManager(loggedInManager);
//                             setIsEditing(true);
//                         }
//                     } else {
//                         alert('Failed to fetch manager data');
//                     }
//                 } catch (error) {
//                     console.error('Error fetching manager data:', error);
//                     alert('Something went wrong while fetching data');
//                 }
//             };
//             fetchManagerData();
//         }
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

//     const handleSubmit = async (e) => {
//         e.preventDefault();

//         try {
//             const url = isEditing
//                 ? 'http://localhost:8080/mobileglow/Manager/update'
//                 : 'http://localhost:8080/mobileglow/Manager/create';
//             const method = isEditing ? 'PUT' : 'POST';
//             const successMessage = isEditing
//                 ? 'Manager profile updated successfully!'
//                 : 'Manager account created successfully!';
//             const redirectPath = isEditing ? '/' : '/login';

//             const response = await fetch(url, {
//                 method,
//                 headers: { 'Content-Type': 'application/json' },
//                 body: JSON.stringify(manager)
//             });

//             if (response.ok) {
//                 alert(successMessage);
//                 navigate(redirectPath);
//             } else {
//                 alert(`Error ${isEditing ? 'updating' : 'creating'} profile!`);
//             }
//         } catch (error) {
//             console.error('Error:', error);
//             alert('Something went wrong!');
//         }
//     };

//     const handleDelete = async () => {
//         if (window.confirm('Are you sure you want to delete this profile?')) {
//             try {
//                 const response = await fetch(
//                     `http://localhost:8080/mobileglow/Manager/delete/${manager.userId}`,
//                     { method: 'DELETE' }
//                 );

//                 if (response.ok) {
//                     alert('Profile deleted successfully!');
//                     localStorage.clear();
//                     navigate('/login');
//                 } else {
//                     alert('Failed to delete profile');
//                 }
//             } catch (error) {
//                 console.error('Error deleting manager:', error);
//                 alert('Something went wrong while deleting');
//             }
//         }
//     };

//     return (
//         <>
//             <div className="manager-container">
//                 <h2>{isEditing ? 'Edit Manager Profile' : 'Create Manager Account'}</h2>
//                 <form onSubmit={handleSubmit} className="manager-form">
//                     <div className="form-group">
//                         <label>First Name</label>
//                         <input
//                             type="text"
//                             name="userName"
//                             value={manager.userName}
//                             onChange={handleChange}
//                             placeholder="Enter first name"
//                             required
//                         />
//                     </div>

//                     <div className="form-group">
//                         <label>Last Name</label>
//                         <input
//                             type="text"
//                             name="userSurname"
//                             value={manager.userSurname}
//                             onChange={handleChange}
//                             placeholder="Enter last name"
//                             required
//                         />
//                     </div>

//                     <div className="form-group">
//                         <label>Employee Type</label>
//                         <input
//                             type="text"
//                             value={manager.employeeType}
//                             disabled
//                         />
//                     </div>

//                     <div className="form-group">
//                         <label>Hire Date</label>
//                         <input
//                             type="date"
//                             name="hireDate"
//                             value={manager.hireDate}
//                             onChange={handleChange}
//                             required
//                         />
//                     </div>

//                     <div className="form-group role-group">
//                         <label>Role</label>
//                         <div className="radio-group">
//                             <label>
//                                 <input
//                                     type="radio"
//                                     name="roleDescription"
//                                     value="CLIENT"
//                                     checked={manager.roleDescription === 'CLIENT'}
//                                     onChange={handleChange}
//                                 /> Client
//                             </label>
//                             <label>
//                                 <input
//                                     type="radio"
//                                     name="roleDescription"
//                                     value="EMPLOYEE"
//                                     checked={manager.roleDescription === 'EMPLOYEE'}
//                                     onChange={handleChange}
//                                 /> Employee
//                             </label>
//                         </div>
//                     </div>

//                     <h3>Login Details</h3>
//                     <div className="form-group">
//                         <label>Email</label>
//                         <input
//                             type="email"
//                             name="emailAddress"
//                             value={manager.login.emailAddress}
//                             onChange={handleLoginChange}
//                             placeholder="Enter email address"
//                             required
//                         />
//                     </div>

//                     <div className="form-group">
//                         <label>Password</label>
//                         <input
//                             type="password"
//                             name="password"
//                             value={manager.login.password}
//                             onChange={handleLoginChange}
//                             placeholder="Enter password"
//                             required
//                         />
//                     </div>

//                     <div className="button-group">
//                         <button type="submit" className="submit-btn">
//                             {isEditing ? 'Update Profile' : 'Create Account'}
//                         </button>
//                         {isEditing && (
//                             <button type="button" className="delete-btn" onClick={handleDelete}>
//                                 Delete Profile
//                             </button>
//                         )}
//                     </div>
//                 </form>
//             </div>

//             <style>{`
//         .manager-container {
//           max-width: 550px;
//           margin: 50px auto;
//           padding: 30px;
//           background: #ffffff;
//           border-radius: 15px;
//           box-shadow: 0 8px 25px rgba(0,0,0,0.08);
//           font-family: 'Segoe UI', sans-serif;
//         }

//         h2, h3 {
//           text-align: center;
//           color: #333;
//           margin-bottom: 20px;
//         }

//         .manager-form .form-group {
//           display: flex;
//           flex-direction: column;
//           margin-bottom: 18px;
//         }

//         .manager-form label {
//           margin-bottom: 6px;
//           font-weight: 600;
//           color: #444;
//         }

//         .manager-form input {
//           padding: 12px;
//           border-radius: 8px;
//           border: 1px solid #ccc;
//           font-size: 14px;
//           transition: all 0.3s ease;
//         }

//         .manager-form input:focus {
//           border: 1px solid #4CAF50;
//           box-shadow: 0 0 6px rgba(76,175,80,0.2);
//           outline: none;
//         }

//         .radio-group {
//           display: flex;
//           gap: 15px;
//           margin-top: 6px;
//         }

//         .submit-btn {
//           flex: 1;
//           background: #4CAF50;
//           color: white;
//           padding: 14px;
//           font-size: 16px;
//           font-weight: 600;
//           border: none;
//           border-radius: 10px;
//           cursor: pointer;
//           transition: background 0.3s ease;
//         }

//         .submit-btn:hover {
//           background: #45a049;
//         }

//         .delete-btn {
//           flex: 1;
//           background: #fff;
//           color: #d32f2f;
//           padding: 14px;
//           font-size: 16px;
//           font-weight: 600;
//           border: 1px solid #d32f2f;
//           border-radius: 10px;
//           cursor: pointer;
//           transition: all 0.3s ease;
//         }

//         .delete-btn:hover {
//           background: #fdecea;
//         }

//         .button-group {
//           display: flex;
//           gap: 10px;
//           margin-top: 20px;
//         }

//         @media (max-width: 480px) {
//           .button-group {
//             flex-direction: column;
//           }
//         }
//       `}</style>
//         </>
//     );
// };

// export default ManagerForm;





// // import React, { useState, useEffect } from 'react';
// // import { useNavigate } from 'react-router-dom';

// // const ManagerForm = () => {
// //   const [manager, setManager] = useState({
// //     userName: '',
// //     userSurname: '',
// //     isActive: true,
// //     roleDescription: 'EMPLOYEE',
// //     employeeType: 'Manager', // Default value
// //     hireDate: '',
// //     login: {
// //       emailAddress: '',
// //       password: ''
// //     }
// //   });

// //   const [isEditing, setIsEditing] = useState(false);
// //   const navigate = useNavigate(); // this is for navigation.

// //   useEffect(() => {
// //     const userEmail = localStorage.getItem('userEmail');
// //     if (userEmail) {
// //       // Fetch existing manager data
// //       const fetchManagerData = async () => {
// //         try {
// //           const response = await fetch('http://localhost:8080/mobileglow/Manager/getAllManagers');
// //           if (response.ok) {
// //             const managers = await response.json();
// //             const loggedInManager = managers.find(m => m.login.emailAddress === userEmail);
// //             if (loggedInManager) {
// //               setManager(loggedInManager);
// //               setIsEditing(true);
// //             }
// //           } else {
// //             alert('Failed to fetch manager data');
// //           }
// //         } catch (error) {
// //           console.error('Error fetching manager data:', error);
// //           alert('Something went wrong while fetching data');
// //         }
// //       };
// //       fetchManagerData();
// //     }
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

// //   const handleSubmit = async (e) => {
// //   e.preventDefault();

// //   try {
// //     const url = isEditing ? 'http://localhost:8080/mobileglow/Manager/update' : 'http://localhost:8080/mobileglow/Manager/create';
// //     const method = isEditing ? 'PUT' : 'POST';
// //     const successMessage = isEditing ? 'Manager profile updated successfully!' : 'Manager account created successfully!';
// //     const redirectPath = isEditing ? '/' : '/login';

// //     console.log('Creating/updating manager:', manager);
// //     const response = await fetch(url, {
// //       method: method,
// //       headers: {
// //         'Content-Type': 'application/json'
// //       },
// //       body: JSON.stringify(manager)
// //     });

// //     if (response.ok) {
// //       const data = await response.json();
// //       console.log(isEditing ? 'Manager updated:' : 'Manager created:', data);

// //       //show alert and redirect
// //       alert(successMessage);
// //       navigate(redirectPath);

// //     } else {
// //       console.error(`Failed to ${isEditing ? 'update' : 'create'} manager:`, response.statusText);
// //       alert(`Error ${isEditing ? 'updating' : 'creating'} profile!`);
// //     }
// //   } catch (error) {
// //     console.error('Error:', error);
// //     alert('Something went wrong!');
// //   }
// // };

// //   const handleDelete = async () => {
// //     if (window.confirm('Are you sure you want to delete this profile?')) {
// //       try {
// //         const response = await fetch(`http://localhost:8080/mobileglow/Manager/delete/${manager.userId}`, {
// //           method: 'DELETE'
// //         });

// //         if (response.ok) {
// //           alert('Profile deleted successfully!');
// //           localStorage.removeItem('userEmail');
// //           localStorage.removeItem('userRoleDescription');
// //           navigate('/login');
// //         } else {
// //           alert('Failed to delete profile');
// //         }
// //       } catch (error) {
// //         console.error('Error deleting manager:', error);
// //         alert('Something went wrong while deleting');
// //       }
// //     }
// //   };


// //   return (
// //     <>
// //       <div className="manager-container">
// //         <h2>{isEditing ? 'Edit Manager Profile' : 'Manager Signup Form'}</h2>
// //         <form onSubmit={handleSubmit} className="manager-form">
// //           <div className="form-group">
// //             <label>Firstname</label>
// //             <input
// //               type="text"
// //               name="userName"
// //               value={manager.userName}
// //               onChange={handleChange}
// //               placeholder="Enter first name"
// //               required
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Lastname</label>
// //             <input
// //               type="text"
// //               name="userSurname"
// //               value={manager.userSurname}
// //               onChange={handleChange}
// //               placeholder="Enter last name"
// //               required
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
// //               required
// //             />
// //           </div>

// //           <div className="form-group role-group">
// //             <label>Role Description</label>
// //             <div className="radio-group">
// //               <label>
// //                 <input
// //                   type="radio"
// //                   name="roleDescription"
// //                   value="CLIENT"
// //                   checked={manager.roleDescription === 'CLIENT'}
// //                   onChange={handleChange}
// //                 /> Client
// //               </label>
// //               <label>
// //                 <input
// //                   type="radio"
// //                   name="roleDescription"
// //                   value="EMPLOYEE"
// //                   checked={manager.roleDescription === 'EMPLOYEE'}
// //                   onChange={handleChange}
// //                 /> Employee
// //               </label>
// //             </div>
// //           </div>

// //           <h3>Login Details</h3>
// //           <div className="form-group">
// //             <label>Email</label>
// //             <input
// //               type="email"
// //               name="emailAddress"
// //               value={manager.login.emailAddress}
// //               onChange={handleLoginChange}
// //               placeholder="Enter email address"
// //               required
// //             />
// //           </div>

// //           <div className="form-group">
// //             <label>Password</label>
// //             <input
// //               type="password"
// //               name="password"
// //               value={manager.login.password}
// //               onChange={handleLoginChange}
// //               placeholder="Enter password"
// //               required
// //             />
// //           </div>

// //           <div className="button-group">
// //             <button type="submit" className="submit-btn">{isEditing ? 'Update Profile' : 'Create Account'}</button>
// //             {isEditing && (
// //               <button type="button" className="delete-btn" onClick={handleDelete}>
// //                 Delete Profile
// //               </button>
// //             )}
// //           </div>
// //         </form>
// //       </div>

// //       <style>{`
// //         .manager-container {
// //           max-width: 500px;
// //           margin: 40px auto;
// //           padding: 30px;
// //           border-radius: 15px;
// //           background: #fff;
// //           box-shadow: 0 10px 20px rgba(0,0,0,0.1);
// //           font-family: 'Segoe UI', Arial, sans-serif;
// //         }

// //         .manager-container h2, .manager-container h3 {
// //           text-align: center;
// //           margin-bottom: 25px;
// //           font-weight: 600;
// //         }

// //         .manager-form .form-group {
// //           display: flex;
// //           flex-direction: column;
// //           margin-bottom: 18px;
// //         }

// //         .manager-form label {
// //           margin-bottom: 6px;
// //           font-weight: 500;
// //         }

// //         .manager-form input[type="text"],
// //         .manager-form input[type="email"],
// //         .manager-form input[type="password"],
// //         .manager-form input[type="date"] {
// //           padding: 12px;
// //           border-radius: 8px;
// //           border: 1px solid #ccc;
// //           font-size: 14px;
// //           transition: all 0.2s ease;
// //         }

// //         .manager-form input:focus {
// //           outline: none;
// //           border: 1px solid #000;
// //         }

// //         .manager-form input[disabled] {
// //           background: #f0f0f0;
// //           cursor: not-allowed;
// //         }

// //         .role-group {
// //           display: flex;
// //           flex-direction: column;
// //         }

// //         .radio-group {
// //           display: flex;
// //           gap: 20px;
// //           margin-top: 8px;
// //         }

// //         .submit-btn {
// //           width: 100%;
// //           padding: 14px;
// //           border: none;
// //           border-radius: 10px;
// //           background: #000;
// //           color: #fff;
// //           font-weight: 600;
// //           font-size: 16px;
// //           cursor: pointer;
// //           transition: background 0.2s;
// //         }

// //         .submit-btn:hover {
// //           background: #333;
// //         }

// //         .button-group {
// //           display: flex;
// //           justify-content: space-between;
// //           gap: 10px;
// //           margin-top: 25px;
// //         }

// //         .delete-btn {
// //           flex: 1;
// //           padding: 14px;
// //           border: 1px solid #000;
// //           border-radius: 10px;
// //           background: #fff;
// //           color: #000;
// //           font-weight: 600;
// //           font-size: 16px;
// //           cursor: pointer;
// //           transition: background 0.2s;
// //         }

// //         .delete-btn:hover {
// //           background: #f2f2f2;
// //         }

// //         @media (max-width: 480px) {
// //           .manager-container {
// //             padding: 20px;
// //             margin: 20px auto;
// //           }
// //           .button-group {
// //             flex-direction: column;
// //           }
// //         }
// //       `}</style>
// //     </>
// //   );
// // };

// // export default ManagerForm;
