import React, { useEffect, useState } from 'react';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import '../components/Layout.css';

export default function MaterialCostPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [materials, setMaterials] = useState([]);
  const [modal, setModal] = useState(false);
  const [form, setForm] = useState({ materialId: '', price: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    API.get('/materials').then((r) => setMaterials(r.data));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    try {
      await API.post('/materials/cost', {
        ...form,
        price: parseFloat(form.price),
        materialId: parseInt(form.materialId),
        effectiveDate: new Date().toISOString().split('T')[0],
      });
      setSuccess('Material cost updated successfully!');
      setModal(false);
      API.get('/materials').then((r) => setMaterials(r.data));
    } catch (err) {
      setError(err.response?.data?.message || 'Update failed');
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <h2 className="page-title" style={{ margin: 0 }}>Material Cost Updation</h2>
        {isAdmin && <button className="btn btn-primary" onClick={() => { setError(''); setSuccess(''); setModal(true); }}>+ Update Cost</button>}
      </div>

      {success && <div className="alert alert-success">{success}</div>}

      <div className="card" style={{ padding: 0 }}>
        <table>
          <thead>
            <tr><th>Material</th><th>Category</th><th>Unit</th><th>Price</th></tr>
          </thead>
          <tbody>
            {materials.map((m) => (
              <tr key={m.id}>
                <td>{m.name}</td>
                <td>{m.category}</td>
                <td>{m.unit}</td>
                <td style={{ fontWeight: 600 }}>₹{(m.latestPrice ?? m.basePricePerUnit)?.toLocaleString('en-IN')}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>Update Material Cost</h3>
              <button className="modal-close" onClick={() => setModal(false)}>&times;</button>
            </div>
            {error && <div className="alert alert-error">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Select Material</label>
                <select required value={form.materialId} onChange={(e) => setForm({ ...form, materialId: e.target.value })}>
                  <option value="">-- Select --</option>
                  {materials.map((m) => <option key={m.id} value={m.id}>{m.name} (Current: ₹{m.latestPrice ?? m.basePricePerUnit})</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>New Price (₹)</label>
                <input required type="number" min="0" step="0.01"
                  value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} />
              </div>
              <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Update Cost</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
