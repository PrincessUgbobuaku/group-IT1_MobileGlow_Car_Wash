// src/components/EmployeeManagement.js
import React, { useState, useEffect } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { employeeServiceSimple } from '../services/employeeService';
import './EmployeeManagement.css';

// Constants to avoid magic strings
const EMPLOYEE_TYPES = {
    MANAGER: 'Manager',
    ACCOUNTANT: 'Accountant',
    WASH_ATTENDANT: 'Wash Attendant'
};

const SHIFT_TYPES = {
    DAY: 'DAY',
    NIGHT: 'NIGHT',
    SWING: 'SWING'
};

const EmployeeManagement = () => {
    const [employees, setEmployees] = useState([]);
    const [filteredEmployees, setFilteredEmployees] = useState([]);
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [activeTab, setActiveTab] = useState('all');
    const [loading, setLoading] = useState(true);
    const [formLoading, setFormLoading] = useState(false);
    const [deletingId, setDeletingId] = useState(null); // Track which employee is being deleted

    useEffect(() => {
        fetchEmployees();
    }, []);

    const fetchEmployees = async () => {
        try {
            setLoading(true);
            const [managersResponse, accountantsResponse, washAttendantsResponse] = await Promise.all([
                employeeServiceSimple.getAllManagers(),
                employeeServiceSimple.getAllAccountants(),
                employeeServiceSimple.getAllWashAttendants()
            ]);

            const managers = managersResponse.data.map(emp => ({ ...emp, type: EMPLOYEE_TYPES.MANAGER }));
            const accountants = accountantsResponse.data.map(emp => ({ ...emp, type: EMPLOYEE_TYPES.ACCOUNTANT }));
            const washAttendants = washAttendantsResponse.data.map(emp => ({ ...emp, type: EMPLOYEE_TYPES.WASH_ATTENDANT }));

            const allEmployees = [...managers, ...accountants, ...washAttendants];

            setEmployees(allEmployees);
            setFilteredEmployees(allEmployees);
        } catch (error) {
            console.error('Fetch error:', error);
            toast.error('Failed to fetch employees');
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (e) => {
        const term = e.target.value.toLowerCase();
        setSearchTerm(term);

        const filtered = employees.filter(emp =>
            (emp.userName?.toLowerCase() || '').includes(term) ||
            (emp.userSurname?.toLowerCase() || '').includes(term) ||
            (emp.login?.emailAddress?.toLowerCase() || '').includes(term) ||
            (emp.position?.toLowerCase() || '').includes(term) ||
            (emp.contact?.phoneNumber || '').includes(term)
        );

        setFilteredEmployees(filtered);
    };

    const filterByStatus = (status) => {
        setActiveTab(status);
        if (status === 'all') {
            setFilteredEmployees(employees);
        } else if (status === 'active') {
            setFilteredEmployees(employees.filter(emp => emp.isActive));
        } else if (status === 'inactive') {
            setFilteredEmployees(employees.filter(emp => !emp.isActive));
        }
    };

    const handleEdit = (employee) => {
        setSelectedEmployee(employee);
        setShowForm(true);
    };

    const handleDelete = async (id, type) => {
        if (!window.confirm('Are you sure you want to delete this employee?')) {
            return;
        }

        setDeletingId(id); // Start loading for this specific item
        try {
            switch (type) {
                case EMPLOYEE_TYPES.MANAGER:
                    await employeeServiceSimple.deleteManager(id);
                    break;
                case EMPLOYEE_TYPES.ACCOUNTANT:
                    await employeeServiceSimple.deleteAccountant(id);
                    break;
                case EMPLOYEE_TYPES.WASH_ATTENDANT:
                    await employeeServiceSimple.deleteWashAttendant(id);
                    break;
                default:
                    throw new Error('Unknown employee type');
            }

            setEmployees(employees.filter(emp => emp.userId !== id));
            setFilteredEmployees(filteredEmployees.filter(emp => emp.userId !== id));
            toast.success('Employee deleted successfully');
        } catch (error) {
            console.error('Delete error:', error);
            toast.error('Failed to delete employee');
        } finally {
            setDeletingId(null); // Stop loading
        }
    };

    const handleSubmit = async (formData) => {
        try {
            setFormLoading(true);

            // 1. Prepare the base data structure
            const baseData = {
                userName: formData.userName,
                userSurname: formData.userSurname,
                isActive: formData.isActive,
                contact: formData.contact,
                address: formData.address,
                login: formData.login,
                roleDescription: 'EMPLOYEE'
            };

            // 2. If we are updating, include the userId IMMEDIATELY
            if (selectedEmployee) {
                baseData.userId = selectedEmployee.userId;
            }

            let apiData = { ...baseData };
            let serviceCall;

            // 3. Add type-specific fields to the baseData (which now has the userId if it's an update)
            switch (formData.type) {
                case EMPLOYEE_TYPES.MANAGER:
                    apiData = {
                        ...apiData,
                        hireDate: formData.hireDate,
                        department: formData.department,
                        position: formData.position
                    };
                    serviceCall = selectedEmployee ?
                        employeeServiceSimple.updateManager(apiData) :
                        employeeServiceSimple.createManager(apiData);
                    break;

                case EMPLOYEE_TYPES.ACCOUNTANT:
                    apiData = {
                        ...apiData,
                        employeeType: formData.employeeType,
                        hasTaxFillingAuthority: formData.hasTaxFillingAuthority,
                        certificationNumber: formData.certificationNumber
                    };
                    serviceCall = selectedEmployee ?
                        employeeServiceSimple.updateAccountant(apiData) :
                        employeeServiceSimple.createAccountant(apiData);
                    break;

                case EMPLOYEE_TYPES.WASH_ATTENDANT:
                    apiData = {
                        ...apiData,
                        shift: formData.shift,
                        hourlyRate: formData.hourlyRate,
                        yearsOfExperience: formData.yearsOfExperience,
                        specialization: formData.specialization
                    };
                    serviceCall = selectedEmployee ?
                        employeeServiceSimple.updateWashAttendant(apiData) :
                        employeeServiceSimple.createWashAttendant(apiData);
                    break;

                default:
                    throw new Error('Unknown employee type');
            }

            // 4. Make the API call
            await serviceCall;
            toast.success(`Employee ${selectedEmployee ? 'updated' : 'created'} successfully`);
            await fetchEmployees(); // Refresh the list
            setShowForm(false);
            setSelectedEmployee(null);

        } catch (error) {
            console.error('Submit error:', error);
            toast.error(selectedEmployee ? 'Failed to update employee' : 'Failed to create employee');
        } finally {
            setFormLoading(false);
        }
    };

    const handleCancel = () => {
        setShowForm(false);
        setSelectedEmployee(null);
    };

    const stats = {
        total: employees.length,
        active: employees.filter(emp => emp.isActive).length,
        managers: employees.filter(emp => emp.type === EMPLOYEE_TYPES.MANAGER).length,
        accountants: employees.filter(emp => emp.type === EMPLOYEE_TYPES.ACCOUNTANT).length,
        washAttendants: employees.filter(emp => emp.type === EMPLOYEE_TYPES.WASH_ATTENDANT).length
    };

    if (loading) {
        return (
            <div className="employee-management-loading">
                <div className="loading-spinner"></div>
                <p>Loading employees...</p>
            </div>
        );
    }

    return (
        <div className="employee-management">
            <div className="em-header">
                <div className="em-header-content">
                    <h1>Employee Management</h1>
                    <p>Manage your team members and their roles</p>
                </div>
                <button
                    className="em-primary-btn"
                    onClick={() => setShowForm(true)}
                    disabled={formLoading}
                >
                    {formLoading ? '⏳' : '+'} Add Employee
                </button>
            </div>

            <div className="em-controls">
                <div className="em-search">
                    <input
                        type="text"
                        placeholder="Search employees..."
                        value={searchTerm}
                        onChange={handleSearch}
                        className="em-search-input"
                        disabled={formLoading}
                    />
                </div>

                <div className="em-filters">
                    <button
                        className={`em-filter-btn ${activeTab === 'all' ? 'active' : ''}`}
                        onClick={() => filterByStatus('all')}
                        disabled={formLoading}
                    >
                        All Employees
                    </button>
                    <button
                        className={`em-filter-btn ${activeTab === 'active' ? 'active' : ''}`}
                        onClick={() => filterByStatus('active')}
                        disabled={formLoading}
                    >
                        Active
                    </button>
                    <button
                        className={`em-filter-btn ${activeTab === 'inactive' ? 'active' : ''}`}
                        onClick={() => filterByStatus('inactive')}
                        disabled={formLoading}
                    >
                        Inactive
                    </button>
                </div>
            </div>

            <div className="em-stats">
                <div className="em-stat-card">
                    <h3>{stats.total}</h3>
                    <p>Total Employees</p>
                </div>
                <div className="em-stat-card">
                    <h3>{stats.active}</h3>
                    <p>Active</p>
                </div>
                <div className="em-stat-card">
                    <h3>{stats.managers}</h3>
                    <p>Managers</p>
                </div>
                <div className="em-stat-card">
                    <h3>{stats.accountants}</h3>
                    <p>Accountants</p>
                </div>
                <div className="em-stat-card">
                    <h3>{stats.washAttendants}</h3>
                    <p>Wash Attendants</p>
                </div>
            </div>

            <div className="em-content">
                {showForm ? (
                    <EmployeeForm
                        employee={selectedEmployee}
                        onSubmit={handleSubmit}
                        onCancel={handleCancel}
                        loading={formLoading}
                    />
                ) : (
                    <EmployeeTable
                        employees={filteredEmployees}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onRefresh={fetchEmployees}
                        onAddNew={() => setShowForm(true)}
                        loading={formLoading}
                        deletingId={deletingId}
                    />
                )}
            </div>

            <ToastContainer position="top-right" autoClose={3000} />
        </div>
    );
};

// Employee Form Component
const EmployeeForm = ({ employee, onSubmit, onCancel, loading }) => {
    const [formData, setFormData] = useState({
        userName: '',
        userSurname: '',
        type: EMPLOYEE_TYPES.MANAGER,
        login: { emailAddress: '' },
        contact: { phoneNumber: '' },
        address: {
            streetNumber: '',
            streetName: '',
            city: '',
            postalCode: ''
        },
        isActive: true,
        // Manager specific
        hireDate: '',
        department: '',
        position: '',
        // Accountant specific
        employeeType: '',
        hasTaxFillingAuthority: false,
        certificationNumber: '',
        // Wash Attendant specific
        shift: SHIFT_TYPES.DAY,
        hourlyRate: '',
        yearsOfExperience: '',
        specialization: ''
    });

    useEffect(() => {
        if (employee) {
            const baseData = {
                userName: employee.userName || '',
                userSurname: employee.userSurname || '',
                type: employee.type || EMPLOYEE_TYPES.MANAGER,
                login: { emailAddress: employee.login?.emailAddress || '' },
                contact: { phoneNumber: employee.contact?.phoneNumber || '' },
                address: {
                    streetNumber: employee.address?.streetNumber || '',
                    streetName: employee.address?.streetName || '',
                    city: employee.address?.city || '',
                    postalCode: employee.address?.postalCode || ''
                },
                isActive: employee.isActive !== undefined ? employee.isActive : true
            };

            if (employee.type === EMPLOYEE_TYPES.MANAGER) {
                baseData.hireDate = employee.hireDate || '';
                baseData.department = employee.department || '';
                baseData.position = employee.position || '';
            } else if (employee.type === EMPLOYEE_TYPES.ACCOUNTANT) {
                baseData.employeeType = employee.employeeType || '';
                baseData.hasTaxFillingAuthority = employee.hasTaxFillingAuthority || false;
                baseData.certificationNumber = employee.certificationNumber || '';
            } else if (employee.type === EMPLOYEE_TYPES.WASH_ATTENDANT) {
                baseData.shift = employee.shift || SHIFT_TYPES.DAY;
                baseData.hourlyRate = employee.hourlyRate || '';
                baseData.yearsOfExperience = employee.yearsOfExperience || '';
                baseData.specialization = employee.specialization || '';
            }

            setFormData(baseData);
        }
    }, [employee]);

    const handleInputChange = (field, value) => {
        if (field.includes('.')) {
            const [parent, child] = field.split('.');
            setFormData(prev => ({
                ...prev,
                [parent]: {
                    ...prev[parent],
                    [child]: value
                }
            }));
        } else {
            setFormData(prev => ({ ...prev, [field]: value }));
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const requiredFields = ['userName', 'userSurname', 'login.emailAddress', 'contact.phoneNumber'];
        const missingFields = requiredFields.filter(field => {
            const value = field.includes('.')
                ? formData[field.split('.')[0]][field.split('.')[1]]
                : formData[field];
            return !value;
        });

        if (missingFields.length > 0) {
            toast.error('Please fill in all required fields');
            return;
        }

        if (formData.type === EMPLOYEE_TYPES.ACCOUNTANT && !formData.certificationNumber) {
            toast.error('Certification number is required for accountants');
            return;
        }

        if (formData.type === EMPLOYEE_TYPES.WASH_ATTENDANT && !formData.hourlyRate) {
            toast.error('Hourly rate is required for wash attendants');
            return;
        }

        onSubmit(formData);
    };

    const renderTypeSpecificFields = () => {
        switch (formData.type) {
            case EMPLOYEE_TYPES.MANAGER:
                return (
                    <div className="form-section manager-fields">
                        <h4>Manager Details</h4>
                        <div className="form-grid">
                            <div className="form-group">
                                <label>Hire Date</label>
                                <input
                                    type="date"
                                    value={formData.hireDate}
                                    onChange={(e) => handleInputChange('hireDate', e.target.value)}
                                    placeholder="Select hire date"
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Department</label>
                                <input
                                    type="text"
                                    value={formData.department}
                                    onChange={(e) => handleInputChange('department', e.target.value)}
                                    placeholder="Enter department"
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Position</label>
                                <input
                                    type="text"
                                    value={formData.position}
                                    onChange={(e) => handleInputChange('position', e.target.value)}
                                    placeholder="Enter position"
                                    disabled={loading}
                                />
                            </div>
                        </div>
                    </div>
                );

            case EMPLOYEE_TYPES.ACCOUNTANT:
                return (
                    <div className="form-section accountant-fields">
                        <h4>Accountant Details</h4>
                        <div className="form-grid">
                            <div className="form-group">
                                <label className="required-field">Employee Type</label>
                                <select
                                    value={formData.employeeType}
                                    onChange={(e) => handleInputChange('employeeType', e.target.value)}
                                    className={!formData.employeeType ? 'error' : ''}
                                    required
                                    disabled={loading}
                                >
                                    <option value="">Select type</option>
                                    <option value="SENIOR">Senior Accountant</option>
                                    <option value="JUNIOR">Junior Accountant</option>
                                    <option value="INTERN">Accounting Intern</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label className="required-field">Certification Number</label>
                                <input
                                    type="text"
                                    value={formData.certificationNumber}
                                    onChange={(e) => handleInputChange('certificationNumber', e.target.value)}
                                    className={!formData.certificationNumber ? 'error' : ''}
                                    placeholder="Enter certification number"
                                    required
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        checked={formData.hasTaxFillingAuthority}
                                        onChange={(e) => handleInputChange('hasTaxFillingAuthority', e.target.checked)}
                                        disabled={loading}
                                    />
                                    <span className="checkmark"></span>
                                    Has Tax Filing Authority
                                </label>
                            </div>
                        </div>
                    </div>
                );

            case EMPLOYEE_TYPES.WASH_ATTENDANT:
                return (
                    <div className="form-section wash-attendant-fields">
                        <h4>Wash Attendant Details</h4>
                        <div className="form-grid">
                            <div className="form-group">
                                <label>Shift</label>
                                <select
                                    value={formData.shift}
                                    onChange={(e) => handleInputChange('shift', e.target.value)}
                                    disabled={loading}
                                >
                                    <option value={SHIFT_TYPES.DAY}>Day Shift</option>
                                    <option value={SHIFT_TYPES.NIGHT}>Night Shift</option>
                                    <option value={SHIFT_TYPES.SWING}>Swing Shift</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label className="required-field">Hourly Rate (ZAR)</label>
                                <input
                                    type="number"
                                    step="0.01"
                                    value={formData.hourlyRate}
                                    onChange={(e) => handleInputChange('hourlyRate', e.target.value)}
                                    className={!formData.hourlyRate ? 'error' : ''}
                                    placeholder="Enter hourly rate"
                                    required
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Years of Experience</label>
                                <input
                                    type="number"
                                    value={formData.yearsOfExperience}
                                    onChange={(e) => handleInputChange('yearsOfExperience', e.target.value)}
                                    placeholder="Enter years of experience"
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Specialization</label>
                                <select
                                    value={formData.specialization}
                                    onChange={(e) => handleInputChange('specialization', e.target.value)}
                                    disabled={loading}
                                >
                                    <option value="">Select specialization</option>
                                    <option value="INTERIOR">Interior Cleaning</option>
                                    <option value="EXTERIOR">Exterior Cleaning</option>
                                    <option value="DETAILING">Detailing</option>
                                    <option value="POLISHING">Polishing</option>
                                </select>
                            </div>
                        </div>
                    </div>
                );

            default:
                return null;
        }
    };

    return (
        <div className="em-form-overlay">
            <div className="em-form-container scrollable-form">
                <div className="em-form-header">
                    <h2>{employee ? 'Edit Employee' : 'Add New Employee'}</h2>
                    <button
                        className="em-close-btn"
                        onClick={onCancel}
                        disabled={loading}
                    >
                        ×
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="em-form compact-form">
                    <div className="form-row">
                        <div className="form-group">
                            <label className="required-field">Employee Type</label>
                            <select
                                value={formData.type}
                                onChange={(e) => handleInputChange('type', e.target.value)}
                                className={!formData.type ? 'error' : ''}
                                disabled={!!employee || loading}
                                required
                            >
                                <option value={EMPLOYEE_TYPES.MANAGER}>Manager</option>
                                <option value={EMPLOYEE_TYPES.ACCOUNTANT}>Accountant</option>
                                <option value={EMPLOYEE_TYPES.WASH_ATTENDANT}>Wash Attendant</option>
                            </select>
                        </div>
                    </div>

                    <div className="section-header">
                        <span className="section-icon">👤</span>
                        <h3>Personal Information</h3>
                    </div>

                    <div className="form-grid">
                        <div className="form-group">
                            <label className="required-field">First Name</label>
                            <input
                                type="text"
                                value={formData.userName}
                                onChange={(e) => handleInputChange('userName', e.target.value)}
                                className={!formData.userName ? 'error' : ''}
                                placeholder="Enter first name"
                                required
                                disabled={loading}
                            />
                        </div>
                        <div className="form-group">
                            <label className="required-field">Last Name</label>
                            <input
                                type="text"
                                value={formData.userSurname}
                                onChange={(e) => handleInputChange('userSurname', e.target.value)}
                                className={!formData.userSurname ? 'error' : ''}
                                placeholder="Enter last name"
                                required
                                disabled={loading}
                            />
                        </div>
                    </div>

                    <div className="section-header">
                        <span className="section-icon">📞</span>
                        <h3>Contact Information</h3>
                    </div>

                    <div className="form-grid">
                        <div className="form-group">
                            <label className="required-field">Email Address</label>
                            <input
                                type="email"
                                value={formData.login.emailAddress}
                                onChange={(e) => handleInputChange('login.emailAddress', e.target.value)}
                                className={!formData.login.emailAddress ? 'error' : ''}
                                placeholder="Enter email"
                                required
                                disabled={loading}
                            />
                        </div>
                        <div className="form-group">
                            <label className="required-field">Phone Number</label>
                            <input
                                type="tel"
                                value={formData.contact.phoneNumber}
                                onChange={(e) => handleInputChange('contact.phoneNumber', e.target.value)}
                                className={!formData.contact.phoneNumber ? 'error' : ''}
                                placeholder="Enter phone number"
                                required
                                disabled={loading}
                            />
                        </div>
                    </div>

                    <div className="section-header">
                        <span className="section-icon">🏠</span>
                        <h3>Address Information</h3>
                    </div>

                    <div className="form-grid">
                        <div className="form-group">
                            <label>Street Number</label>
                            <input
                                type="text"
                                value={formData.address.streetNumber}
                                onChange={(e) => handleInputChange('address.streetNumber', e.target.value)}
                                placeholder="Enter street number"
                                disabled={loading}
                            />
                        </div>
                        <div className="form-group">
                            <label>Street Name</label>
                            <input
                                type="text"
                                value={formData.address.streetName}
                                onChange={(e) => handleInputChange('address.streetName', e.target.value)}
                                placeholder="Enter street name"
                                disabled={loading}
                            />
                        </div>
                        <div className="form-group">
                            <label>City</label>
                            <input
                                type="text"
                                value={formData.address.city}
                                onChange={(e) => handleInputChange('address.city', e.target.value)}
                                placeholder="Enter city"
                                disabled={loading}
                            />
                        </div>
                        <div className="form-group">
                            <label>Postal Code</label>
                            <input
                                type="text"
                                value={formData.address.postalCode}
                                onChange={(e) => handleInputChange('address.postalCode', e.target.value)}
                                placeholder="Enter postal code"
                                disabled={loading}
                            />
                        </div>
                    </div>

                    {renderTypeSpecificFields()}

                    <div className="section-header">
                        <span className="section-icon">⚙️</span>
                        <h3>Employment Status</h3>
                    </div>

                    <div className="form-group">
                        <label className="checkbox-label">
                            <input
                                type="checkbox"
                                checked={formData.isActive}
                                onChange={(e) => handleInputChange('isActive', e.target.checked)}
                                disabled={loading}
                            />
                            <span className="checkmark"></span>
                            Active Employee
                        </label>
                    </div>

                    <div className="form-actions">
                        <button
                            type="submit"
                            className="em-primary-btn"
                            disabled={loading}
                        >
                            {loading ? '⏳ ' : ''}
                            {employee ? 'Update' : 'Create'} Employee
                        </button>
                        <button
                            type="button"
                            className="em-secondary-btn"
                            onClick={onCancel}
                            disabled={loading}
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

// Employee Table Component
const EmployeeTable = ({ employees, onEdit, onDelete, onRefresh, onAddNew, loading, deletingId }) => {
    return (
        <div className="em-table-container">
            <div className="em-table-header">
                <h2>Employee List</h2>
                <div className="em-table-actions">
                    <button
                        className="em-secondary-btn"
                        onClick={onRefresh}
                        disabled={loading}
                    >
                        🔄 Refresh
                    </button>
                    <button
                        className="em-primary-btn"
                        onClick={onAddNew}
                        disabled={loading}
                    >
                        ➕ Add Employee
                    </button>
                </div>
            </div>

            <div className="em-employees-grid">
                {employees.map((employee) => (
                    <div key={`${employee.type}-${employee.userId}`} className="em-employee-card">
                        <div className="em-employee-header">
                            <div className="em-employee-avatar">
                                {employee.userName?.charAt(0)}{employee.userSurname?.charAt(0)}
                            </div>
                            <div className="em-employee-status">
                                <span className={`status-dot ${employee.isActive ? 'active' : 'inactive'}`}></span>
                                {employee.isActive ? 'Active' : 'Inactive'}
                            </div>
                        </div>

                        <div className="em-employee-info">
                            <h3>{employee.userName} {employee.userSurname}</h3>
                            <p className="em-employee-type">{employee.type}</p>
                            <p className="em-employee-email">{employee.login?.emailAddress}</p>
                            <p className="em-employee-phone">{employee.contact?.phoneNumber}</p>
                            <p className="em-employee-position">{employee.position}</p>

                            {employee.type === EMPLOYEE_TYPES.ACCOUNTANT && (
                                <p className="em-employee-detail">
                                    📊 {employee.employeeType} • {employee.hasTaxFillingAuthority ? 'Tax Authority' : 'No Tax Authority'}
                                </p>
                            )}
                            {employee.type === EMPLOYEE_TYPES.WASH_ATTENDANT && (
                                <p className="em-employee-detail">
                                    ⏰ {employee.shift} • ZAR {employee.hourlyRate}/hr
                                </p>
                            )}
                            {employee.type === EMPLOYEE_TYPES.MANAGER && employee.hireDate && (
                                <p className="em-employee-date">
                                    📅 Hired: {new Date(employee.hireDate).toLocaleDateString()}
                                </p>
                            )}
                        </div>

                        <div className="em-employee-actions">
                            <button
                                className="em-action-btn edit"
                                onClick={() => onEdit(employee)}
                                title="Edit"
                                disabled={loading}
                            >
                                ✏️
                            </button>
                            <button
                                className="em-action-btn delete"
                                onClick={() => onDelete(employee.userId, employee.type)}
                                title="Delete"
                                disabled={loading || deletingId === employee.userId}
                            >
                                {deletingId === employee.userId ? '⏳' : '🗑️'}
                            </button>
                        </div>
                    </div>
                ))}

                {employees.length === 0 && (
                    <div className="em-empty-state">
                        <div className="em-empty-icon">👥</div>
                        <h3>No employees found</h3>
                        <p>Try adjusting your search or add a new employee</p>
                        <button
                            className="em-primary-btn"
                            onClick={onAddNew}
                            disabled={loading}
                        >
                            Add First Employee
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default EmployeeManagement;