package com.somosayni.notificaciones.infrastructure.persistence.mapper;

import com.somosayni.notificaciones.application.port.NotificacionRepository;
import com.somosayni.notificaciones.domain.model.Notificacion;
import com.somosayni.notificaciones.infrastructure.persistence.entity.NotificacionEntity;
import com.somosayni.notificaciones.infrastructure.persistence.repository.JpaNotificacionRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class NotificacionRepositoryImpl implements NotificacionRepository {

    private final JpaNotificacionRepository jpaRepository;

    public NotificacionRepositoryImpl(JpaNotificacionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notificacion save(Notificacion n) {
        return jpaRepository.save(NotificacionEntity.fromDomain(n)).toDomain();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Notificacion> findById(String id) {
        return jpaRepository.findById(id).map(NotificacionEntity::toDomain);
    }

    @Override
    public List<Notificacion> findByDestinatarioId(String destinatarioId) {
        return jpaRepository.findByDestinatarioId(destinatarioId).stream().map(NotificacionEntity::toDomain).toList();
    }

    @Override
    public List<Notificacion> findByDestinatarioIdAndLeida(String destinatarioId, boolean leida) {
        return jpaRepository.findByDestinatarioIdAndLeida(destinatarioId, leida).stream().map(NotificacionEntity::toDomain).toList();
    }
}
