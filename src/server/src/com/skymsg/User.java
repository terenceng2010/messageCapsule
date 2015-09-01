package com.skymsg;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private int userid;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gcmregid;
    private String timeZonePreference;
    
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
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
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeZonePreference() {
        return timeZonePreference;
    }
    public void setTimeZonePreference(String timeZonePreference) {
        this.timeZonePreference = timeZonePreference;
    }
    
    public String getGcmregid(){
    	return gcmregid;
    }
    
    public void setGcmregid(String gcmregid){
    	this.gcmregid = gcmregid;
    }   
    @Override
    public String toString() {
        return "User [userid=" + userid + ", firstName=" + firstName
                + ", lastName=" + lastName + ", email="
                + email + ", password=" + password + ", gcmregid=" + gcmregid + "]";
    }   
    
    public JSONObject toJson(){
        Map map = new HashMap();
        map.put("userid",userid );
        map.put("firstName",firstName );
        map.put("lastName",lastName );
        map.put("email",email);
        map.put("password",password);
        map.put("gcmredid",gcmregid);

        return new JSONObject(map);
    	
    }
    
}