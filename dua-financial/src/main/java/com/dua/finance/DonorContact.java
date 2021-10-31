package com.dua.finance;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class DonorContact {

	public DonorContact(String name, String email, String phone)
	{
		this.name = name;
		this.email = email;
		this.phone = phone;		
	}
	
	@Override
	public String toString() {
		return "DonorContact [name=" + name + ", email=" + email + ", phone=" + phone + "]";
	}

	public String getName() {
		return name;
	}

	public void setLastName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@CsvBindByName(column = "Name")
	@CsvBindByPosition(position = 0)
	public String name;
	
	@CsvBindByName(column = "Email")
	@CsvBindByPosition(position = 1)
	public String email;
	
	@CsvBindByName(column = "Phone")
	@CsvBindByPosition(position = 2)
	public String phone;
	
	public static String getHeader()
	{
		return "Name, Email,	Phone\n";
	}
}
