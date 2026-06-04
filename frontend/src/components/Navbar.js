import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const navItems = [
  { path: '/dashboard', label: 'Dashboard' },
  { path: '/projects', label: 'Projects' },
  { path: '/materials', label: 'Materials' },
  { path: '/material-costs', label: 'Material Costs' },
  { path: '/estimate', label: 'Cost Estimation' },
  { path: '/reports', label: 'Reports' },
];

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <span>&#127959;</span> CES
      </div>
      <ul className="navbar-links">
        {navItems.map((item) => (
          <li key={item.path}>
            <Link
              to={item.path}
              className={location.pathname === item.path ? 'active' : ''}
            >
              {item.label}
            </Link>
          </li>
        ))}
      </ul>
      <div className="navbar-user">
        <span className="role-badge">{user?.role}</span>
        <span>{user?.username}</span>
        <button onClick={handleLogout} className="btn-logout">Logout</button>
      </div>
    </nav>
  );
}
