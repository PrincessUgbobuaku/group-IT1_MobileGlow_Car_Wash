import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { employeeService } from "../services/employeeService";
import api from "../services/api";

const EditEmployeeProfile = () => {
  const navigate = useNavigate();
  const [employee, setEmployee] = useState({
    userName: "",
    userSurname: "",
    employeeType: "",
    hireDate: "",
    contact: { phoneNumber: "" },
    address: { streetNumber: "", streetName: "", city: "", postalCode: "" },
    login: { emailAddress: "" },
    imageFile: null,
    imagePreviewUrl: null,
  });
  const [loading, setLoading] = useState(true);
  const [statusMessage, setStatusMessage] = useState('');
  const [messageType, setMessageType] = useState('error');

  useEffect(() => {
    const fetchUserData = async () => {
      const userEmail = localStorage.getItem('userEmail');
      if (!userEmail) {
        setStatusMessage('No user logged in');
        setMessageType('error');
        setLoading(false);
        return;
      }

      try {
        let users = [];
        const endpointTypes = [
          { url: 'http://localhost:8080/mobileglow/Manager/getAllManagers', type: 'Manager' },
          { url: 'http://localhost:8080/mobileglow/Accountant/getAllAccountants', type: 'Accountant' },
          { url: 'http://localhost:8080/mobileglow/wash-attendants/getAllWashAttendants', type: 'WashAttendant' }
        ];
        for (const { url, type } of endpointTypes) {
          try {
            const response = await fetch(url);
            if (response.ok) {
              const data = await response.json();
              users = users.concat(data.map(u => ({ ...u, employeeType: type })));
            }
          } catch (error) {
            console.error('Error fetching from', url, error);
          }
        }
        const loggedInUser = users.find(u => u.login.emailAddress === userEmail);
        if (loggedInUser) {
          setEmployee({ ...loggedInUser, userId: loggedInUser.userId });
        } else {
          setStatusMessage('User data not found');
          setMessageType('error');
        }
      } catch (error) {
        console.error('Error fetching user data:', error);
        setStatusMessage('Something went wrong while fetching data');
        setMessageType('error');
      } finally {
        setLoading(false);
      }
    };
    fetchUserData();
  }, []);

  const handleChange = (e, section, field) => {
    if (section) {
      setEmployee({
        ...employee,
        [section]: { ...employee[section], [field]: e.target.value },
      });
    } else {
      setEmployee({ ...employee, [field]: e.target.value });
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setEmployee({
        ...employee,
        imageFile: file,
        imagePreviewUrl: URL.createObjectURL(file),
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      for (const key in employee) {
        if (key === "imageFile") continue;
        if (typeof employee[key] === "object" && employee[key] !== null) {
          for (const subKey in employee[key]) {
            formData.append(`${key}.${subKey}`, employee[key][subKey]);
          }
        } else {
          formData.append(key, employee[key]);
        }
      }
      if (employee.imageFile) {
        formData.append("imageFile", employee.imageFile);
      }

      await employeeService.updateEmployee(formData);

      setStatusMessage('Profile updated successfully');
      setMessageType('success');
      navigate("/profiles");
    } catch (error) {
      console.error('Error updating profile:', error);
      setStatusMessage('Failed to update profile');
      setMessageType('error');
    }
  };

  if (loading) {
    return (
      <div style={styles.loadingContainer}>
        <div style={styles.spinner}></div>
        <h2>Loading Profile...</h2>
      </div>
    );
  }

  return (
    <div style={styles.wrapper}>
      <button style={styles.backBtn} onClick={() => navigate("/profiles")}>←</button>

      <h2 style={styles.title}>Edit Profile</h2>
      {statusMessage && <div style={styles[messageType + 'Message']}>{statusMessage}</div>}

      <form style={styles.form} onSubmit={handleSubmit}>
        <div style={styles.row}>
          <div style={styles.group}>
            <label style={styles.label}>First Name</label>
            <input
              type="text"
              value={employee.userName}
              onChange={(e) => handleChange(e, null, "userName")}
              style={styles.input}
              placeholder="Enter first name"
            />
          </div>
          <div style={styles.group}>
            <label style={styles.label}>Last Name</label>
            <input
              type="text"
              value={employee.userSurname}
              onChange={(e) => handleChange(e, null, "userSurname")}
              style={styles.input}
              placeholder="Enter last name"
            />
          </div>
        </div>

        <div style={styles.group}>
          <label style={styles.label}>Employee Type</label>
          <select
            value={employee.employeeType}
            disabled
            style={styles.input}
          >
            <option value="">Select type</option>
            <option value="Accountant">Accountant</option>
            <option value="Manager">Manager</option>
            <option value="WashAttendant">WashAttendant</option>
          </select>
        </div>

        <div style={styles.group}>
          <label style={styles.label}>Hire Date</label>
          <input
            type="date"
            value={employee.hireDate}
            onChange={(e) => handleChange(e, null, "hireDate")}
            style={styles.input}
          />
        </div>

        <div style={styles.group}>
          <label style={styles.label}>Mobile Number</label>
          <input
            type="text"
            value={employee.contact.phoneNumber}
            onChange={(e) => handleChange(e, "contact", "phoneNumber")}
            style={styles.input}
            placeholder="Enter mobile number"
          />
        </div>

        <div style={styles.group}>
          <label style={styles.label}>Email Address</label>
          <input
            type="email"
            value={employee.login.emailAddress}
            onChange={(e) => handleChange(e, "login", "emailAddress")}
            style={styles.input}
            placeholder="Enter email"
          />
        </div>

        <div style={styles.row}>
          <div style={styles.group}>
            <label style={styles.label}>Street number</label>
            <input
              type="text"
              value={employee.address.streetNumber}
              onChange={(e) => handleChange(e, "address", "streetNumber")}
              style={styles.input}
              placeholder="Enter street number"
            />
          </div>
          <div style={styles.group}>
            <label style={styles.label}>Street name</label>
            <input
              type="text"
              value={employee.address.streetName}
              onChange={(e) => handleChange(e, "address", "streetName")}
              style={styles.input}
              placeholder="Enter street name"
            />
          </div>
        </div>

        <div style={styles.row}>
          <div style={styles.group}>
            <label style={styles.label}>City</label>
            <input
              type="text"
              value={employee.address.city}
              onChange={(e) => handleChange(e, "address", "city")}
              style={styles.input}
              placeholder="Enter city"
            />
          </div>
        <div style={styles.group}>
          <label style={styles.label}>Postal code</label>
          <input
            type="text"
            value={employee.address.postalCode}
            onChange={(e) => handleChange(e, "address", "postalCode")}
            style={styles.input}
            placeholder="Enter postal code"
          />
        </div>
      </div>

      <div style={styles.group}>
        <label style={styles.label}>Choose File</label>
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          style={styles.input}
        />
        {employee.imagePreviewUrl && (
          <img
            src={employee.imagePreviewUrl}
            alt="Preview"
            style={{ width: 100, height: 100, borderRadius: "50%", marginTop: 10 }}
          />
        )}
      </div>

      <button type="submit" style={styles.saveBtn}>Save Changes</button>
      </form>
    </div>
  );
};

const styles = {
  loadingContainer: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    minHeight: "100vh",
    backgroundColor: "#f5f7fa",
  },
  spinner: {
    border: "4px solid #f3f3f3",
    borderTop: "4px solid #1d7a6e",
    borderRadius: "50%",
    width: "40px",
    height: "40px",
    animation: "spin 1s linear infinite",
    marginBottom: "1rem",
  },
  errorMessage: {
    color: "#d32f2f",
    backgroundColor: "#ffe5e5",
    padding: "0.75rem",
    borderRadius: "12px",
    marginBottom: "1rem",
    textAlign: "center",
    fontWeight: 500,
  },
  successMessage: {
    color: "#2e7d32",
    backgroundColor: "#e6f4ea",
    padding: "0.75rem",
    borderRadius: "12px",
    marginBottom: "1rem",
    textAlign: "center",
    fontWeight: 500,
  },
  wrapper: {
    fontFamily: "'Segoe UI', sans-serif",
    backgroundColor: "#f5f7fa",
    minHeight: "100vh",
    padding: "2rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  backBtn: {
    background: "none",
    border: "none",
    fontSize: "1.5rem",
    cursor: "pointer",
    marginBottom: "1rem",
    alignSelf: "flex-start",
    color: "#1d7a6e",
    fontWeight: 600,
  },
  title: {
    fontSize: "2rem",
    fontWeight: 700,
    marginBottom: "1.5rem",
    color: "#1d1d1d",
  },
  form: {
    backgroundColor: "#fff",
    padding: "2rem",
    borderRadius: "16px",
    boxShadow: "0 8px 20px rgba(0,0,0,0.08)",
    maxWidth: "700px",
    width: "100%",
    display: "flex",
    flexDirection: "column",
    gap: "1.5rem",
  },
  row: {
    display: "flex",
    gap: "1.5rem",
    flexWrap: "wrap",
  },
  group: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
  },
  label: {
    fontWeight: 600,
    marginBottom: "0.5rem",
    color: "#555",
  },
  input: {
    padding: "0.75rem 1rem",
    borderRadius: "12px",
    border: "1px solid #ccc",
    fontSize: "1rem",
    transition: "all 0.2s ease",
    outline: "none",
  },
  saveBtn: {
    backgroundColor: "#1d7a6e",
    color: "#fff",
    border: "none",
    padding: "0.9rem 1.5rem",
    borderRadius: "12px",
    fontSize: "1.1rem",
    cursor: "pointer",
    marginTop: "1rem",
    fontWeight: 600,
    transition: "background 0.3s ease, transform 0.2s ease",
  },
};

styles.input[':focus'] = { borderColor: "#1d7a6e", boxShadow: "0 0 5px rgba(29,122,110,0.3)" };
styles.saveBtn[':hover'] = { backgroundColor: "#155f55", transform: "translateY(-1px)" };

export default EditEmployeeProfile;
