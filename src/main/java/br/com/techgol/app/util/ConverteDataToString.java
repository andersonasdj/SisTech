package br.com.techgol.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConverteDataToString {

	public String convert(Date data) {
		
		if(data != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			return simpleDateFormat.format(data);
		}else {
			return " ";
		}
		
	}
	
}
