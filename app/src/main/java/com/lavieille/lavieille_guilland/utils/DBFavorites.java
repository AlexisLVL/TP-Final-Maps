package com.lavieille.lavieille_guilland.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBFavorites {
    private final String connectionUrl = "jdbc:mysql://noproxy.hexteckgate.ga:13306/tp_note_app_mobile?useSSL=false";
    private final String user = "tp_app_mobile";
    private final String pass = "kp7Yawd4okt5E3ehWQQL";

    private static Connection connection = null;

    public void getConnection() {
        if (connection != null) return;

        try {
            connection = DriverManager.getConnection(connectionUrl, user, pass);
        } catch (Exception ignored) {}
    }

    public ArrayList<String> getFavorites(String uid) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT favoriteId FROM UserFavorites WHERE userUid='" + uid + "';");
            statement.execute();
            ResultSet res = statement.getResultSet();

            ArrayList<String> favorites = new ArrayList<>();
            while(res.next()) {
                favorites.add(res.getString(1));
            }

            return favorites;
        } catch (Exception ignored) {
            System.out.println(ignored);
        }

        return new ArrayList<>();
    }

    public boolean addFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO UserFavorites VALUES('" + uid + "', '" + favoriteID + "')");
            statement.execute();

            return true;
        } catch (Exception ignored) {
            System.out.println(ignored);
            System.out.println("Ouaip wtf");
        }

        return false;
    }

    public boolean removeFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM UserFavorites WHERE userUid = '" + uid + "' and favoriteId = '" + favoriteID + "'");
            statement.execute();

            return true;
        } catch (Exception ignored) {
            System.out.println(ignored);
        }

        return false;
    }
}
