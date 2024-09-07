package com.pavan.service;

import java.util.Map;

import com.pavan.dto.LoginFormDTO;
import com.pavan.dto.RegisterFormDTO;
import com.pavan.dto.ResetPwdFormDTO;
import com.pavan.dto.UserDTO;

public interface UserService {
 
	
	public Map<Integer, String> getCountries();
	public Map<Integer, String> getStates(Integer countryId);
	public Map<Integer, String> getCities(Integer stateId);
	public boolean duplicateEmailCheck(String email);
	public boolean saveUser(RegisterFormDTO regFormDTO);
	public UserDTO login(LoginFormDTO loginFormDTO);
	public boolean resetPwd(ResetPwdFormDTO resetPwdFormDTO);
	
}
