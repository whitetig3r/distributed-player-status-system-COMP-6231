package dpss;

public class Player {
	private String fName, lName, uName, password, ipAddress;
	private int age;
	
	public Player(String fName, String lName, String uName, String password, String ipAddress, int age) {
		this.fName = fName;
		this.lName = lName;
		this.uName = uName;
		this.password = password;
		this.ipAddress = ipAddress;
		this.age = age;
	}
	
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String passWord) {
		this.password = passWord;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
