/**
 * 
 */
package com.sliit.synchronizertokenpattern.model;

/**
 * @author Saranki
 *
 */
public class Transfer {
	
	private String accountNumber;
	
	private String senderNumber;
	
	private float amount;
	
	private String csrf;


	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCsrf() {
		return csrf;
	}

	public void setCsrf(String csrf) {
		this.csrf = csrf;
	}
	
	
	

}
