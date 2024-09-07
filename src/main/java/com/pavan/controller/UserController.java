package com.pavan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pavan.dto.LoginFormDTO;
import com.pavan.dto.QuoteApiResponseDTO;
import com.pavan.dto.RegisterFormDTO;
import com.pavan.dto.ResetPwdFormDTO;
import com.pavan.dto.UserDTO;
import com.pavan.service.DashboardService;
import com.pavan.service.UserService;



@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/register")
	public String loadRegisterPage(Model model) {
		
		Map<Integer, String> countriesMap = userService.getCountries();
		model.addAttribute("countries", countriesMap);
		
		
		
		RegisterFormDTO registerFormDTO = new RegisterFormDTO();
		model.addAttribute("registerForm", registerFormDTO);
		
		
		
		//returning view name
		return "register";
	}
	
	@GetMapping("/states/{countryId}")
	@ResponseBody//directly to send response to ui, instead of view page we are using Responsebody annotation
	public Map<Integer, String> getStates(@PathVariable Integer countryId,Model model) {
		
		Map<Integer, String> statesMap = userService.getStates(countryId);
		//model.addAttribute("states", statesMap);
		
		//returning view name
		return statesMap;
	}
	
	@GetMapping("/cities/{stateId}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable Integer stateId,Model model) {
		
		Map<Integer, String> citiesMap = userService.getCities(stateId);
	//	model.addAttribute("states", citiesMap);
		
		//returning view name
		return citiesMap;
	}
	
	@PostMapping("/register")
	public String handleRegistration(RegisterFormDTO registerFormDTO, Model model) {
		
		boolean status= userService.duplicateEmailCheck(registerFormDTO.getEmail());
		
		if(status) {
			model.addAttribute("emsg", "Duplicate Email Found");
		}else {
			boolean saveUser = userService.saveUser(registerFormDTO);
			if(saveUser) {
				model.addAttribute("smsg", "Registration Successfull, Please check you email..!!");
			}else {
				model.addAttribute("emsg", "Registration Failed!");
			}
		}
		
		model.addAttribute("registerForm", new RegisterFormDTO());
		model.addAttribute("countries", userService.getCountries());
		
		//returning view name
		return "register";
	}
	
	
	@GetMapping("/")
	public String index(Model model) {
		LoginFormDTO loginFormDTO = new LoginFormDTO();
		model.addAttribute("loginForm",loginFormDTO);
		return "login";
	}
	
	
	@PostMapping("/login")
	public String handleUserLogin(LoginFormDTO loginFormDTO, Model model) {
		UserDTO userDTO = userService.login(loginFormDTO);
		if(userDTO == null) {
			model.addAttribute("emsg","Invalid Credentials");
			model.addAttribute("loginForm", new LoginFormDTO());
		}else {
			
			//valid login, store counsellorid in session for future purpose like add enq, view enq
			
			//for every login, one session object created
			
			//session will available , until we logout
			String pwdUpdated=userDTO.getPwdUpdated();
			if("Yes".equals(pwdUpdated)) {
				// display dashboard
				return "redirect:/dashboard";
			}else {
				// display reset pwd page
				return "redirect:rest-pwd-page?email="+userDTO.getEmail();
			}
		}
		return "login";
		
	}
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		
		QuoteApiResponseDTO quoteApiResponseDTO = dashboardService.getQuote();
		
	    model.addAttribute("quote", quoteApiResponseDTO);
		return "dashboard";
	}
	
	@GetMapping("/rest-pwd-page")
	public  String loadRestPwdPage(@RequestParam String email,Model model) {
		
        ResetPwdFormDTO resetPwdFormDTO = new ResetPwdFormDTO();
		resetPwdFormDTO.setEmail(email);
		
		model.addAttribute("resetPwd", resetPwdFormDTO);

		return "resetPwd";
	}
	
	@PostMapping("/resetPwd")
	public  String handlePwdReset(ResetPwdFormDTO resetPwdFormDTO,Model model) {
		
        boolean resetPwd=userService.resetPwd(resetPwdFormDTO);
        
        if(resetPwd) {
        	return "redirect:dashboard";
        }

		return "resetPwd";
	}
	

}
