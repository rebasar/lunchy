package net.rebworks.lunchy.injection.factories;

import net.rebworks.lunchy.domain.date.DateCalculator;
import org.glassfish.hk2.api.Factory;

import java.time.LocalDate;

public class DateCalculatorFactory implements Factory<DateCalculator> {

    @Override
    public DateCalculator provide() {
        return new DateCalculator(LocalDate.now());
    }

    @Override
    public void dispose(final DateCalculator dateCalculator) {

    }
}
