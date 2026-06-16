package com.somosayni.notificaciones.domain.model;

import com.somosayni.shared.domain.model.AggregateRoot;
import java.time.LocalDateTime;

public class Notificacion extends AggregateRoot {

    private String destinatarioId;
    private TipoNotificacion tipo;
    private String mensaje;
    private boolean leida;
    private LocalDateTime fechaCreacion;

    public Notificacion() {}

    public Notificacion(String destinatarioId, TipoNotificacion tipo, String mensaje) {
        this.destinatarioId = destinatarioId;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.leida = false;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void marcarLeida() {
        this.leida = true;
    }

    public String getDestinatarioId() { return destinatarioId; }
    public TipoNotificacion getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public boolean isLeida() { return leida; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public enum TipoNotificacion {
        NUEVA_POSTULACION, APROBADO, RECHAZADO, RETO_CERRADO
    }
}
