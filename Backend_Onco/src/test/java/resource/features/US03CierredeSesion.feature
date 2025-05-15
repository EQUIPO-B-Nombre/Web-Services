Feature: US03 Cierre de sesión
  Como usuario
  quiero cerrar sesión al terminar de usar la aplicación
  para proteger mi información personal.

  Scenario Outline: Cierre de sesión exitoso
    Given el usuario ha iniciado sesión
    When selecciona la opción de "Cerrar sesión"
    Then el sistema finaliza la sesión y redirige al usuario a la página de inicio

    Examples:
      | usuario          |
      | user@email.com   |
