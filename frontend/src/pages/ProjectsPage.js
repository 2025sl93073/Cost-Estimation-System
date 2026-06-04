import React, { useEffect, useState } from 'react';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import '../components/Layout.css';

const TYPES = {
  constructionType: ['COMMERCIAL', 'RESIDENTIAL'],
  type1: ['BUSINESS', 'HOUSING'],
  type2: ['COMMERCIAL_ESTABLISHMENT', 'RESIDENTIAL'],
  structureType: ['COMPLEX', 'BRIDGE', 'HOUSE', 'FLYOVER', 'OTHER'],
};

const EMPTY = {
  name: '', constructionType: '', type1: '', type2: '', structureType: '',
  area: '', street: '', city: '', areaSqft: '', timeRequiredMonths: '', qualityFactor: '1.0',
};

export default function ProjectsPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';
  const [projects, setProjects] = useState([]);
  const [modal, setModal] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [error, setError] = useState('');

  const load = () => {
    const endpoint = isAdmin ? '/projects' : '/projects/my';
    API.get(endpoint).then((r) => setProjects(r.data));
  };
  useEffect(() => { load(); }, []);

  const openCreate = () => { setEditing(null); setForm(EMPTY); setError(''); setModal(true); };
  const openEdit = (p) => { setEditing(p.id); setForm({ ...p }); setError(''); setModal(true); };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const payload = { ...form, areaSqft: parseFloat(form.areaSqft), timeRequiredMonths: parseInt(form.timeRequiredMonths), qualityFactor: parseFloat(form.qualityFactor) };
    try {
      if (editing) {
        await API.put(`/projects/${editing}`, payload);
      } else {
        await API.post('/projects', payload);
      }
      setModal(false);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Operation failed');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this project?')) return;
    await API.delete(`/projects/${id}`);
    load();
  };

  const f = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <h2 className="page-title" style={{ margin: 0 }}>Construction Projects</h2>
        <button className="btn btn-primary" onClick={openCreate}>+ New Project</button>
      </div>
      <div className="card" style={{ padding: 0 }}>
        <table>
          <thead>
            <tr><th>#</th><th>Name</th><th>Type</th><th>Structure</th><th>City</th><th>Area (sqft)</th><th>Time (months)</th><th>Quality</th><th>By</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {projects.map((p, i) => (
              <tr key={p.id}>
                <td>{i + 1}</td>
                <td>{p.name}</td>
                <td>{p.constructionType}</td>
                <td>{p.structureType}</td>
                <td>{p.city}</td>
                <td>{p.areaSqft?.toLocaleString('en-IN')}</td>
                <td>{p.timeRequiredMonths}</td>
                <td>{p.qualityFactor}x</td>
                <td>{p.createdBy}</td>
                <td>
                  <button className="btn btn-warning" style={{ marginRight: 6 }} onClick={() => openEdit(p)}>Edit</button>
                  {isAdmin && <button className="btn btn-danger" onClick={() => handleDelete(p.id)}>Del</button>}
                </td>
              </tr>
            ))}
            {projects.length === 0 && <tr><td colSpan={10} style={{ textAlign: 'center', color: '#aaa' }}>No projects yet</td></tr>}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="modal-overlay">
          <div className="modal">
            <div className="modal-header">
              <h3>{editing ? 'Edit Project' : 'New Construction Project'}</h3>
              <button className="modal-close" onClick={() => setModal(false)}>&times;</button>
            </div>
            {error && <div className="alert alert-error">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Project Name</label>
                <input required value={form.name} onChange={f('name')} placeholder="e.g. Downtown Mall Construction" />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Construction Type</label>
                  <select required value={form.constructionType} onChange={f('constructionType')}>
                    <option value="">-- Select --</option>
                    {TYPES.constructionType.map((v) => <option key={v}>{v}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Type 1 (Business/Housing)</label>
                  <select required value={form.type1} onChange={f('type1')}>
                    <option value="">-- Select --</option>
                    {TYPES.type1.map((v) => <option key={v}>{v}</option>)}
                  </select>
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Type 2</label>
                  <select required value={form.type2} onChange={f('type2')}>
                    <option value="">-- Select --</option>
                    {TYPES.type2.map((v) => <option key={v}>{v}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Structure Type</label>
                  <select required value={form.structureType} onChange={f('structureType')}>
                    <option value="">-- Select --</option>
                    {TYPES.structureType.map((v) => <option key={v}>{v}</option>)}
                  </select>
                </div>
              </div>
              <div className="form-row-3">
                <div className="form-group">
                  <label>Area/Locality</label>
                  <input required value={form.area} onChange={f('area')} />
                </div>
                <div className="form-group">
                  <label>Street</label>
                  <input required value={form.street} onChange={f('street')} />
                </div>
                <div className="form-group">
                  <label>City</label>
                  <input required value={form.city} onChange={f('city')} />
                </div>
              </div>
              <div className="form-row-3">
                <div className="form-group">
                  <label>Area (sqft)</label>
                  <input required type="number" min="1" value={form.areaSqft} onChange={f('areaSqft')} />
                </div>
                <div className="form-group">
                  <label>Time (months)</label>
                  <input required type="number" min="1" value={form.timeRequiredMonths} onChange={f('timeRequiredMonths')} />
                </div>
                <div className="form-group">
                  <label>Quality Factor</label>
                  <select value={form.qualityFactor} onChange={f('qualityFactor')}>
                    <option value="1.0">1.0 - Standard</option>
                    <option value="1.2">1.2 - Good</option>
                    <option value="1.5">1.5 - Premium</option>
                    <option value="2.0">2.0 - Luxury</option>
                  </select>
                </div>
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
