package com.prueba.lox.batch;

import com.prueba.lox.batch.Utility.Utility;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class Failed extends Utility implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        // Usamos el servicio LOX y un mensaje limpio de copyright
        this.getLoxR174().executeCreateCreditContract(
                this.getMapEvent("KO", "Fallo la ejecucion del JOB LOX Local")
        );
        return RepeatStatus.FINISHED;
    }
}