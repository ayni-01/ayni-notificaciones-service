package com.somosayni.notificaciones.infrastructure.rest;

import com.somosayni.notificaciones.application.command.*;
import com.somosayni.notificaciones.application.query.*;
import com.somosayni.notificaciones.domain.model.Notificacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    private final CrearNotificacionCommandHandler crearHandler;
    private final MarcarLeidaCommandHandler marcarLeidaHandler;
    private final ObtenerNotificacionesQueryHandler obtenerHandler;

    public NotificacionController(
            CrearNotificacionCommandHandler crearHandler,
            MarcarLeidaCommandHandler marcarLeidaHandler,
            ObtenerNotificacionesQueryHandler obtenerHandler) {
        this.crearHandler = crearHandler;
        this.marcarLeidaHandler = marcarLeidaHandler;
        this.obtenerHandler = obtenerHandler;
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody CrearNotificacionCommand command) {
        Notificacion n = crearHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(n);
    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable String id) {
        marcarLeidaHandler.handle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtener(
            @RequestHeader("X-User-Id") String destinatarioId,
            @RequestParam(required = false) Boolean leida) {
        List<Notificacion> notificaciones = obtenerHandler.handle(new ObtenerNotificacionesQuery(destinatarioId, leida));
        return ResponseEntity.ok(notificaciones);
    }
}
