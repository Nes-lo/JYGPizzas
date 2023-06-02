package com.jyg.nomina.services;


import com.jyg.nomina.models.DayJob;
import com.jyg.nomina.models.PaymentPeriod;
import com.jyg.nomina.repositories.DayJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayJobService {

    @Autowired
    DayJobRepository dayJobRepository;


    public List<DayJob> findByPaymentAndStatus(PaymentPeriod paymentPeriod, String status){
        return dayJobRepository.findByPaymentPeriodAndLiquidationStatus(paymentPeriod, status);
    }

    public List<DayJob> findByPaymentAndStatusNot(PaymentPeriod paymentPeriod, String status){
        return dayJobRepository.findByPaymentPeriodAndLiquidationStatusNot(paymentPeriod, status);
    }

    public List<DayJob> findByPayment(PaymentPeriod paymentPeriod){
        return dayJobRepository.findByPaymentPeriod(paymentPeriod);
    }

    public DayJob findDayJobPaymentPeriod(Long id, PaymentPeriod paymentPeriod){
        return dayJobRepository.findByIdAndPaymentPeriod(id,paymentPeriod).orElse(null);
    }

    public void saveDayJob(DayJob dayJob){
        dayJobRepository.save(dayJob);
    }

    public void saveAll(List<DayJob> dayJobs){
        dayJobRepository.saveAll(dayJobs);
    }



}
