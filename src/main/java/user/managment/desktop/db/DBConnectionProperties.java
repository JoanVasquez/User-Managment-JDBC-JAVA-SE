package user.managment.desktop.db;

public class DBConnectionProperties {
	private String driver;
	private String user;
	private String password;
	private String url;

	public DBConnectionProperties() {
	}

	public String getDriver() {
		return driver;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String db) {
		this.url = db;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
