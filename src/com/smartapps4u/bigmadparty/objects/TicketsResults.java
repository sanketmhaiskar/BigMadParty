package com.smartapps4u.bigmadparty.objects;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TicketsResults implements Serializable {
	int TicketID, Age, EventID, EventInstanceID;
	String FirstName = "", LastName = "", TicketName = "", CheckinTime = "",
			details = "", TicketNumber = "";
	Boolean Gender, isProcessed;

	public void setFirstName(String FirstName) {
		this.FirstName = FirstName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setTicketName(String TicketName) {
		this.TicketName = TicketName;
	}

	public String getTicketName() {
		return TicketName;
	}

	public void setLastName(String LastName) {
		this.LastName = LastName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDetails() {
		return details;
	}

	public void setTicketNumber(String TicketNumber) {
		this.TicketNumber = TicketNumber;
	}

	public String getTicketNumber() {
		return TicketNumber;
	}

	public void setTicketID(int TicketID) {
		this.TicketID = TicketID;
	}

	public int getTicketID() {
		return TicketID;
	}

	public void setAge(int Age) {
		this.Age = Age;
	}

	public int getAge() {
		return Age;
	}

	public void setEventID(int EventID) {
		this.EventID = EventID;
	}

	public int getEventID() {
		return EventID;
	}

	public void setEventInstanceID(int EventInstanceID) {
		this.EventInstanceID = EventInstanceID;
	}

	public int getEventInstanceID() {
		return EventInstanceID;
	}

	public void setProcessedStatus(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Boolean isProcessed() {
		return isProcessed;
	}

	public void setGender(Boolean Gender) {
		this.Gender = Gender;
	}

	public Boolean getGender() {
		return Gender;
	}
}
