package com.somosayni.notificaciones.infrastructure.persistence.entity;

import com.somosayni.notificaciones.domain.model.Notificacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificacion")
public class NotificacionEntity {

    @Id
    private String id;

    @Column(name = "destinatario_id", nullable = false)
    private String destinatarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Notificacion.TipoNotificacion tipo;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Column(nullable = false)
    private boolean leida;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID().toString();
        fechaCreacion = LocalDateTime.now();
    }

    public Notificacion toDomain() {
        Notificacion n = new Notificacion(destinatarioId, tipo, mensaje);
        n.setId(id);
        if (leida) n.marcarLeida();
        return n;
    }

    public static NotificacionEntity fromDomain(Notificacion n) {
        NotificacionEntity e = new NotificacionEntity();
        e.id = n.getId();
        e.destinatarioId = n.getDestinatarioId();
        e.tipo = n.getTipo();
        e.mensaje = n.getMensaje();
        e.leida = n.isLeida();
        return e;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(String destinatarioId) { this.destinatarioId = destinatarioId; }
    public Notificacion.TipoNotificacion getTipo() { return tipo; }
    public void setTipo(Notificacion.TipoNotificacion tipo) { this.tipo = tipo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
