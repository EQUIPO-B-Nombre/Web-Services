Feature: US02 Inicio de sesión
  Como usuario
  quiero iniciar sesión con mi correo y contraseña
  para acceder a las funciones de la aplicación.

  Scenario Outline: Inicio de sesión exitoso
    Given el usuario ingresa un <correo> y <contraseña> válidos
    When envía la solicitud de inicio de sesión
    Then el sistema permite el acceso y redirige al usuario a la página principal

    Examples:
      | correo         | contraseña  |
      | user@email.com | pass123     |

  Scenario Outline: Inicio de sesión fallido por credenciales inválidas
    Given el usuario ingresa un <correo> o <contraseña> inválido
    When envía la solicitud de inicio de sesión
    Then el sistema muestra un <mensaje de error>

    Examples:
      | correo         | contraseña  | mensaje de error                 |
      | user@email.com | wrongpass   | "Contraseña incorrecta"          |
      |               | pass123     | "El correo es obligatorio"       |
      | useremail.com  | pass123     | "Correo electrónico inválido"    |
