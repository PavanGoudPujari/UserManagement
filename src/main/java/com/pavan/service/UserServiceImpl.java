package com.pavan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pavan.dto.LoginFormDTO;
import com.pavan.dto.RegisterFormDTO;
import com.pavan.dto.ResetPwdFormDTO;
import com.pavan.dto.UserDTO;
import com.pavan.entity.CityEntity;
import com.pavan.entity.CountryEntity;
import com.pavan.entity.StateEntity;
import com.pavan.entity.UserEntity;
import com.pavan.repo.CityRepo;
import com.pavan.repo.CountryRepo;
import com.pavan.repo.StateRepo;
import com.pavan.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private CountryRepo countryRepo;
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private EmailService emailService;
	
	@Override
	public Map<Integer, String> getCountries() {
		List<CountryEntity>countriesList=countryRepo.findAll();
		
		Map<Integer, String> countryMap= new HashMap<>();
		
		countriesList.stream().forEach(c -> {countryMap.put(c.getCountryId(), c.getCountryName());});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer, String> stateMap= new HashMap<>();
		
		List<StateEntity> statesList= stateRepo.findByCountryId(countryId);
		
		statesList.stream().forEach(s -> {stateMap.put(s.getStateId(),s.getStateName());});
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		
Map<Integer, String> cityMap= new HashMap<>();
		
		List<CityEntity> citiesList= cityRepo.findByStateId(stateId);
		
		citiesList.stream().forEach(s -> {cityMap.put(s.getCityId(),s.getCityName());});
		
		return cityMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
		UserEntity byEmail=userRepo.findByEmail(email);
		return byEmail !=null;
	}

	@Override
	public boolean saveUser(RegisterFormDTO regFormDTO) {
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(regFormDTO, userEntity);
		
		CountryEntity country =countryRepo.findById(regFormDTO.getCountryId()).orElse(null);
		userEntity.setCountry(country);
		StateEntity state=stateRepo.findById(regFormDTO.getStateId()).orElse(null);
		userEntity.setState(state);
		CityEntity city =cityRepo.findById(regFormDTO.getCityId()).orElse(null);
		userEntity.setCity(city);
		
		String randomPwd = generateRandomPwd();
		userEntity.setPwd(randomPwd);
		userEntity.setPwdUpdated("No");
		UserEntity savedUser =userRepo.save(userEntity);
		
		if(null != savedUser.getUserId()) {
			
			String subject ="Your Account Created";
			String body ="Your Password to Login :"+randomPwd;
			String to = regFormDTO.getEmail();
			emailService.sendEmail(subject, body, to);
			
			return true;
		}
		return false;
	}

	@Override
	public UserDTO login(LoginFormDTO loginFormDTO) {
		
		UserEntity userEntity=userRepo.findByEmailAndPwd(loginFormDTO.getEmail(), loginFormDTO.getPwd());
		
		if(userEntity != null) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userEntity, userDTO);
			return userDTO;
		}
		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdFormDTO resetPwdFormDTO) {
		String email=resetPwdFormDTO.getEmail();
		
		UserEntity entity=userRepo.findByEmail(email);
		
		// setting new pwd
		entity.setPwd(resetPwdFormDTO.getNewpwd());
		entity.setPwdUpdated("Yes");
		
		userRepo.save(entity); //UPSERT
		
		return true;
	}
	
	private String generateRandomPwd() {
		String uppercaseLetters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowercaseLetters="abcdefghijklmnopqrstuvwxyz";
		
		String alphabets = uppercaseLetters+lowercaseLetters;
		
		Random random = new Random();
		
		StringBuffer generatedPwd = new StringBuffer();
		
		for(int i=0; i<5;i++) {
			//give any number from 0 to 51
			int randomIndex=random.nextInt(alphabets.length());
			
			generatedPwd.append(alphabets.charAt(randomIndex));
		}
		
		return generatedPwd.toString();
	}

}
