Feature: US09 Registrar fecha de cita médica
  Como usuario
  quiero registrar la fecha y hora de una cita médica
  para mantener un orden y registro de mis citas.

  Scenario Outline: Registro exitoso de cita médica
    Given el usuario ingresa una <fecha> y <hora> válidas para la cita
    When envía la solicitud de registro de cita
    Then el sistema agrega la cita al calendario y muestra confirmación

    Examples:
      | fecha       | hora   |
      | 2025-06-15  | 10:00  |
      | 2025-06-20  | 15:30  |

  Scenario Outline: Registro fallido por datos inválidos
    Given el usuario ingresa una <fecha> o <hora> inválida para la cita
    When envía la solicitud de registro de cita
    Then el sistema muestra un <mensaje de error>

    Examples:
      | fecha       | hora   | mensaje de error                 |
      |             | 10:00  | "La fecha es obligatoria"        |
      | 2025-06-15  |        | "La hora es obligatoria"         |
      | 2025-02-30  | 09:00  | "Fecha no válida"                |
