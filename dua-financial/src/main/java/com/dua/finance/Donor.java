package com.dua.finance;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class Donor {

	public Donor()
	{
		
	}
	
	public Donor(String fullName, String firstName, String lastName, String email, double donationAmount, 
			String phone, String address1, String address2, String city, String state, String zip)
	{
		this.fullName = fullName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.donationAmount = donationAmount;
		this.phone = phone;
		this.address1 = Utility.getCamelCase(address1);
		this.address2 = Utility.getCamelCase(address2);
		this.city = Utility.getCamelCase(city);
		this.state = state;
		this.zip = zip;		
		
		if(city == null && address1 !=null)
		{
			String[] temp = address1.split(",");
			if(temp.length > 0)
				this.address1 = temp[0].trim();
			if(temp.length > 1)
				this.city = Utility.getCamelCase(temp[1].trim());
			if(temp.length > 2)			
				this.state = temp[2].trim();
			if(temp.length > 3 & this.zip == null)
				this.zip = temp[3].trim();
		}
		if(this.state!=null)
		{
			this.state = this.state.toUpperCase();
			if("TEXAS".equals(this.state))
				this.state = "TX";
				
		}
	}
	
	@CsvBindByName(column = "DonorId")
	@CsvBindByPosition(position = 0)
	public int donorId;
	
	@CsvBindByName(column = "Full Name")
	@CsvBindByPosition(position = 1)
	public String fullName;	
	
	@CsvBindByName(column = "First Name")
	@CsvBindByPosition(position = 2)
	public String firstName;
	
	@CsvBindByName(column = "Last Name")
	@CsvBindByPosition(position = 3)
	public String lastName;
	
	@CsvBindByName(column = "Email")
	@CsvBindByPosition(position = 4)
	public String email;
	
	@CsvBindByName(column = "Donation Amount")
	@CsvBindByPosition(position = 5)
	public double donationAmount;
	
	@CsvBindByName(column = "Phone")
	@CsvBindByPosition(position = 6)
	public String phone;
	
	@CsvBindByName(column = "Address Line1")
	@CsvBindByPosition(position = 7)
	public String address1;
	
	@CsvBindByName(column = "Address Line2")
	@CsvBindByPosition(position = 8)
	public String address2;
	
	@CsvBindByName(column = "City")
	@CsvBindByPosition(position = 9)
	public String city;
	
	@CsvBindByName(column = "State")
	@CsvBindByPosition(position = 10)
	public String state;
	
	@CsvBindByName(column = "Zip")
	@CsvBindByPosition(position = 11)
	public String zip;
	
	public String getFullName() {
		return Utility.getCamelCase(fullName);
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getFirstName() {
		if(firstName == null)
		{
			if(fullName != null) {
				return getFullName().substring(0,getFullName().indexOf(" "));
			}
		}
		return Utility.getCamelCase(firstName);		
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		if(lastName == null)
		{
			if(fullName != null) {
				return getFullName().substring(getFullName().indexOf(" ")+1, getFullName().length());
			}
		}
		return Utility.getCamelCase(lastName);
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getDonationAmount() {
		return donationAmount;
	}
	public void setDonationAmount(double donationAmount) {
		this.donationAmount = donationAmount;
	}
	public String getPhone() {
		return Utility.formatPhone(phone);
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	public int getDonorId() {
		return donorId;
	}

	public void setDonorId(int donorId) {
		this.donorId = donorId;
	}

	@Override
	public String toString() {
		return "Donor [fullName=" + fullName + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", donationAmount=" + donationAmount + ", phone=" + phone + ", address1=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zip=" + zip + ", donorId="
				+ donorId + "]";
	}
}
