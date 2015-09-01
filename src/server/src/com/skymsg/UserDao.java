package com.skymsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserDao {
    private Connection connection;
    public UserDao() {
        try {
			connection = DbUtil.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public Boolean addUser(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into public.user(firstname,lastname,email,password,gcmregid) values (?, ?, ?, ?, ?)");
            // Parameters start with 1
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getGcmregid());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        return true;
        
    }

    public void deleteUser(int userId) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from public.user where userid=?");
            // Parameters start with 1
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update public.user set firstname=?, lastname=?, email=?, password=?, gcmregid=?" +
                            "where userid=? and password @@ to_tsquery(?) ");
            // Parameters start with 1
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getGcmregid());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean updateUsergcmregid(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update public.user set gcmregid =?" +
                            "where email @@ to_tsquery(?) and password @@ to_tsquery(?)");
            // Parameters start with 1
            preparedStatement.setString(1, user.getGcmregid());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from public.user");
            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setGcmregid(rs.getString("gcmregid"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User getUserById(int userId) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from public.user where userid=?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setGcmregid(rs.getString("gcmregid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    
    public User getUserBygcmregid(String gcmregid) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from public.user where gcmregid @@ to_tsquery(?)");
            preparedStatement.setString(1, gcmregid);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setGcmregid(rs.getString("gcmregid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        return user;
    }
    
    public User getUserByEmail(String email) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from public.user where email @@ to_tsquery(?)");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setGcmregid(rs.getString("gcmregid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        return user;
    }  
    
    public User getUserByPassword(String email, String password){
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from public.user where email @@ to_tsquery(?) and password @@ to_tsquery(?)");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setGcmregid(rs.getString("gcmregid"));
            }
            else
            {
            	user=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        return user;
    }  
}
