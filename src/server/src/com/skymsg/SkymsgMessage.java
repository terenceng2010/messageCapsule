package com.skymsg;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import org.postgis.*;

public class SkymsgMessage {

	private int messageid;
	
	private int senderid;
	private int receiverid;
	private String body;	
	
	private Timestamp createDate;
	private String revealDate;
	private String notifyDate;
	
	private Boolean temperatureRelated;
	private int temperature;
	private int weatherCondition;
	

	private Boolean locationRelated;
	private Boolean movable;
	private Geometry location;	
    private String address;

	


	
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
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
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
	public int getReceiverid() {
		return receiverid;
	}
	public void setReceiverid(int receiverid) {
		this.receiverid = receiverid;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Geometry getLocation() {
		return location;
	}
	public void setLocation(Geometry array) {
		this.location = array;
	}
	
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public JSONObject toJson(){
        Map map = new HashMap();
        map.put("messageid",messageid );
        map.put("weatherCondition",weatherCondition);
       map.put("createDate",createDate);
       map.put("revealDate",revealDate);
       map.put("notifyDate",notifyDate);
        map.put("locationRelated",locationRelated);
        map.put("movable",movable);
       map.put("temperatureRelated",temperatureRelated);
        map.put("senderid",senderid);
        map.put("receiverid",receiverid);
        map.put("body",body);
       if(this.getLocation()!=null)
       {
    	   System.out.println(messageid);
    	   System.out.println("set Location!" + this.getLocation().getFirstPoint().getX()+ this.getLocation().getFirstPoint().getY());
	       map.put("latitude",this.getLocation().getFirstPoint().getX());
	       map.put("longitude",this.getLocation().getFirstPoint().getY());
       }
       map.put("address",address);
        return new JSONObject(map);
    	
    }	
    
    


}
