import React, { useEffect, useState } from 'react';
import API from '../services/api';
import '../components/Layout.css';

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.get('/reports/summary').then((r) => setStats(r.data)).finally(() => setLoading(false));
  }, []);

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
      <h2 className="page-title">Dashboard</h2>
      {loading ? (
        <p>Loading statistics...</p>
      ) : (
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
        <ul style={{ marginTop: 14, paddingLeft: 20, color: '#555', lineHeight: 2 }}>
          <li>Module 1: Registration / Login</li>
          <li>Module 2: Material Requirement Data & Updation</li>
          <li>Module 3: Material Cost Updation</li>
          <li>Module 4: Cost Estimation</li>
          <li>Module 5: Cost Fluctuation Tracking</li>
          <li>Module 6: Reports</li>
        </ul>
      </div>
    </div>
  );
}
