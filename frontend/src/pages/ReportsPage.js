import React, { useEffect, useState } from 'react';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Legend } from 'recharts';
import '../components/Layout.css';

const COLORS = ['#1e3a5f', '#27ae60', '#e67e22', '#8e44ad', '#e74c3c', '#2e86de', '#f39c12'];

export default function ReportsPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [summary, setSummary] = useState(null);
  const [estimations, setEstimations] = useState([]);
  const [byCity, setByCity] = useState([]);
  const [byType, setByType] = useState([]);

  useEffect(() => {
    API.get('/reports/summary').then((r) => setSummary(r.data));
    const estEndpoint = isAdmin ? '/estimations' : '/estimations/my';
    API.get(estEndpoint).then((r) => setEstimations(r.data));
    API.get('/reports/by-city').then((r) => {
      const data = Object.entries(r.data.byCity || {}).map(([city, count]) => ({ city, count }));
      setByCity(data);
    });
    API.get('/reports/by-type').then((r) => {
      const data = Object.entries(r.data.byType || {}).map(([name, value]) => ({ name, value }));
      setByType(data);
    });
  }, []);

  const fmt = (n) => `₹${Number(n || 0).toLocaleString('en-IN', { maximumFractionDigits: 0 })}`;

  return (
    <div>
      <h2 className="page-title">Reports</h2>

      {summary && (
        <div className="stat-grid">
          {[
            { label: 'Total Projects', value: summary.totalProjects, color: '#1e3a5f' },
            { label: 'Total Materials', value: summary.totalMaterials, color: '#27ae60' },
            { label: 'Total Users', value: summary.totalUsers, color: '#8e44ad' },
            { label: 'Estimations', value: summary.totalEstimations, color: '#e67e22' },
            { label: 'Total Cost', value: fmt(summary.aggregateCost), color: '#e74c3c' },
          ].map((s) => (
            <div className="stat-card" key={s.label} style={{ borderTopColor: s.color }}>
              <div className="stat-value" style={{ color: s.color }}>{s.value}</div>
              <div className="stat-label">{s.label}</div>
            </div>
          ))}
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
        <div className="card">
          <h3 style={{ marginBottom: 16, color: '#1e3a5f' }}>Projects by City</h3>
          {byCity.length > 0 ? (
            <ResponsiveContainer width="100%" height={240}>
              <BarChart data={byCity}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="city" />
                <YAxis allowDecimals={false} />
                <Tooltip />
                <Bar dataKey="count" fill="#1e3a5f" />
              </BarChart>
            </ResponsiveContainer>
          ) : <p style={{ color: '#aaa' }}>No data</p>}
        </div>

        <div className="card">
          <h3 style={{ marginBottom: 16, color: '#1e3a5f' }}>Projects by Type</h3>
          {byType.length > 0 ? (
            <ResponsiveContainer width="100%" height={240}>
              <PieChart>
                <Pie data={byType} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={80} label>
                  {byType.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                </Pie>
                <Legend />
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          ) : <p style={{ color: '#aaa' }}>No data</p>}
        </div>
      </div>

      <div className="card">
        <h3 style={{ marginBottom: 16, color: '#1e3a5f' }}>Cost Estimation Report</h3>
        <table>
          <thead>
            <tr><th>#</th><th>Project</th><th>Material Cost</th><th>Labour Cost</th><th>Quality Surcharge</th><th>Quality Factor</th><th>Total Cost</th><th>Estimated By</th><th>Date</th></tr>
          </thead>
          <tbody>
            {estimations.map((e, i) => (
              <tr key={e.id}>
                <td>{i + 1}</td>
                <td>{e.projectName}</td>
                <td>{fmt(e.materialCost)}</td>
                <td>{fmt(e.labourCost)}</td>
                <td>{fmt(e.qualityFactorCost)}</td>
                <td style={{ textAlign: 'center' }}>{e.qualityFactor}x</td>
                <td style={{ fontWeight: 700, color: '#27ae60' }}>{fmt(e.totalCost)}</td>
                <td>{e.estimatedBy}</td>
                <td>{e.estimatedAt ? new Date(e.estimatedAt).toLocaleDateString() : '-'}</td>
              </tr>
            ))}
            {estimations.length === 0 && (
              <tr><td colSpan={8} style={{ textAlign: 'center', color: '#aaa' }}>No estimations yet</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
