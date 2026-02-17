package com.prueba.lox.batch;

import org.springframework.batch.item.ItemReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Reader implements ItemReader<List<Map<String, Object>>> {

    private boolean isRead = false; // Bandera para que solo lea una vez

    @Override
    public List<Map<String, Object>> read() throws Exception {
        // Si ya leímos los datos, devolvemos null para que el Job termine
        if (isRead) {
            return null;
        }

        // --- SIMULACIÓN DE DATOS (Aquí evitamos el NullPointerException) ---
        List<Map<String, Object>> listaContratos = new ArrayList<>();

        Map<String, Object> contrato1 = new HashMap<>();
        contrato1.put("CONTRATO_ID", "987654321");
        contrato1.put("ESTADO", "ACTIVO");
        contrato1.put("IMPORTE", "1500.50");

        listaContratos.add(contrato1);

        isRead = true; // Marcamos que ya enviamos la lista
        System.out.println(">>> READER: Enviando lista con " + listaContratos.size() + " contrato(s)");

        return listaContratos;
    }
}