package Client;
import java.sql.*;
public class Authentication{

	final String DATABASE_URL = "jdbc:mysql://localhost/mail_db";
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private Statement statement;
	private String[] contacts;
	private String[] arr;

	Authentication(){
		connect();
	}
	
	public void connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(DATABASE_URL, "mail_login", "123456");
			pst=con.prepareStatement("SELECT * FROM users WHERE user_name=? and password=?");
			
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT * FROM users");
			ResultSetMetaData metaData = rs.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			System.out.println( "Authors Table of Books Database:\n" );
			//rs.getString(i);
			for ( int i = 1; i <= numberOfColumns; i++ )
				System.out.printf( "%-17s\t", metaData.getColumnName( i ) );
			System.out.println();
			
			while(rs.next())
			{
				for ( int i = 1; i <= numberOfColumns; i++ )
					System.out.printf( "%-17s\t", rs.getObject( i ) );
				System.out.println();
			} // end while
		}
		catch( SQLException sqlException )
		{
			sqlException.printStackTrace();
		} // end catch
		catch(Exception e){
			System.out.println("catch");
			System.out.println(e);
		}
	}
	
	public void closeConnection(){
		try
		{
			rs.close();
			statement.close();
			con.close();
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	public boolean checkLogin(String uname, String pass){
		try{
			pst.setString(1, uname);
			pst.setString(2, pass);
			
			rs=pst.executeQuery();
			if(rs.next()){
				return true;
			}
			else
				return false;
		}catch(Exception e){
			System.out.println("Error while walidating "+e);
			return false;
		}
	}
}