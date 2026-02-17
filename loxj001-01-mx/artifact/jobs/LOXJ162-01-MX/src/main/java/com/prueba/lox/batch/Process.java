package com.prueba.lox.batch;

import com.prueba.lox.batch.Utility.Utility;
import com.prueba.lox.batch.model_1.MovimientoDTO;
import org.springframework.batch.item.ItemProcessor;
import java.util.Map;

/**
 * Processor enfocado en la orquestación de la lógica de negocio.
 */
public class Process extends Utility implements ItemProcessor<MovimientoDTO, String> {

    private Integer count = 0;

    @Override
    public String process(MovimientoDTO item) throws Exception {
        // 1. El Reader ya nos dio el 'item' con los datos de la 1er consulta (Nombre, Cuenta, Monto).

        // 2. Delegamos a la librería.
        // Dentro de 'executeCreateCreditContract', la librería llamará a 'consultarDetalleSaldo'
        // usando el número de cuenta que ya viene en el DTO.
        Map<String, Object> event = this.getMapEvent("PROCESS", item);
        this.getLoxR174().executeCreateCreditContract(event);

        // 3. Lógica de filtrado (Opcional)
        // Si después de la 2da consulta la librería decide que no es apto,
        // podrías retornar null para que el Writer no escriba esta línea.
        // if (item.getSaldoDisponible() <= 0) return null;

        count++;

        // 4. Transformamos el DTO ya enriquecido a la cadena de texto final para el TXT.
        return this.complementDataContracts(item);
    }
}