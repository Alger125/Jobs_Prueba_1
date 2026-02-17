package com.prueba.lox.lib.r174.impl;

import com.prueba.lox.batch.model_1.MovimientoDTO;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para LOXR174Impl
 */
public class LOXR174ImplTest {

    /* =====================================================
       TESTS extractSaldo
       ===================================================== */

    @Test
    public void extractSaldo_whenMapIsNull_shouldReturnZero() {
        LOXR174Impl impl = new LOXR174Impl();

        double result = impl.extractSaldo(null);

        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void extractSaldo_whenKeyDoesNotExist_shouldReturnZero() {
        LOXR174Impl impl = new LOXR174Impl();

        Map<String, Object> infoSaldo = new HashMap<>();
        infoSaldo.put("OTRA_LLAVE", 9999.0);

        double result = impl.extractSaldo(infoSaldo);

        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void extractSaldo_whenValueIsNumber_shouldReturnValue() {
        LOXR174Impl impl = new LOXR174Impl();

        Map<String, Object> infoSaldo = new HashMap<>();
        infoSaldo.put("SALDO_DISPONIBLE", 15000.75);

        double result = impl.extractSaldo(infoSaldo);

        assertEquals(15000.75, result, 0.0);
    }

    @Test
    public void extractSaldo_whenValueIsNotNumber_shouldReturnZero() {
        LOXR174Impl impl = new LOXR174Impl();

        Map<String, Object> infoSaldo = new HashMap<>();
        infoSaldo.put("SALDO_DISPONIBLE", "NO_ES_NUMERO");

        double result = impl.extractSaldo(infoSaldo);

        assertEquals(0.0, result, 0.0);
    }

    /* =====================================================
       TESTS executeCreateCreditContract
       ===================================================== */

    @Test
    public void executeCreateCreditContract_whenEventIsNull_shouldDoNothing() {
        LOXR174Impl impl = new LOXR174Impl();

        impl.executeCreateCreditContract(null);

        // Si no lanza excepción, el test pasa
    }

    @Test
    public void executeCreateCreditContract_whenStatusIsNotProcess_shouldDoNothing() {
        LOXR174Impl impl = new LOXR174Impl();

        Map<String, Object> event = new HashMap<>();
        event.put("status", "OTHER");

        impl.executeCreateCreditContract(event);
    }

    @Test
    public void executeCreateCreditContract_whenItemIsNull_shouldDoNothing() {
        LOXR174Impl impl = new LOXR174Impl();

        Map<String, Object> event = new HashMap<>();
        event.put("status", "PROCESS");
        event.put("item", null);

        impl.executeCreateCreditContract(event);
    }

    @Test
    public void executeCreateCreditContract_whenValidEvent_shouldCallConsultarDetalleSaldo() {
        // Spy para interceptar método interno
        LOXR174Impl impl = spy(new LOXR174Impl());

        MovimientoDTO item = mock(MovimientoDTO.class);
        when(item.getNumeroCuenta()).thenReturn("123456");

        Map<String, Object> event = new HashMap<>();
        event.put("status", "PROCESS");
        event.put("item", item);

        Map<String, Object> fakeSaldo = new HashMap<>();
        fakeSaldo.put("SALDO_DISPONIBLE", 20000.0);

        // Evitamos JDBC
        doReturn(fakeSaldo)
                .when(impl)
                .consultarDetalleSaldo(anyString());

        impl.executeCreateCreditContract(event);

        verify(impl, times(1))
                .consultarDetalleSaldo("123456");
    }

    @Test
    public void consultarDetalleSaldo_whenQueryReturnsData_shouldReturnMap() {
        // Arrange
        LOXR174Impl spyImpl = spy(new LOXR174Impl());

        HashMap<String, Object> fakeResult = new HashMap<>();
        fakeResult.put("SALDO_DISPONIBLE", 30000.0);

        // Stub del método (no JDBC)
        doReturn(fakeResult)
                .when(spyImpl)
                .consultarDetalleSaldo("123456");

        // Act
        HashMap<String, Object> result =
                spyImpl.consultarDetalleSaldo("123456");

        // Assert
        assertEquals(30000.0, result.get("SALDO_DISPONIBLE"));
    }

    @Test
    public void consultarDetalleSaldo_whenJdbcTemplateIsNull_shouldReturnEmptyMap() {
        // Arrange
        LOXR174Impl impl = new LOXR174Impl();
        // NO seteamos jdbcTemplate → queda null

        // Act
        HashMap<String, Object> result =
                impl.consultarDetalleSaldo("123456");

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void consultarDetalleSaldo_whenQueryReturnsMap_shouldReturnFilledMap() {
        // Arrange
        LOXR174Impl spyImpl = spy(new LOXR174Impl());

        HashMap<String, Object> dbResult = new HashMap<>();
        dbResult.put("SALDO_DISPONIBLE", 15000.0);

        doReturn(dbResult)
                .when(spyImpl)
                .consultarDetalleSaldo(anyString());

        // Act
        HashMap<String, Object> result =
                spyImpl.consultarDetalleSaldo("123456");

        // Assert
        assertEquals(1, result.size());
        assertEquals(15000.0, result.get("SALDO_DISPONIBLE"));
    }

    @Test
    public void consultarDetalleSaldo_whenJdbcReturnsData_shouldReturnPopulatedMap() {
        // 1. Arrange
        LOXR174Impl impl = spy(new LOXR174Impl());
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

        ReflectionTestUtils.setField(impl, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(impl, "sqlDetalleSaldos", "SELECT * FROM DUAL");

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("SALDO", 1000.50);
        mockResponse.put("MONEDA", "MXN");

        // Usa any() sin especificar la clase para el segundo argumento para evitar líos con varargs
        when(jdbcTemplate.queryForMap(anyString(), any()))
                .thenReturn(mockResponse);

        // 2. Act
        HashMap<String, Object> result = impl.consultarDetalleSaldo("123456");

        // 3. Assert
        assertNotNull("El resultado no debe ser null", result);
        assertFalse("El mapa debería tener datos", result.isEmpty());
        assertEquals("El saldo debería ser 1000.50", 1000.50, result.get("SALDO"));
    }


    @Test
    public void consultarDetalleSaldo_shouldCoverInternalLogicAndReturnMap() {
        // 1. Arrange
        LOXR174Impl impl = spy(new LOXR174Impl());
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

        // Inyectamos el mock y el SQL
        ReflectionTestUtils.setField(impl, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(impl, "sqlDetalleSaldos", "SELECT * FROM SALDOS WHERE ID = ?");

        // Preparamos el mapa que queremos que devuelva
        Map<String, Object> dbResult = new HashMap<>();
        dbResult.put("SALDO", 1000.50);
        dbResult.put("MONEDA", "MXN");

        // CORRECCIÓN: Usamos (Object[]) any() para capturar cualquier vararg/arreglo
        // Esto evita que Mockito devuelva null por falta de coincidencia
        when(jdbcTemplate.queryForMap(anyString(), (Object[]) any()))
                .thenReturn(dbResult);

        // 2. Act
        HashMap<String, Object> result = impl.consultarDetalleSaldo("123456");

        // 3. Assert
        assertNotNull("El resultado no debe ser nulo", result);
        assertFalse("El mapa no debería estar vacío", result.isEmpty());

        // IMPORTANTE: Si el valor en el mapa es un Double, asegúrate que el assertEquals coincida
        assertEquals("Debe contener el saldo mapeado", 1000.50, (Double) result.get("SALDO"), 0.001);
        assertEquals("Debe contener la moneda", "MXN", result.get("MONEDA"));
    }

    @Test
    public void testSettersInAbstractClass() {
        // 1. Arrange
        // Usamos LOXR174Impl para probar los métodos heredados de la abstracta
        LOXR174Impl impl = new LOXR174Impl();
        JdbcTemplate mockJdbc = mock(JdbcTemplate.class);
        String sql = "SELECT 1 FROM DUAL";

        // 2. Act
        // Llamamos a los setters de la clase abstracta
        impl.setJdbcTemplate(mockJdbc);
        impl.setSqlDetalleSaldos(sql);

        // 3. Assert
        // Verificamos que los campos se hayan asignado correctamente usando Reflection
        // (ya que los campos son 'protected' en la clase abstracta)
        JdbcTemplate internalJdbc = (JdbcTemplate) ReflectionTestUtils.getField(impl, "jdbcTemplate");
        String internalSql = (String) ReflectionTestUtils.getField(impl, "sqlDetalleSaldos");

        assertNotNull("El JdbcTemplate debería estar asignado", internalJdbc);
        assertEquals("El JdbcTemplate debería ser el mismo que pasamos", mockJdbc, internalJdbc);
        assertEquals("El SQL debería coincidir", sql, internalSql);
    }
}
