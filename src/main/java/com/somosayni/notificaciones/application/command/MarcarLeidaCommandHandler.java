package com.somosayni.notificaciones.application.command;

import com.somosayni.notificaciones.application.port.NotificacionRepository;
import org.springframework.stereotype.Component;

@Component
public class MarcarLeidaCommandHandler {

    private final NotificacionRepository repository;

    public MarcarLeidaCommandHandler(NotificacionRepository repository) {
        this.repository = repository;
    }

    public void handle(String notificacionId) {
        repository.findById(notificacionId).ifPresent(notificacion -> {
            notificacion.marcarLeida();
            repository.save(notificacion);
        });
    }
}
