// src/scripts/auth-validation.js
import $ from 'jquery'; // Importamos JQuery

$(function() {
  // Guardamos los elementos del formulario
  const $form = $('#register-form');
  const $password = $('#password');
  const $passwordConfirm = $('#password-confirm');
  const $error = $('#password-error');

  // Función para checar si las contraseñas son iguales
  function validatePasswords() {
    if ($password.val() !== $passwordConfirm.val()) {
      $error.show(); // Muestra error
      $passwordConfirm.addClass('border-red-500'); // Borde rojo
      return false;
    } else {
      $error.hide(); // Oculta error
      $passwordConfirm.removeClass('border-red-500');
      return true;
    }
  }

  // Validamos CADA VEZ que el usuario escribe
  $password.on('keyup', validatePasswords);
  $passwordConfirm.on('keyup', validatePasswords);

  // Cuando le dan clic a "Registrarse"
  $form.on('submit', function(event) {
    event.preventDefault(); // Evitamos que la página se recargue

    if (!validatePasswords()) {
      alert("Las contraseñas no coinciden.");
      return; // Detenemos
    }

    // Si todo bien, juntamos los datos
    const formData = {
      name: $('#name').val(),
      email: $('#email').val(),
      password: $password.val(),
    };

    // Le dejamos esto listo al compañero de back-end
    console.log("Datos listos para enviar:", formData);
    alert("¡Front-end validado! Listo para el back-end.");

    // Simulación de éxito
    setTimeout(() => {
        window.location.href = '/'; // Redirigimos al inicio
    }, 1000);
  });
});