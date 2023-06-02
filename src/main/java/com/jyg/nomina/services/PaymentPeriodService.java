package com.jyg.nomina.services;

import com.jyg.nomina.models.Employee;
import com.jyg.nomina.models.PaymentPeriod;
import com.jyg.nomina.repositories.PaymentPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentPeriodService {
    @Autowired
    PaymentPeriodRepository paymentPeriodRepository;

    public PaymentPeriod findStateEmployee(Employee employee, String state){

        Optional<PaymentPeriod> optionalPaymentPeriod=paymentPeriodRepository.findByEmployeeAndStatePeriod(employee, state);
        if(optionalPaymentPeriod.isPresent())
        {
            return optionalPaymentPeriod.get();
        }

        return null;

    }

    public PaymentPeriod findPaymentPeriodIdAndStatus(Long id, String status){
        return paymentPeriodRepository.findByIdAndStatePeriod(id,status).orElse(null);
    }
    public PaymentPeriod findPaymentPeriodId(Long id){
        return paymentPeriodRepository.findById(id).orElse(null);
    }
    public List<PaymentPeriod> findByEmployee(Employee employee){
        return paymentPeriodRepository.findByEmployee(employee);
    }

    public void savePayment(PaymentPeriod paymentPeriod){
        paymentPeriodRepository.save(paymentPeriod);
    }
}
