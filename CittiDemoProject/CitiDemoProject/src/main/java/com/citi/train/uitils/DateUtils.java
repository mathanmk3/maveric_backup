package com.citi.train.uitils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {

	private DateUtils(){

	}
	static String outputFormatStr = "yyyy-MM-dd HH:mm:ss";


	public static String currentDateTimeFormatInString() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(outputFormatStr);
		return LocalDateTime.now().format(format);
	}

	public static LocalDateTime currentDateTimeFormatInDateFormat() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(outputFormatStr);
		String dateTime = LocalDateTime.now().format(format);
		return LocalDateTime.parse(dateTime, format);
	}

	public static LocalDateTime stringToDateFormat(String str) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(outputFormatStr);
		return LocalDateTime.parse(str, format);
	}

	public static LocalDateTime generateSpecificDate(Long date){
		DateTimeFormatter format = DateTimeFormatter.ofPattern(outputFormatStr);
		String dateTime = LocalDateTime.now().plusDays(date).format(format);
		return LocalDateTime.parse(dateTime, format);
	}

}
