package com.pavan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pavan.service.DashboardService;

@Controller
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;
}
