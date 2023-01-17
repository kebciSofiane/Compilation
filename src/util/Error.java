package util;

public enum Error{
    TYPE(3, "erreur de types"),
    TS(4, "table des symboles");

    private final String message;
    private final int code;
    
    Error(int code, String message){
	this.code = code;
	this.message = message;
    }

    public String toString(){return message;}
    public String message(){return message;}
    public int code(){return code;}
}
