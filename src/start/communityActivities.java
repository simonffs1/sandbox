package start;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class communityActivities extends Testscript{
	
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
	
	@Test
	public void postMedia() throws Exception{
		
		testLogTitle("Post Media");
		
		uploadSuccess=false;
		
		clickMenu();
		clickMenuItem("explore");
		
		clickMediaTab();
		clickAddMedia();
		//Dismiss the page
		driver.findElementById("com.sphero.sprk:id/dialog_title").getText().contains("Add Media");
		clickClose();
		clickAddMedia();
		
		//Discard dialog
		driver.findElementById("com.sphero.sprk:id/dialog_title").getText().contains("Add Media");
		driver.findElementById("com.sphero.sprk:id/choose_sphero").click();
		clickClose();
		clickButton("yes");
		clickAddMedia();
		driver.findElementById("com.sphero.sprk:id/dialog_title").getText().contains("Add Media");
		//Tap Publish Now
		driver.findElementById("com.sphero.sprk:id/publish_button").click();
		driver.findElementById("com.sphero.sprk:id/dialog_title").getText().contains("Add Media");
		driver.findElementById("com.sphero.sprk:id/media_description").sendKeys("Media description automated");
		driver.findElementById("com.sphero.sprk:id/media_title").sendKeys("Media title automated, selecting ollie");
		driver.hideKeyboard();
		driver.findElementById("com.sphero.sprk:id/choose_ollie").click();
		driver.findElementById("com.sphero.sprk:id/add_media_image").click();
		//Image button
		driver.findElementById("com.sphero.sprk:id/buttonDefaultPositive").click();
		//Choose picture on Nexus (may vary between devices) *use try later for various file viewers
		try{
			waitShort.until(ExpectedConditions.presenceOfElementLocated(By.id("com.android.documentsui:id/icon_thumb")));
			driver.findElementById("com.android.documentsui:id/icon_thumb").click();
		}
		catch(Exception e){
			System.out.println("Device is not using stock android document UI");
			touch.tap((int)(screenWidth*0.50), (int)(screenHeight/4)).perform();
			Thread.sleep(1500);
			touch.tap((int)(screenWidth*0.66),screenHeight/4).perform();
		}
		
		driver.findElementById("com.sphero.sprk:id/publish_button").click();
		System.out.println("Clicked publish button");
		
		waitLong.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/toolbar_title")));
		//check if able to go back to build, if not upload failed
		if(driver.findElementsById("com.sphero.sprk:id/dialog_title").size()==0){
			uploadSuccess=true;
			System.out.println("Upload success");
		}
		else{
			Assert.fail("Upload failed");
			System.out.println("Upload failed");
		}
	}

	

}
