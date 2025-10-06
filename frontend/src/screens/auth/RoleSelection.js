import React from 'react';
import { useNavigate } from 'react-router-dom';

const RoleSelection = ({ onBack }) => {
    const navigate = useNavigate();

    const handleSelectRole = (role) => {
        // Navigate to SignUp and pass role as state
        navigate('/signup', { state: { role } });
    };

    return (
        <div style={styles.pageContainer}>
            <div style={styles.leftContainer}>
                <button onClick={() => navigate('/')} style={styles.backButton} aria-label="Go back">
                    ←
                </button>
                <h1 style={styles.title}>Choose Role</h1>
                <div style={styles.rolesContainer}>
                    <div
                        style={styles.roleCard}
                        onClick={() => handleSelectRole('Customer')}
                        role="button"
                        tabIndex={0}
                        onKeyPress={(e) => { if (e.key === 'Enter') handleSelectRole('Customer'); }}
                    >
                        <h2 style={styles.roleTitle}>Customer</h2>
                        <p style={styles.roleDescription}>Book mobile car wash services</p>
                        <span style={styles.arrow}>→</span>
                    </div>
                    <div
                        style={styles.roleCard}
                        onClick={() => handleSelectRole('Employee')}
                        role="button"
                        tabIndex={0}
                        onKeyPress={(e) => { if (e.key === 'Enter') handleSelectRole('Employee'); }}
                    >
                        <h2 style={styles.roleTitle}>Employee</h2>
                        <p style={styles.roleDescription}>Manage and provide car wash services</p>
                        <span style={styles.arrow}>→</span>
                    </div>
                </div>
            </div>
            <div style={styles.rightContainer}>
                <img
                    src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80"
                    alt="Car wash"
                    style={styles.image}
                />
            </div>
        </div>
    );
};

const styles = {
    pageContainer: {
        display: 'flex',
        height: '100vh',
        fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    },
    leftContainer: {
        flex: 1,
        padding: '40px 30px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        position: 'relative',
    },
    backButton: {
        position: 'absolute',
        top: 20,
        left: 20,
        fontSize: 24,
        background: 'none',
        border: 'none',
        cursor: 'pointer',
    },
    title: {
        fontSize: 32,
        fontWeight: 'bold',
        marginBottom: 40,
        textAlign: 'center',
        width: '100%',
    },
    rolesContainer: {
        display: 'flex',
        flexDirection: 'column',
        gap: 20,
        width: '100%',
        maxWidth: 280,
    },
    roleCard: {
        border: '1px solid #ddd',
        borderRadius: 8,
        padding: 20,
        cursor: 'pointer',
        position: 'relative',
        boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
        minWidth: 420,
        maxWidth: 460,
        margin: '0 auto',
    },
    roleTitle: {
        margin: 0,
        fontSize: 20,
        fontWeight: '600',
    },
    roleDescription: {
        marginTop: 6,
        color: '#666',
        fontSize: 14,
    },
    arrow: {
        position: 'absolute',
        right: 20,
        top: '50%',
        transform: 'translateY(-50%)',
        fontSize: 18,
        color: '#888',
    },
    rightContainer: {
        flex: 1,
        overflow: 'hidden',
    },
    image: {
        width: '100%',
        height: '100%',
        objectFit: 'cover',
    },
};

export default RoleSelection;


// import React from 'react';
//
// // const RoleSelection = ({ onSelectRole, onBack }) => {
//
// const RoleSelection = ({ onBack }) => {
//     const navigate = useNavigate();
//
//     const handleSelectRole = (role) => {
//         // Navigate to SignUp and pass role as state
//         navigate('/signup', { state: { role } });
//     };
//
//     return (
//         <div style={styles.pageContainer}>
//             <div style={styles.leftContainer}>
//                 <button onClick={onBack} style={styles.backButton} aria-label="Go back">
//                     ←
//                 </button>
//                 <h1 style={styles.title}>Choose Role</h1>
//                 <div style={styles.rolesContainer}>
//                     <div
//                         style={styles.roleCard}
//                         onClick={() => onSelectRole('Customer')}
//                         role="button"
//                         tabIndex={0}
//                         onKeyPress={(e) => { if (e.key === 'Enter') onSelectRole('Customer'); }}
//                     >
//                         <h2 style={styles.roleTitle}>Customer</h2>
//                         <p style={styles.roleDescription}>Book mobile car wash services</p>
//                         <span style={styles.arrow}>→</span>
//                     </div>
//                     <div
//                         style={styles.roleCard}
//                         onClick={() => onSelectRole('Employee')}
//                         role="button"
//                         tabIndex={0}
//                         onKeyPress={(e) => { if (e.key === 'Enter') onSelectRole('Employee'); }}
//                     >
//                         <h2 style={styles.roleTitle}>Employee</h2>
//                         <p style={styles.roleDescription}>Manage and provide car wash services</p>
//                         <span style={styles.arrow}>→</span>
//                     </div>
//                 </div>
//             </div>
//             <div style={styles.rightContainer}>
//                 <img
//                     src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80"
//                     alt="Car wash"
//                     style={styles.image}
//                 />
//             </div>
//         </div>
//     );
// };
//
// const styles = {
//     pageContainer: {
//         display: 'flex',
//         height: '100vh',
//         fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
//     },
//     leftContainer: {
//         flex: 1,
//         padding: '40px 30px',
//         display: 'flex',
//         flexDirection: 'column',
//         alignItems: 'center',
//         position: 'relative',
//     },
//     backButton: {
//         position: 'absolute',
//         top: 20,
//         left: 20,
//         fontSize: 24,
//         background: 'none',
//         border: 'none',
//         cursor: 'pointer',
//     },
//     title: {
//         fontSize: 32,
//         fontWeight: 'bold',
//         marginBottom: 40,
//         textAlign: 'center',
//         width: '100%',
//     },
//     rolesContainer: {
//         display: 'flex',
//         flexDirection: 'column',
//         gap: 20,
//         width: '100%',
//         maxWidth: 280,
//     },
//     roleCard: {
//         border: '1px solid #ddd',
//         borderRadius: 8,
//         padding: 20,
//         cursor: 'pointer',
//         position: 'relative',
//         boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
//         minWidth: 420,
//         maxWidth: 460,
//         margin: '0 auto',
//     },
//     roleTitle: {
//         margin: 0,
//         fontSize: 20,
//         fontWeight: '600',
//     },
//     roleDescription: {
//         marginTop: 6,
//         color: '#666',
//         fontSize: 14,
//     },
//     arrow: {
//         position: 'absolute',
//         right: 20,
//         top: '50%',
//         transform: 'translateY(-50%)',
//         fontSize: 18,
//         color: '#888',
//     },
//     rightContainer: {
//         flex: 1,
//         overflow: 'hidden',
//     },
//     image: {
//         width: '100%',
//         height: '100%',
//         objectFit: 'cover',
//     },
// };
//
// export default RoleSelection;
