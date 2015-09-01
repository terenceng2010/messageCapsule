package com.terence;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MessageRecord implements Comparable<MessageRecord> {


	private Integer messageid;
	
	private Integer senderid;
	private String senderName;
	private String senderEmail;
	private Integer receiverid;
	private String receiverName;
	private String receiverEmail;
	private String body;	
	private String image;
	
	private String createDate;
	private String revealDate;
	private String notifyDate;
	private Boolean dateRelated;
	
	private Boolean temperatureRelated;
	private int temperature;
	private int weatherCondition;
	

	private Boolean locationRelated;
	private Boolean movable;
	private String location;
	private String latitude;
	private String longitude;
    private String address;
    private double initalSpeed;
    private String heading;
    private Boolean publicMsg;
    
	private float distance;
	private Boolean revealed;
	
	public MessageRecord(){
		
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
	public int getWeatherCondition() {
		return weatherCondition;
	}
	public void setWeatherCondition(int i) {
		this.weatherCondition = i;
	}
	public int getTemperature(){
		return temperature;
	}
	public void setTemperature(int temperature)
	{
		this.temperature = temperature;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getRevealDate() {
		return revealDate;
	}
	public void setRevealDate(String revealDate) {
		this.revealDate = revealDate;
	}
	public String getNotifyDate() {
		return notifyDate;
	}
	public void setNotifyDate(String notifyDate) {
		this.notifyDate = notifyDate;
	}
	public Boolean getLocationRelated() {
		return locationRelated;
	}
	public void setLocationRelated(Boolean locationRelated) {
		this.locationRelated = locationRelated;
	}
	public Boolean getMovable() {
		return movable;
	}
	public void setMovable(Boolean movable) {
		this.movable = movable;
	}
	public Boolean getTemperatureRelated() {
		return temperatureRelated;
	}
	public void setTemperatureRelated(Boolean temperatureRelated) {
		this.temperatureRelated = temperatureRelated;
	}
	public int getSenderid() {
		return senderid;
	}
	public void setSenderid(int senderid) {
		this.senderid = senderid;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public int getReceiverid() {
		return receiverid;
	}
	public void setReceiverid(int receiverid) {
		this.receiverid = receiverid;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getBody() {
		return body;
	}
	
	public String getBodyInUTF8(){
		
		String encodedAddressString="";
	    
	      try {
	    	if(this.body!=null)
			encodedAddressString = URLEncoder.encode(this.body, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedAddressString;		
	} 
	public void setBody(String body) {
		this.body = body;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String array) {
		this.location = array;
	}
	
	public String getAddressInUTF8(){
		
		String encodedAddressString="";
	    
	      try {
	    	if(this.address!=null)
			encodedAddressString = URLEncoder.encode(this.address, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedAddressString;		
	}
	public String getAddress(){
		
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public double getInitalSpeed() {
		return initalSpeed;
	}
	public void setInitalSpeed(double initalSpeed) {
		this.initalSpeed = initalSpeed;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public Boolean getPublicMsg() {
		return publicMsg;
	}
	public void setPublicMsg(Boolean publicMsg) {
		this.publicMsg = publicMsg;
	}
	public Boolean getRevealed() {
		return revealed;
	}
	public void setRevealed(Boolean revealed) {
		this.revealed = revealed;
	}
	public Boolean getDateRelated() {
		return dateRelated;
	}
	public void setDateRelated(Boolean dateRelated) {
		this.dateRelated = dateRelated;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getReceiverEmail() {
		return receiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	@Override
	public int compareTo(MessageRecord another) {
		// TODO Auto-generated method stub
		if(this.messageid>another.messageid){
			return -1;
		}else if(this.messageid<another.messageid){
			return 1;
		}else{
			return 0;
		}
		
	}


	
}
