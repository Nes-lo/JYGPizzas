package com.jyg.nomina.services;

import com.jyg.nomina.models.Role;
import com.jyg.nomina.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    public RoleRepository roleRepository;

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role findByName(String name){
        Optional<Role> optional=roleRepository.findByRoleName(name);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

}
