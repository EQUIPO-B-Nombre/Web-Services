Feature: US04 Recuperación de cuenta
  Como usuario
  quiero recuperar mi cuenta si olvido mi contraseña
  para no perder mi acceso a la aplicación.

  Scenario Outline: Recuperación de cuenta exitosa
    Given el usuario solicita la recuperación de cuenta con su <correo>
    When el sistema valida que el correo está registrado
    Then envía un <método de recuperación> al usuario

    Examples:
      | correo         | método de recuperación         |
      | user@email.com | "Correo electrónico con enlace de recuperación" |
      | user2@email.com| "SMS con código de verificación"                |
