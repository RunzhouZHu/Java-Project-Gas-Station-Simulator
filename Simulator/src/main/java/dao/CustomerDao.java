package dao;

import datasource.MariaDbConnection;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao {
    public void persist(ArrayList<Customer> customerResults) {
        Connection conn = MariaDbConnection.getConnection();
        try {
            for (Customer customer : customerResults) {
                String sql =
                        "INSERT INTO customer (arrive_time, " +
                                "line_refuel_time, enter_refuel_time, exit_refuel_time," +
                                "line_wash_time, enter_wash_time, exit_wash_time," +
                                "line_shop_time, enter_shop_time, exit_shop_time," +
                                "line_drying_time, enter_drying_time, exit_drying_time," +
                                "line_cash_time, enter_cash_time, exit_cash_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, customer.getArriveTime());
                stmt.setDouble(2, customer.getLineRefuelTime());
                stmt.setDouble(3, customer.getEnterRefuelTime());
                stmt.setDouble(4, customer.getExitRefuelTime());
                stmt.setDouble(5, customer.getLineWashTime());
                stmt.setDouble(6, customer.getEnterWashingTime());
                stmt.setDouble(7, customer.getExitWashingTime());
                stmt.setDouble(8, customer.getLineShopTime());
                stmt.setDouble(9, customer.getEnterShopTime());
                stmt.setDouble(10, customer.getExitShopTime());
                stmt.setDouble(11, customer.getLineDryingTime());
                stmt.setDouble(12, customer.getEnterDryingTime());
                stmt.setDouble(13, customer.getExitDryingTime());
                stmt.setDouble(14, customer.getLineCashTime());
                stmt.setDouble(15, customer.getEnterCashTime());
                stmt.setDouble(16, customer.getExitCashTime());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}