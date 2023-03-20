package com.lavieille.lavieille_guilland.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;

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

    /**
     * Return a list of favorites
     * @param uid The user ID given by Firebase
     * @return ArrayList<String>
     */
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

    /**
     * Add a favorite in the database
     * @param uid The user ID given by Firebase
     * @param favoriteID The ID of the position of the favorite
     * @return True on success, false otherwise
     */
    public boolean addFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO UserFavorites VALUES('" + uid + "', '" + favoriteID + "')");
            statement.execute();

            return true;
        } catch (Exception ignored) {}

        return false;
    }

    /**
     * Remove a favorite in the database
     * @param uid The user ID given by Firebase
     * @param favoriteID The ID of the position of the favorite
     * @return True on success, false otherwise
     */
    public boolean removeFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM UserFavorites WHERE userUid = '" + uid + "' and favoriteId = '" + favoriteID + "'");
            statement.execute();

            return true;
        } catch (Exception ignored) {}

        return false;
    }

    /**
     * Check if the given favorite is in the database
     * @param uid The user ID given by Firebase
     * @param favoriteID The ID of the position of the favorite
     * @return True if is a favorite, false otherwise
     */
    public boolean isFavorite(String uid, String favoriteID) {
        getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM UserFavorites WHERE userUid = '" + uid + "' and favoriteId = '" + favoriteID + "'");
            statement.execute();
            return statement.getResultSet().next();
        } catch (Exception ignored) {}

        return false;
    }

    /**
     * Get the performances of a user
     * @param uid The user ID given by Firebase
     * @return PerformData
     */
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

    /**
     * Save to the database the performances of a user to restore values after closed the app.
     * @param uid The user ID given by Firebase
     * @param distance The actual distance of the user
     * @param date The actual Date
     * @return True on success, false otherwise
     */
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
