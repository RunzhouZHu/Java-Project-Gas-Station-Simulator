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
                        "INSERT INTO customer (id, arrive_time, " +
                                "line_refuel_time, enter_refuel_time, exit_refuel_time," +
                                "line_wash_time, enter_wash_time, exit_wash_time," +
                                "line_shop_time, enter_shop_time, exit_shop_time," +
                                "line_drying_time, enter_drying_time, exit_drying_time," +
                                "line_cash_time, enter_cash_time, exit_cash_time) VALUES (? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, customer.getId());
                stmt.setDouble(2, customer.getArriveTime());
                stmt.setDouble(3, customer.getLineRefuelTime());
                stmt.setDouble(4, customer.getEnterRefuelTime());
                stmt.setDouble(5, customer.getExitRefuelTime());
                stmt.setDouble(6, customer.getLineWashTime());
                stmt.setDouble(7, customer.getEnterWashingTime());
                stmt.setDouble(8, customer.getExitWashingTime());
                stmt.setDouble(9, customer.getLineShopTime());
                stmt.setDouble(10, customer.getEnterShopTime());
                stmt.setDouble(11, customer.getExitShopTime());
                stmt.setDouble(12, customer.getLineDryingTime());
                stmt.setDouble(13, customer.getEnterDryingTime());
                stmt.setDouble(14, customer.getExitDryingTime());
                stmt.setDouble(15, customer.getLineCashTime());
                stmt.setDouble(16, customer.getEnterCashTime());
                stmt.setDouble(17, customer.getExitCashTime());
                stmt.executeUpdate();
            }
            System.out.println("!!!!!!!!Customers persisted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}