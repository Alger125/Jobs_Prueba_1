package com.prueba.lox.batch;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReaderTest {

    private Reader reader;

    @Before
    public void setUp() {
        reader = new Reader();
    }

    @Test
    public void read_shouldReturnListOnFirstCallAndNullOnSecondCall() throws Exception {
        // 1. Primera llamada: Debe devolver la lista simulada
        List<Map<String, Object>> firstResult = reader.read();

        assertNotNull("La primera lectura no debe ser nula", firstResult);
        assertEquals("La lista debe tener 1 contrato", 1, firstResult.size());
        assertEquals("El ID del contrato debe coincidir", "987654321", firstResult.get(0).get("CONTRATO_ID"));

        // 2. Segunda llamada: La bandera 'isRead' ya es true, debe devolver null
        List<Map<String, Object>> secondResult = reader.read();

        assertNull("La segunda lectura debe devolver null para finalizar el Step", secondResult);
    }
}