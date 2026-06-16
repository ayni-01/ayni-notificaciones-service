package com.somosayni.notificaciones.application.port;

import com.somosayni.notificaciones.domain.model.Notificacion;
import java.util.List;
import java.util.Optional;

public interface NotificacionRepository {
    Optional<Notificacion> findById(String id);
    List<Notificacion> findByDestinatarioId(String destinatarioId);
    List<Notificacion> findByDestinatarioIdAndLeida(String destinatarioId, boolean leida);
    Notificacion save(Notificacion notificacion);
    void deleteById(String id);
}
