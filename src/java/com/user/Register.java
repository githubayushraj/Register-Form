package com.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@MultipartConfig
public class Register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String name = request.getParameter("user_name");
            String email = request.getParameter("user_email");
            String password = request.getParameter("user_password");

            Part filePart = request.getPart("user_img");
            String fileName = filePart.getSubmittedFileName();

            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/register_form";
            String username = "root";
            String sqlpassword = "Ayush@123#";

            try {
                // Simulate a delay (remove in production)
                Thread.sleep(3000);

                Class.forName("com.mysql.cj.jdbc.Driver"); // Use the correct driver class
                Connection conn = DriverManager.getConnection(url, username, sqlpassword);

                // Insert user data
                String sqlQuery = "INSERT INTO register_form(name, email, password,image_filename) VALUES (?, ?, ?, ?)";
                try (PreparedStatement prestmt = conn.prepareStatement(sqlQuery)) {
                    prestmt.setString(1, name);
                    prestmt.setString(2, email);
                    prestmt.setString(3, password);
                    prestmt.setString(4, fileName);

                    InputStream inputsteam = filePart.getInputStream();
                    byte[] bytedata = new byte[inputsteam.available()];
                    inputsteam.read(bytedata);
                    // it read data and send into bytedata byte array
                    String path = request.getRealPath("/") + "img" + File.separator + fileName;

                    try (FileOutputStream fos = new FileOutputStream(path)) {
                        fos.write(bytedata);
                    }
                    prestmt.executeUpdate();
                    out.println("successful");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException | InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
