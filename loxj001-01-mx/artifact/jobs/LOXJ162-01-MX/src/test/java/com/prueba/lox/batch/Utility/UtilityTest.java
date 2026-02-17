package com.prueba.lox.batch.Utility;

import com.prueba.lox.batch.model_1.MovimientoDTO;
import com.prueba.lox.lib.r174.interfaz.LOXR174;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class UtilityTest {

    private Utility utility;
    private final String ODATE_TEST = "20260212";

    @Before
    public void setUp() {
        utility = new Utility();
        utility.setOdate(ODATE_TEST);
    }

    @Test
    public void testGettersAndSetters() {
        // Probamos LOXR174
        LOXR174 mockLox = mock(LOXR174.class);
        utility.setLoxR174(mockLox);
        assertEquals(mockLox, utility.getLoxR174());

        // Probamos Odate
        utility.setOdate("20250101");
        assertEquals("20250101", utility.getOdate());
    }

    @Test
    public void testComplementDataContracts_WithDTO() {
        // Caso normal
        MovimientoDTO dto = new MovimientoDTO();
        dto.setNombre("JUAN PEREZ");
        dto.setNumeroCuenta("12345");
        dto.setMonto(100.50);

        String result = utility.complementDataContracts(dto);
        assertTrue(result.contains("JUAN PEREZ"));
        assertTrue(result.contains("12345"));
        assertTrue(result.contains(ODATE_TEST));

        // Caso null (cobertura de la validación de seguridad)
        assertEquals("", utility.complementDataContracts((MovimientoDTO) null));
    }

    @Test
    public void testComplementDataContracts_WithList() {
        // Caso con lista poblada
        List<Map<String, Object>> lista = new ArrayList<>();
        Map<String, Object> contrato = new HashMap<>();
        contrato.put("CONTRATO_ID", "999");
        lista.add(contrato);

        String result = utility.complementDataContracts(lista);
        assertTrue(result.contains("CONTRATO: 999"));
        assertTrue(result.contains(ODATE_TEST));

        // Caso contrato sin ID (cobertura del getOrDefault)
        lista.clear();
        lista.add(new HashMap<>());
        assertTrue(utility.complementDataContracts(lista).contains("N/A"));

        // Casos vacíos o nulos
        assertEquals("", utility.complementDataContracts((List<Map<String, Object>>) null));
        assertEquals("", utility.complementDataContracts(new ArrayList<Map<String, Object>>()));
    }

    @Test
    public void testGetMapEvent_StatusAndMessage() {
        Map<String, Object> event = utility.getMapEvent("OK", "Mensaje de prueba");

        assertEquals("OK", event.get("STATUS"));
        assertEquals("Mensaje de prueba", event.get("MESSAGE"));
        assertEquals(ODATE_TEST, event.get("ODATE"));
    }

    @Test
    public void testGetMapEvent_StatusAndObject() {
        Object item = new Object();
        Map<String, Object> event = utility.getMapEvent("PROCESS", item);

        assertEquals("PROCESS", event.get("STATUS"));
        assertEquals(item, event.get("item"));
        assertEquals(ODATE_TEST, event.get("ODATE"));
    }

    @Test
    public void testObtenerFormatoSalida() {
        // Caso con dato
        assertEquals("Hola", utility.obtenerFormatoSalida("Hola"));
        // Caso null
        assertEquals("", utility.obtenerFormatoSalida(null));
    }
}