package com.prueba.lox.batch.mapper;

import com.prueba.lox.batch.model_1.MovimientoDTO;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PimientosMapperTest {

    @Test
    public void mapRow_whenResultSetHasData_shouldMapCorrectly() throws Exception {
        // Arrange
        PimientosMapper mapper = new PimientosMapper();
        ResultSet rs = mock(ResultSet.class);

        when(rs.getString("NOMBRE")).thenReturn("Pedro");
        when(rs.getString("NUMERO_CUENTA")).thenReturn("123456");
        when(rs.getDouble("MONTO")).thenReturn(5000.0);

        // Act
        MovimientoDTO result = mapper.mapRow(rs, 1);

        // Assert
        assertNotNull(result);
        assertEquals("Pedro", result.getNombre());
        assertEquals("123456", result.getNumeroCuenta());
        assertEquals(Double.valueOf(5000.0), result.getMonto());
    }
}
