package com.pavan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pavan.entity.CityEntity;
import com.pavan.entity.CountryEntity;
import com.pavan.entity.StateEntity;

public interface CityRepo extends JpaRepository<CityEntity, Integer>{
	
	public List<CityEntity> findByStateId(Integer stateId);
}
