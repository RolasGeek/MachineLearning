package com.studies.classifiers;

public class User {
	private String name;
	private double koef;
	private Integer amount;
	
	public User(String name, double koef, Integer amount) {
		this.name = name;
		this.koef = koef;
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getKoef() {
		return koef;
	}
	public void setKoef(double koef) {
		this.koef = koef;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	
	
}
