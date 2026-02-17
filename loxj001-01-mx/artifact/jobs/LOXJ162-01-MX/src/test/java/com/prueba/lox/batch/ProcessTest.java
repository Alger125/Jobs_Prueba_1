package com.prueba.lox.batch;

import com.prueba.lox.batch.model_1.MovimientoDTO;
import com.prueba.lox.lib.r174.interfaz.LOXR174;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

public class ProcessTest {

    @InjectMocks
    private Process process;

    @Mock
    private LOXR174 loxR174;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Aseguramos que el contador inicie en 0 antes de cada test
        ReflectionTestUtils.setField(process, "count", 0);
    }

    @Test
    public void process_shouldInvokeLibraryAndReturnFormattedString() throws Exception {
        // 1. Arrange
        MovimientoDTO item = new MovimientoDTO();
        item.setNombre("Juan Perez");
        item.setNumeroCuenta("12345678");
        item.setMonto(500.0);

        // Como Process hereda de Utility, necesitamos que getLoxR174() no sea null
        // Si Utility inyecta por setter:
        // process.setLoxR174(loxR174);

        // 2. Act
        String result = process.process(item);

        // 3. Assert
        // Verificamos que se llamó a la librería con el evento "PROCESS"
        verify(loxR174, times(1)).executeCreateCreditContract(anyMap());

        // Verificamos que el contador subió a 1
        Integer currentCount = (Integer) ReflectionTestUtils.getField(process, "count");
        assertEquals("El contador debería haber incrementado", Integer.valueOf(1), currentCount);

        // Verificamos que devolvió algo (asumiendo que complementDataContracts funciona)
        assertNotNull("El resultado del procesamiento no debe ser null", result);
    }
}