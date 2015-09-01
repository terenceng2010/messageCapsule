package com.skymsg;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgis.PGgeometry;

public class MessageDao {
	private Connection connection;
    public MessageDao() {
        try {
			connection = DbUtil.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

// to be devloped
//    public Boolean addMessage(SkymsgMessage skymsgMessage) {
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("insert into public.user(firstname,lastname,email,password,gcmregid) values (?, ?, ?, ?, ?)");
//            // Parameters start with 1
//            preparedStatement.setString(1, skymsgMessage.getFirstName());
//            preparedStatement.setString(2, skymsgMessage.getLastName());
//            preparedStatement.setString(3, skymsgMessage.getEmail());
//            preparedStatement.setString(4, skymsgMessage.getPassword());
//            preparedStatement.setString(5, skymsgMessage.getGcmregid());
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//           return false;
//        }
//        return true;
//        
//    }

    public Boolean addPlainOldMessage(SkymsgMessage skymsgMessage) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into public.message(body,senderid,receiverid) values (?, ?, ?)");
            // Parameters start with 1
            preparedStatement.setString(1, skymsgMessage.getBody());
            preparedStatement.setInt(2, skymsgMessage.getSenderid());
            preparedStatement.setInt(3, skymsgMessage.getReceiverid());
            
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        return true;
        
    } 
    
	public Boolean addDateMessage(SkymsgMessage skymsgMessage) {
        try {
        	if(skymsgMessage.getNotifyDate().toString().equals("noNotifyDate"))
        	{
        		
	            PreparedStatement preparedStatement = connection
	                    .prepareStatement("insert into public.message(body,senderid,receiverid,date_to_reveal,createdate) values (?, ?, ? ,"+"TIMESTAMP WITH TIME ZONE'"+skymsgMessage.getRevealDate().toString()+"', now())");
	            // Parameters start with 1
	            preparedStatement.setString(1, skymsgMessage.getBody());
	            preparedStatement.setInt(2, skymsgMessage.getSenderid());
	            preparedStatement.setInt(3, skymsgMessage.getReceiverid());
	          
	            preparedStatement.executeUpdate();
        	}
        	else
        	{
	            PreparedStatement preparedStatement = connection
	                    .prepareStatement("insert into public.message(body,senderid,receiverid,date_to_notify,date_to_reveal,createdate) values (?, ?, ? ,"+"TIMESTAMP WITH TIME ZONE'"+skymsgMessage.getNotifyDate()+"',"+"TIMESTAMP WITH TIME ZONE'"+skymsgMessage.getRevealDate()+"',now())");
	           
	            // Parameters start with 1
	            preparedStatement.setString(1, skymsgMessage.getBody());
	            preparedStatement.setInt(2, skymsgMessage.getSenderid());
	            preparedStatement.setInt(3, skymsgMessage.getReceiverid());
	            System.out.println(preparedStatement.toString());
	            preparedStatement.executeUpdate();
        	}
        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        return true;
	}

	public Boolean addLocationMessage(SkymsgMessage skymsgMessage) {
        try {
       
        	if(skymsgMessage.getNotifyDate().toString().equals("noNotifyDate") && skymsgMessage.getRevealDate().toString().equals("noRevealDate") ){
                System.out.println();
        		PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into public.message(body,senderid,receiverid,address,location ,createdate) values (?, ?, ?,?,ST_GeomFromText('POINT("+skymsgMessage.getLocation().getFirstPoint().x +" "+skymsgMessage.getLocation().getFirstPoint().y+")', 4326), now())");
                // Parameters start with 1
                preparedStatement.setString(1, skymsgMessage.getBody());
                preparedStatement.setInt(2, skymsgMessage.getSenderid());
                preparedStatement.setInt(3, skymsgMessage.getReceiverid());
                preparedStatement.setString(4, skymsgMessage.getAddress());
               
                
                preparedStatement.executeUpdate();        		
        	}else if(skymsgMessage.getRevealDate().toString().equals("noRevealDate")){
                PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into public.message(body,senderid,receiverid,date_to_notify,createdate) values (?, ?, ?,)");
                // Parameters start with 1
                preparedStatement.setString(1, skymsgMessage.getBody());
                preparedStatement.setInt(2, skymsgMessage.getSenderid());
                preparedStatement.setInt(3, skymsgMessage.getReceiverid());
                preparedStatement.setString(4, skymsgMessage.getLocation().getValue());
                
                preparedStatement.executeUpdate();        		
        	}else{
	            PreparedStatement preparedStatement = connection
	                    .prepareStatement("insert into public.message(body,senderid,receiverid,date_to_notify,createdate) values (?, ?, ?,)");
	            // Parameters start with 1
	            preparedStatement.setString(1, skymsgMessage.getBody());
	            preparedStatement.setInt(2, skymsgMessage.getSenderid());
	            preparedStatement.setInt(3, skymsgMessage.getReceiverid());
	            preparedStatement.setString(4, skymsgMessage.getLocation().getValue());
	            
	            preparedStatement.executeUpdate();
        	}
        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        return true;
	}   
	
    public SkymsgMessage getMessage(Integer userId,Integer messageId) {
    	SkymsgMessage skymsgMessage = new SkymsgMessage();
    	try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from public.message where userId=? and messageid=?");
            // Parameters start with 1
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {

            	skymsgMessage.setMessageid(rs.getInt("messageid"));
            	skymsgMessage.setWeatherCondition( rs.getInt("weather_condition") );
            	skymsgMessage.setRevealDate(rs.getString("date_to_reveal"));
            	skymsgMessage.setNotifyDate(rs.getString("date_to_notify"));
            	skymsgMessage.setMovable(rs.getBoolean("movable"));
            	skymsgMessage.setTemperature(rs.getInt("temperature"));
            	skymsgMessage.setTemperatureRelated(rs.getBoolean("temperature_lower_or_higher"));
            	skymsgMessage.setBody(rs.getString("body"));
            	skymsgMessage.setSenderid(rs.getInt("senderid"));
            	skymsgMessage.setReceiverid(rs.getInt("receiverid"));
            	skymsgMessage.setLocation(PGgeometry.geomFromString(rs.getString("location")));
            	skymsgMessage.setCreateDate(rs.getTimestamp("createdate"));
            	

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return skymsgMessage;  	
    }
    
    public List<SkymsgMessage> getAllMessages(Integer userId) {
        List<SkymsgMessage> skymsgMessages = new ArrayList<SkymsgMessage>();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * , ST_AsText(location) AS locationDetail from public.message where receiverid=?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
            	SkymsgMessage skymsgMessage = new SkymsgMessage();
            	skymsgMessage.setMessageid(rs.getInt("messageid"));
            	skymsgMessage.setWeatherCondition(rs.getInt("weather_condition"));
            	skymsgMessage.setRevealDate(rs.getString("date_to_reveal"));
            	skymsgMessage.setNotifyDate(rs.getString("date_to_notify"));
            	skymsgMessage.setMovable(rs.getBoolean("movable"));
            	skymsgMessage.setTemperature(rs.getInt("temperature"));
            	skymsgMessage.setTemperatureRelated(rs.getBoolean("temperature_lower_or_higher"));
            	skymsgMessage.setBody(rs.getString("body"));
            	skymsgMessage.setSenderid(rs.getInt("senderid"));
            	skymsgMessage.setReceiverid(rs.getInt("receiverid"));
            	if(rs.getString("locationDetail")!=null)
            	{
            		skymsgMessage.setLocation(PGgeometry.geomFromString(rs.getString("locationDetail")));
            		
            		System.out.println("get Location info: "+ rs.getString("locationDetail"));
            	}
            	skymsgMessage.setCreateDate(rs.getTimestamp("createdate"));
            	skymsgMessage.setAddress(rs.getString("address"));
            	
            	skymsgMessages.add(skymsgMessage);
            	
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skymsgMessages;
    }
    
    //Function for TimeServlet
    public List<SkymsgMessage> getAllMessagesToBeNotified() {
        List<SkymsgMessage> skymsgMessages = new ArrayList<SkymsgMessage>();
        try {
        	//select all messages to be notified within the future 1 minute
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * , ST_AsText(location) AS locationDetail from public.message where date_to_notify - NOW() > '0 second' and date_to_notify - NOW() < '1 minute'");
           
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
            	SkymsgMessage skymsgMessage = new SkymsgMessage();
            	skymsgMessage.setMessageid(rs.getInt("messageid"));
            	skymsgMessage.setWeatherCondition(rs.getInt("weather_condition"));
            	skymsgMessage.setRevealDate(rs.getString("date_to_reveal"));
            	skymsgMessage.setNotifyDate(rs.getString("date_to_notify"));
            	skymsgMessage.setMovable(rs.getBoolean("movable"));
            	skymsgMessage.setTemperature(rs.getInt("temperature"));
            	skymsgMessage.setTemperatureRelated(rs.getBoolean("temperature_lower_or_higher"));
            	skymsgMessage.setBody(rs.getString("body"));
            	skymsgMessage.setSenderid(rs.getInt("senderid"));
            	skymsgMessage.setReceiverid(rs.getInt("receiverid"));
            	if(rs.getString("locationDetail")!=null)
            		skymsgMessage.setLocation(PGgeometry.geomFromString(rs.getString("locationDetail")));
            	skymsgMessage.setCreateDate(rs.getTimestamp("createdate"));
            	skymsgMessage.setAddress(rs.getString("address"));
            	
            	skymsgMessages.add(skymsgMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skymsgMessages;
    }
    
    public List<SkymsgMessage> getAllMessagesToBeRevealed() {
        List<SkymsgMessage> skymsgMessages = new ArrayList<SkymsgMessage>();
        try {
        	//select all messages to be revealed within the future 1 minute and it is not needed to be revealed by Proper Location
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * , ST_AsText(location) AS locationDetail  from public.message where date_to_reveal - NOW() > '0 second' and date_to_reveal - NOW() <= '1 minute' ");
           
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
            	SkymsgMessage skymsgMessage = new SkymsgMessage();
            	skymsgMessage.setMessageid(rs.getInt("messageid"));
            	skymsgMessage.setWeatherCondition(rs.getInt("weather_condition"));
            	skymsgMessage.setRevealDate(rs.getString("date_to_reveal"));
            	skymsgMessage.setNotifyDate(rs.getString("date_to_notify"));
            	skymsgMessage.setMovable(rs.getBoolean("movable"));
            	skymsgMessage.setTemperature(rs.getInt("temperature"));
            	skymsgMessage.setTemperatureRelated(rs.getBoolean("temperature_lower_or_higher"));
            	skymsgMessage.setBody(rs.getString("body"));
            	skymsgMessage.setSenderid(rs.getInt("senderid"));
            	skymsgMessage.setReceiverid(rs.getInt("receiverid"));
            	
            	if(rs.getString("locationDetail")!=null)
            		skymsgMessage.setLocation(PGgeometry.geomFromString(rs.getString("locationDetail")));
            	skymsgMessage.setCreateDate(rs.getTimestamp("createdate"));
            	skymsgMessage.setAddress(rs.getString("address"));
            	
            	//with location not reveal
//            	if(rs.getString("locationDetail")==null){
//            		
//            	}
            	skymsgMessages.add(skymsgMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skymsgMessages;
    }


}
