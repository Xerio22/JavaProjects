package models;

public class ObjectWithMessage <T> {
	private T obj;
	private String message;
	
	public ObjectWithMessage(T obj, String message) {
		this.obj = obj;
		this.message = message;
	}

	public T getObj() {
		return obj;
	}

	public String getMessage() {
		return message;
	}
}
