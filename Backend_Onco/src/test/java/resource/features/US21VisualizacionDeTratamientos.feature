Feature: US21 Visualización de tratamientos
  Como paciente
  quiero visualizar mis tratamientos realizados
  para conocer mi historial médico.

  Scenario Outline: Visualización exitosa de tratamientos
    Given el paciente ha iniciado sesión
    When accede a la sección de "Tratamientos"
    Then el sistema muestra la <lista de tratamientos> realizados

    Examples:
      | lista de tratamientos                     |
      | Quimioterapia, Radioterapia               |
      | Cirugía, Inmunoterapia                    |
