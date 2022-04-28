package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;

public class Program {

	public static void main(String[] args) {

		Connection connection = null;
		PreparedStatement preparedStatement;

		try {

			connection = DB.getConnection();
			preparedStatement = connection.prepareStatement("select * from seller");

			ResultSet rows = preparedStatement.executeQuery();

			while (rows.next()) {
				System.out.print(rows.getInt("Id") + " - ");
				System.out.println(rows.getString("Name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
