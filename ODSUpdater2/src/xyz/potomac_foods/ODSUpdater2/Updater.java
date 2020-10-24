package xyz.potomac_foods.ODSUpdater2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Updater {
	
	public static void main(String[] args) {
		Downloader downloader;
		boolean updateODS = true;
		boolean updatedODS = false;
		
		if (args.length != 0) {
			updateODS = Boolean.parseBoolean(args[0]);
			System.out.println("Found args: " + updateODS + " | " + args[0]);
		}
		
		if (updateODS) {
			// Download everything needed
			if (Utilities.hasInternetConnection()) {
				downloader = new Downloader("https://www.potomac-foods.xyz/ods2/downloads/");
				String dataReceived = "";
				
				// Add layouts, images, other needed files to download queue
				try {
					dataReceived = Utilities.getHTML("https://www.potomac-foods.xyz/ods2/getdownloads.php");
				} catch (IOException e) {
					System.err.println("There was an error downloading the data");
				}
				
				if (!dataReceived.equals("")) {
					String[] toDownload = dataReceived.split(",");
					for (String download : toDownload) {
						String newDownload = download.replace("\n", "");
						System.out.println("Found " + newDownload + "!");
						downloader.add(newDownload);
					}
				}
				
				downloader.add("/OrderDisplaySystem2.jar");
				downloader.add("/version.txt");
				
				// Finally download everything
				try {
					downloader.downloadAll();
				} catch (IOException e) {
					System.err.println("There was a fatal error downloading a file! " + e);
				}
				
				updatedODS = true;
			} else {
				System.out.println("No internet connection! Skipping update...");
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
		    Date date = new Date(); 
			
			if(updatedODS)
				try {
					Utilities.getHTML("https://potomac-foods.xyz/ods2/log.php?store_num=None" + "&program=ODSUpdater&log=Updated%20Successfully%20On%20" + formatter.format(date) + "!");
					System.out.println("Done");
				} catch (IOException e) {
					System.err.println(e);
				}
		
		} else {
			System.out.println("Chose not to update ODS!");
		}
	}
}
