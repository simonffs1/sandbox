package start;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidKeyCode;

public class communityPrograms extends myPrograms {
	
	@Test
	public void canvasTutorialCard(){
		clickNavBar("programs");
		clickTab("Sphero");
		driver.findElementByXPath("//android.support.v7.widget.RecyclerView//android.widget.RelativeLayout[@index='0']").click();
		driver.findElementById("com.sphero.sprk:id/fragment_container");
		clickButton("x");
		//dismissed connect robot
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/fragment_container").size(), 0);
	}

	
	@Test//(dataProvider = "program-tabs")
	@Parameters("section")
	public void copyProgram(String section) throws Exception{
		testLogTitle("Copy a " + section + " Program");
		
		/* can copy sphero program signed out
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		*/
		
		clickNavBar("programs");
		if(driver.findElementsById("com.sphero.sprk:id/no_content_container").size()==1){
			System.out.println("0 Programs");
		}
		else{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		}
		
		int numOfProg = countProgram();
		clickTab(section);
		clickProgram();
		driver.findElementById("com.sphero.sprk:id/copy_button").click();
		clickButton("yes"); //save
		clickButton("x");
		clickTab("My Programs");
		if(numOfProg!=0){
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		}
		int numOfProgPost = countProgram();
		Assert.assertEquals(numOfProgPost, numOfProg+1);
		System.out.println("A copy of the " + section + " program has been made");
	}
	
	@Test //open a sample program and copy
	public void copySampleProgramRepeatedly() throws Exception{
		testLogTitle("Repeat Copy a Sample Program");
		clickNavBar("programs");
		
		int numOfProg = 0;
		
		clickTab("Sphero");

		while(numOfProg<=200){
			clickProgram();
			driver.findElementById("com.sphero.sprk:id/copy_button").click();
			clickButton("yes"); //confirm
			clickButton("yes"); //ok
			System.out.println("Program # " + numOfProg +"has been made");
			numOfProg++;
		}
	}
	

	
	@Test
	//@Parameters("section")
	//(dataProvider = "data-provider")
	public void scrollPrograms(String section) throws Exception{
		
		clickNavBar("programs");
		
		if(section.toLowerCase().equals("community")){
			testLogTitle("Scroll Community programs");
			clickTab("Community");
		}
		else{
			testLogTitle("Scroll Sphero Programs");
			clickTab("Sphero");
		}
		
		int scrollLimit ;
		
		
		if(screenWidth>screenHeight){
			scrollLimit = 5;
		}
		else{
			scrollLimit = 10;
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
	}
	@DataProvider(name = "program-tabs")
    public Object[][] dataProviderMethod2() {
        return new Object[][] { {"sphero"},  {"community"} };
	}
	@Test//(dataProvider = "program-tabs")
	@Parameters("section")
	public void viewProgram(String section) throws Exception{
		
		testLogTitle("View " + section + " programs");
		
		section = section.toLowerCase();
		
		if(signedIn==false && section.intern()!="sphero"){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		
		clickNavBar("programs");
		
		if(driver.findElementsById("com.sphero.sprk:id/no_content_container").size()==1){
			System.out.println("0");
		}
		else{
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		}
		
		int numOfProg = countProgram();
		
		clickTab(section);
	
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		
		//Find a program without a video
		if(driver.findElementsById("com.sphero.sprk:id/video_badge").size()>0){
			System.out.println("Program with a video exists");
		}
		
		for(int i=0;i<4;i++){
			if(section.equals("sphero") && i==0){
				i++; //to skip canvas tutorial card
			}
			if(driver.findElementsByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/video_badge']").size()==0){
				System.out.println("Card " + i + " has no video, clicking...");
				System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
				driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']").click();
				break;
			}
			else{
				System.out.println("Card " + i + " has a video");
			}
			//if no program is clicked, scroll down and reset index to 0
			if(i == 3){
				scrollVertical("down",1000);
				i = 0;
			}
		}
		
		
		//verify screen element presence
		driver.findElement(By.id("com.sphero.sprk:id/share_button"));
		driver.findElement(By.id("com.sphero.sprk:id/single_image"));
		driver.findElement(By.id("com.sphero.sprk:id/photo")); //profile pic
		driver.findElement(By.id("com.sphero.sprk:id/name")); //username
	    //driver.findElement(By.id("com.sphero.sprk:id/robot_section")); //robot
		driver.findElement(By.id("com.sphero.sprk:id/favourite_section")); //like
		driver.findElement(By.id("com.sphero.sprk:id/copy_button"));
		driver.findElement(By.id("com.sphero.sprk:id/view_button"));
		driver.findElement(By.id("com.sphero.sprk:id/dialog_close")); //x
		
		//if sphero or community, find the share button and report
		
		if(section.intern()!="my programs"){
			driver.findElement(By.id("com.sphero.sprk:id/share_button"));
			driver.findElement(By.id("com.sphero.sprk:id/flag_icon"));
		}
		
		if(driver.findElementsById("com.sphero.sprk:id/write_comment_button").size()==0){
			driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.2), 700);
		}
		driver.findElementsById("com.sphero.sprk:id/write_comment_button");
		
		driver.findElementById("com.sphero.sprk:id/single_image").click();
		driver.findElementById("com.sphero.sprk:id/fullscreen_image");
		System.out.println("Full screen image opened");
		driver.findElement(By.id("com.sphero.sprk:id/close_button")).click();

		driver.findElementById("com.sphero.sprk:id/single_image");
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		Thread.sleep(2000);
		if(driver.currentActivity().toLowerCase().contains("unity")){
			leaveCanvas();
			clickButton("x");
			clickTab("My Programs");
			if(numOfProg!=0){
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
			}
			Assert.assertEquals(numOfProg, countProgram());
		}
		else{
			clickButton("no"); // close invalid lab file prompt
			clickButton("x"); //close program details
			clickTab("My Programs");
			Reporter.log("Viewed an invalid format lab file, did not enter canvas");
			System.out.println("Invalid lab file");
			Assert.assertEquals(numOfProg, countProgram());
		}
	}
	@Test
	public void FindProgramWithoutVideo(){
		clickNavBar("Programs");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		WebElement Program = driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']");
		
		Program.findElement(By.id("com.sphero.sprk:id/video_badge"));
		
		Assert.assertEquals(Program.findElement(By.id("com.sphero.sprk:id/video_badge")), true);
		
		System.out.println("Video icon found in first program");
	}
	
	@Test
	public void viewCommunityVideo(){
		testLogTitle("View video in Community Programs");
		
		clickNavBar("Programs");
		clickTab("Community");
		
		while(driver.findElementsById("com.sphero.sprk:id/video_badge").size()==0){
			scrollVertical("down",1000);
		}
		driver.findElementById("com.sphero.sprk:id/video_badge").click();
		driver.findElementById("com.sphero.sprk:id/playable_media_overlay").click(); 
		System.out.println("Clicked video play icon");
		
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
		}
		driver.findElementById("com.sphero.sprk:id/playable_media_overlay");
		clickButton("x");
	}
	
	@Test
	@Parameters("section")
	public void ReportProgram(String section){

		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		
		clickNavBar("programs");
		clickTab(section);
		clickProgram();
		driver.findElementById("com.sphero.sprk:id/fragment_container");
		//first comment flag button
		driver.findElementByXPath("//android.widget.LinearLayout[@index='4']//*[@resource-id='com.sphero.sprk:id/flag_icon']").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		clickButton("no");
		driver.findElementByXPath("//android.widget.LinearLayout[@index='4']//*[@resource-id='com.sphero.sprk:id/flag_icon']").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		clickButton("yes");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		driver.findElementByXPath("//android.widget.LinearLayout[@index='4']//*[@resource-id='com.sphero.sprk:id/flag_icon']").click();
		Assert.assertEquals(driver.findElementsByXPath("//*[@resouce-id='com.sphero.sprk:id/dialog_title' and text='Flag']").size(), 0);
		System.out.println("Flag dialog did not appear, comment already flagged");
		//implement toast check using teseract OCR
		
	}
	
	@DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][] { {"sphero" }, {"community"}};
	}
	
	
	//Check unauthenticated user gate prompt visibility
	@Test(dataProvider = "data-provider")
	//@Parameters("gate")
	public void userGatesPrograms(String gate){
		
		testLogTitle("User gates when signed out on " + gate + " in Programs");
		
		if(signedIn==true){
			signOut();
			clickNavBar("programs");
			driver.findElementById("com.sphero.sprk:id/programs_onboarding_overlay");
			driver.findElementById("com.sphero.sprk:id/onboarding_add_new_program_button").click();
			clickButton("no");
		}
		
		clickNavBar("programs");
		clickTab(gate);
		
		clickProgram();
		if(gate.equals("community")){
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		}

		//like
		System.out.println("Like button gate");
		GateDialog("com.sphero.sprk:id/is_favourite_icon","no");
		GateDialog("com.sphero.sprk:id/is_favourite_icon","yes");
		clickButton("x");
		
		
		//report
		System.out.println("Report button gate");
		GateDialog("com.sphero.sprk:id/flag_section","no");
		GateDialog("com.sphero.sprk:id/flag_section","yes");
		clickButton("x");
		
		//write comment
		System.out.println("Write Comment button gate");
		GateDialog("com.sphero.sprk:id/write_comment_button","no");
		GateDialog("com.sphero.sprk:id/write_comment_button","yes");
		clickButton("x");
		
		
		driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.2), 500);
		
		System.out.println("Comment flag button gate");
		if(driver.findElementsById("com.sphero.sprk:id/date").size()>0){
			GateDialog("com.sphero.sprk:id/flag_button","no");
			GateDialog("com.sphero.sprk:id/flag_button","yes");
			clickButton("x");
			
		}
		else{
			System.out.println("No comments, user gate check skipped");
		}
		
		String currentNavBar = driver.findElementById("com.sphero.sprk:id/bottom_navigation_small_item_title").getText();
		//Click on Make a Copy
		if(driver.findElementByXPath("//android.support.v7.a.d/android.widget.TextView[@text='Community']").isSelected()){
		
			System.out.println("View button gate");
			GateDialog("com.sphero.sprk:id/view_button","no");
			GateDialog("com.sphero.sprk:id/view_button","yes");
			clickButton("x");
			
			System.out.println("Copy button gate");
			GateDialog("com.sphero.sprk:id/copy_button","no");
			GateDialog("com.sphero.sprk:id/copy_button","yes");
			clickButton("x");
			
		}
		clickButton("x");
	}
	
	public void GateDialog(String s,String a){
		driver.findElementById(s).click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		String dialogTitle = driver.findElementById("com.sphero.sprk:id/dialog_title").getText();
		Assert.assertEquals(dialogTitle, "Sign In Required");
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/positive_action").getText(), "Sign In Now");
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/negative_action").getText(), "Maybe Later");
		clickButton(a);
	}


}
