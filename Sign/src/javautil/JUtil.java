package javautil;

import java.util.regex.Pattern;
public class JUtil {
	public static boolean isPhone(String str) {
	    //010, 011, 016, 017, 018, 019
	    return str.matches("(01[016789])-(\\d{3,4})-(\\d{4})");
	}
	public static boolean isEmail(String str) {
		 return Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", str);
	}
	public static boolean isEmpNo(String str) {
		 return Pattern.matches("(P|p|D|d|S|s|M|m)\\d{4}", str);
	}
	public static boolean isBirth(String str){
		return Pattern.matches("^[1-2]{1}[0-9]{3}-[0-1]{1}[0-9]{1}-[0-3]{1}[0-9]{1}$", str);
	}
	public static boolean isDate(String str){
		return Pattern.matches("^[1-2]{1}[0-9]{3}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}$", str);
	}
	public static boolean isTime(String str){
		return Pattern.matches("^[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$", str);
	}
}

