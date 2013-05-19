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
                "jdbc:h2:datenbank", "sa", "");
        
        
		}catch (Exception e) {
            e.printStackTrace();
        }
		return conn;
	}
	
	public double addGuthaben(Connection conn, double guth, Integer kundenNr){
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
	
	public boolean payBill(Connection conn, double bill, Integer kundenNr){
		Boolean fin = false;
		
		try{
			Statement stmt = conn.createStatement();
			String getQuery = "SELECT Guthaben FROM Kunden WHERE KundenNr = "+kundenNr;
			ResultSet selectRS = stmt.executeQuery(getQuery);
			selectRS.next();
			double guthaben_neu = selectRS.getDouble(1)-bill;
			
			String addQuery = "UPDATE Kunden SET Guthaben ="+guthaben_neu+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
			fin = true;
		}catch (Exception e) {
            e.printStackTrace();
        }
		
		return fin;
				
	}
	
	public double getGuthaben(Connection conn, Integer kundenNr){
		double guthaben = 0.00;
		
		try{
			Statement stmt = conn.createStatement();
			String getQuery = "SELECT Guthaben FROM Kunden WHERE KundenNr = "+kundenNr;
			ResultSet selectRS = stmt.executeQuery(getQuery);
			selectRS.next();
			guthaben = selectRS.getDouble(1);
			
		}catch (Exception e) {
            e.printStackTrace();
        }
		
		return guthaben;
				
	}
	
	public boolean setGuthaben(Connection conn, double guth, Integer kundenNr){
		boolean fin = false;;
		
		try{
			Statement stmt = conn.createStatement();
		
			String addQuery = "UPDATE Kunden SET Guthaben ="+guth+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
			fin = true;			
	        
			}catch (Exception e) {
	            e.printStackTrace();
	        }
		
			return fin;
		
	}
	
	public boolean setName(Connection conn, String name, Integer kundenNr){
		boolean fin = false;;
		
		try{
			Statement stmt = conn.createStatement();
		
			String addQuery = "UPDATE Kunden SET Name ="+name+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
			fin = true;			
	        
			}catch (Exception e) {
	            e.printStackTrace();
	        }
		
			return fin;
		
	}
	
	public boolean setVorname(Connection conn, String vorname, Integer kundenNr){
		boolean fin = false;;
		
		try{
			Statement stmt = conn.createStatement();
		
			String addQuery = "UPDATE Kunden SET Vorname ="+vorname+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
			fin = true;			
	        
			}catch (Exception e) {
	            e.printStackTrace();
	        }
		
			return fin;
		
	}
	
	public boolean setStr(Connection conn, String str, Integer kundenNr){
		boolean fin = false;;
		
		try{
			Statement stmt = conn.createStatement();
		
			String addQuery = "UPDATE Kunden SET Str ="+str+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
			fin = true;			
	        
			}catch (Exception e) {
	            e.printStackTrace();
	        }
		
			return fin;
		
	}

	public boolean setPlz(Connection conn, Integer plz, Integer kundenNr){
		boolean fin = false;;
		
		try{
			Statement stmt = conn.createStatement();
		
			String addQuery = "UPDATE Kunden SET Plz ="+plz+" WHERE KundenNR = kundenNr";
			stmt.executeUpdate(addQuery);
			
			fin = true;			
	        
			}catch (Exception e) {
	            e.printStackTrace();
	        }
		
			return fin;
		
	}
	
}
