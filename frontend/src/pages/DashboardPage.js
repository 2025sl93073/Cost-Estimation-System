import React, { useEffect, useState } from 'react';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import '../components/Layout.css';

export default function DashboardPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (isAdmin) {
      API.get('/reports/summary').then((r) => setStats(r.data)).finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [isAdmin]);

  const statCards = stats
    ? [
        { label: 'Total Projects', value: stats.totalProjects, color: '#1e3a5f' },
        { label: 'Total Materials', value: stats.totalMaterials, color: '#27ae60' },
        { label: 'Total Users', value: stats.totalUsers, color: '#8e44ad' },
        { label: 'Total Estimations', value: stats.totalEstimations, color: '#e67e22' },
        { label: 'Aggregate Cost (₹)', value: `₹${Number(stats.aggregateCost || 0).toLocaleString('en-IN')}`, color: '#c0392b' },
      ]
    : [];

  return (
    <div>
      <h2 className="page-title">{isAdmin ? 'Dashboard' : 'About'}</h2>
      {isAdmin && loading && <p>Loading statistics...</p>}
      {isAdmin && !loading && (
        <div className="stat-grid">
          {statCards.map((s) => (
            <div className="stat-card" key={s.label} style={{ borderTopColor: s.color }}>
              <div className="stat-value" style={{ color: s.color }}>{s.value}</div>
              <div className="stat-label">{s.label}</div>
            </div>
          ))}
        </div>
      )}
      <div className="card">
        <h3 style={{ color: '#1e3a5f', marginBottom: 12 }}>About This System</h3>
        <p style={{ lineHeight: 1.7, color: '#555' }}>
          The <strong>Cost Estimation System</strong> enables estimation of building construction costs.
          It supports <em>Admin</em> and <em>Regular User</em> roles. Admins can manage materials,
          update costs, and view full reports. All users can create projects and generate cost estimations
          based on materials, labour force, time, and quality factors.
        </p>
      </div>
    </div>
  );
}
