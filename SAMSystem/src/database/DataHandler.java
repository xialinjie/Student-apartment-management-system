package database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package gui_tutorial;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.category.DefaultCategoryDataset;

public class DataHandler {
// DB details
    //private static final String dbURL = "jdbc:ucanaccess://Database1.accdb;sysSchema=true";

    private static final String dbURL = "jdbc:ucanaccess://sams.accdb;sysSchema=true";
    private static java.sql.Connection con;
    private static java.sql.Statement stm;
    private static java.sql.ResultSet rs;
    private static java.sql.ResultSetMetaData rsMeta;
    private static int columnCount;
    private static java.sql.PreparedStatement pstm;

    public static Vector<String> getTables() {
        Vector<String> l = new Vector<>();
        /*l.add("Employee");
        l.add("Dependant");
        l.add("Department");
        l.add("Project");
        l.add("Workson");*/
        String sqlQuery = "SELECT Name FROM sys.MSysObjects WHERE Type=1 AND Flags=0";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            stm = con.createStatement();
            rs = stm.executeQuery(sqlQuery);
            while (rs.next()) {
                // each row is an array of objects
                l.add((String) rs.getObject(1));
            }
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        } finally {
            try {
                if (null != con) {
                    // cleanup resources, once after processing
                    rs.close();
                    stm.close();
                    // and then finally close connection
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l;
    }

    public static void searchRecords(String table) {
        String sqlQuery = "SELECT * FROM " + table;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            stm = con.createStatement(
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sqlQuery);
            rsMeta = rs.getMetaData();
            columnCount = rsMeta.getColumnCount();
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static Object[] getTitles(String table) {
        Object[] columnNames = new Object[columnCount];
        try {
            for (int col = columnCount; col > 0; col--) {
                columnNames[col - 1]
                        = rsMeta.getColumnName(col);
            }
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } finally {
            try {
                if (null != con) {
                    // cleanup resources, once after processing
                    rs.close();
                    stm.close();
                    // and then finally close connection
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return columnNames;
    }

    public static Object[][] getRows(String table) {
        searchRecords(table);
        Object[][] content;
        try {
// determine the number of rows
            rs.last();
            int number = rs.getRow();
            content = new Object[number][columnCount];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
// each row is an array of objects
                for (int col = 1; col <= columnCount; col++) {
                    content[i][col - 1] = rs.getObject(col);
                }
                i++;
            }
            return content;
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        }
        return null;
    }

    public static void insertInToMusic(String mName, String mLink) {
//        String sqlInsert = "INSERT INTO music (m_name, m_link) VALUES (" + mName + "," + mLink + ")";
        String sqlInsert = "INSERT INTO music (m_name, m_link) VALUES (?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
////            stm = con.createStatement();
//            stm = con.createStatement(
//                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    java.sql.ResultSet.CONCUR_READ_ONLY);
//            int rows = stm.executeUpdate(mLink);

            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, mName);
            pstm.setObject(2, mLink);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static void deleteMusic(String deleteID) {

        String sqlInsert = "DELETE FROM music WHERE m_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, deleteID);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static void uppdateMusic(String updateID, String mName, String mLink) {
        String sqlInsert = "UPDATE music SET m_name = ?, m_link = ? WHERE m_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, mName);
            pstm.setObject(2, mLink);
            pstm.setObject(3, updateID);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static void insertInToClock(Integer available, String clockTime) {

        String sqlInsert = "INSERT INTO clock (available, clock_time) VALUES (?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, available);
            pstm.setObject(2, clockTime);
            pstm.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static DefaultCategoryDataset createDataset(String timeStamp) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //-----------------------------------
        //String sqlQuery = "SELECT level FROM sleep_sound where time_stamp like ?";
        String sqlQuery = "SELECT sleep_level,time_stamp FROM sleep_sound where time_stamp like ?";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            pstm.setObject(1, timeStamp + "%");
            rs = pstm.executeQuery();
//            stm = con.createStatement(
//                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    java.sql.ResultSet.CONCUR_READ_ONLY);
//            rs = stm.executeQuery(sqlQuery);
//            rsMeta = rs.getMetaData();
//            columnCount = rsMeta.getColumnCount();
            int count = 0;
            while (rs.next()) {
                count++;
                //System.out.println(rs.getString(2));
                dataset.addValue(rs.getInt(1), timeStamp, "" + count);

            }
//            for (int i = 0; i < columnCount; i++) {
//                rs.get
//                System.out.println(rsMeta.getColumnName(i));
//            }
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
        //------------------------------------

        return dataset;
    }

    //********************************************************************************
    public static int checkLogin(String username, String password) {

        int result = -1;
        String sqlQuery = "SELECT u_id FROM user_info WHERE u_name = ? and u_password = ?";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            pstm.setObject(1, username);
            pstm.setObject(2, password);
            rs = pstm.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);

            }

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

        return result;
    }

    //********************************************************************************
    public static int checkUsernameLogin(String username) {

        int result = -1;
        String sqlQuery = "SELECT u_id FROM user_info WHERE u_name = ?";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            pstm.setObject(1, username);
            rs = pstm.executeQuery();
            while (rs.next()) {
                result = 1;
            }

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

        return result;
    }

    //********************************************************************************
    public static int signup(String username, String password) {
        int result = -1;

        String sqlInsert = "INSERT INTO user_info (u_name, u_password) VALUES (?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, username);
            pstm.setObject(2, password);

            pstm.executeUpdate();
            rs = pstm.getGeneratedKeys();
            while (rs.next()) {
                result = rs.getInt(1);
                System.out.println("getGeneratedKeys:" + result);
            }
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
        return result;
    }

    //********************************************************************************
    public static int checkRoomAvailability(int type) {

        int result = -1;
        int num = 1;
        String sqlQuery = "SELECT room_number FROM room WHERE availability = ? and type = ? LIMIT ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            pstm.setObject(1, num);
            pstm.setObject(2, type);
            pstm.setObject(3, num);
            rs = pstm.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

        return result;
    }

    //********************************************************************************
    public static void bookRoom(int uidPage, int roomNumber) {
        bookRoomUpdateUserinfo(uidPage, roomNumber);
        updateRoom(0, roomNumber);
    }

    public static void bookRoomUpdateUserinfo(int uidPage, int roomNumber) {
        String sqlInsert = "UPDATE user_info SET room_number = ? WHERE u_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, roomNumber);
            pstm.setObject(2, uidPage);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    public static void updateRoom(int availability, int roomNumber) {
        String sqlInsert = "UPDATE room SET availability = ? WHERE room_number = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, availability);
            pstm.setObject(2, roomNumber);

            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void byGoods(Integer goodsid, Integer userid) {
        Date date = new Date();

        String sqlInsert = "INSERT INTO sale (g_id, u_id, sale_date) VALUES (?, ?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, goodsid);
            pstm.setObject(2, userid);
            pstm.setObject(3, date);
            pstm.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void submitComment(String comment, Integer userid) {
        Date date = new Date();

        String sqlInsert = "INSERT INTO comment (u_id, message, submit_date) VALUES (?, ?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, userid);
            pstm.setObject(2, comment);
            pstm.setObject(3, date);
            pstm.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static List getRoomNumberByUid(Integer uidPage) {
        List<Object> lst = new ArrayList<>();
        lst.add("");
        lst.add("");
        System.out.println(uidPage + "502");

        String sqlQuery = "select u.room_number, rt.description from user_info u, room r, room_type rt "
                + "where u.u_id = ? and u.room_number = r.room_number and r.type = rt.type;";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            pstm.setObject(1, uidPage);

            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println("````````````" + rs.getInt(1));
                lst.set(0, rs.getInt(1));
                if (rs.getString(2) != null && rs.getString(2) != "") {
                    lst.set(1, rs.getString(2));
                }

                System.out.println("517");
            }

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

        return lst;
    }

    //********************************************************************************
    public static void checkOut(int uid) {
        String sqlInsert = "UPDATE user_info SET room_number = ? WHERE u_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            System.out.println("542");
            pstm.setObject(1, 0);
            pstm.setObject(2, uid);
            System.out.println("542");
            pstm.executeUpdate();
            System.out.println("542");
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static String adminLogin(String username, String password) {

        String result = "";
        String sqlQuery = "SELECT level FROM admin WHERE username = ? and password = ?";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            pstm.setObject(1, username);
            pstm.setObject(2, password);
            rs = pstm.executeQuery();
            while (rs.next()) {
                result = rs.getString(1);

            }

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

        return result;
    }

    //********************************************************************************
    public static void insertUser(String username, String password, String roomnumber) {

        String sqlInsert = "INSERT INTO user_info (u_name, u_password, room_number) VALUES (?, ?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, username);
            pstm.setObject(2, password);
            pstm.setObject(3, roomnumber);

            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

    }
    //********************************************************************************

    public static void updateUsser(String uid, String username, String password, String roomnumber) {
        String sqlInsert = "UPDATE user_info SET u_name = ?, u_password = ?, room_number = ? WHERE u_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, username);
            pstm.setObject(2, password);
            pstm.setObject(3, roomnumber);
            pstm.setObject(4, uid);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void deleteUserById(String deleteID) {

        String sqlInsert = "DELETE FROM user_info WHERE u_id = ?";
        System.out.println(sqlInsert);
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, deleteID);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void insertRoom(String roomnumber, String type, String availablility) {

        String sqlInsert = "INSERT INTO room (room_number, type, availability) VALUES (?, ?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, roomnumber);
            pstm.setObject(2, type);
            pstm.setObject(3, availablility);

            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

    }
    //********************************************************************************

    public static void updateRoom(String rid, String roomnumber, String type, String availablility) {
        String sqlInsert = "UPDATE room SET room_number = ?, type = ?, availability = ? WHERE r_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, roomnumber);
            pstm.setObject(2, type);
            pstm.setObject(3, availablility);
            pstm.setObject(4, rid);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void deleteRoomById(String deleteID) {

        String sqlInsert = "DELETE FROM room WHERE r_id = ?";
        System.out.println(sqlInsert);
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, deleteID);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void insertAdmin(String username, String password, String level) {

        String sqlInsert = "INSERT INTO admin (username, password, level) VALUES (?, ?, ?)";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, username);
            pstm.setObject(2, password);
            pstm.setObject(3, level);

            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }

    }
    //********************************************************************************

    public static void updateAdmin(String aid, String username, String password, String roomnumber) {
        String sqlInsert = "UPDATE admin SET username = ?, password = ?, level = ? WHERE a_id = ?";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, username);
            pstm.setObject(2, password);
            pstm.setObject(3, roomnumber);
            pstm.setObject(4, aid);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void deleteAdminById(String deleteID) {

        String sqlInsert = "DELETE FROM admin WHERE a_id = ?";
        System.out.println(sqlInsert);
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);
            pstm.setObject(1, deleteID);
            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static void updateComment(String cid) {
        String sqlInsert = "UPDATE comment SET finish = ? WHERE c_id = ?";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlInsert);

            pstm.setObject(1, "Yes");

            pstm.setObject(2, cid);

            pstm.executeUpdate();

        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
    }

    //********************************************************************************
    public static DefaultCategoryDataset ueryGoodssDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //-----------------------------------
        //String sqlQuery = "SELECT level FROM sleep_sound where time_stamp like ?";
        String sqlQuery = "SELECT g.g_id, g.name, COUNT(s.g_id) AS sales_count\n"
                + "FROM (sale AS s INNER JOIN goods AS g ON s.g_id = g.g_id)\n"
                + "GROUP BY g.g_id, g.name;";

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection(dbURL, "", "");
            pstm = con.prepareStatement(sqlQuery);
            
            rs = pstm.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                //System.out.println(rs.getString(2));
                dataset.addValue(rs.getInt(3), rs.getString(2), " ");

            }
//            for (int i = 0; i < columnCount; i++) {
//                rs.get
//                System.out.println(rsMeta.getColumnName(i));
//            }
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Issue with the JDBC driver.");
            System.exit(1); // terminate program - cannot recover
        } catch (java.sql.SQLException sqlex) {
            System.err.println(sqlex);
        } catch (Exception ex) {
            System.err.println(ex);
            //ex.printStackTrace();
        }
        //------------------------------------

        return dataset;
    }

}
