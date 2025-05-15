Feature: US01 Registro de usuario
  Como usuario
  quiero registrarme con correo, contraseña y datos de perfil
  para crear una cuenta y acceder a la aplicación OnContigo.

  Scenario Outline: Registro exitoso de usuario
    Given el usuario ingresa un <correo>, <contraseña> y <datos de perfil> válidos
    When envía la solicitud de registro
    Then el sistema crea una nueva cuenta y muestra un mensaje de bienvenida
    And el usuario es redirigido a la página principal

    Examples:
      | correo           | contraseña | datos de perfil                   |
      | user@email.com   | Pass1234!  | nombre: Juan, apellido: Pérez    |
      | maria@gmail.com  | Pass5678!  | nombre: María, apellido: López   |

  Scenario Outline: Registro fallido por datos inválidos
    Given el usuario ingresa un <correo> o <contraseña> inválidos
    When envía la solicitud de registro
    Then el sistema muestra un mensaje de error <mensaje de error>

    Examples:
      | correo          | contraseña | mensaje de error                     |
      | useremail.com   | Pass1234!  | "Correo electrónico inválido"       |
      | user@email.com  |            | "La contraseña es obligatoria"       |
      |                | Pass1234!  | "El correo es obligatorio"           |
