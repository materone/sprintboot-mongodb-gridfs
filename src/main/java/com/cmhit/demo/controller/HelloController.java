package com.cmhit.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@RequestMapping("/hello")
	public String index() {
		System.out.println("Hello in request!");
		return "Hello World!";
	}
}
