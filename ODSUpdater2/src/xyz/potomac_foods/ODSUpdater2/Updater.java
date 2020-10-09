package xyz.potomac_foods.ODSUpdater2;

import java.io.IOException;

public class Updater {
	
	public static void main(String[] args) {
		Downloader<String> downloader;
		
		// Download everything needed
		if (Utilities.hasInternetConnection()) {
			downloader = new Downloader<>("https://www.potomac-foods.xyz/ods2/downloads/");
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
			
		} else {
			System.out.println("No internet connection! Skipping update...");
		}
	}
}
