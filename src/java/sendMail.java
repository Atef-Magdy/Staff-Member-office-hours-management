/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Atef Magdy
 */
@WebServlet(urlPatterns = {"/sendMail"})
public class sendMail extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            /// Get data from ajax
            String userName = request.getParameter("userName");
            String reciever = request.getParameter("email");
            String content = request.getParameter("content");
            String subject = "";
            
            /// Check mail type
            if(content.equals("sendPass"))
            {
                Connection Con = null; 
                PreparedStatement stmt = null;
                String query = null;
                ResultSet rs = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://localhost:3306/staffmanager";
                    String user = "root";
                    String Password = "hema@1234";
                    Con = DriverManager.getConnection(url, user, Password);
                    query = "select password from user where userName=?";
                    stmt = Con.prepareStatement(query);
                    stmt.setString(1, userName);
                    rs = stmt.executeQuery();
                    
                    if (rs.next()){
                        String pass = rs.getString(1);
                        content = "Thanks for register in Staff Manger\nYour Password is : "+pass+"\n use it to login";
                        subject = "Your Mail Password";
                    }
                }
                catch (SQLException ex) {
                     Logger.getLogger(sendMail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (content.equals("sendMasg"))
            {
                Connection Con = null; 
                PreparedStatement stmt = null;
                String query = null;
                ResultSet rs = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://localhost:3306/staffmanager";
                    String user = "root";
                    String Password = "hema@1234";
                    Con = DriverManager.getConnection(url, user, Password);
                    query = "select email from user where userName=?";
                    stmt = Con.prepareStatement(query);
                    stmt.setString(1, reciever);
                    rs = stmt.executeQuery();
                    if (rs.next()){
                        String result = rs.getString(1);
                        reciever = result;
                        content = "You recieved a mesage from : "+userName + "\nOpen Staff Manger to see it";
                        subject = "Message Notification";
                    }
                }
                catch (SQLException ex) {
                     Logger.getLogger(sendMail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /// The Sender Email And Passord
            String senderEmail = "noo6670@gmail.com";
            String senderPassword = "atef35420751";

            Properties props = System.getProperties();
            String host = "smtp.gmail.com";
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", senderEmail);
            props.put("mail.smtp.password", senderPassword);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress(senderEmail));
                InternetAddress toAddress = new InternetAddress(reciever);
                message.addRecipient(Message.RecipientType.TO, toAddress);

                /// Message Content
                message.setSubject(subject);
                message.setText(content);

                Transport transport;
                transport = session.getTransport("smtp");
                transport.connect(host, senderEmail, senderPassword);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                out.print("{\"success\": 0}");
            }
            catch (AddressException ae) {
                out.print("{\"success\": 3}");
            } catch (MessagingException ex) {
                out.print(ex);
                out.print("{\"success\": 4}");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(sendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
