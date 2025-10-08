import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const navigate = useNavigate();
    const [login, setLogin] = useState({ email: '', password: '' }); //The Details typed in the login  are kept here.

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLogin({ ...login, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/mobileglow/Login/authenticate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    emailAddress: login.email,
                    password: login.password
                })
            });

            if (response.ok) {
                const data = await response.json();
                alert(data.message);
                localStorage.setItem('userEmail', login.email);
                localStorage.setItem('userRoleDescription', data.role_description);
                localStorage.setItem('authToken', data.token); // Store JWT token
                localStorage.setItem('userId', data.user_id || '1'); // Store user ID, default to 1 if not provided
                if (data.role_description === 'EMPLOYEE') {
                    navigate('/LandingEmployee');
                } else {
                    navigate('/LandingCustomer');
                }
            } else {
                const error = await response.text();
                alert(error);
            }
        } catch (error) {
            console.error('Error logging in:', error);
            alert('Something went wrong!');
        }
    };

    return (
        <>
            <div className="login-page">
                <div className="login-container">
                    <h2>Welcome Back 👋</h2>
                    <p className="subtitle">Sign in to continue</p>
                    <form onSubmit={handleSubmit} className="login-form">
                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                name="email"
                                value={login.email}
                                onChange={handleChange}
                                placeholder="Enter your email"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Password</label>
                            <input
                                type="password"
                                name="password"
                                value={login.password}
                                onChange={handleChange}
                                placeholder="Enter your password"
                                required
                            />
                        </div>

                        <button type="submit" className="submit-btn">Login</button>
                    </form>
                </div>
            </div>

            <style>{`
        /* Full page gradient background */
        .login-page {
          min-height: 100vh;
          display: flex;
          justify-content: center;
          align-items: center;
          background: linear-gradient(135deg, #0d47a1, #1976d2, #42a5f5, #90caf9);
          background-size: 400% 400%;
          animation: gradientBG 12s ease infinite;
        }

        @keyframes gradientBG {
          0% { background-position: 0% 50%; }
          50% { background-position: 100% 50%; }
          100% { background-position: 0% 50%; }
        }

        .login-container {
          max-width: 420px;
          width: 100%;
          padding: 40px;
          border-radius: 20px;
          background: #fff;
          box-shadow: 0 12px 25px rgba(0,0,0,0.15);
          font-family: 'Segoe UI', Arial, sans-serif;
          text-align: center;
        }

        .login-container h2 {
          margin-bottom: 10px;
          font-weight: 700;
          color: #0d47a1;
        }

        .subtitle {
          color: #777;
          margin-bottom: 30px;
          font-size: 14px;
        }

        .login-form .form-group {
          text-align: left;
          margin-bottom: 20px;
        }

        .login-form label {
          display: block;
          margin-bottom: 6px;
          font-weight: 500;
          color: #444;
        }

        .login-form input[type="email"],
        .login-form input[type="password"] {
          width: 100%;
          padding: 14px;
          border-radius: 10px;
          border: 1px solid #ccc;
          font-size: 15px;
          transition: 0.3s ease;
        }

        .login-form input:focus {
          outline: none;
          border-color: #1976d2;
          box-shadow: 0 0 6px rgba(25, 118, 210, 0.4);
        }

        .submit-btn {
          width: 100%;
          padding: 14px;
          border: none;
          border-radius: 12px;
          background: linear-gradient(90deg, #1976d2, #42a5f5);
          color: #fff;
          font-weight: 600;
          font-size: 16px;
          cursor: pointer;
          transition: transform 0.2s, background 0.3s;
        }

        .submit-btn:hover {
          transform: translateY(-2px);
          background: linear-gradient(90deg, #1565c0, #1e88e5);
        }

        @media (max-width: 480px) {
          .login-container {
            padding: 30px 20px;
          }
        }
      `}</style>
        </>
    );
};

export default Login;


// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';

// const LoginForm = () => {
//   const navigate = useNavigate();
//   const [login, setLogin] = useState({ email: '', password: '' });

//   const handleChange = (e) => {
//     const { name, value } = e.target;
//     setLogin({ ...login, [name]: value });
//   };

//   const handleSubmit = async (e) => {
//   e.preventDefault();

//   try {
//     const response = await fetch('http://localhost:8080/mobileglow/Login/authenticate', {
//       method: 'POST',
//       headers: { 'Content-Type': 'application/json' },
//       body: JSON.stringify({
//         emailAddress: login.email,
//         password: login.password
//       })
//     });

//     if (response.ok) {
//       const data = await response.json();
//       alert(data.message);
//       // Save user session (e.g., email and role_description) to localStorage
//       localStorage.setItem('userEmail', login.email);
//       localStorage.setItem('userRoleDescription', data.role_description);
//       // Redirect based on role_description
//       if (data.role_description === 'EMPLOYEE') {
//         navigate('/profile'); // This goes to ManagerProfile
//       } else {
//         navigate('/profile'); //this is suppose to be client but when it changes it shows there's no client data since we haven't connected with customer and added security.
//       }
//     } else {
//       const error = await response.text();
//       alert(error);
//     }
//   } catch (error) {
//     console.error('Error logging in:', error);
//     alert('Something went wrong!');
//   }
// };

//   return (
//     <>
//       <div className="login-container">
//         <h2>Login</h2>
//         <form onSubmit={handleSubmit} className="login-form">
//           <div className="form-group">
//             <label>Email</label>
//             <input
//               type="email"
//               name="email"
//               value={login.email}
//               onChange={handleChange}
//               placeholder="Enter your email"
//               required
//             />
//           </div>

//           <div className="form-group">
//             <label>Password</label>
//             <input
//               type="password"
//               name="password"
//               value={login.password}
//               onChange={handleChange}
//               placeholder="Enter your password"
//               required
//             />
//           </div>

//           <button type="submit" className="submit-btn">Login</button>
//         </form>
//       </div>

//       <style>{`
//         .login-container {
//           max-width: 400px;
//           margin: 80px auto;
//           padding: 30px;
//           border-radius: 15px;
//           background: #fff;
//           box-shadow: 0 10px 20px rgba(0,0,0,0.1);
//           font-family: 'Segoe UI', Arial, sans-serif;
//         }

//         .login-container h2 {
//           text-align: center;
//           margin-bottom: 25px;
//           font-weight: 600;
//         }

//         .login-form .form-group {
//           display: flex;
//           flex-direction: column;
//           margin-bottom: 18px;
//         }

//         .login-form label {
//           margin-bottom: 6px;
//           font-weight: 500;
//         }

//         .login-form input[type="email"],
//         .login-form input[type="password"] {
//           padding: 12px;
//           border-radius: 8px;
//           border: 1px solid #ccc;
//           font-size: 14px;
//           transition: all 0.2s ease;
//         }

//         .login-form input:focus {
//           outline: none;
//           border: 1px solid #000;
//         }

//         .submit-btn {
//           width: 100%;
//           padding: 14px;
//           border: none;
//           border-radius: 10px;
//           background: #000;
//           color: #fff;
//           font-weight: 600;
//           font-size: 16px;
//           cursor: pointer;
//           transition: background 0.2s;
//         }

//         .submit-btn:hover {
//           background: #333;
//         }

//         @media (max-width: 480px) {
//           .login-container {
//             padding: 20px;
//             margin: 40px auto;
//           }
//         }
//       `}</style>
//     </>
//   );
// };

// export default LoginForm;
