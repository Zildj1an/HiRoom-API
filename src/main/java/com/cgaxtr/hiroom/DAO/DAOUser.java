package com.cgaxtr.hiroom.DAO;

import com.cgaxtr.hiroom.DB;
import com.cgaxtr.hiroom.Exceptions.InternalServerError;
import com.cgaxtr.hiroom.Exceptions.UserAlreadyExists;
import com.cgaxtr.hiroom.POJO.Credential;
import com.cgaxtr.hiroom.POJO.User;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

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

        String querySelect = "SELECT * FROM users WHERE email=?" ;

        try{
            PreparedStatement preparedStatement = connect.prepareStatement(querySelect);
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                user = populateUser(rs);
            }
        } catch (SQLException | ParseException e) {
            throw new InternalServerError();
        }
        return user;
    }

    public User getUserById(int id) throws InternalServerError {

        User user = null;

        String querySelect = "SELECT * FROM users WHERE id=?" ;

        try{
            PreparedStatement preparedStatement = connect.prepareStatement(querySelect);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                user = populateUser(rs);
            }
        } catch (SQLException | ParseException e) {
            throw new InternalServerError();
        }
        return user;
    }

    public Boolean updateUser(int id, User user) throws InternalServerError {

        boolean successful = false;

        String updateQuery = "UPDATE users SET name = ?, surname = ?, email = ?, phoneNumber = ?, birthDate = ?, gender = ?, city = ?, smoker = ?, worker = ?, " +
                "description = ?, partying = ?, organized = ?, athlete = ?, freak = ?, sociable = ?, active = ? where id = ?";

        try {
            PreparedStatement ps = connect.prepareStatement(updateQuery);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getPhoneNumber());
            ps.setString(5, dateFormatDB(user.getBirthDate()));
            ps.setString(6, user.getGender());
            ps.setString(7, user.getCity());
            ps.setBoolean(8,user.getSmoker());
            ps.setString(9,user.getWorker());
            ps.setString(10, user.getDescription());
            ps.setInt(11, user.getPartying());
            ps.setInt(12, user.getOrganized());
            ps.setInt(13, user.getAthlete());
            ps.setInt(14, user.getFreak());
            ps.setInt(15, user.getSociable());
            ps.setInt(16, user.getActive());
            ps.setInt(17, id);

            if( ps.executeUpdate() != 0){
                successful = true;
            }

        } catch (SQLException | ParseException e) {
            throw new InternalServerError();
        }

        return successful;
    }

    public boolean updateAvatar(String path, int id) throws InternalServerError {
        boolean successful = false;

        String update = "UPDATE users SET imgPath= ? where id = ?";
        try {
            PreparedStatement ps = connect.prepareStatement(update);
            ps.setString(1, path);
            ps.setInt(2, id);

            if( ps.executeUpdate() != 0){
                successful = true;
            }

        } catch (SQLException e) {
            throw  new InternalServerError();
        }

        return successful;
    }

    private User populateUser(ResultSet rs) throws SQLException, ParseException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getInt("phoneNumber"));
        user.setBirthDate(dateFormatUser(rs.getString("birthDate")));
        user.setPathImg(rs.getString("imgPath"));
        user.setCity(rs.getString("city"));
        user.setGender(rs.getString("gender"));
        user.setSmoker(rs.getBoolean("smoker"));
        user.setWorker(rs.getString("worker"));
        user.setDescription(rs.getString("description"));
        user.setPartying(rs.getInt("partying"));
        user.setOrganized(rs.getInt("organized"));
        user.setAthlete(rs.getInt("athlete"));
        user.setFreak(rs.getInt("freak"));
        user.setSociable(rs.getInt("sociable"));
        user.setActive(rs.getInt("active"));

        return user;
    }

    private String dateFormatDB(String date) throws ParseException {
        final String OLD_FORMAT = "dd/MM/yyyy";
        final String NEW_FORMAT = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(date);
        sdf.applyPattern(NEW_FORMAT);

        return sdf.format(d);

    }

    private String dateFormatUser(String date) throws ParseException {
        if(date == null)
            return null;

        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(date);
        sdf.applyPattern(NEW_FORMAT);

        return sdf.format(d);
    }

}







