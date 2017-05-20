package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Service {
	public String serviceName;
	private int priceOfService; // this is in cents
	private int duration; // How long the service takes in 30 minute periods
	
	public Service( String name, int price, int time){
		this.priceOfService = price;
		this.serviceName = name;
		this.duration = time;
	}
	
	public String toString() {
		int priceDollars = this.priceOfService / 100;
		int priceCents = this.priceOfService - priceDollars;
		return this.serviceName + ":" + this.duration + ":$" + priceDollars + "." + priceCents;
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
			i += serv.duration;
		}
		return i;
	}
}
