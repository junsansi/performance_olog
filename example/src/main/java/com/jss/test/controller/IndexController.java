package com.jss.test.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController extends CommonController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request,
			Map<String, Object> mapModel){
		logger.info("index show!");
		
		return "index";
	}
	
}
