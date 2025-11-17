# API Contracts - RateMyProfs Backend

Base URL: `http://localhost:8080/api`

## Autenticación

### Endpoints Públicos
Los siguientes endpoints NO requieren autenticación:
- `POST /api/register`
- `POST /api/login`
- `POST /api/password/recovery`
- `POST /api/password/reset`
- `GET /api/reviews` (nuevo)
- `GET /api/professors`
- `GET /api/professors/{id}`
- `GET /api/ranking`
- `GET /api/subjects`
- `GET /api/universities`

### Endpoints Protegidos
Todos los demás endpoints requieren un token JWT en el header de autorización.

---

## Ejemplos de Uso con jQuery/AJAX

### Petición sin autenticación (GET)
```javascript
$.ajax({
    url: 'http://localhost:8080/api/professors',
    type: 'GET',
    data: {
        name: 'Rodriguez',
        page: 0,
        size: 10
    }
}).done(function(data) {
    // data es un objeto con paginación
    console.log('Profesores:', data.content);
}).fail(function(xhr) {
    // Manejar error
    console.error('Error:', xhr.responseJSON);
});
```

### Petición sin autenticación (POST) con body
```javascript
$.ajax({
    url: 'http://localhost:8080/api/login',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
        email: 'juan.perez@example.com',
        password: 'securePassword123'
    })
}).done(function(response) {
    // Guardar token en localStorage
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response));
    // Redirigir a página principal
    window.location.href = '/index.html';
}).fail(function(xhr) {
    // Manejar error de login
    alert('Error: ' + xhr.responseJSON.message);
});
```

### Petición con autenticación (GET)
```javascript
$.ajax({
    url: 'http://localhost:8080/api/users/1',
    type: 'GET',
    headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
}).done(function(data) {
    // data es el perfil del usuario
    console.log('Usuario:', data);
}).fail(function(xhr) {
    if (xhr.status === 401) {
        // Token inválido o expirado
        localStorage.removeItem('token');
        window.location.href = '/login.html';
    }
});
```

### Petición con autenticación (POST/PUT) con body
```javascript
$.ajax({
    url: 'http://localhost:8080/api/reviews',
    type: 'POST',
    headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
    },
    contentType: 'application/json',
    data: JSON.stringify({
        professorId: 1,
        subjectId: 1,
        rating: 5,
        comment: 'Excelente profesor!',
        isAnonymous: true,
        imageUrls: []
    })
}).done(function(data) {
    // Reseña creada exitosamente
    alert('Reseña enviada para aprobación');
    window.location.href = '/professor-detail.html?id=1';
}).fail(function(xhr) {
    // Manejar error
    console.error('Error:', xhr.responseJSON);
});
```

### Petición con autenticación (DELETE)
```javascript
$.ajax({
    url: 'http://localhost:8080/api/users/1/reviews/5',
    type: 'DELETE',
    headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
}).done(function() {
    // Reseña eliminada exitosamente
    alert('Reseña eliminada');
    location.reload();
}).fail(function(xhr) {
    // Manejar error
    alert('Error al eliminar: ' + xhr.responseJSON.message);
});
```

### Petición con query parameters (búsqueda avanzada)
```javascript
$.ajax({
    url: 'http://localhost:8080/api/professors',
    type: 'GET',
    data: {
        name: 'Rodriguez',        // string
        universityId: 1,          // number
        subjectId: 2,             // number
        page: 0,                  // number
        size: 10                  // number
    }
}).done(function(data) {
    // data.content = array de profesores
    // data.totalPages, data.totalElements, etc.
}).fail(function(xhr) {
    // Manejar error
});
```

### Validar token al cargar página
```javascript
// Ejecutar en cada página protegida
$(document).ready(function() {
    var token = localStorage.getItem('token');
    
    if (!token) {
        window.location.href = '/login.html';
        return;
    }
    
    // Verificar si el token es válido
    $.ajax({
        url: 'http://localhost:8080/api/users/me',
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).fail(function(xhr) {
        if (xhr.status === 401 || xhr.status === 403) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login.html';
        }
    });
});
```

---

## Endpoints Públicos

### POST /api/register
Registrar nuevo usuario (Estudiante).

**Request Body:**
```json
{
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "password": "securePassword123"
}
```

**Response:** `201 Created`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "role": "Estudiante"
}
```

**Redirigir a:** `/index.html` (página principal)

---

### POST /api/login
Iniciar sesión.

**Request Body:**
```json
{
  "email": "juan.perez@example.com",
  "password": "securePassword123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "role": "Estudiante"
}
```

**Redirigir a:** `/index.html` (página principal)

---

### POST /api/password/recovery
Iniciar proceso de recuperación de contraseña (genera token en BD).

**Request Body:**
```json
{
  "email": "juan.perez@example.com"
}
```

**Validaciones:**
- `email`: obligatorio, formato email válido

**Response:** `200 OK` (siempre retorna el mismo mensaje por seguridad)
```json
{
  "message": "If your email exists in our system, you will receive password recovery instructions",
  "email": "juan.perez@example.com",
  "recoveryToken": null
}
```

**Nota de seguridad:** No revela si el email existe o no. El token se guarda en BD con expiración de 1 hora y NO se retorna en la respuesta.

**Mostrar en:** `/forgot-password.html`

---

### POST /api/password/reset
Resetear contraseña con token válido.

**Request Body:**
```json
{
  "token": "uuid-token-from-email",
  "newPassword": "NewSecurePass123!"
}
```

**Validaciones:**
- `token`: obligatorio
- `newPassword`: obligatorio, mínimo 8 caracteres

**Response:** `200 OK`
```json
{
  "message": "Password successfully reset. You can now login with your new password.",
  "email": "juan.perez@example.com",
  "recoveryToken": null
}
```

**Errores:**
- `400 Bad Request`: Token inválido, expirado o ya usado

**Nota:** El token solo puede usarse una vez y expira en 1 hora.

**Mostrar en:** `/reset-password.html`

---

### GET /api/reviews
Listar reseñas con filtros opcionales (público).

**Query Parameters:**
- `professorId` (optional): `number` - Filtrar por profesor
- `subjectId` (optional): `number` - Filtrar por materia
- `status` (optional): `string` - Filtrar por estado (default: "Approved")
- `page` (optional): `number` - Número de página (default: 0)
- `size` (optional): `number` - Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "reviewId": 1,
      "userId": 1,
      "userName": "Juan Pérez",
      "professorId": 1,
      "professorName": "Dr. Carlos Rodriguez",
      "subjectId": 1,
      "subjectName": "Data Structures",
      "rating": 5,
      "comment": "Excelente profesor!",
      "imageUrls": [],
      "likeCount": 10,
      "dislikeCount": 2,
      "status": "Approved",
      "createdAt": "2025-11-17T10:00:00"
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "number": 0,
  "size": 10
}
```

**Nota:** Por defecto solo muestra reseñas aprobadas (status="Approved").

**Mostrar en:** `/professors.html`, `/professor-detail.html`

---

### GET /api/professors
Listar profesores con filtros opcionales.

**Query Parameters:**
- `name` (optional): `string` - Filtrar por nombre
- `universityId` (optional): `number` - Filtrar por universidad
- `subjectId` (optional): `number` - Filtrar por materia
- `page` (optional): `number` - Número de página (default: 0)
- `size` (optional): `number` - Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "professorId": 1,
      "name": "Dr. Carlos Rodriguez",
      "title": "Dr.",
      "departmentName": "Computer Science",
      "photoUrl": "https://example.com/photo.jpg",
      "averageRating": 4.5,
      "reviewCount": 25,
      "universities": ["FIME UANL"],
      "subjects": ["Data Structures", "Algorithms"]
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "number": 0,
  "size": 10
}
```

**Mostrar en:** `/index.html`, `/professors.html`

---

### GET /api/professors/{id}
Obtener detalle de un profesor.

**Response:** `200 OK`
```json
{
  "professorId": 1,
  "name": "Dr. Carlos Rodriguez",
  "title": "Dr.",
  "departmentName": "Computer Science",
  "photoUrl": "https://example.com/photo.jpg",
  "averageRating": 4.5,
  "reviewCount": 25,
  "universities": ["FIME UANL"],
  "subjects": ["Data Structures", "Algorithms"],
  "reviews": [
    {
      "reviewId": 1,
      "userId": null,
      "userName": "Anonymous",
      "rating": 5,
      "comment": "Excellent professor, very clear explanations",
      "isAnonymous": true,
      "status": "Approved",
      "createdAt": "2025-11-15T10:30:00",
      "totalVotes": 15,
      "likeCount": 18,
      "dislikeCount": 3
    }
  ]
}
```

**Mostrar en:** `/professor-detail.html`

---

### GET /api/ranking
Obtener ranking de profesores mejor evaluados.

**Query Parameters:**
- `limit` (optional): `number` - Número de profesores a retornar (default: 10)

**Response:** `200 OK`
```json
{
  "professors": [
    {
      "professorId": 1,
      "name": "Dr. Carlos Rodriguez",
      "title": "Dr.",
      "departmentName": "Computer Science",
      "photoUrl": "https://example.com/photo.jpg",
      "averageRating": 4.8,
      "reviewCount": 50,
      "universities": ["FIME UANL"],
      "subjects": ["Data Structures", "Algorithms"]
    }
  ]
}
```

**Mostrar en:** `/ranking.html`, `/index.html` (sección destacada)

---

## Perfil de Usuario (Autenticado)

Todos estos endpoints requieren autenticación con rol **Estudiante**.

### POST /api/reviews
Crear una nueva reseña para un profesor.

**Headers:** Requiere autenticación

**Request Body:**
```json
{
  "professorId": 1,
  "subjectId": 1,
  "rating": 5,
  "comment": "Excellent professor, very clear and organized. Highly recommended!",
  "isAnonymous": true,
  "imageUrls": [
    "https://example.com/image1.jpg"
  ]
}
```

**Response:** `201 Created`
```json
{
  "reviewId": 1,
  "userId": 1,
  "userName": "Anonymous",
  "professorId": 1,
  "professorName": "Dr. Carlos Rodriguez",
  "subjectId": 1,
  "subjectName": "Data Structures",
  "rating": 5,
  "comment": "Excellent professor, very clear and organized. Highly recommended!",
  "isAnonymous": true,
  "status": "Pending",
  "createdAt": "2025-11-17T10:30:00",
  "imageUrls": ["https://example.com/image1.jpg"],
  "totalVotes": 0,
  "likeCount": 0,
  "dislikeCount": 0
}
```

**Nota:** La reseña se crea con estado `Pending` y debe ser aprobada por un administrador.

**Mostrar en:** `/create-review.html`

---

### POST /api/reviews/{reviewId}/vote
Votar (Like/Dislike) una reseña.

**Headers:** Requiere autenticación

**Request Body:**
```json
{
  "voteType": "Like"
}
```

**Valores permitidos:** `"Like"`, `"Dislike"`

**Response:** `200 OK`
```json
{
  "message": "Vote registered successfully",
  "reviewId": 1,
  "voteType": "Like",
  "totalVotes": 1
}
```

**Error (409 Conflict) - Voto duplicado:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "You have already voted this type on this review",
  "path": "/api/reviews/1/vote"
}
```

**Nota:** El constraint `UQ_User_Review_Vote` asegura que un usuario solo puede votar una vez por tipo en cada reseña.

---

### GET /api/users/{userId}
Obtener perfil del usuario autenticado.

**Headers:** Requiere autenticación

**Response:** `200 OK`
```json
{
  "userId": 1,
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "role": "Estudiante",
  "createdAt": "2025-11-01T08:00:00",
  "reviewCount": 5
}
```

**Mostrar en:** `/profile.html`

---

### PUT /api/users/{userId}
Actualizar perfil del usuario.

**Headers:** Requiere autenticación

**Request Body:**
```json
{
  "name": "Juan Alberto Pérez",
  "email": "juan.alberto@example.com",
  "password": "newSecurePassword456"
}
```

**Response:** `200 OK`
```json
{
  "userId": 1,
  "name": "Juan Alberto Pérez",
  "email": "juan.alberto@example.com",
  "role": "Estudiante",
  "createdAt": "2025-11-01T08:00:00"
}
```

---

### GET /api/users/{userId}/reviews
Obtener reseñas del usuario autenticado.

**Headers:** Requiere autenticación

**Response:** `200 OK`
```json
{
  "reviews": [
    {
      "reviewId": 1,
      "professorId": 1,
      "professorName": "Dr. Carlos Rodriguez",
      "subjectId": 1,
      "subjectName": "Data Structures",
      "rating": 5,
      "comment": "Excellent professor",
      "isAnonymous": true,
      "status": "Approved",
      "createdAt": "2025-11-15T10:30:00",
      "totalVotes": 15,
      "likeCount": 18,
      "dislikeCount": 3
    }
  ]
}
```

**Mostrar en:** `/my-reviews.html`

---

### DELETE /api/users/{userId}/reviews/{reviewId}
Eliminar reseña propia.

**Headers:** Requiere autenticación

**Response:** `200 OK`
```json
{
  "message": "Review deleted successfully"
}
```

---

## Administración

Todos estos endpoints requieren autenticación con rol **Admin**.

### Gestión de Usuarios

#### GET /admin/users
Listar todos los usuarios.

**Headers:** Requiere autenticación (rol Admin)

**Query Parameters:**
- `page` (optional): `number` - Número de página (default: 0)
- `size` (optional): `number` - Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "userId": 1,
      "name": "Juan Pérez",
      "email": "juan.perez@example.com",
      "role": "Estudiante",
      "createdAt": "2025-11-01T08:00:00",
      "reviewCount": 5
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "number": 0,
  "size": 10
}
```

**Mostrar en:** `/admin/users.html`

---

#### POST /admin/users
Crear nuevo usuario manualmente (solo admin).

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "name": "María González",
  "email": "maria.gonzalez@example.com",
  "password": "securePass123",
  "roleName": "Estudiante"
}
```

**Validaciones:**
- `name`: obligatorio, 2-255 caracteres
- `email`: obligatorio, formato email válido, único en el sistema
- `password`: obligatorio, mínimo 8 caracteres
- `roleName`: obligatorio, "Admin" o "Estudiante"

**Response:** `201 Created`
```json
{
  "userId": 2,
  "name": "María González",
  "email": "maria.gonzalez@example.com",
  "role": "Estudiante",
  "createdAt": "2025-11-17T11:00:00"
}
```

**Errores:**
- `409 Conflict`: Email ya existe
- `400 Bad Request`: Rol inválido o validaciones fallidas

---

#### PUT /admin/users/{userId}/role
Actualizar rol de un usuario.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "roleName": "Admin"
}
```

**Response:** `200 OK`
```json
{
  "userId": 2,
  "name": "María González",
  "email": "maria.gonzalez@example.com",
  "role": "Admin",
  "createdAt": "2025-11-17T11:00:00"
}
```

---

#### PUT /admin/users/{userId}
Actualizar usuario existente.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "name": "María Isabel González",
  "email": "maria.isabel@example.com",
  "roleId": 1
}
```

**Response:** `200 OK`
```json
{
  "userId": 2,
  "name": "María Isabel González",
  "email": "maria.isabel@example.com",
  "role": "Admin",
  "createdAt": "2025-11-17T11:00:00"
}
```

---

#### DELETE /admin/users/{userId}
Eliminar usuario.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "message": "User deleted successfully"
}
```

**Nota:** Al eliminar un usuario, sus reseñas quedan con `userId` NULL (ON DELETE SET NULL).

---

### Gestión de Profesores

#### POST /admin/professors
Crear nuevo profesor.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "name": "Dr. Ana Martínez",
  "title": "Dr.",
  "departmentName": "Mathematics",
  "photoUrl": "https://example.com/photo.jpg",
  "universityIds": [1],
  "subjectIds": [1, 2]
}
```

**Response:** `201 Created`
```json
{
  "professorId": 2,
  "name": "Dr. Ana Martínez",
  "title": "Dr.",
  "departmentName": "Mathematics",
  "photoUrl": "https://example.com/photo.jpg",
  "averageRating": null,
  "reviewCount": 0,
  "universities": ["FIME UANL"],
  "subjects": ["Calculus I", "Linear Algebra"]
}
```

**Mostrar en:** `/admin/professors.html`

---

#### PUT /admin/professors/{professorId}
Actualizar profesor existente.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "name": "Dr. Ana Patricia Martínez",
  "title": "Dr.",
  "departmentName": "Applied Mathematics",
  "photoUrl": "https://example.com/new-photo.jpg",
  "universityIds": [1],
  "subjectIds": [1, 2, 3]
}
```

**Response:** `200 OK`
```json
{
  "professorId": 2,
  "name": "Dr. Ana Patricia Martínez",
  "title": "Dr.",
  "departmentName": "Applied Mathematics",
  "photoUrl": "https://example.com/new-photo.jpg",
  "averageRating": 4.5,
  "reviewCount": 10,
  "universities": ["FIME UANL"],
  "subjects": ["Calculus I", "Linear Algebra", "Differential Equations"]
}
```

---

#### DELETE /admin/professors/{professorId}
Eliminar profesor.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "message": "Professor deleted successfully"
}
```

**Error (409 Conflict) - Profesor con reseñas:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Cannot delete professor with existing reviews",
  "path": "/admin/professors/1"
}
```

**Nota:** No se puede eliminar un profesor si tiene reseñas asociadas (ON DELETE RESTRICT).

---

### Gestión de Materias

#### POST /admin/subjects
Crear nueva materia.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "name": "Machine Learning",
  "departmentName": "Computer Science"
}
```

**Response:** `201 Created`
```json
{
  "subjectId": 3,
  "name": "Machine Learning",
  "departmentName": "Computer Science"
}
```

**Mostrar en:** `/admin/subjects.html`

---

#### PUT /admin/subjects/{subjectId}
Actualizar materia existente.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "name": "Advanced Machine Learning",
  "departmentName": "Computer Science"
}
```

**Response:** `200 OK`
```json
{
  "subjectId": 3,
  "name": "Advanced Machine Learning",
  "departmentName": "Computer Science"
}
```

---

#### DELETE /admin/subjects/{subjectId}
Eliminar materia.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "message": "Subject deleted successfully"
}
```

**Nota:** No se puede eliminar una materia si tiene reseñas asociadas (ON DELETE RESTRICT).

---

### Moderación de Reseñas

#### GET /admin/reviews/moderation
Listar reseñas pendientes de moderación.

**Headers:** Requiere autenticación (rol Admin)

**Query Parameters:**
- `status` (optional): `string` - Filtrar por estado (default: "Pending")
- `page` (optional): `number` - Número de página (default: 0)
- `size` (optional): `number` - Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "reviewId": 1,
      "userId": 1,
      "userName": "Juan Pérez",
      "professorId": 1,
      "professorName": "Dr. Carlos Rodriguez",
      "subjectId": 1,
      "subjectName": "Data Structures",
      "rating": 5,
      "comment": "Excellent professor",
      "isAnonymous": false,
      "status": "Pending",
      "createdAt": "2025-11-17T10:30:00",
      "imageUrls": [],
      "totalVotes": 0,
      "likeCount": 0,
      "dislikeCount": 0
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "number": 0,
  "size": 10
}
```

**Mostrar en:** `/admin/moderation.html`

---

#### PUT /admin/reviews/{reviewId}/approve
Aprobar una reseña.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "reviewId": 1,
  "status": "Approved",
  "message": "Review approved successfully"
}
```

---

#### PUT /admin/reviews/{reviewId}/reject
Rechazar una reseña.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "reviewId": 1,
  "status": "Rejected",
  "message": "Review rejected successfully"
}
```

---

#### PUT /admin/reviews/{reviewId}
Editar el contenido de una reseña (rating y comentario). Solo para administradores.

**Headers:** Requiere autenticación (rol Admin)

**Request Body:**
```json
{
  "rating": 4,
  "comment": "Edited comment for moderation purposes"
}
```

**Validaciones:**
- `rating`: Requerido, debe ser un número entre 1 y 5
- `comment`: Requerido, no puede estar vacío, máximo 1000 caracteres

**Response:** `200 OK`
```json
{
  "reviewId": 1,
  "userId": 1,
  "userName": "Juan Pérez",
  "professorId": 1,
  "professorName": "Dr. Carlos Rodriguez",
  "subjectId": 1,
  "subjectName": "Data Structures",
  "rating": 4,
  "comment": "Edited comment for moderation purposes",
  "isAnonymous": false,
  "status": "Approved",
  "createdAt": "2025-11-17T10:30:00",
  "imageUrls": [],
  "totalVotes": 0,
  "likeCount": 0,
  "dislikeCount": 0
}
```

**Errores:**
- `404 Not Found`: Reseña no encontrada
- `400 Bad Request`: Validaciones fallidas (rating fuera de rango, comentario vacío, etc.)

**Ejemplo jQuery:**
```javascript
$.ajax({
    url: 'http://localhost:8080/api/admin/reviews/1',
    type: 'PUT',
    headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
    },
    contentType: 'application/json',
    data: JSON.stringify({
        rating: 4,
        comment: 'Comentario editado por moderación'
    })
}).done(function(data) {
    console.log('Reseña actualizada:', data);
}).fail(function(xhr) {
    if (xhr.status === 404) {
        alert('Reseña no encontrada');
    } else if (xhr.status === 400) {
        alert('Error de validación: ' + xhr.responseJSON.message);
    }
});
```

---

#### DELETE /admin/reviews/{reviewId}
Eliminar una reseña.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "message": "Review deleted successfully"
}
```

---

### Dashboard de Analíticas

#### GET /admin/analytics
Obtener métricas y estadísticas de la plataforma.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
{
  "activeUsers": 150,
  "totalReviews": 500,
  "approvedReviews": 450,
  "pendingReviews": 30,
  "rejectedReviews": 20,
  "totalProfessors": 50,
  "topRatedProfessor": {
    "professorId": 1,
    "name": "Dr. Carlos Rodriguez",
    "averageRating": 4.8,
    "reviewCount": 50
  },
  "recentActivity": [
    {
      "type": "NEW_REVIEW",
      "timestamp": "2025-11-17T10:30:00",
      "description": "New review submitted for Dr. Carlos Rodriguez"
    },
    {
      "type": "NEW_USER",
      "timestamp": "2025-11-17T09:15:00",
      "description": "New user registered: Juan Pérez"
    }
  ]
}
```

**Mostrar en:** `/admin/dashboard.html`

---

## Códigos de Error

| Código | Descripción |
|--------|-------------|
| 400 | Bad Request - Datos inválidos o parámetros faltantes |
| 401 | Unauthorized - Token inválido, ausente o expirado |
| 403 | Forbidden - Sin permisos suficientes para el recurso |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Conflicto con el estado actual (email duplicado, voto duplicado) |
| 422 | Validation Error - Error de validación en los datos |
| 500 | Internal Server Error - Error interno del servidor |

**Formato de Error:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": "number",
  "error": "string",
  "message": "string",
  "path": "string"
}
```

### Ejemplos de Errores

**400 Bad Request:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request parameters",
  "path": "/api/reviews"
}
```

**401 Unauthorized:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password",
  "path": "/api/login"
}
```

**403 Forbidden:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "You don't have permission to access this resource",
  "path": "/admin/users"
}
```

**404 Not Found:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Professor not found with id: '999'",
  "path": "/api/professors/999"
}
```

**409 Conflict - Email duplicado:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists",
  "path": "/api/register"
}
```

**409 Conflict - Voto duplicado:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "You have already voted this type on this review",
  "path": "/api/reviews/1/vote"
}
```

**422 Validation Error:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "email": "Email should be valid",
    "password": "Password must be between 6 and 100 characters"
  },
  "path": "/api/register"
}
```

---

## Notas Importantes

1. **Autenticación y Tokens:**
   - Los tokens JWT expiran después de 24 horas
   - Almacenar el token en `localStorage` después de login/registro
   - Incluir en cada petición protegida: `Authorization: Bearer <token>`
   - Si el token expira (401), redirigir al usuario a `/login.html`

2. **Estados de Reseñas:**
   - `Pending`: Reseña recién creada, esperando aprobación
   - `Approved`: Reseña aprobada, visible para todos los usuarios
   - `Rejected`: Reseña rechazada, no visible públicamente

3. **Sistema de Votos:**
   - Constraint `UQ_User_Review_Vote` permite un voto por tipo por usuario
   - Un usuario puede dar Like Y Dislike a la misma reseña (votos diferentes)
   - No se puede votar dos veces el mismo tipo en la misma reseña

4. **Reseñas Anónimas:**
   - Cuando `isAnonymous` es `true`, el campo `userName` muestra "Anonymous"
   - El `userId` se mantiene internamente para auditoría
   - Solo el usuario y los administradores pueden ver el autor real

5. **Paginación:**
   - Parámetros: `page` (default: 0) y `size` (default: 10)
   - Respuesta incluye: `content`, `totalPages`, `totalElements`, `number`, `size`
   - Usar en todas las listas largas (profesores, usuarios, reseñas)

6. **Restricciones de Eliminación:**
   - **Profesores**: No se pueden eliminar si tienen reseñas (ON DELETE RESTRICT)
   - **Materias**: No se pueden eliminar si tienen reseñas (ON DELETE RESTRICT)
   - **Usuarios**: Al eliminar, sus reseñas quedan con userId NULL (ON DELETE SET NULL)
   - **Reseñas**: Al eliminar, se eliminan sus votos automáticamente (ON DELETE CASCADE)

7. **Fechas:**
   - Todas las fechas están en formato ISO 8601 (UTC)
   - Ejemplo: `"2025-11-17T10:30:00"`
   - Convertir en frontend según la zona horaria del usuario

8. **Roles y Permisos:**
   - **Estudiante** (roleId: 2): Puede crear reseñas, votar, editar su perfil
   - **Admin** (roleId: 1): Acceso completo a gestión y moderación
   - Un usuario solo puede modificar sus propios recursos (excepto Admin)

9. **Validaciones:**
   - Email: Formato válido requerido
   - Password: Mínimo 6 caracteres
   - Rating: Valores entre 1 y 5
   - Campos requeridos: No pueden ser nulos o vacíos

10. **CORS:**
    - El backend está configurado para aceptar peticiones desde:
      - `http://localhost:3000`
      - `http://127.0.0.1:5500`
      - `http://localhost:5500`
    - Modificar en `SecurityConfig.java` si es necesario

11. **Tipos de Datos:**
    - `"string"` = texto
    - `"number"` = número (entero o decimal)
    - `"boolean"` = true/false
    - `"array"` = arreglo []
    - `"string (ISO 8601)"` = fecha en formato texto ISO 8601

---

## Ejemplos de Flujo Completo

### Flujo de Registro e Inicio de Sesión
```javascript
// 1. Registro
$.ajax({
    url: 'http://localhost:8080/api/register',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
        name: 'Juan Pérez',
        email: 'juan@example.com',
        password: 'pass123'
    })
}).done(function(response) {
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response));
    window.location.href = '/index.html';
});

// 2. Verificar autenticación en cada página
$(document).ready(function() {
    var token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login.html';
    }
});

// 3. Logout
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login.html';
}
```

### Flujo de Creación de Reseña
```javascript
// 1. Obtener datos del profesor
$.ajax({
    url: 'http://localhost:8080/api/professors/1',
    type: 'GET'
}).done(function(professor) {
    // Mostrar información del profesor
    $('#professorName').text(professor.name);
    
    // Llenar select de materias
    professor.subjects.forEach(function(subject) {
        $('#subjectSelect').append(
            $('<option>').val(subject.id).text(subject.name)
        );
    });
});

// 2. Enviar reseña
$('#reviewForm').submit(function(e) {
    e.preventDefault();
    
    $.ajax({
        url: 'http://localhost:8080/api/reviews',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        contentType: 'application/json',
        data: JSON.stringify({
            professorId: parseInt($('#professorId').val()),
            subjectId: parseInt($('#subjectSelect').val()),
            rating: parseInt($('#rating').val()),
            comment: $('#comment').val(),
            isAnonymous: $('#anonymous').is(':checked'),
            imageUrls: []
        })
    }).done(function() {
        alert('Reseña enviada para aprobación');
        window.location.href = '/my-reviews.html';
    });
});
```

### Flujo de Votación
```javascript
// Votar Like
$('.btn-like').click(function() {
    var reviewId = $(this).data('review-id');
    
    $.ajax({
        url: 'http://localhost:8080/api/reviews/' + reviewId + '/vote',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        contentType: 'application/json',
        data: JSON.stringify({ voteType: 'Like' })
    }).done(function(response) {
        // Actualizar contador
        $('#likes-' + reviewId).text(response.totalVotes);
        alert('Voto registrado');
    }).fail(function(xhr) {
        if (xhr.status === 409) {
            alert('Ya votaste en esta reseña');
        } else if (xhr.status === 401) {
            alert('Debes iniciar sesión para votar');
            window.location.href = '/login.html';
        }
    });
});
```

---

## Endpoints de Desarrollo (TEMPORAL)

### GET /api/admin/recovery-tokens
Lista todos los tokens de recuperación (solo para desarrollo/testing).

**ADVERTENCIA:** Este endpoint es TEMPORAL y solo para desarrollo. **DEBE SER ELIMINADO EN PRODUCCIÓN** por seguridad.

**Headers:** Requiere autenticación (rol Admin)

**Response:** `200 OK`
```json
[
  {
    "tokenId": 1,
    "token": "uuid-token-string",
    "userId": 1,
    "userEmail": "user@example.com",
    "userName": "Juan Pérez",
    "expiresAt": "2025-11-17T18:00:00",
    "used": false,
    "expired": false,
    "valid": true,
    "createdAt": "2025-11-17T17:00:00"
  }
]
```

**Nota:** Este endpoint existe para facilitar el testing sin servicio de email. En producción, los tokens se enviarían por email y este endpoint no existiría.

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0.0  
**Stack:** Java 21 + Spring Boot 3.4 + PostgreSQL 16 + JWT

**¡Sistema RateMyProfs API listo para integración!**
