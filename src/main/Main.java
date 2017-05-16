package main;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import application.GuiMain;
import model.database.SQLiteConnection;

public class Main {
	
	
	public static void main(String[] args) {
		Logger LOGGER = Logger.getLogger("main");
		SQLiteConnection n = new SQLiteConnection();
		Handler handler;
		try {
			handler = new FileHandler("logs\\" + new SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().getTime()) + ".txt");
			LOGGER.setLevel(Level.FINEST);
			handler.setLevel(Level.FINEST);
			
		}catch(IOException e) {
			handler = new ConsoleHandler();
			LOGGER.setLevel(Level.WARNING);
			handler.setLevel(Level.WARNING);
			LOGGER.warning("Cannot create logging file, using console logger");
		}
		handler.setFormatter(new SimpleFormatter());
		LOGGER.addHandler(handler);
		LOGGER.setUseParentHandlers(false);
		
		GuiMain.main(args);
	}
	
}