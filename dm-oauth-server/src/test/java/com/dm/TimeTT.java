package com.dm;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeTT {
	public static void main(String[] args) throws IOException {
		URL url = new URL("http://www.baidu.com");
		URLConnection conn = url.openConnection();
		conn.connect();
		long dateL = conn.getDate();
		System.out.println(ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateL), ZoneId.of("Asia/Shanghai")));
	}

}
