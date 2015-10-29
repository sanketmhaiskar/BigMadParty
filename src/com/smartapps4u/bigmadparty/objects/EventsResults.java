package com.smartapps4u.bigmadparty.objects;

public class EventsResults {
	String EventID="", EventName="", Imagename, StartDate, PlaceName="", PlaceTypes="",Address;
	int Status;

	public void setEventID(String EventID) {
		this.EventID = EventID;
	}

	public String getEventID() {
		return EventID;
	}

	public void setEventName(String EventName) {
		this.EventName = EventName;
	}

	public String getEventName() {
		return EventName;
	}

	public void setImageName(String Imagename) {
		this.Imagename = Imagename;
	}

	public String getImageName() {
		return Imagename;
	}

	public void setStartDate(String StartDate) {
		this.StartDate = StartDate;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStatus(int Status) {
		this.Status = Status;
	}

	public int getStatus() {
		return Status;
	}

	public void setPlaceName(String PlaceName) {
		this.PlaceName = PlaceName;
	}

	public String getPlaceName() {
		return PlaceName;
	}

	public void setPlaceTypes(String PlaceTypes) {
		this.PlaceTypes = PlaceTypes;
	}

	public String getPlaceTypes() {
		return PlaceTypes;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public String getAddress() {
		return Address;
	}
}
