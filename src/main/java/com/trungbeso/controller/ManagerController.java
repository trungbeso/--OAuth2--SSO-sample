package com.trungbeso.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

	@GetMapping
	public String get() {
		return "Get method from manager controller";
	}

	@PostMapping
	public String post() {
		return "Post method from manager controller";
	}

	@PutMapping
	public String put() {
		return "Put method from manager controller";
	}

	@DeleteMapping
	public String delete() {
		return "Delete method from manager controller";
	}
}
