package datenbank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBHandler {
	
	public Connection setConn(){
		Connection conn = null;
		
		try{
		
		Class.forName("org.h2.Driver");
        
        conn = DriverManager.getConnection(
                "jdbc:h2:D:/Programme/H2DB/database", "sa", "");
        
        
		}catch (Exception e) {
            e.printStackTrace();
        }
		return conn;
	}
	
	public double addGuthaben(Connection conn, Double guth, Integer kundenNr){
		double guthaben_neu = 0.00;
		
		try{
			Statement stmt = conn.createStatement();
			String getQuery = "SELECT Guthaben FROM Kunden WHERE KundenNr = "+kundenNr;
			ResultSet selectRS = stmt.executeQuery(getQuery);
			selectRS.next();
			guthaben_neu = selectRS.getDouble(1)+guth;
			
			String addQuery = "UPDATE Kunden SET Guthaben ="+guthaben_neu+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
						
	        
			}catch (Exception e) {
	            e.printStackTrace();
	        }
			return guthaben_neu;
		
	}
	
	

}
