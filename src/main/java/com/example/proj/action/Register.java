package com.example.proj.action;

import com.opensymphony.xwork2.ActionSupport;                                                                                                            
                            
import java.sql.Statement;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;                
import java.sql.DriverManager;
import java.sql.SQLException;
import com.example.proj.model.Person;

public class Register extends ActionSupport {
    
    private static final long serialVersionUID = 1L;
                                 
    private Person personBean;
    private String error = "Random";
    public String encryptedText;



    public String execute() throws Exception {
        personBean = getPersonBean();
        if(saveToDB()) {
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    public boolean saveToDB() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            String URL = "jdbc:mysql://localhost:3306/mydb?useTimezone=true&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, "root", "madmad123");

            if (connection != null) {
                statement = connection.createStatement();
                personBean.setPassWord(MD5(personBean.getPassWord()));
                String sql = "INSERT INTO users(userName, passWord, firstName, lastName, accountType) VALUES('"+personBean.getUserName()+"','"+personBean.getPassWord()+"','"+personBean.getFirstName()+"','"+personBean.getLastName()+"','"+personBean.getAccountType()+"')";
                statement.executeUpdate(sql);
                // System.out.println("NORMAL: " +personBean.getPassWord());
                // System.out.println("SHA: " + SHA256(personBean.getPassWord()));
                return true;
            } else {
                error = "DB connection failed";
                return false;
            }
         } catch (Exception e) {
             error = e.toString();
             return false;  
         } finally {
            if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
         }
    }

    public String getError() {
        return error;
    }
    
    public Person getPersonBean() {
        return personBean;
    }
    
    public void setPersonBean(Person person) {
        personBean = person;
    }

    public String MD5(String text){
        try{
        /* MessageDigest instance for hashing using SHA256 */
        MessageDigest md = MessageDigest.getInstance("MD5");
        /* digest() method called to calculate message digest of ’text’ and return array of byte */
        byte[] hash = md.digest(text.getBytes(StandardCharsets.UTF_8));
        /* The bytes array has bytes in decimal form. Convert it into hexadecimal format. */
        StringBuilder s = new StringBuilder();
        for(int i=0; i< hash.length; i++)
        {
        s.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        /* Complete hashed text in hexadecimal format */
        encryptedText = s.toString();
        }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return encryptedText;
    }

}