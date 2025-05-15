Feature: US24 Gestión de pacientes
  Como médico
  quiero agregar y eliminar pacientes de mi lista
  para mantener organizada mi gestión.

  Scenario Outline: Agregar paciente exitosamente
    Given el médico ingresa el <DNI> del paciente válido
    When presiona el botón "Agregar Paciente"
    Then el sistema agrega al paciente a la lista del médico

    Examples:
      | DNI        |
      | 12345678   |
      | 87654321   |

  Scenario Outline: Eliminar paciente exitosamente
    Given el médico selecciona un <DNI> de su lista de pacientes
    When presiona el botón "Eliminar Paciente"
    Then el sistema elimina al paciente de la lista

    Examples:
      | DNI        |
      | 12345678   |
