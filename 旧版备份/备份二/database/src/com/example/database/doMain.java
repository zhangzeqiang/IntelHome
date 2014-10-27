package com.example.database;
import java.sql.SQLException;

import database.DBTransfer;
import Pack.Message;
//import Pack.Message;
//import Pack.pack;
import Pack.pack;

public class doMain {
	@SuppressWarnings("unused")
	public static void main(String[] args){
		DBTransfer db = new DBTransfer();
		
		
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}