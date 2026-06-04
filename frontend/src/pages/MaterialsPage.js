import React, { useEffect, useState } from 'react';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import '../components/Layout.css';

const EMPTY = { name: '', description: '', unit: '', basePricePerUnit: '', category: '' };

export default function MaterialsPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [materials, setMaterials] = useState([]);
  const [modal, setModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState('');

  const load = () => API.get('/materials').then((r) => setMaterials(r.data));
  useEffect(() => { load(); }, []);

  const openCreate = () => { setEditing(null); setForm(EMPTY); setError(''); setModal(true); };
  const openEdit = (m) => { setEditing(m.id); setForm({ ...m }); setError(''); setModal(true); };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (editing) {
        await API.put(`/materials/${editing}`, { ...form, basePricePerUnit: parseFloat(form.basePricePerUnit) });
      } else {
        await API.post('/materials', { ...form, basePricePerUnit: parseFloat(form.basePricePerUnit) });
      }
      setModal(false);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Operation failed');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this material?')) return;
    await API.delete(`/materials/${id}`);
    load();
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <h2 className="page-title" style={{ margin: 0 }}>Materials (Module 2)</h2>
        {isAdmin && <button className="btn btn-primary" onClick={openCreate}>+ Add Material</button>}
      </div>
      <div className="card" style={{ padding: 0 }}>
        <table>
          <thead>
            <tr>
              <th>#</th><th>Name</th><th>Category</th><th>Unit</th>
              <th>Base Price</th><th>Latest Price</th><th>Description</th>
              {isAdmin && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {materials.map((m, i) => (
              <tr key={m.id}>
                <td>{i + 1}</td>
                <td>{m.name}</td>
                <td>{m.category}</td>
                <td>{m.unit}</td>
                <td>₹{m.basePricePerUnit?.toLocaleString('en-IN')}</td>
                <td style={{ fontWeight: 600 }}>₹{m.latestPrice?.toLocaleString('en-IN')}</td>
                <td>{m.description}</td>
                {isAdmin && (
                  <td>
                    <button className="btn btn-warning" style={{ marginRight: 6 }} onClick={() => openEdit(m)}>Edit</button>
                    <button className="btn btn-danger" onClick={() => handleDelete(m.id)}>Del</button>
                  </td>
                )}
              </tr>
            ))}
            {materials.length === 0 && (
              <tr><td colSpan={8} style={{ textAlign: 'center', color: '#aaa' }}>No materials yet</td></tr>
            )}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>{editing ? 'Edit Material' : 'Add Material'}</h3>
              <button className="modal-close" onClick={() => setModal(false)}>&times;</button>
            </div>
            {error && <div className="alert alert-error">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Name</label>
                  <input required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
                </div>
                <div className="form-group">
                  <label>Category</label>
                  <input value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} placeholder="cement, steel, sand..." />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Unit</label>
                  <input required value={form.unit} onChange={(e) => setForm({ ...form, unit: e.target.value })} placeholder="kg, bags, sqft..." />
                </div>
                <div className="form-group">
                  <label>Base Price/Unit (₹)</label>
                  <input required type="number" min="0" step="0.01"
                    value={form.basePricePerUnit} onChange={(e) => setForm({ ...form, basePricePerUnit: e.target.value })} />
                </div>
              </div>
              <div className="form-group">
                <label>Description</label>
                <input value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
              </div>
              <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
                <button type="button" className="btn btn-secondary" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">{editing ? 'Update' : 'Create'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
