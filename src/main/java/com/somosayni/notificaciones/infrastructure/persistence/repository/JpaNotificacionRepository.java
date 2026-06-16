package com.somosayni.notificaciones.infrastructure.persistence.repository;

import com.somosayni.notificaciones.infrastructure.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaNotificacionRepository extends JpaRepository<NotificacionEntity, String> {
    List<NotificacionEntity> findByDestinatarioId(String destinatarioId);
    List<NotificacionEntity> findByDestinatarioIdAndLeida(String destinatarioId, boolean leida);
}
