package com.somosayni.notificaciones.application.command;

import com.somosayni.notificaciones.application.port.NotificacionRepository;
import com.somosayni.notificaciones.domain.model.Notificacion;
import org.springframework.stereotype.Component;

@Component
public class CrearNotificacionCommandHandler {

    private final NotificacionRepository repository;

    public CrearNotificacionCommandHandler(NotificacionRepository repository) {
        this.repository = repository;
    }

    public Notificacion handle(CrearNotificacionCommand command) {
        Notificacion.TipoNotificacion tipo = Notificacion.TipoNotificacion.valueOf(command.tipo().toUpperCase());
        Notificacion notificacion = new Notificacion(command.destinatarioId(), tipo, command.mensaje());
        return repository.save(notificacion);
    }
}
