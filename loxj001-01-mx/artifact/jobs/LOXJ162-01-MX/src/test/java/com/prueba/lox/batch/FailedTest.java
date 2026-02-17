package com.prueba.lox.batch;

import com.prueba.lox.lib.r174.interfaz.LOXR174;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.repeat.RepeatStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

public class FailedTest {

    @InjectMocks
    private Failed failed;

    @Mock
    private LOXR174 loxR174;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute_shouldCallServiceWithErrorMessage() throws Exception {
        // Act
        RepeatStatus status = failed.execute(null, null);

        // Assert
        assertEquals(RepeatStatus.FINISHED, status);

        // Verificamos que se llamó al servicio con el mapa de error "KO"
        verify(loxR174, times(1)).executeCreateCreditContract(anyMap());
    }
}