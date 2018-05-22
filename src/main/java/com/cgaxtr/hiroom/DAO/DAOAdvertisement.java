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
        String getAdd = "SELECT advertisements.id, advertisements.title, advertisements.type, advertisements.address, advertisements.number, advertisements.city, advertisements.price, advertisements.size, advertisements.floor, advertisements.description, users.name, users.id as user_id, users.email, users.phoneNumber FROM advertisements INNER JOIN users ON users.id = advertisements.id_user WHERE advertisements.id = ?";
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

        String insertAdd = "INSERT INTO advertisements (id_user, title, type, address, number, city, price, size, floor, description) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(insertAdd);
            preparedStatement.setInt(1,adv.getOwnerId());
            preparedStatement.setString(2, adv.getTitle());
            preparedStatement.setString(3, adv.getType());
            preparedStatement.setString(4, adv.getAddress());
            preparedStatement.setInt(5, adv.getNumber());
            preparedStatement.setString(6, adv.getCity());
            preparedStatement.setInt(7, adv.getPrice());
            preparedStatement.setInt(8, adv.getSize());
            preparedStatement.setInt(9, adv.getFloor());
            preparedStatement.setString(10, adv.getDescription());

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

        String select = "SELECT advertisements.id, advertisements.title, advertisements.type, advertisements.address, advertisements.number, advertisements.city, advertisements.price, advertisements.size, advertisements.floor, advertisements.description, users.name, users.id as user_id, users.email, users.phoneNumber FROM advertisements INNER JOIN users ON users.id = advertisements.id_user ";

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
        add.setTitle(rs.getString("title"));
        add.setType(rs.getString("type"));
        add.setAddress(rs.getString("address"));
        add.setNumber(rs.getInt("number"));
        add.setCity(rs.getString("city"));
        add.setPrice(rs.getInt("price"));
        add.setSize(rs.getInt("size"));
        add.setFloor(rs.getInt("floor"));
        add.setDescription(rs.getString("description"));
        add.setOwnerName(rs.getString("name"));
        add.setOwnerId(rs.getInt("user_id"));
        add.setOwnerEmail(rs.getString("email"));
        add.setOwnerPhone(rs.getInt("phoneNumber"));

        List<String> listImages;
        listImages = getImagesForAdvertisement(add.getIdAdvertisement());
        add.setImages(listImages);

        return add;
    }

    public List<Advertisement> search(String zone) throws InternalServerError {
        List<Advertisement> list = new ArrayList<>();

        String select = "SELECT advertisements.id, advertisements.title, advertisements.type, advertisements.address, advertisements.number, advertisements.city, advertisements.price, advertisements.size, advertisements.floor, advertisements.description, users.name, users.id as user_id, users.email, users.phoneNumber FROM advertisements INNER JOIN users ON users.id = advertisements.id_user WHERE advertisements.city = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1, zone);

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

    public boolean addImage(String path, int id) throws InternalServerError {
        boolean ok = true;

        String insert = "INSERT INTO advertisement_images (id_advertisement, path) VALUES (?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(insert);
            ps.setString(2, path);
            ps.setInt(1, id);

            if( ps.executeUpdate() != 0){
                ok = true;
            }

        } catch (SQLException e) {
            throw  new InternalServerError();
        }

        return ok;
    }
}
