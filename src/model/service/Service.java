package model.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import model.database.SQLiteConnection;

public class Service {
	public String serviceName;
	public int priceOfService; // this is in cents
	public int secondsOfService; // How long the service takes in seconds (time)
	
	public Service( String name, int price, int time, boolean createDBEntry){
		this.priceOfService = price;
		this.serviceName = name;
		this.secondsOfService = time;
		if (createDBEntry) {
			try {
				SQLiteConnection.getService(serviceName, "SARJ's Milk Business");
			}
			catch(SQLException e) {
				
			}
			
		}
	}
	
	public Service(String name, String price, String time, boolean createDBEntry) throws Exception {
		this.priceOfService = Integer.parseInt(price);
		this.serviceName = name;
		this.secondsOfService = Integer.parseInt(time);
		if (createDBEntry) {
			try {
				SQLiteConnection.getService(serviceName, "SARJ's Milk Business");
			}
			catch(SQLException e) {
				
			}
			
		}
	}
	
	public String toString() {
		int priceDollars = this.priceOfService / 100;
		int priceCents = this.priceOfService - priceDollars;
		return this.serviceName + " $" + priceDollars + "." + priceCents;
	}
	
	public static String arrayOfServicesToString(ArrayList<Service> services, boolean needBothNameAndPrice) {
		String s = "";
		if (services == null || services.size() == 0) {
			return s;
		}
		Iterator<Service> iter = services.iterator();
		while(iter != null  && iter.hasNext()) {
			Service serv = iter.next();
			if (needBothNameAndPrice) {
				s = s + " " + serv.toString();
			}
			else {
				s = s + " " + serv.serviceName;
			}
		}
		return s;
	}
	
	public static int getTotalArrayDuration(ArrayList<Service> services) {
		int i = 0;
		Iterator<Service> iter = services.iterator();
		while(iter.hasNext()) {
			Service serv = iter.next();
			i = serv.secondsOfService;
		}
		return i;
	}
	
	public static ArrayList<Service> stringOfServicesToArrayList(String services) {
		String[] servicesSplit = services.split(" ");
		ArrayList<Service> servs = new ArrayList<Service>();
		try {
			for(int i = 0; i < servicesSplit.length; i++) {
				ResultSet rs = SQLiteConnection.getService(servicesSplit[i], "SARJ's Milk Business");
				if (rs == null) {
				}
				else {
					Service s = new Service(rs.getString("servicename"), rs.getString("serviceprice"), rs.getString("serviceminutes"), false);
					servs.add(s);
				}
			}
			return servs;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
