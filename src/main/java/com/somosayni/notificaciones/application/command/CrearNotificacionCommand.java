package com.somosayni.notificaciones.application.command;

public record CrearNotificacionCommand(
        String destinatarioId,
        String tipo,
        String mensaje
) {}
