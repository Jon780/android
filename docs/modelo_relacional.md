# AulaNota – Modelo relacional

Relaciones (1 : N):
usuario → materia → horario_clase → asistencia
materia → apunte
materia → mazo → tarjeta

## Tablas

**usuario** (**id_usuario** PK, nombre, correo UNIQUE, contrasena_hash, fecha_registro)

**materia** (**id_materia** PK, *id_usuario* FK→usuario, nombre, profesor, aula, color)

**horario_clase** (**id_horario** PK, *id_materia* FK→materia, dia_semana, hora_inicio, hora_fin)

**asistencia** (**id_asistencia** PK, *id_horario* FK→horario_clase, fecha, estado) — UNIQUE(id_horario, fecha); estado ∈ {asistio, falta, retardo}

**apunte** (**id_apunte** PK, *id_materia* FK→materia, titulo, contenido, fecha_creacion, fecha_modificacion)

**mazo** (**id_mazo** PK, *id_materia* FK→materia, titulo)

**tarjeta** (**id_tarjeta** PK, *id_mazo* FK→mazo, pregunta, respuesta, dominada)

Script completo: `modelo_relacional.sql`. Diagrama: `diagrama_relacional.png`.
