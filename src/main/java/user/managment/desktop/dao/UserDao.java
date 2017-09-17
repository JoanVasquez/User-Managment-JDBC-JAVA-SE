package user.managment.desktop.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import user.managment.desktop.crud.Crud;
import user.managment.desktop.db.DBConnection;
import user.managment.desktop.db.DBConnectionProperties;
import user.managment.desktop.model.User;
import user.managment.desktop.query.Queries;
import user.managment.desktop.security.AES256;

//DAO CLASS - IMPLEMENT CRUD INTERFACE
public class UserDao extends Queries implements Crud<User> {

	private DBConnection dbConnection; // VAR OF DBConnection TO OPEN THE CONNECTION
	private PreparedStatement prepareStatement;
	private ResultSet resultSet;
	private DBConnectionProperties dbConnectionProperties; // DATABASE PROPERTIES

	// CONSTRUCTOR
	public UserDao() {
		dbConnectionProperties = new DBConnectionProperties();
		dbConnectionProperties.setDriver("com.mysql.jdbc.Driver");// THE DRIVER - MYSQL IN THIS CASE
		dbConnectionProperties.setUrl("jdbc:mysql://localhost:3306/userprocesses");// DATABASE URL
		dbConnectionProperties.setUser("root");// DATABASE USER
		dbConnectionProperties.setPassword("pass22");// DATABASE PASSWORD
		dbConnection = DBConnection.openConnection(dbConnectionProperties);
	}

	// DELETE USER
	@Override
	public boolean deleteEntity(int id) throws SQLException {
		boolean result = false;
		prepareStatement = dbConnection.getConnection().prepareStatement(DELETE_USER);
		prepareStatement.setInt(1, id);

		if (prepareStatement.executeUpdate() > 0)
			result = true;

		prepareStatement.close();

		return result;
	}

	// GET A LIST OF ALL OF THE USERS
	@Override
	public ArrayList<User> entities() throws SQLException {
		ArrayList<User> listUser = new ArrayList<User>();

		prepareStatement = dbConnection.getConnection().prepareStatement(READ_USERS);

		resultSet = prepareStatement.executeQuery();

		while (resultSet.next()) {
			User user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
					resultSet.getString(4), resultSet.getBytes(5), AES256.decryption(resultSet.getString(5)));
			listUser.add(user);
		}
		
		resultSet.close();
		prepareStatement.close();

		return listUser;
	}

	// SAVE USER TO THE DATABASE
	@Override
	public User insertEntity(User e) throws SQLException {
		int userId = 0;
		prepareStatement = dbConnection.getConnection().prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
		prepareStatement.setString(1, e.getFirstName());
		prepareStatement.setString(2, e.getLastName());
		prepareStatement.setString(3, e.getEmail());
		prepareStatement.setBytes(4, e.getPassword());

		if (prepareStatement.executeUpdate() > 0) {
			resultSet = prepareStatement.getGeneratedKeys();
			if(resultSet.next()) {
				userId = resultSet.getInt(1);
				e.setIdUser(userId);
			}
		} else
			e = null;

		resultSet.close();
		prepareStatement.close();

		return e;
	}

	// UPDATE USER
	@Override
	public boolean updateEntity(User e) throws SQLException {
		boolean result = false;
		prepareStatement = dbConnection.getConnection().prepareStatement(UPDATE_USER);
		prepareStatement.setString(1, e.getFirstName());
		prepareStatement.setString(2, e.getLastName());
		prepareStatement.setBytes(3, e.getPassword());
		prepareStatement.setInt(4, e.getIdUser());

		if (prepareStatement.executeUpdate() > 0)
			result = true;

		prepareStatement.close();

		return result;
	}
}
