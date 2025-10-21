import React from 'react';
import { useNavigate } from 'react-router-dom';
import image1 from '../../assets/image-1.png';
import './RoleSelection.css';

const RoleSelection = ({ onBack }) => {
    const navigate = useNavigate();

    const handleSelectRole = (role) => {
        // Navigate to SignUp and pass role as state
        navigate('/signup', { state: { role } });
    };

    return (
        <div className="role-selection-wrapper">
            <div className="role-selection-section">
                <button onClick={() => navigate('/')} className="back-button" aria-label="Go back">
                    <i className="fas fa-arrow-left"></i>
                    <span>Back</span>
                </button>
                <h1 className="role-selection-title">Choose Role</h1>
                <div className="roles-container">
                    <div
                        className="role-card"
                        onClick={() => handleSelectRole('Customer')}
                        role="button"
                        tabIndex={0}
                        onKeyPress={(e) => { if (e.key === 'Enter') handleSelectRole('Customer'); }}
                    >
                        <div className="role-icon">
                            <i className="fas fa-user"></i>
                        </div>
                        <div className="role-content">
                            <h2 className="role-title">Customer</h2>
                            <p className="role-description">Book mobile car wash services</p>
                        </div>
                        <span className="role-arrow">→</span>
                    </div>
                    <div
                        className="role-card"
                        onClick={() => handleSelectRole('Employee')}
                        role="button"
                        tabIndex={0}
                        onKeyPress={(e) => { if (e.key === 'Enter') handleSelectRole('Employee'); }}
                    >
                        <div className="role-icon">
                            <i className="fas fa-briefcase"></i>
                        </div>
                        <div className="role-content">
                            <h2 className="role-title">Employee</h2>
                            <p className="role-description">Manage and provide car wash services</p>
                        </div>
                        <span className="role-arrow">→</span>
                    </div>
                </div>
            </div>
            <div className="image-section">
                <img
                    src={image1}
                    alt="Car wash"
                />
            </div>
        </div>
    );
};

export default RoleSelection;
