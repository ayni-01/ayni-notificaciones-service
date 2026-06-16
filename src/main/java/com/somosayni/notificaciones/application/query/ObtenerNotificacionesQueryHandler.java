package com.somosayni.notificaciones.application.query;

import com.somosayni.notificaciones.application.port.NotificacionRepository;
import com.somosayni.notificaciones.domain.model.Notificacion;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ObtenerNotificacionesQueryHandler {

    private final NotificacionRepository repository;

    public ObtenerNotificacionesQueryHandler(NotificacionRepository repository) {
        this.repository = repository;
    }

    public List<Notificacion> handle(ObtenerNotificacionesQuery query) {
        if (query.leida() != null) {
            return repository.findByDestinatarioIdAndLeida(query.destinatarioId(), query.leida());
        }
        return repository.findByDestinatarioId(query.destinatarioId());
    }
}
