// Configuración centralizada de la API
export const API_BASE_URL = 'http://localhost:8080/api';

// Helper para construir headers con autenticación
export function getAuthHeaders(): HeadersInit {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token && { 'Authorization': `Bearer ${token}` })
  };
}

// Helper para verificar si el usuario es admin
export function isAdmin(): boolean {
  const user = localStorage.getItem('user');
  if (!user) return false;
  try {
    const userData = JSON.parse(user);
    return userData.role === 'Admin';
  } catch {
    return false;
  }
}

// Helper para obtener el usuario actual
export function getCurrentUser() {
  const user = localStorage.getItem('user');
  if (!user) return null;
  try {
    return JSON.parse(user);
  } catch {
    return null;
  }
}
