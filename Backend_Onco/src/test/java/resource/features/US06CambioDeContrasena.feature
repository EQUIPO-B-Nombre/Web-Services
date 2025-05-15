Feature: US06 Cambio de contraseña
  Como usuario
  quiero cambiar mi contraseña
  para mantener la seguridad de mi cuenta.

  Scenario Outline: Cambio exitoso de contraseña
    Given el usuario ha iniciado sesión y está en su perfil
    When ingresa la <nueva contraseña> y la confirma correctamente
    Then el sistema actualiza la contraseña y muestra mensaje de éxito

    Examples:
      | nueva contraseña   |
      | NuevaPass2024!     |
