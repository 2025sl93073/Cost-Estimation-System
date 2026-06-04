import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import Layout from './components/Layout';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import MaterialsPage from './pages/MaterialsPage';
import MaterialCostPage from './pages/MaterialCostPage';
import ProjectsPage from './pages/ProjectsPage';
import EstimatePage from './pages/EstimatePage';
import ReportsPage from './pages/ReportsPage';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route element={<PrivateRoute />}>
            <Route element={<Layout />}>
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="/materials" element={<MaterialsPage />} />
              <Route path="/material-costs" element={<MaterialCostPage />} />
              <Route path="/projects" element={<ProjectsPage />} />
              <Route path="/estimate" element={<EstimatePage />} />
              <Route path="/reports" element={<ReportsPage />} />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
