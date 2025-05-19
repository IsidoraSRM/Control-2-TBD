<template>
  <div class="login-container">
    <div class="login-card">
      <h2>{{ isLogin ? 'Iniciar Sesión' : 'Registro' }}</h2>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      
      <!-- Formulario de login -->
      <form @submit.prevent="loginUser" v-if="isLogin">
        <div class="form-group">
          <label for="email">Correo Electrónico</label>
          <input 
            type="email" 
            id="email" 
            v-model="credentials.email" 
            placeholder="Ingrese su correo" 
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Contraseña</label>
          <input 
            type="password" 
            id="password" 
            v-model="credentials.password" 
            placeholder="Ingrese su contraseña" 
            required
          />
        </div>

        <button type="submit" class="login-button" :disabled="loading">
          {{ loading ? 'Cargando...' : 'Iniciar Sesión' }}
        </button>
        
        <div class="register-link">
          ¿No tienes una cuenta? 
          <a href="#" @click.prevent="toggleForm">Regístrate</a>
        </div>
      </form>
      
      <!-- Formulario de registro -->
      <form @submit.prevent="registerUser" v-else>
        <div class="form-group">
          <label for="register-rut">RUT</label>
          <input 
            type="text" 
            id="register-rut" 
            v-model="registerData.rut" 
            placeholder="Ingrese su RUT" 
            required
          />
        </div>
        
        <div class="form-group">
          <label for="register-name">Nombre Completo</label>
          <input 
            type="text" 
            id="register-name" 
            v-model="registerData.nameParam" 
            placeholder="Ingrese su nombre completo" 
            required
          />
        </div>
        
        <div class="form-group">
          <label for="register-email">Correo Electrónico</label>
          <input 
            type="email" 
            id="register-email" 
            v-model="registerData.email" 
            placeholder="Ingrese su correo" 
            required
          />
        </div>
        
        <div class="form-group">
          <label for="register-phone">Teléfono</label>
          <input 
            type="tel" 
            id="register-phone" 
            v-model="registerData.phone" 
            placeholder="Ingrese su teléfono" 
          />
        </div>
        
        <div class="form-group">
          <label for="register-birthdate">Fecha de nacimiento</label>
          <input 
            type="date" 
            id="register-birthdate" 
            v-model="registerData.birthdate" 
          />
        </div>
        
        <div class="form-group">
          <label for="register-password">Contraseña</label>
          <input 
            type="password" 
            id="register-password" 
            v-model="registerData.password" 
            placeholder="Ingrese su contraseña" 
            required
          />
        </div>
        
        <div class="form-group">
          <label for="register-confirm">Confirmar Contraseña</label>
          <input 
            type="password" 
            id="register-confirm" 
            v-model="passwordConfirmation" 
            placeholder="Confirme su contraseña" 
            required
          />
        </div>

        <button type="submit" class="login-button" :disabled="loading">
          {{ loading ? 'Registrando...' : 'Registrarse' }}
        </button>
        
        <div class="register-link">
          ¿Ya tienes una cuenta? 
          <a href="#" @click.prevent="toggleForm">Inicia sesión</a>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginView',
  data() {
    return {
      isLogin: true,
      credentials: {
        email: '',
        password: ''
      },
      registerData: {
        rut: '',
        nameParam: '',
        email: '',
        phone: '',
        birthdate: '',
        password: ''
      },
      passwordConfirmation: '',
      loading: false,
      errorMessage: ''
    }
  },
  methods: {
    async loginUser() {
      this.loading = true;
      this.errorMessage = '';
      
      try {
        const response = await axios.post(
          process.env.VUE_APP_API_URL + 'auth/login',
          this.credentials
        );
        
        // Guardar token y datos de usuario en localStorage
        localStorage.setItem('authToken', response.data.token);
        localStorage.setItem('userId', response.data.userId);
        localStorage.setItem('userRole', response.data.role);
        
        
          // Guardar el email para usarlo como nombre si es necesario
        localStorage.setItem('userEmail', this.credentials.email);

            // Si el backend devuelve el nombre en la respuesta, guárdalo
        if (response.data.name) {
          localStorage.setItem('userName', response.data.name);
        }
    
        
        // Redireccionar según el rol
        this.redirectBasedOnRole(response.data.role);
        
      } catch (error) {
        console.error('Error de login:', error);
        this.errorMessage = error.response?.data || 'Error al iniciar sesión. Verifique sus credenciales.';
      } finally {
        this.loading = false;
      }
    },
    
    async registerUser() {
      if (this.registerData.password !== this.passwordConfirmation) {
        this.errorMessage = 'Las contraseñas no coinciden';
        return;
      }
      
      this.loading = true;
      this.errorMessage = '';
      
      try {
        // Asegurar que solo se registra como CLIENTE
        const userData = {
          ...this.registerData,
          role: 'CLIENTE' // Forzar rol CLIENTE
        };
        
        // Eliminar la asignación a 'response' si no la usas
        await axios.post(
          process.env.VUE_APP_API_URL + 'auth/register',
          userData
        );
        
        // Mostrar mensaje de éxito y cambiar a la pantalla de login
        alert('Registro exitoso. Por favor inicie sesión.');
        this.clearRegisterForm();
        this.isLogin = true;
        
      } catch (error) {
        console.error('Error de registro:', error);
        this.errorMessage = error.response?.data?.message || 
                           'Error al registrar. Verifique los datos ingresados.';
        
        if (error.response?.status === 409) {
          this.errorMessage = 'Este correo ya está registrado';
        }
      } finally {
        this.loading = false;
      }
    },
    
    redirectBasedOnRole(role) {
      if (role === 'ADMIN') {
        this.$router.push('/admin');
      } else if (role === 'TRABAJADOR') {
        this.$router.push('/trabajador');
      } else if (role === 'CLIENTE') {
        this.$router.push('/homeClient');  // Cambiado de '/' a '/homeClient'
      } else {
        // Por defecto (opcional)
        this.$router.push('/');
      }
    },
    
    toggleForm() {
      this.isLogin = !this.isLogin;
      this.errorMessage = '';
    },
    
    clearRegisterForm() {
      this.registerData = {
        rut: '',
        nameParam: '',
        email: '',
        phone: '',
        birthdate: '',
        password: ''
      };
      this.passwordConfirmation = '';
    }
  }
}
</script>

<style scoped>

/*
colores:
- Naranjo quemado (rgb(209, 118, 0))  #D17600 → Botones y CTAs
- Mostaza (rgb(245, 196, 72))   #F5C448 → Secciones destacadas
- Terracota (rgb(181, 85, 41)) #B55529  → Contrastes elegantes
- Gris cálido (rgb(226, 220, 210)) #E2DCD2  → Fondos
- Azul petróleo (rgb(18, 90, 108)) 	#125A6C  → Detalles y profundidad
*/

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.login-card {
  background: #ffffff;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(18, 90, 108, 0.13);
  width: 100%;
  max-width: 400px;
}

h2 {
  font-weight: bold;
  font-size: 30px;
  text-align: center;
  margin-bottom: 24px;
  color: #125A6C;
}

.form-group {
  margin-bottom: 20px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}


label {
  display: block;
  margin-bottom: 6px;
  font-weight: bold;
  color: #555;
  font-size: 16px;
  align-self: flex-start;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border 0.3s ease;
}

input:focus {
  border-color: #D17600;
  outline: none;
}

.login-button {
  width: 100%;
  padding: 12px;
  background: #B55529 ;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.login-button:hover {
  background: #D17600;
}

.login-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.register-link {
  margin-top: 20px;
  text-align: center;
}

.register-link a {
  color: #125A6C;
  text-decoration: underline;
}

.error-message {
  background: #fff2f2;
  color: #ff3333;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
  text-align: center;
  border: 1px solid #ffcaca;
}
</style>