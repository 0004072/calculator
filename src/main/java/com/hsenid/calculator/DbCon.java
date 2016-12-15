package com.hsenid.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that handles the database connection
 * Created by hsenid on 12/15/16.
 */
public class DbCon {
    private final String DB_NAME = "calculator";
    private final String DB_DRV = "com.mysql.cj.jdbc.Driver";
    private final String USERNAME = "root";
    private final String SERVER_URL = "jdbc:mysql://localhost/";
    private final String DB_URL = SERVER_URL.concat(DB_NAME);
    private String PASSWORD;
    private Connection con;
    private Statement stmt;
    private Logger logger;

    public DbCon() {
        this.con = null;
        this.stmt = null;
        //String pass = "Admin123";
        logger = LogManager.getLogger(DbCon.class);
        try {
            //this.PASSWORD = pass;
            Object password = JOptionPane.showInputDialog(null, "Enter your password to the database.", "Connect to the Database", JOptionPane.PLAIN_MESSAGE, null, null, "");
            if (password != null)
                this.PASSWORD = password.toString();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void main(String[] args) {
        DbCon obj = new DbCon();
    }

    public void connect() {
        try {
            Class.forName(DB_DRV);

            this.con = DriverManager.getConnection(this.DB_URL, this.USERNAME, this.PASSWORD);
        } catch (SQLException sqlEx) {
            System.out.println("SQL Error!");
            logger.error(sqlEx);
        } catch (Exception ex) {
            System.out.println("Error!");
            logger.error(ex);
        }
    }

    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            this.stmt = this.con.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqlEx) {
            logger.error(sqlEx);
        }

        return rs;
    }

    public boolean executeUpdate(String sql) {
        boolean success = false;
        try {
            this.stmt = this.con.createStatement();
            this.stmt.executeUpdate(sql);
            success = true;
        } catch (SQLException sqlEx) {
            System.out.print("SQL Error: ");
            logger.error(sqlEx);
        }
        return success;
    }

    public void close() {
        try {
            if (this.con != null)
                con.close();

            if (this.stmt != null)
                this.stmt.close();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public ArrayList<Map<String, String>> traverseResultSet(ResultSet rs) {
        ArrayList<Map<String, String>> rsTraversed = new ArrayList<>();
        List<String> rsColumnNames = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int rsColumnCount = rsmd.getColumnCount();

            for (int i = 1; i <= rsColumnCount; i++) {
                rsColumnNames.add(rsmd.getColumnName(i));
            }

            while (rs.next()) {
                Map<String, String> tuple = new HashMap<>();
                for (int i = 0; i < rsColumnNames.size(); i++) {
                    String columnName = rsColumnNames.get(i);
                    String value = "";
                    switch (rsmd.getColumnType(i + 1)) {
                        case Types.VARCHAR:
                            value = rs.getString(columnName);
                            break;

                        case Types.DECIMAL:
                            value = String.valueOf(rs.getDouble(columnName));
                            break;

                        case Types.INTEGER:
                            value = String.valueOf(rs.getInt(columnName));
                            break;
                    }

                    tuple.put(columnName, value);
                }
                rsTraversed.add(tuple);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return rsTraversed;
    }
}
