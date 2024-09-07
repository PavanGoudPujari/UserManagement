package com.pavan.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pavan.entity.CountryEntity;
import com.pavan.entity.StateEntity;

public interface CountryRepo extends JpaRepository<CountryEntity, Integer>{

}
