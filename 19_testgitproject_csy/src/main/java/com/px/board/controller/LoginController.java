package com.px.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

Logger logger=LoggerFactory.getLogger(getClass());
	
	@GetMapping(value = "/")
	public String login() {
		logger.info("LOGIN페이지이동");
		return "login";
	}
}
