package com.trungbeso.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	@GetMapping
	public String get() {
		return "Get method from Admin controller";
	}

	@PostMapping
	public String post() {
		return "Post method from Admin controller";
	}

	@PutMapping
	public String put() {
		return "Put method from Admin controller";
	}

	@DeleteMapping
	public String delete() {
		return "Delete method from Admin controller";
	}
}
