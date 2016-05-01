import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAccess {
    private ResultSet resultSet;
    
    public String processQuery(String query) throws ClassNotFoundException {
        StringBuilder row = new StringBuilder(); 
        String result = null;
        
        // Load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");
        
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:db/oscar-movie_imdb.sqlite");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 seconds
            
            // Process query and get result
            resultSet = statement.executeQuery(query);
            
            // For each row in result table
            while(resultSet.next()) {
                // Concat the values in each column into one string using StringBuilder
                for (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++) {
                    row.append(resultSet.getString(i) + " ");
                }
                result = row.toString();     // save row
                row.delete(0, row.length()); // clear StringBuilder b4 going to next row
            }
        }
        catch(SQLException e) {  
            System.out.println("Error5: Uh oh, there was a problem while processing the sql statement. Check input query or try a different query format.");
        }
        finally {
            try { // Close db if still open
                if(connection != null) { 
                    connection.close();
                }
            }
            catch(SQLException e) {
                System.out.println("Error6: Unable to close database.");
            }
        }
        return result;
    }
}
