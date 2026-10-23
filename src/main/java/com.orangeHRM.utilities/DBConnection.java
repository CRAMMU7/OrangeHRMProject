package com.orangeHRM.utilities;

import com.orangeHRM.base.BaseClass;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static final Logger logger = BaseClass.Logger;

    public static Connection getDBConnection() {
        try {
            logger.info("Starting DB connection...");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("DB connection established");
            return conn;
        } catch (SQLException e) {
            logger.info("Error establishing the DB Connection.");
            e.printStackTrace();
            return null;
        }
    }

    //Get the employee details from DB and store in a map
    public static Map<String, String> getEmployeeDetails(String employee_id) {
        String query = "SELECT emp_firstname, emp_middle_name, emp_lastname FROM hs_hr_employee WHERE employee_id ="
                + employee_id;
        Map<String, String> employeeDetails = new HashMap<>();
        try (
                Connection conn = getDBConnection();
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);
        ) {
            logger.info("Executiong query: " + query);
            if (rs.next()) {
                String firstName = rs.getString("emp_firstname");
                String middleName = rs.getString("emp_middle_name");
                String lastName = rs.getString("emp_lastname");


                // Store in a map
                employeeDetails.put("firstName", firstName);
                employeeDetails.put("middleName", middleName != null ? middleName : "");
                employeeDetails.put("lastName", lastName);

                logger.info("Query Executed Successfully");
                logger.info("Employee Data Fetched: " + employeeDetails);

            } else {
                logger.error("No employee data found");
            }
        } catch (Exception e) {
            logger.info("Error executing query: " + query);
            e.printStackTrace();
        }
        return employeeDetails;
    }
}

