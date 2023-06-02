package com.jyg.nomina.repositories;

import com.jyg.nomina.models.DayJob;
import com.jyg.nomina.models.PaymentPeriod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DayJobRepository extends CrudRepository<DayJob,Long> {

    List<DayJob> findByPaymentPeriod(PaymentPeriod paymentPeriod);
    List<DayJob> findByPaymentPeriodAndLiquidationStatus(PaymentPeriod  paymentPeriod, String status);
    List<DayJob> findByPaymentPeriodAndLiquidationStatusNot(PaymentPeriod  paymentPeriod, String status);

    Optional<DayJob> findByIdAndPaymentPeriod(Long id, PaymentPeriod paymentPeriod);

}
