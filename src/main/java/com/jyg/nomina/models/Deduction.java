package com.jyg.nomina.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "deductions")
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String tipoDeduction;

    @NotNull
    private Double valorDeduction;

    //------------------------------Relaciones--------------------------------
    @JsonBackReference(value="deduction-json")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentPeriod_id")
    private PaymentPeriod paymentPeriod;
}
