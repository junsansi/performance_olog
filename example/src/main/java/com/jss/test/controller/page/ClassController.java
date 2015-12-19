package com.jss.test.controller.page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jss.test.controller.CommonController;

@Controller
@RequestMapping("/page")
public class ClassController  extends CommonController {

	@RequestMapping("/classlist")
	public String list(HttpServletRequest request,
			Map<String, Object> mapModel){
		
		return "/page/classlist";
	}
	
	@RequestMapping("/classshow")
	public String show(HttpServletRequest request,
			Map<String, Object> mapModel){
		
		return "/page/classshow";
	}
	
}
