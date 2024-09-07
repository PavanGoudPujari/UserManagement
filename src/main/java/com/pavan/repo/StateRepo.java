package com.pavan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pavan.entity.StateEntity;

public interface StateRepo extends JpaRepository<StateEntity, Integer> {

	
	public List<StateEntity> findByCountryId(Integer countryId);
}
