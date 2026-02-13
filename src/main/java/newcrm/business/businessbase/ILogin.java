package newcrm.business.businessbase;

public interface ILogin {
	default public void inputUserName(String username) {
		System.out.println(username);
	};
	public void inputPassWord(String password);
	public void submit();
}
