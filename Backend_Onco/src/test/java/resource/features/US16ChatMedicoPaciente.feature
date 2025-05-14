Feature: US16 Chat médico-paciente
  Como usuario
  quiero comunicarme por chat con mi médico o paciente
  para resolver dudas en tiempo real.

  Scenario Outline: Mensaje enviado exitosamente
    Given el usuario <usuario_emisor> está en la conversación con <usuario_receptor>
    When escribe y envía un <mensaje>
    Then el sistema entrega el mensaje a <usuario_receptor> y lo muestra en la conversación

    Examples:
      | usuario_emisor  | usuario_receptor | mensaje                   |
      | paciente1       | dr.juan         | "Tengo dudas sobre mi tratamiento" |
      | dr.juan         | paciente1       | "Recuerde tomar sus medicamentos"  |
