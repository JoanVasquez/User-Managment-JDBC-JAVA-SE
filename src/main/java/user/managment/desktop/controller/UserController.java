package user.managment.desktop.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import user.managment.desktop.dao.UserDao;
import user.managment.desktop.model.User;

public class UserController {

	private UserDao userDao;

	public UserController() {
		userDao = new UserDao();
	}
	
	public boolean deleteUser(int userId) throws SQLException {
		return userDao.deleteEntity(userId);
	}
	
	public ArrayList<User> readUser() throws SQLException {
		return userDao.entities();
	}
	
	public User saveUser(User user) throws SQLException {
		return userDao.insertEntity(user);
	}

	public boolean updateUser(User user) throws SQLException {
		return userDao.updateEntity(user);
	}

}
