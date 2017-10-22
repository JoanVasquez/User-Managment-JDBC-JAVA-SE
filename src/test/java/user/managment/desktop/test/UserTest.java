package user.managment.desktop.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import user.managment.desktop.controller.UserController;
import user.managment.desktop.db.DBConnection;
import user.managment.desktop.db.DBConnectionProperties;
import user.managment.desktop.model.User;
import user.managment.desktop.security.AES256;
import user.managment.desktop.views.Desktop;

//USER TEST CLASS
public class UserTest {

	private static UserController userController;// USER DAO
	private static User user;// USER ENTITY
	private static DBConnection dbConnection;// DATABASE CONNECTION
	private static DBConnectionProperties dbConnectionProperties;
	
	@BeforeClass
	public static void setUpClass() {
		Desktop.isTest = true;
		dbConnectionProperties = new DBConnectionProperties();
		dbConnectionProperties.setDriver("com.mysql.jdbc.Driver");// THE DRIVER - MYSQL IN THIS CASE
		dbConnectionProperties.setUrl("jdbc:mysql://localhost:3306/userprocesses_test");// DATABASE URL
		dbConnectionProperties.setUser("root");// DATABASE USER
		dbConnectionProperties.setPassword("pass");// DATABASE PASSWORD
		dbConnection = DBConnection.openConnection(dbConnectionProperties);
		try {
			dbConnection.getConnection().setAutoCommit(false);
		}catch(Exception e) {
			System.err.println("Error :: " + e.getMessage());
		}
		user = new User();
		userController = new UserController();
	}
	
	@Test
	public void testUserController() {
		user.setFirstName("Test");
		user.setLastName("Test");
		user.setEmail("test@test.com");
		user.setPassword(AES256.encryption("123"));
		
		try {
			user = userController.saveUser(user);
			Assert.assertNotEquals(0, user.getIdUser());
			user.setFirstName("update test");
			boolean result = userController.updateUser(user);
			Assert.assertTrue(result);
			
			List<User> users = userController.readUser();
			Assert.assertNotEquals(0, users.size());
			
			result = userController.deleteUser(user.getIdUser());
			Assert.assertTrue(result);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void endTest() {
		try {
			dbConnection.getConnection().rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBConnection.closeConnection();
		}
	}
}
