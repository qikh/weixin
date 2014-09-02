package weixin.utils;
import static spark.Spark.*;

public class Http {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int PARTIAL_INFO = 203;
    public static final int NO_RESPONSE = 204;
    public static final int MOVED = 301;
    public static final int FOUND = 302;
    public static final int METHOD = 303;
    public static final int NOT_MODIFIED = 304;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int PAYMENT_REQUIRED = 402;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int INTERNAL_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int OVERLOADED = 502;
    public static final int GATEWAY_TIMEOUT = 503;

	public static void created() {
		halt(CREATED);
	}
	
	public static void created(String message) {
		halt(CREATED, message);
	}
	
	public static void badrequest() {
		halt(BAD_REQUEST);
	}
	
	public static void badrequest(String message) {
		halt(BAD_REQUEST, message);
	}
	
	public static void notfound() {
		halt(NOT_FOUND);
	}
	
	public static void notfound(String message) {
		halt(NOT_FOUND, message);
	}
	
	public static void notmodified() {
		halt(NOT_MODIFIED);
	}
	
	public static void notmodified(String message) {
		halt(NOT_MODIFIED, message);
	}
	
	public static void conflict() {
		halt(CONFLICT);
	}
	
	public static void conflict(String message) {
		halt(CONFLICT, message);
	}
}
