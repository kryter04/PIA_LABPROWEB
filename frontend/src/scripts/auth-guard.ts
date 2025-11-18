// Script para proteger rutas de administrador
import { isAdmin } from '../config/api.ts';

export function requireAuth() {
  const token = localStorage.getItem('token');
  if (!token) {
    window.location.href = '/login';
    return false;
  }
  return true;
}

export function requireAdmin() {
  if (!requireAuth()) return false;
  
  if (!isAdmin()) {
    alert('No tienes permisos para acceder a esta página');
    window.location.href = '/';
    return false;
  }
  return true;
}
