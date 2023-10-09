package org.BotSasSE.reactController.interfaces;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

public interface AdresWorkerProjection {
    LocalDate getStart_date();
    LocalDate getEnd_date();
}
