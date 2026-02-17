package com.prueba.lox.batch.Utility;

import com.prueba.lox.batch.model_1.MovimientoDTO;
import com.prueba.lox.lib.r174.interfaz.LOXR174;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase de soporte que centraliza herramientas comunes para el Batch.
 * Gestiona la inyección de la librería de negocio, la fecha de proceso (ODATE)
 * y el formateo de datos para el archivo de salida.
 */
public class Utility {

    // Variable que almacena la fecha de operación (formato YYYYMMDD) recibida desde el Job
    private String odate;

    // Interfaz de la librería de negocio. Spring la inyecta para ejecutar la lógica en el Process
    private LOXR174 loxR174;

    // --- SECCIÓN: MÉTODOS DE ACCESO (ENCAPSULAMIENTO) ---

    public String getOdate() { return odate; }
    public void setOdate(String odate) { this.odate = odate; }

    public LOXR174 getLoxR174() { return loxR174; }
    public void setLoxR174(LOXR174 loxR174) { this.loxR174 = loxR174; }


    // --- SECCIÓN: FORMATEO PARA ARCHIVO DE SALIDA ---

    /**
     * SOBRECARGA 1: Formatea un objeto individual (DTO).
     * Se usa en el flujo actual donde el Reader lee fila por fila.
     * @param item Objeto con los datos de la consulta Oracle.
     * @return Línea de texto formateada para el archivo TXT.
     */
    public String complementDataContracts(MovimientoDTO item) {
        // Validación de seguridad para evitar NullPointerException
        if (item == null) return "";

        // Retorna una línea con anchos fijos o delimitadores:
        // %-20s = String de 20 caracteres alineado a la izquierda
        // %10.2f = Decimal de 10 posiciones con 2 decimales
        return String.format("NOMBRE: %-20s | CUENTA: %-15s | MONTO: %10.2f | FECHA: %s",
                item.getNombre(),
                item.getNumeroCuenta(),
                item.getMonto(),
                this.odate);
    }

    /**
     * SOBRECARGA 2: Mantenida por compatibilidad si otros procesos envían listas.
     * @param listcontract Lista de mapas con datos genéricos.
     * @return String con múltiples líneas (una por cada contrato).
     */
    public String complementDataContracts(List<Map<String, Object>> listcontract) {
        if (listcontract == null || listcontract.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> contract : listcontract) {
            // Extrae el ID usando la llave del mapa o pone N/A si no existe
            sb.append("CONTRATO: ").append(contract.getOrDefault("CONTRATO_ID", "N/A"))
                    .append(" | FECHA: ").append(this.odate)
                    .append("\n"); // Salto de línea para el archivo
        }
        return sb.toString();
    }


    // --- SECCIÓN: GESTIÓN DE EVENTOS (COMUNICACIÓN CON LIBRERÍA) ---

    /**
     * Crea un mapa de control para notificar estatus globales (Éxito/Error).
     * @param status Ejemplo: "OK", "ERROR", "FINISHED"
     * @param message Descripción detallada del suceso.
     * @return Mapa estructurado para la interfaz LOXR174.
     */
    public Map<String, Object> getMapEvent(String status, String message) {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("STATUS", status);   // Estado de la operación
        mapEvent.put("MESSAGE", message); // Mensaje informativo
        mapEvent.put("ODATE", this.odate);// Fecha de la ejecución
        return mapEvent;
    }

    /**
     * SOBRECARGA: Envía el objeto DTO completo a la librería.
     * Es CRUCIAL para que LOXR174Impl pueda extraer el número de cuenta
     * y realizar la segunda consulta dependiente.
     * @param status Ejemplo: "PROCESS"
     * @param item El objeto MovimientoDTO con toda la info de la fila.
     */
    public Map<String, Object> getMapEvent(String status, Object item) {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("STATUS", status);
        mapEvent.put("item", item);       // Aquí viaja la "llave" para la 2da consulta
        mapEvent.put("ODATE", this.odate);
        return mapEvent;
    }


    // --- SECCIÓN: OTROS ---

    /**
     * Convierte cualquier objeto a String de forma segura.
     */
    public String obtenerFormatoSalida(Object data) {
        return (data != null) ? data.toString() : "";
    }
}