package com.jyg.nomina.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String documentTypeEmployee;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    @Size(min = 5, max = 20)
    private String documentNumberEmployee;

    @NotEmpty
    @Size(min = 3, max = 50, message = "El nombre debe tener maximo 50 caracteres.")
    private String employeeName;

    @Email
    private String employeeEmail;

    @Size(max = 13)
    @Pattern(regexp="[0-9]{0,13}")
    private String employeePhone;

    private String employeeAddress;

    private Boolean asset;

    private Double valueHourWorked;

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

    //------------------------------Relaciones--------------------------------
 /*   @JsonManagedReference(value="dayjob-json")
    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)//orphanRemoval sirve para eliminar registros huerfanos que no esten asociados a ningun cliente. es opcional
    private List<DayJob> dayJobs;*/

    @JsonManagedReference(value="employePayment-json")
    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)//orphanRemoval sirve para eliminar registros huerfanos que no esten asociados a ningun cliente. es opcional
    private List<PaymentPeriod> paymentPeriods;


}
