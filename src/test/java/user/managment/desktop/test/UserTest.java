package user.managment.desktop.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import user.managment.desktop.controller.UserController;
import user.managment.desktop.db.DBConnection;
import user.managment.desktop.db.DBConnectionProperties;
import user.managment.desktop.model.User;
import user.managment.desktop.security.AES256;

//USER TEST CLASS
public class UserTest {

	private static UserController userController;// USER DAO
	private static User user;// USER ENTITY

	private DBConnection dbConnection;// DATABASE CONNECTION
	private PreparedStatement prepareStatement;
	private ResultSet resultSet;
	private DBConnectionProperties dbConnectionProperties;
	private int idUser = 0;// USER ID

	@BeforeClass
	public static void setUpClass() {
		user = new User();
	}

	@Test // SAVE USER TEST
	public void test1() throws SQLException {
		userController = new UserController();
		user.setEmail("yoloprogramo22@gmail.com");
		user.setFirstName("Test insert");
		user.setLastName("Test insert");
		user.setPassword(AES256.encryption("pass"));
		Assert.assertNotEquals(null, userController.saveUser(user));
	}

	@Before // GET AN USER ID
	public void test2() throws SQLException {
		dbConnectionProperties = new DBConnectionProperties();
		dbConnectionProperties.setDriver("com.mysql.jdbc.Driver");
		dbConnectionProperties.setUrl("jdbc:mysql://localhost:3306/userprocesses");
		dbConnectionProperties.setUser("root");
		dbConnectionProperties.setPassword("pass22");
		dbConnection = DBConnection.openConnection(dbConnectionProperties);

		prepareStatement = dbConnection.getConnection().prepareStatement("SELECT iduser FROM user LIMIT 1");
		resultSet = prepareStatement.executeQuery();
		if (resultSet.next())
			idUser = resultSet.getInt(1);

		resultSet.close();
		prepareStatement.close();
		dbConnection.closeConnection();
	}

	@Test // UPDATE USER TEST
	public void test3() throws SQLException {
		if (idUser > 0) {
			userController = new UserController();
			user.setIdUser(idUser);
			user.setFirstName("Test update");
			user.setLastName("Test update");
			user.setPassword(AES256.encryption("pass"));
			Assert.assertTrue(userController.updateUser(user));
		}
	}

	@Test // GET ALL OF THE USER TEST
	public void test4() throws SQLException {
		userController = new UserController();
		Assert.assertNotEquals(0, userController.readUser());
	}

	@Test // DELETE USER TEST
	public void test7() throws SQLException {
		if (idUser > 0) {
			userController = new UserController();
			Assert.assertTrue(userController.deleteUser(idUser));
		}

	}
}
