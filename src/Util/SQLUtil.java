package Util;

import java.util.*;
import java.sql.*;

public class SQLUtil {
public static synchronized String getHtmlRows(ResultSet results)
throws SQLException{
	StringBuffer htmlRows = new StringBuffer();
	ResultSetMetaData metaData = results.getMetaData();
	int columnCount = metaData.getColumnCount();
	
	System.out.println("Column Count: " + metaData.getColumnCount()); // Joe troubleshooting
		
	htmlRows.append("<tr>");
	for (int i = 1; i <= columnCount; i++)
		htmlRows.append("<td><b>" + metaData.getColumnName(i) + "</b></td>");
	htmlRows.append("</tr>");
	
	
	while (results.next()){
		htmlRows.append("<tr>");
		for (int i = 1; i <= columnCount; i++)
			htmlRows.append("<td>" + results.getString(i) + "</td>");
		htmlRows.append("</tr>");
	}
	
	return htmlRows.toString();
	
}

public static synchronized String encode(String s){
	if (s == null) return s;
	StringBuffer sb = new StringBuffer(s);
	for (int i =1; i <sb.length(); i++){
		char ch = sb.charAt(i);
		if (ch == 39){// 39 is the ASCII code for an apostrophe
			sb.insert(i++, "'");
		}
	}
	return sb.toString();
}
}

