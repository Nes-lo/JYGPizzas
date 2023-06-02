package com.jyg.nomina.repositories;


import com.jyg.nomina.models.Employee;
import com.jyg.nomina.models.PaymentPeriod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentPeriodRepository extends CrudRepository<PaymentPeriod,Long> {

    List<PaymentPeriod> findAll();
    List<PaymentPeriod> findByEmployee(Employee employee);
    PaymentPeriod findByStatePeriod(String state);
    List<PaymentPeriod> findByPeriodName(String periodName);
    Optional<PaymentPeriod>  findByIdAndStatePeriod(Long id,String state);
    Optional<PaymentPeriod> findByEmployeeAndStatePeriod(Employee employee,String state);


}
