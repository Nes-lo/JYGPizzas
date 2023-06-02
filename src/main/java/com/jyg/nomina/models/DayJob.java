package com.jyg.nomina.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "day_jobs")
public class DayJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Min(0)
    @Max(12)
    @NotNull
    private Integer workingHour;

    @NotNull
    private String workingDay;

    @NotNull
    private String liquidationStatus;

    private String descriptionDayJob;

    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @PrePersist
    @DateTimeFormat(pattern="yyyy-MM-dd")
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    @DateTimeFormat(pattern="yyyy-MM-dd")
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
    //----------------------------------------Relaciones---------------------------------------------------

   /* @JsonBackReference(value="dayjob-json")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;*/

    @JsonBackReference(value="paymentPeriod-json")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_period_id")
    private PaymentPeriod paymentPeriod;
}
