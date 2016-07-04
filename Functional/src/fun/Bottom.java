package fun;

@SuppressWarnings("serial")
public class Bottom extends RuntimeException {

	public static <T> T error(String message) {
		throw new RuntimeException(message);
	}

	public static <T> T undefined() {
		return error("undefined");
	}

	private Bottom(String message) {
		throw new RuntimeException(message);
	}

}
