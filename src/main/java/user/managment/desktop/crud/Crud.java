package user.managment.desktop.crud;

import java.sql.SQLException;
import java.util.ArrayList;

//CRUD METHODS
public interface Crud<Entity> {

	boolean deleteEntity(int id) throws SQLException;

	ArrayList<Entity> entities() throws SQLException;

	Entity insertEntity(Entity e) throws SQLException;

	boolean updateEntity(Entity e) throws SQLException;;

}
