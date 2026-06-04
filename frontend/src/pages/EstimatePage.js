import React, { useEffect, useState } from 'react';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import '../components/Layout.css';

export default function EstimatePage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [projects, setProjects] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState({
    projectId: '', labourForceCount: '', labourCostPerPerson: '', materials: [],
  });

  useEffect(() => {
    const pEndpoint = isAdmin ? '/projects' : '/projects/my';
    Promise.all([API.get(pEndpoint), API.get('/materials')]).then(([p, m]) => {
      setProjects(p.data);
      setMaterials(m.data);
    });
  }, []);

  const addMaterialRow = () =>
    setForm((f) => ({ ...f, materials: [...f.materials, { materialId: '', quantity: '' }] }));

  const removeMaterialRow = (i) =>
    setForm((f) => ({ ...f, materials: f.materials.filter((_, idx) => idx !== i) }));

  const updateMatRow = (i, key, val) =>
    setForm((f) => {
      const updated = [...f.materials];
      updated[i] = { ...updated[i], [key]: val };
      return { ...f, materials: updated };
    });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setResult(null);
    setLoading(true);
    try {
      const payload = {
        projectId: parseInt(form.projectId),
        labourForceCount: parseInt(form.labourForceCount),
        labourCostPerPerson: parseFloat(form.labourCostPerPerson),
        materials: form.materials.map((m) => ({ materialId: parseInt(m.materialId), quantity: parseFloat(m.quantity) })),
      };
      const res = await API.post('/estimations', payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Estimation failed');
    } finally {
      setLoading(false);
    }
  };

  const fmt = (n) => `₹${Number(n || 0).toLocaleString('en-IN', { maximumFractionDigits: 2 })}`;

  return (
    <div>
      <h2 className="page-title">Cost Estimation (Module 4)</h2>
      <div className="card">
        {error && <div className="alert alert-error">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Select Project</label>
            <select required value={form.projectId} onChange={(e) => setForm({ ...form, projectId: e.target.value })}>
              <option value="">-- Select a project --</option>
              {projects.map((p) => <option key={p.id} value={p.id}>{p.name} — {p.city}</option>)}
            </select>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label>Labour Force Count</label>
              <input required type="number" min="1" value={form.labourForceCount}
                onChange={(e) => setForm({ ...form, labourForceCount: e.target.value })} />
            </div>
            <div className="form-group">
              <label>Labour Cost Per Person / Month (₹)</label>
              <input required type="number" min="1" value={form.labourCostPerPerson}
                onChange={(e) => setForm({ ...form, labourCostPerPerson: e.target.value })} />
            </div>
          </div>

          <div style={{ marginBottom: 16 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
              <label style={{ fontWeight: 600, color: '#555' }}>Materials Required</label>
              <button type="button" className="btn btn-secondary" style={{ padding: '4px 12px' }} onClick={addMaterialRow}>+ Add Material</button>
            </div>
            {form.materials.map((row, i) => (
              <div key={i} className="form-row" style={{ alignItems: 'flex-end' }}>
                <div className="form-group" style={{ margin: 0 }}>
                  <label>Material</label>
                  <select required value={row.materialId} onChange={(e) => updateMatRow(i, 'materialId', e.target.value)}>
                    <option value="">-- Select --</option>
                    {materials.map((m) => <option key={m.id} value={m.id}>{m.name} (₹{m.latestPrice}/{m.unit})</option>)}
                  </select>
                </div>
                <div className="form-group" style={{ margin: 0 }}>
                  <label>Quantity ({materials.find((m) => m.id === parseInt(row.materialId))?.unit || 'unit'})</label>
                  <input required type="number" min="0.01" step="0.01"
                    value={row.quantity} onChange={(e) => updateMatRow(i, 'quantity', e.target.value)} />
                </div>
                <button type="button" className="btn btn-danger" style={{ height: 38, padding: '0 10px' }}
                  onClick={() => removeMaterialRow(i)}>✕</button>
              </div>
            ))}
            {form.materials.length === 0 && <p style={{ color: '#aaa', fontSize: '0.85rem' }}>No materials added yet. Click "+ Add Material".</p>}
          </div>

          <button type="submit" className="btn btn-success" disabled={loading}>
            {loading ? 'Calculating...' : 'Calculate Estimate'}
          </button>
        </form>
      </div>

      {result && (
        <div className="card" style={{ borderLeft: '4px solid #27ae60' }}>
          <h3 style={{ color: '#1e3a5f', marginBottom: 16 }}>Estimation Result — {result.projectName}</h3>
          <table style={{ maxWidth: 500 }}>
            <tbody>
              <tr><td style={{ color: '#555' }}>Material Cost</td><td style={{ fontWeight: 600 }}>{fmt(result.materialCost)}</td></tr>
              <tr><td style={{ color: '#555' }}>Labour Cost ({result.labourForceCount} workers)</td><td style={{ fontWeight: 600 }}>{fmt(result.labourCost)}</td></tr>
              <tr><td style={{ color: '#555' }}>Time Surcharge</td><td style={{ fontWeight: 600 }}>{fmt(result.timeFactorCost)}</td></tr>
              <tr><td style={{ color: '#555' }}>Quality Surcharge ({result.qualityFactor}x multiplier)</td><td style={{ fontWeight: 600 }}>{fmt(result.qualityFactorCost)}</td></tr>
              <tr style={{ background: '#eafaf1' }}>
                <td style={{ fontWeight: 700, fontSize: '1rem' }}>TOTAL COST</td>
                <td style={{ fontWeight: 700, fontSize: '1.2rem', color: '#27ae60' }}>{fmt(result.totalCost)}</td>
              </tr>
            </tbody>
          </table>
          <p style={{ marginTop: 12, color: '#888', fontSize: '0.8rem' }}>
            Estimated at {new Date(result.estimatedAt).toLocaleString()} by {result.estimatedBy}
          </p>
        </div>
      )}
    </div>
  );
}
