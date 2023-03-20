package com.lavieille.lavieille_guilland.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class DBUsers {
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
        } catch (Exception ignored) {}

        return new ArrayList<>();
    }

    public boolean addFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO UserFavorites VALUES('" + uid + "', '" + favoriteID + "')");
            statement.execute();

            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean removeFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM UserFavorites WHERE userUid = '" + uid + "' and favoriteId = '" + favoriteID + "'");
            statement.execute();

            return true;
        } catch (Exception ignored) {}

        return false;
    }

    public boolean isFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM UserFavorites WHERE userUid = '" + uid + "' and favoriteId = '" + favoriteID + "'");
            statement.execute();
            return statement.getResultSet().next();
        } catch (Exception ignored) {}

        return false;
    }

    public PerformData getPerform(String uid) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM UserPerform WHERE userUid='" + uid + "';");
            statement.execute();
            ResultSet res = statement.getResultSet();

            if (res.next()) {
                PerformData performData = new PerformData();
                performData.setDistance(Double.parseDouble(res.getString("distance")));
                performData.setDate(Instant.parse(res.getString("date")));

                return performData;
            }
        } catch (Exception ignored) {}

        return null;
    }

    public boolean setPerform(String uid, double distance, String date) {
        getConnection();

        try {
            if (getPerform(uid) != null) {
                PreparedStatement statement = connection.prepareStatement("UPDATE UserPerform SET distance='" + distance + "', date='" + date + "' WHERE userUid='" + uid + "'");
                statement.execute();
            } else {
                // Initiate values in the database
                PreparedStatement statement = connection.prepareStatement("INSERT INTO UserPerform VALUES('" + uid + "', '" + distance + "', '" + date + "');");
                statement.execute();
            }
            return true;
        } catch (Exception ignored) {}
        return false;
    }
}
