package com.training.dto;

import java.io.Serializable;

public class ErrorResponseDTO implements Serializable{

	private String error_code;
	private String error_descritpion;
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_descritpion() {
		return error_descritpion;
	}
	public void setError_descritpion(String error_descritpion) {
		this.error_descritpion = error_descritpion;
	}
	
}
