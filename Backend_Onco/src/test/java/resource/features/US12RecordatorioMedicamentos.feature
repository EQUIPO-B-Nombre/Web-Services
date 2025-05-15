Feature: US12 Recordatorio de medicamentos
  Como usuario
  quiero recibir recordatorios de medicamentos
  para no olvidar tomar mi medicación.

  Scenario Outline: Recordatorio programado exitosamente
    Given el usuario registra un <medicamento> con <hora> de toma
    When llega la <hora> programada
    Then el sistema envía una notificación de recordatorio al usuario

    Examples:
      | medicamento       | hora   |
      | Paracetamol 500mg | 08:00  |
      | Tamoxifeno        | 21:00  |
