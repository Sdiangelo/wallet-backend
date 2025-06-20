# Wallet Bancaria - Proyecto Spring Boot + JWT

Aplicación backend de ejemplo para gestión de usuarios, cuentas y transacciones bancarias, con seguridad JWT y roles (USER y ADMIN).  
Ideal para portfolio de desarrollador backend.

---  
## 🚀 Tecnologías utilizadas  
- Java 17+  
- Spring Boot  
- Spring Security + JWT  
- Spring Data JPA (Hibernate)  
- MySQL  
- Maven  

---  
## ⚙️ Configuración  
1. Clona el repositorio y configura la base de datos  
   Crea una base de datos en MySQL, por ejemplo `wallet_db`.  
2. Configura el archivo `application.properties` con los datos de conexión a tu base de datos.  

---  
## 👤 Usuarios de prueba  
- **Usuario normal**  
  - Usuario: `juanp`  
  - Email: `juan@mail.com`  
  - Contraseña: `1234`  
- **Usuario admin** (modifica estos datos en la base de datos)  
  - Usuario: `admin`  
  - Email: `admin@mail.com`  
  - Contraseña: `admin123`  
  - Rol: `ADMIN`  

> **Nota:** Para crear el usuario admin, regístralo normalmente y luego cambia el campo rol a ADMIN en la tabla usuarios de MySQL:  
> ```sql
> UPDATE usuarios SET rol = 'ADMIN' WHERE username = 'admin';
> ```  

---  
## 🛠️ Endpoints principales  
### Autenticación  
- `POST /api/auth/register` — Registro de usuario  
- `POST /api/auth/login` — Login (devuelve JWT)  

### Usuario autenticado  
- `GET /api/users/me` — Datos del usuario autenticado  

### Cuenta  
- `GET /api/accounts/me` — Datos de la cuenta  
- `GET /api/accounts/me/saldo` — Saldo de la cuenta  

### Transacciones  
- `POST /api/transactions/transfer` — Transferir dinero  
- `GET /api/transactions/history` — Historial de transacciones  

### Administración (solo ADMIN)  
- `GET /admin/users` — Ver todos los usuarios  
- `GET /admin/transactions` — Ver todas las transacciones  

---  
## 🔑 Autenticación  
Para acceder a los endpoints protegidos, debes enviar el token JWT en la cabecera Authorization con el formato:  
Authorization: Bearer <token>  

---  
## 📦 Prueba rápida con Postman  
1. **Registra un usuario:**  
   `POST /api/auth/register`  
   Body (JSON):  
   {  
     "username": "juanp",  
     "email": "juan@mail.com",  
     "password": "1234"  
   }  

2. **Haz login:**  
   `POST /api/auth/login`  
   Body (JSON):  
   {  
     "username": "juanp",  
     "password": "1234"  
   }  
   Copia el token JWT de la respuesta.  

3. **Consulta tus datos:**  
   `GET /api/users/me`  
   Header:  
   Authorization: Bearer <token>  

4. **Realiza transferencias, consulta saldo, historial, etc.** usando los endpoints disponibles y enviando siempre el token en la cabecera.  

---
