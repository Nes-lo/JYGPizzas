package com.jyg.nomina.repositories;

import com.jyg.nomina.models.Deduction;
import com.jyg.nomina.models.PaymentPeriod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeductionRepository extends CrudRepository<Deduction,Long> {

    List<Deduction> findByPaymentPeriod(PaymentPeriod paymentPeriod);

}
