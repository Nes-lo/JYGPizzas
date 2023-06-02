package com.jyg.nomina.services;

import com.jyg.nomina.models.Employee;
import com.jyg.nomina.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }


    public Employee findById(Long id){
        if(id!=null){
            return employeeRepository.findById(id).orElse(null);
        }
        return null;

    }

    public Employee findByIdAndState(Long id, Boolean assent){
        return employeeRepository.findByIdAndAsset(id,assent).orElse(null);
    }

    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }

}
