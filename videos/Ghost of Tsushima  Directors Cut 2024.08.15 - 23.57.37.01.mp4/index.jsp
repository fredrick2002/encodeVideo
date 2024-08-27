<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>List Users</title>
</head>
<body>
    <h1>User List</h1>
    
    <%
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Load the database driver (for MySQL)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish the connection (replace with your database URL, user, and password)
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "root", "password");
            
            // Create a statement
            stmt = conn.createStatement();
            
            // Execute a query
            String sql = "SELECT id, name, email FROM users";
            rs = stmt.executeQuery(sql);
            
            // Display the results
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th></tr>");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    %>
</body>
</html>
