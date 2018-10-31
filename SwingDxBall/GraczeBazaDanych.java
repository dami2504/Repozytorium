package Cwiczenia2.Grafika.gra;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class GraczeBazaDanych
{
	private Connection connection;
	private Statement statement;

	public GraczeBazaDanych()
	{
		try
		{
			//wczytywanie sterownika
			//Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); - baza derby apache
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Nie udalo sie wczytac sterownika");
			e.printStackTrace();
		}
		try
		{
			String url="jdbc:oracle:thin:@localhost:1521:xe";
			String usernname = "damian";
			String passwsord = "dsa";
			connection = DriverManager.getConnection(url,usernname,passwsord);

			statement=connection.createStatement();
		}
		catch (SQLException e)
		{
			System.err.println("Nie udalo sie nawiazac polaczenia");
			e.printStackTrace();
		}

	}
	/*
	public boolean tworzenie_struktur()
	{
		String sql = "CREATE TABLE gracze"
				+"("
				+"graczImie varchar(10),"
				+"punkty integer,"
				+"sekundyStoper varchar(10)"
				+")";
		try
		{
			statement.execute(sql);
		}
		catch (SQLException e)
		{
			System.err.println("Nie udalo sie utworzyc struktur");
			e.printStackTrace();
			return false;
		}
		return true;
	}*/

	public boolean dodajGracza(String imie, int wynik, String czas)
	{
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("insert into gracze values (?,?,?)");
			preparedStatement.setString(1, imie);
			preparedStatement.setString(2, Integer.toString(wynik));
			preparedStatement.setString(3, czas);
			preparedStatement.execute();
		}
		catch (SQLException e)
		{
			System.err.println("Nie udalo sie dodac akordu");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//pobieranie listy graczy posortowanych według punktow
	public List<Gracz> pobierzListeGraczy()
	{
		List<Gracz> list = new LinkedList<Gracz>();
		try
		{
			ResultSet resultSet = statement.executeQuery("SELECT * FROM gracze ORDER BY punkty DESC");
			while(resultSet.next())
			{
				String imie = resultSet.getString("graczImie");
				int wynik = resultSet.getInt("punkty");
				String czas = resultSet.getString("sekundyStoper");
				list.add(new Gracz(imie,wynik,czas));
			}

		}
		catch (SQLException e)
		{
			System.err.println("Nie udalo sie pobrac lsity graczy");
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public void closeConnection()
	{
		try
		{

			connection.close();
		}
		catch (SQLException e)
		{
			System.err.println("Nie udalo sie zamknac polaczenia");
			e.printStackTrace();
		}
	}


}
