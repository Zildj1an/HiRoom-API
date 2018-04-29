package com.cgaxtr.hiroom.DAO;

import com.cgaxtr.hiroom.DB;
import com.cgaxtr.hiroom.Exceptions.InternalServerError;
import com.cgaxtr.hiroom.Exceptions.UserAlreadyExists;
import com.cgaxtr.hiroom.POJO.Credential;
import com.cgaxtr.hiroom.POJO.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUser {
    private Connection connect;

    public DAOUser(){
        this.connect = DB.getInstance().getConnection();
    }

    public int register(User user) throws UserAlreadyExists, InternalServerError {
        int id;
        String queryInsert = " INSERT INTO users (name, email, pass) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connect.prepareStatement(queryInsert);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPass());

            preparedStatement.execute();

            User u = getUserByEmail(user.getEmail());
            id = u.getId();

        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e){
            throw new UserAlreadyExists();
        }catch (SQLException e) {
            throw new InternalServerError();
        }

        return id;

    }

    public User login(Credential credential) throws InternalServerError {
        User user = null;

        String querySelect = "SELECT * FROM users WHERE email=? and pass=?";

        try {
            PreparedStatement preparedStatement = connect.prepareStatement(querySelect);
            preparedStatement.setString(1, credential.getEmail());
            preparedStatement.setString(2,credential.getPass());

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                user = getUserByEmail(credential.getEmail());
            }
        } catch (SQLException e) {
            throw  new InternalServerError();
        }

        return user;
    }

    public User getUserByEmail(String email) throws InternalServerError {

        User user = null;

        String querySelect = "SELECT  * FROM users WHERE email=?" ;

        try{
            PreparedStatement preparedStatement = connect.prepareStatement(querySelect);
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                user = populateUser(rs);
            }
        } catch (SQLException e) {
            throw new InternalServerError();
        }
        return user;
    }

    public User getUserById(int id) throws InternalServerError {

        User user = null;

        String querySelect = "SELECT  * FROM users WHERE id=?" ;

        try{
            PreparedStatement preparedStatement = connect.prepareStatement(querySelect);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                user = populateUser(rs);
            }
        } catch (SQLException e) {
            throw new InternalServerError();
        }
        return user;
    }

    public Boolean updateUser(int id, User user) throws InternalServerError {

        boolean successful = false;

        String updateQuery = "UPDATE users SET name = ?, email = ?, gender = ?, city = ?, smoker = ?, worker = ?, " +
                "description = ?, partying = ?, organized = ?, athlete = ?, freak = ?, sociable = ?, active = ? where id = ?";

        try {
            PreparedStatement ps = connect.prepareStatement(updateQuery);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getGender());
            ps.setString(4, user.getCity());
            ps.setBoolean(5,user.getSmoker());
            ps.setString(6,user.getWorker());
            ps.setString(7, user.getDescription());
            ps.setInt(8, user.getPartying());
            ps.setInt(9, user.getOrganized());
            ps.setInt(10, user.getAthlete());
            ps.setInt(11, user.getFreak());
            ps.setInt(12, user.getSociable());
            ps.setInt(13, user.getActive());
            ps.setInt(14, id);

            if( ps.executeUpdate() != 0){
                successful = true;
            }

        } catch (SQLException e) {
            throw new InternalServerError();
        }

        return successful;
    }

    private User populateUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPathImg(rs.getString("imgPath"));
        user.setCity(rs.getString("city"));
        user.setGender(rs.getString("gender"));
        user.setSmoker(rs.getBoolean("smoker"));
        user.setWorker(rs.getString("worker"));
        user.setDescription(rs.getString("description"));
        user.setPartying(rs.getInt("partying"));
        user.setOrganized(rs.getInt("organized"));
        user.setAthlete(rs.getInt("athlete"));
        user.setOrganized(rs.getInt("freak"));
        user.setSociable(rs.getInt("sociable"));
        user.setActive(rs.getInt("active"));

        return user;
    }
}







