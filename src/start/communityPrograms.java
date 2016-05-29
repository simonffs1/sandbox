package start;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidKeyCode;

public class communityPrograms extends Testscript {

	@Test
	//@Parameters("section")
	(dataProvider = "data-provider")
	public void scrollExplore(String section) throws Exception{
		
		if(section.toLowerCase().equals("media")){
			testLogTitle("Scroll Explore Media");
		}
		else{
			testLogTitle("Scroll Explore Programs");
		}
		
		int scrollLimit ;
		
		
		if(screenWidth>screenHeight){
			scrollLimit = 5;
		}
		else{
			scrollLimit = 10;
		}
		
		clickMenu();
		clickMenuItem("explore");
	
		dismissHint();
		
		if(section.toLowerCase().equals("media")){
			clickMediaTab();
		}
		
		int scrollCount = 0;
		while(true){
			scrollVertical("down",200);
			scrollCount++;
			try{
				Boolean footer = driver.findElementsById("com.sphero.sprk:id/progress_bar").size()>0;
				if (footer == true){
					System.out.println("Spinner found, waiting 5 seconds before scrolling again");
					Thread.sleep(5000);
					footer = false;
					scrollCount = 0;
				}
				if (footer == false && scrollCount>scrollLimit){
					System.out.println("End of my programs");
					break;
				}
			}
			catch(Exception e){
				System.out.println("Footer refresh not found");
			}
		}
		
		
		while (true){
			scrollVertical("up",200);
			try{
			Boolean header = driver.findElementsByXPath("//android.view.View[@resource-id='com.sphero.sprk:id/swipe_refresh']/android.widget.ImageView").size() > 0 ;
				if (header == true){
					System.out.println("Found refresh icon");
					break;
				}
			}
			catch(Exception e){
				System.out.println("Something went wrong scrolling up");
			}
		}
		
		clickMenu();
		clickMenuItem("build");
	}
	
	@Test//(invocationCount=10)
	public void viewExploreProgram() throws Exception{
		
		testLogTitle("View Explore Programs");
		
		Boolean progress = false;
		
		int numOfProg = countProgram();
		
		driver.findElementByXPath("//android.support.v7.app.ActionBar$Tab/android.widget.TextView[@text='Community']").click();
		
		clickProgram();
		
		//verify screen element presence
		driver.findElement(By.id("com.sphero.sprk:id/share_button"));
		driver.findElement(By.id("com.sphero.sprk:id/single_image"));
		driver.findElement(By.id("com.sphero.sprk:id/photo"));
		driver.findElement(By.id("com.sphero.sprk:id/name"));
		driver.findElement(By.id("com.sphero.sprk:id/robot_section"));
		driver.findElement(By.id("com.sphero.sprk:id/favourite_section"));
		driver.findElement(By.id("com.sphero.sprk:id/flag_section"));
		driver.findElement(By.id("com.sphero.sprk:id/dialog_close"));
		
		
		try{
			driver.findElements(By.id("com.sphero.sprk:id/progress_bar"));
			System.out.println("Spinner was found");
			progress = driver.findElements(By.id("com.sphero.sprk:id/progress_bar")).size()>0;
		}
		catch(Exception preload){
			System.out.println("No spinner was found");
		}
		
		//handle progress indicator blocking tap
		/*
		try{
			Boolean progress = driver.findElements(By.id("com.sphero.sprk:id/progress_bar")).size()>0;
			if(progress == true){
				System.out.println("Waiting for progress spinner to disappear");
				while(driver.findElement(By.id("com.sphero.sprk:id/progress_bar")).isDisplayed()){
					Thread.sleep(2000);
				}
			}
		}
		catch(Exception e){
			System.out.println("No progress indicator was found");
		}
		*/
		
		if(driver.findElementsById("com.sphero.sprk:id/playable_media_overlay").size()==0){
			if(progress==true){
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/progress_bar")));
			}
			driver.findElementById("com.sphero.sprk:id/single_image").click();
			try{
				driver.findElementById("com.sphero.sprk:id/fullscreen_image");
				System.out.println("Full screen image opened");
				driver.pressKeyCode(AndroidKeyCode.BACK);
			}
			catch(Exception e){
				System.out.println("No user attached image");
			}
		}
		else{
			System.out.println("Video found");
			//implement view video
		}
		
		driver.findElementById("com.sphero.sprk:id/single_image");
		clickView();
		Thread.sleep(2000);
		if(driver.currentActivity().toLowerCase().contains("unity")){
			leaveCanvas();
			driver.findElementByXPath("//android.support.v7.app.ActionBar$Tab/android.widget.TextView[@text='My Programs']").click();
			Assert.assertEquals(numOfProg, countProgram());
		}
		else{
			clickButton("no"); // close invalid lab file prompt
			clickClose(); //close program details
			driver.findElementByXPath("//android.support.v7.app.ActionBar$Tab/android.widget.TextView[@text='My Programs']").click();
			Reporter.log("Viewed an invalid format lab file, did not enter canvas");
			System.out.println("Invalid lab file");
		}
	}
	
	@Test(dataProvider = "data-provider")
	public void viewExploreVideo(String section){
		testLogTitle("View video in Explore Programs");
		
		Boolean videoFound = false;
		driver.findElementByXPath("//android.support.v7.app.ActionBar$Tab/android.widget.TextView[@text='Community']").click();
		
		while(videoFound==false){
			try{
				driver.findElementById("com.sphero.sprk:id/video_badge").click();
				driver.findElementById("com.sphero.sprk:id/playable_media_overlay").click(); //program 
			
				videoFound=true;
				System.out.println("Clicked video play icon");
				
				//Click youtube
				try{
					driver.findElementByName("YouTube").click();
				}
				catch(Exception e){
					System.out.println("Youtube icon doesn't exist");
				}
				//click once 
				try{
					driver.findElementById("android:id/button_once").click();
				}
				catch(Exception e){
					System.out.println("Just Once doesn't exist");
				}
				if(driver.currentActivity().toLowerCase().contains("youtube")){
					System.out.println("In youtube app");
					driver.pressKeyCode(AndroidKeyCode.BACK);
					break;
				}
				//click once 
				
			}
			catch(Exception e){
				System.out.println("Video badge not found");
			}
			scrollVertical("down",1500);
		}
		clickClose();
		driver.findElementByXPath("//android.support.v7.app.ActionBar$Tab/android.widget.TextView[@text='My Programs']").click();
	}

	
}
