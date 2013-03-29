package brick;

@SuppressWarnings("serial")
public class MalformedLDrawException extends Exception {
	public MalformedLDrawException() {
		super("LDraw error, but no line text available");
	}
		
	public MalformedLDrawException(String line) {
		super("LDraw line '" + line + "' has errors");
	}
}
