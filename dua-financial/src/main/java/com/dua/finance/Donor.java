package com.dua.finance;

public class Donor {

	Donor()
	{
		
	}
	
	Donor(String fullName, String firstName, String lastName, String email, double donationAmount, 
			String phone, String address1, String address2, String city, String state, String zip)
	{
		this.fullName = fullName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.donationAmount = donationAmount;
		this.phone = phone;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public String fullName;
	public String firstName;
	public String lastName;
	public String email;
	public double donationAmount;
	public String phone;
	public String address1;
	public String address2;
	public String city;
	public String state;
	public String zip;
	
	public String getFullName() {
		return Utility.getCamelCase(fullName);
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
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

	@Override
	public String toString() {
		return "Donor [fullName=" + getFullName() + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", donationAmount=" + donationAmount + ", phone=" + getPhone() + ", address1=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zip=" + zip + "]";
	}
	
}
