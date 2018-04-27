package com.cgaxtr.hiroom.DAO;

import com.cgaxtr.hiroom.DB;
import com.cgaxtr.hiroom.Exceptions.InternalServerError;
import com.cgaxtr.hiroom.POJO.Advertisement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAdvertisement {

    private Connection connection;

    public DAOAdvertisement(){
        this.connection = DB.getInstance().getConnection();
    }

    public Advertisement getAdvertisement(int id) throws InternalServerError {
        String getAdd = "SELECT advertisements.id, advertisements.address, advertisements.price, advertisements.size, advertisements.floor, advertisements.description, users.name, users.id as user_id, users.email FROM advertisements INNER JOIN users ON users.id = advertisements.id_user WHERE advertisements.id = ?";
        Advertisement add = null;

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(getAdd);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                add = populateAdd(rs);
            }

        } catch (SQLException e) {
            throw new InternalServerError();
        }

        return add;
    }

    public void addAdvertisement(Advertisement adv) throws InternalServerError {

        String insertAdd = "INSERT INTO advertisements (id_user, address, price, size, floor, description) VALUES (?,?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertAdd);
            preparedStatement.setInt(1,adv.getOwnerId());
            preparedStatement.setString(2, adv.getAddress());
            preparedStatement.setInt(3, adv.getPrice());
            preparedStatement.setInt(4, adv.getSize());
            preparedStatement.setInt(5, adv.getFloor());
            preparedStatement.setString(6, adv.getDescription());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }

    public List<Advertisement> getAllAdvertisements() throws InternalServerError {
        return getAllAdvertisements(0);
    }

    public List<Advertisement> getSelfAdvertisements(int id) throws InternalServerError {
        return getAllAdvertisements(id);
    }

    public void deleteAdvertisement(int id) throws InternalServerError {
        String query = "DELETE FROM advertisements WHERE id = ?";

        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,id);
            ps.execute();

        } catch (SQLException e) {
            throw new InternalServerError();
        }
    }



    private List<Advertisement> getAllAdvertisements(int idUser) throws InternalServerError {
        List<Advertisement> list = new ArrayList<>();

        String select = "SELECT advertisements.id, advertisements.address, advertisements.price, advertisements.size, advertisements.floor, advertisements.description, users.name, users.id as user_id, users.email FROM advertisements INNER JOIN users ON users.id = advertisements.id_user ";

        if(idUser != 0){
            select += "WHERE users.id =?";
        }

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            if(idUser != 0){
                preparedStatement.setInt(1, idUser);
            }
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Advertisement add;
                add = populateAdd(rs);
                list.add(add);
            }

        } catch (SQLException e) {
            throw new InternalServerError();
        }

        return list;
    }

    private List<String> getImagesForAdvertisement(int id) throws SQLException {
        List<String> list = new ArrayList<>();

        String selectImages = "SELECT path from advertisement_images where id_advertisement = ?";
        PreparedStatement images = connection.prepareStatement(selectImages);
        images.setInt(1, id);
        ResultSet rsImages = images.executeQuery();

        while(rsImages.next()){
            list.add(rsImages.getString("path"));
        }
        return list;
    }

    private Advertisement populateAdd(ResultSet rs) throws SQLException {
        Advertisement add = new Advertisement();
        add.setIdAdvertisement(rs.getInt("id"));
        add.setAddress(rs.getString("address"));
        add.setPrice(rs.getInt("price"));
        add.setSize(rs.getInt("size"));
        add.setFloor(rs.getInt("floor"));
        add.setDescription(rs.getString("description"));
        add.setOwnerName(rs.getString("name"));
        //
        add.setOwnerId(rs.getInt("user_id"));
        add.setOwnerEmail(rs.getString("email"));

        List<String> listImages;
        listImages = getImagesForAdvertisement(add.getIdAdvertisement());
        add.setImages(listImages);

        return add;
    }
}
