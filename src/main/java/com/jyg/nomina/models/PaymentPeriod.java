package com.jyg.nomina.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pay_periods")
public class PaymentPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finalDate;

   @NotNull
    private String periodName;

    private String statePeriod;

    private Double totalLiquidatedPeriod;


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
    @JsonManagedReference(value="paymentPeriod-json")
    @OneToMany(mappedBy = "paymentPeriod",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)//orphanRemoval sirve para eliminar registros huerfanos que no esten asociados a ningun cliente. es opcional
    private List<DayJob> dayJobs;

    @JsonBackReference(value="employePayment-json")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @JsonManagedReference(value="deduction-json")
    @OneToMany(mappedBy = "paymentPeriod",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)//orphanRemoval sirve para eliminar registros huerfanos que no esten asociados a ningun cliente. es opcional
    private List<Deduction> deductions;


}
