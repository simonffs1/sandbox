package start;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidKeyCode;

public class myPrograms extends Testscript {
	
	@Test
	public void Onboarding() throws Exception{
		if(signedIn==true){
			signOut();
		}
		else{
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
			signOut();
		}
		clickNavBar("Programs");
		driver.findElementById("com.sphero.sprk:id/programs_onboarding_overlay");
		driver.findElementById("com.sphero.sprk:id/onboarding_add_new_program_button").click();
		clickButton("yes");
		leaveCanvas();
	}

	@Test
	public void SortBy(){
		clickNavBar("programs");
		driver.findElementById("com.sphero.sprk:id/spinner_text").click();
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/spinner_text' and @text='Date Oldest']").click();
	}
	
	//Unit test for counting programs
	public int countProgram(){
		if(driver.findElementsById("com.sphero.sprk:id/no_content_container").size()==1){
			return 0;
		}
		else{
			String xpath = "//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout";
			List <WebElement> ListByXpath = driver.findElements(By.xpath(xpath));
			System.out.println(ListByXpath.size());
			return ListByXpath.size();   
		}
	}
	
	//@Test
	public void countPrograms(){
		if(driver.findElementsById("com.sphero.sprk:id/no_content_container").size()==1){
			System.out.println("0 Programs");
		}
		else{
			String xpath = "//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout";
			List <WebElement> ListByXpath = driver.findElements(By.xpath(xpath));
			System.out.println(ListByXpath.size());
		}
	}
	
	@Test
	public void createProgram() throws Exception{
		testLogTitle("Create a program");
		
		clickNavBar("Programs");
		int initialprograms = 0;
	
		if(driver.findElementsByXPath("//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout").size()>0){
			initialprograms = countProgram();
		}
		else{
			//check for no programs message
			driver.findElementById("com.sphero.sprk:id/no_content_message");
		}
		System.out.println(initialprograms);
		clickAddNewProgram();
		driver.findElementById("com.sphero.sprk:id/edit_text").sendKeys("Created by automation");
		//sendKeys("Program created by automation!");
		//Tap Save
		clickButton("save");
		leaveCanvas();
		//Check if program count has increased by 1
		int currentprograms = countProgram();
		System.out.println(currentprograms);
		Assert.assertEquals(currentprograms,initialprograms+1);
		System.out.println("Program created successfully");
	}
	
	
	@Test 
	public void copyMyProgram() throws Exception{
		
		
		testLogTitle("Copy My Programs while signed in");
		
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		
		clickNavBar("Programs");
		
		if(countProgram()==0){
			createProgram();
		}
		
		int numOfProg = countProgram();
		clickProgram(); //click first program
		driver.findElementById("com.sphero.sprk:id/copy_button").click();
		clickButton("save"); //confirm
		clickButton("x");
		
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		Assert.assertEquals(countProgram(), numOfProg+1);
		System.out.println("Program copied successfully");
		//*check name 
	}
	@Test 
	public void viewMyProgram() throws Exception{
		
		
		testLogTitle("View My Program canvas while signed in");
		
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		clickNavBar("Programs");
		
		if(countProgram()==0){
			createProgram();
		}
		
		int numOfProg = countProgram();
		
		clickProgram(); //click first program
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		clickButton("save"); //confirm
		clickButton("x");
		
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		Assert.assertEquals(countProgram(), numOfProg+1);
		System.out.println("Program canvas viewed successfully");
		//*check name 
	}
	
	@Test
	public void editMyProgram() throws Exception{
		
		
		testLogTitle("Edit My Programs while signed in");
		
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		
		clickNavBar("Programs");
		
		if(countProgram()==0){
			createProgram();
		}
		
		clickProgram();
		
		boolean wasPublic = false;
		uploadSuccess=false;
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		//attach image
		driver.findElementById("com.sphero.sprk:id/add_media_button").click();
		//Image button
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/title' and @text='Image']").click();
		//Choose picture on Nexus (may vary between devices) *use try later for various file viewers
		try{
			waitShort.until(ExpectedConditions.presenceOfElementLocated(By.id("com.android.documentsui:id/icon_thumb")));
			driver.findElementById("com.android.documentsui:id/icon_thumb").click();
		}
		//samsung
		catch(Exception e){
			System.out.println("Device is not using stock android document UI");
			touch.tap((int)(screenWidth*0.50), (int)(screenHeight/4)).perform();
			Thread.sleep(1500);
			touch.tap((int)(screenWidth*0.66),screenHeight/4).perform();
		}
		
		//current status
		String currentStatus = driver.findElementById("com.sphero.sprk:id/status_text").getText();
		//Click switch and help icon
		driver.findElementById("com.sphero.sprk:id/public_switch").click(); //*Keep track of switch status later
		String postStatus = driver.findElementById("com.sphero.sprk:id/status_text").getText();
		if(driver.findElementById("com.sphero.sprk:id/public_switch").getText().equals("OFF")){
			wasPublic = true;
			Assert.assertEquals(postStatus, "Private");
		}
		else if (driver.findElementById("com.sphero.sprk:id/public_switch").getText().equals("ON") && !currentStatus.equals("Public")){
			Assert.assertEquals(postStatus, "In Review");
		}
		driver.findElementById("com.sphero.sprk:id/help_button").click();
		clickButton("yes");
		
		//Click robots
		driver.findElementById("com.sphero.sprk:id/sphero_button").click();
		driver.findElementById("com.sphero.sprk:id/ollie_button").click();
		driver.findElementById("com.sphero.sprk:id/bb8_button").click();
		
		
		driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.3), 500);
		
		//Clear fields and type
		driver.findElementById("com.sphero.sprk:id/title").clear();
		driver.findElementById("com.sphero.sprk:id/title").sendKeys("Edited the program title");
		driver.pressKeyCode(AndroidKeyCode.BACK);
		driver.findElementById("com.sphero.sprk:id/description").clear();
		driver.findElementById("com.sphero.sprk:id/description").sendKeys("Edited the program description");
		driver.pressKeyCode(AndroidKeyCode.BACK);

		
		driver.findElementById("com.sphero.sprk:id/dialog_action").click();
		//if in review tap continue
		if(wasPublic == true){
			clickButton("yes");
		}
		
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/content")));
		//waitLong.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/content")));
		//System.out.println("Progress bar not visible");
		
		
		waitLong.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		//check if able to go back to build, if not upload failed
		if(driver.findElementById("com.sphero.sprk:id/status_text").getText().equals(postStatus) && driver.findElementById("com.sphero.sprk:id/body").getText().equals("Edited the program description")) {
			uploadSuccess=true;
			System.out.println("Upload success");
		}
		else{
			Assert.fail("Upload failed");
			//softAssert.fail("Upload failed");
			//clickClose();
			//clickButton("yes"); //close discard change dialog
			System.out.println("Upload failed");
		}
		clickButton("x");

		//touch.press(10,(driver.manage().window().getSize().getHeight())/2);
		//*Add discard dialog test case here later
		//Tap the left most middle area of the screen (TABLET ONLY)
	}
	
	
	@Test 
	public void deleteMyProgramSignIn(){
		testLogTitle("Delete Programs while signed in");
		
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		
		clickNavBar("programs");
		
		int numOfProg = countProgram();
		clickProgram();
		
		//Check Edit title
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		driver.findElementById("com.sphero.sprk:id/dialog_action");
		driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.3), 500);
		driver.findElementById("com.sphero.sprk:id/delete_button").click();
		clickButton("delete");
		Assert.assertEquals(countProgram(), numOfProg-1);
		System.out.println("Program deleted successfully");
		//*check name
	}
	
	@Test
	public void ViewRenameDeleteProgramSignedOut() throws Exception{
		testLogTitle("Delete and Rename Programs signedOut");

		if(signedIn==true){
			signOut();
			clickNavBar("programs");
			driver.findElementById("com.sphero.sprk:id/programs_onboarding_overlay");
			driver.findElementById("com.sphero.sprk:id/onboarding_add_new_program_button").click();
			clickButton("no");
		}
		
		clickNavBar("programs");
		
		if(countProgram()==0){
			createProgram();
		}
		
		int initialprograms = countProgram();
		System.out.println(initialprograms);
		clickProgram();
		
		//Tap View
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/title' and @text='View Program']").click();
		leaveCanvas();
		System.out.println("Program viewed successfully");
		
		//Tap Rename
		clickProgram();
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/title' and @text='Rename Program']").click();
		driver.findElementById("com.sphero.sprk:id/edit_text").clear();
		driver.findElementById("com.sphero.sprk:id/edit_text").sendKeys("Renamed by auto");
		clickButton("save");
		driver.findElementById("com.sphero.sprk:id/program_name").getText().contains("Renamed by auto");
		System.out.println("Program renamed successfully");
		
		//Tap Delete
		clickProgram();
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/title' and @text='Delete Program']").click();
		clickButton("yes");
		System.out.println(countProgram());
		Assert.assertEquals(countProgram(),initialprograms-1);
		System.out.println("Program deleted successfully");
	}
	
	//@Test //(dependsOnMethods={'signIn'}
	public void deleteAllPrograms(){
		
		clickNavBar("programs");
		
		int count = 0;
		while (countProgram()>1){
			deleteMyProgramSignIn();
			count++;
		}
		System.out.println("All programs (" + count + ") deleted");
	}
	
	
	@Test 
	public void deleteAllProgramsSignedOut() throws Exception{
		
		clickNavBar("programs");
		
		while (countProgram()>1){
			clickProgram();
			driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/title' and @text='Delete Program']").click();
			clickButton("yes");
			Thread.sleep(500);
			System.out.println(countProgram());
		}
		System.out.println("All programs deleted");
	}
	
	@Test
	public void likeButton() throws InterruptedException{
		clickNavBar("programs");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']").click();
		
		//before
		String numLikesString = driver.findElementById("com.sphero.sprk:id/like_count").getText();
		String pre[] = numLikesString.split("\\s+");
		int likeCount = Integer.parseInt(pre[0]);
		System.out.println(likeCount);
		
		//click like
		driver.findElementById("com.sphero.sprk:id/is_favourite_icon").click();
		
		//wait 5 seconds
		Thread.sleep(5000);
		
		//after
		numLikesString = driver.findElementById("com.sphero.sprk:id/like_count").getText();
		String post[] = numLikesString.split("\\s+");
		int postLikeCount = Integer.parseInt(post[0]);
		System.out.println(postLikeCount);
		
		if(postLikeCount<likeCount){
			System.out.println("Previously liked program, unliked");
		}
		else{
			System.out.println("Liked program");
		}
		
		clickButton("x");
		
		if(postLikeCount>0){
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']//*[resource-id='com.sphero.sprk:id/likes_counter']")));
			String likeRibbon = driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']//*[resource-id='com.sphero.sprk:id/likes_counter']").getText();
			int likeRibbonInt = Integer.parseInt(likeRibbon);
			
			Assert.assertEquals(likeRibbonInt, postLikeCount);
			System.out.println("Like ribbon count matches");
		}
		else{
			int likeRibbonInt = driver.findElementsByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']//*[resource-id='com.sphero.sprk:id/likes_counter']").size();
			Assert.assertEquals(likeRibbonInt, 0);
			System.out.println("Like ribbon invisibility for program with 0 likes");
		}
	}
	
	public void postCommentMyPrograms(){
		
		clickNavBar("programs");
		clickProgram();
		postComment();
	}
	
	public void postComment(){
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid = " + uuid);
		driver.findElementById("com.sphero.sprk:id/fragment_container");
		//scroll if not visible
		if(driver.findElementsById("com.sphero.sprk:id/write_comment_button").size()==0){
			driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.2), 700);
		}
		driver.findElement(By.id("com.sphero.sprk:id/write_comment_button")).click();
		driver.findElementByXPath("//android.widget.TextView[@text='Write A Comment']");
		driver.findElementById("com.sphero.sprk:id/review_body").sendKeys(uuid);
		driver.findElementById("com.sphero.sprk:id/submit_button").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.2), 700);
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/body' and @text='" + uuid +"']");
		System.out.println("Comment Posted");
		System.out.println( new SimpleDateFormat("MMM d, YYYY | h:mm a").format(Calendar.getInstance().getTime()) );
	}
	
	
	@Test
	//post 200 comments
	public void postMultipleComments(){
		
		clickNavBar("programs");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		clickProgram();
		driver.findElementById("com.sphero.sprk:id/fragment_container");
		String uuid = "comment";
		for(int i=0;i<=200;i++){
			uuid = UUID.randomUUID().toString();
			//scroll if not visible
			if(driver.findElementsById("com.sphero.sprk:id/write_comment_button").size()==0 || driver.findElementById("com.sphero.sprk:id/write_comment_button").getLocation().getY()>(screenHeight*0.8)){
				driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.2), 700);
			}
			driver.findElement(By.id("com.sphero.sprk:id/write_comment_button")).click();
			driver.findElementById("com.sphero.sprk:id/review_body").sendKeys(uuid);
			driver.findElementById("com.sphero.sprk:id/submit_button").click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
			System.out.println("Comment " + i + " Posted");
		}
	}
	
	@Test
	public void flagCommentPrograms(){
		clickNavBar("programs");
		clickProgram();
		flagComment();
	}
	
	public void flagComment(){
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid = " + uuid);
		uuid = uuid + "flag";
		clickNavBar("programs");
		clickProgram();

		postComment();
		
		//first comment flag button
		driver.findElementByXPath("//android.widget.LinearLayout[@index='4']//*[@resource-id='com.sphero.sprk:id/flag_button']").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		clickButton("no");
		driver.findElementByXPath("//android.widget.LinearLayout[@index='4']//*[@resource-id='com.sphero.sprk:id/flag_button']").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		clickButton("yes");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		driver.findElementByXPath("//android.widget.LinearLayout[@index='4']//*[@resource-id='com.sphero.sprk:id/flag_button']").click();
		Assert.assertEquals(driver.findElementsByXPath("//*[@resouce-id='com.sphero.sprk:id/dialog_title' and text='Flag']").size(), 0);
		System.out.println("Flag dialog did not appear, comment already flagged");
		//implement toast check using teseract OCR
	}
	
	@Test
	public void viewProgramStatus(){
		
		clickNavBar("Home");
		clickTab("Profile");
		//check if signed in as programstatus account
		//check if sign out button is visible
		if(driver.findElementsById("com.sphero.sprk:id/sign_out_button").size()==1){
			//check if accoun is not programstatus
			if(!("programstatus").equals(driver.findElementById("com.sphero.sprk:id/profile_text_field").getText())){
			//sign out then sign in as programstatus
			signOut();
			signIn("programstatus","yyyyyy","instructor", true, false, false);
			}
		}
		else{
			//if the sign out btuton is not visible then sign in as program status
			signIn("programstatus","yyyyyy","instructor", true, false, false);
		}

		clickNavBar("Programs");
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		
		List<WebElement> programs = driver.findElements(By.xpath("//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout//*[@resource-id='com.sphero.sprk:id/status_indicator']"));
		HashSet<String> statuslist = new HashSet<String>();
		for(WebElement temp : programs){
			statuslist.add(temp.getText());
		}
		
		HashSet<String> comparestatus = new HashSet<String>();
		comparestatus.add("In Review");
		comparestatus.add("Public");
		comparestatus.add("Rejected");
		comparestatus.add("Private");
		
		//check if the 4 program types exists
		Assert.assertEquals(statuslist,comparestatus);
		
		for(WebElement temp : programs){
			if(temp.getText().equals("Public")){
				temp.click();
				System.out.println("Clicked Public program");
				break;
			}
		}
		
		//overflow button for public
		driver.findElementById("com.sphero.sprk:id/share_button"); //check share button is visible
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public"); //check status in program details
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public"); //check status in edit
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/public_switch")).getAttribute("checked"), "true");//check switch status
		
		//toggle switch check status
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public");
		//driver.findElement(By.id("com.sphero.sprk:id/share_button")); //check visibility of share btuton
		clickButton("sphero");
		clickButton("back");
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Discard Changes");
		clickButton("no");
		
		driver.findElementById("com.sphero.sprk:id/dialog_action").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Continue editing?");
		clickButton("no");
		clickButton("sphero");
		driver.findElementById("com.sphero.sprk:id/dialog_action").click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public");
		clickButton("x");
		
		/*
		for(WebElement temp : programs){
			if(temp.getText().equals("Rejected")){
				temp.click();
				break;
			}
		}
		
		
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Rejected");
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Rejected");
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/program_public_switch")).getAttribute("checked"), "false");
		//toggle status
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Rejected");
		clickButton("ollie");
		clickButton("back");
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Discard Changes");
		clickButton("yes");
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Rejected");
		clickButton("x");
		*/
		
		for(WebElement temp : programs){
			if(temp.getText().equals("In Review")){
				temp.click();
				System.out.println("Clicked Review program");
				break;
			}
		}
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/share_button").size(),0); //check share button is invisible
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review"); //check status in program details
		driver.findElementById("com.sphero.sprk:id/edit_button").click(); // go to edit
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review"); //check status in edit
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/public_switch")).getAttribute("checked"), "true");//check switch status
		
		//toggle switch check status
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		
		clickButton("bb8");
		clickButton("back");
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Discard Changes");
		clickButton("no");
		
		driver.findElementById("com.sphero.sprk:id/dialog_action").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Continue editing?");
		clickButton("no");
		clickButton("bb8");//toggle back to original
		clickButton("save");
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		clickButton("x");
		
		for(WebElement temp : programs){
			if(temp.getText().equals("Private")){
				temp.click();
				System.out.println("Clicked Private program");
				break;
			}
		}
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/share_button").size(),0); //check share button is invisible
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private"); //check status in program details
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private"); //check status in edit
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/public_switch")).getAttribute("checked"), "false");//check switch status
		
		//toggle switch check status
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");

		clickButton("bb8");
		clickButton("back");
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/dialog_title").getText(), "Discard Changes");
		clickButton("no");
		
		//set to in review
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		clickButton("save");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/single_image")));
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review"); // program details changed from private to inreview
		
		//changing back from in review to private
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		driver.findElement(By.id("com.sphero.sprk:id/public_switch")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		clickButton("save");
		clickButton("yes");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/single_image")));
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		clickButton("x");


	}
	
	@Test
	public void publicProgramGate() throws Exception{
		
		clickNavBar("Home");
		clickTab("Profile");
		//check if signed in as programstatus account
		//check if sign out button is visible
		if(driver.findElementsById("com.sphero.sprk:id/sign_out_button").size()==1){
			//check if accoun is not programstatus
			if(!("programstatus").equals(driver.findElementById("com.sphero.sprk:id/profile_text_field").getText())){
			//sign out then sign in as programstatus
			signOut();
			signIn("programstatus","yyyyyy","instructor", true, false, false);
			}
		}
		else{
			//if the sign out btuton is not visible then sign in as program status
			signIn("programstatus","yyyyyy","instructor", true, false, false);
		}

		clickNavBar("Programs");
		
		waitLong.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		
		//checkPublicProgramOrder();
		//overflow button for public
		List<WebElement> programs = driver.findElements(By.xpath("//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout//*[@resource-id='com.sphero.sprk:id/status_indicator']"));
		HashSet<String> statuslist = new HashSet<String>();
		for(WebElement temp : programs){
			statuslist.add(temp.getText());
		}
		
		HashSet<String> comparestatus = new HashSet<String>();
		comparestatus.add("In Review");
		comparestatus.add("Public");
		comparestatus.add("Rejected");
		comparestatus.add("Private");
		
		//check if the 4 program types exists
		Assert.assertEquals(statuslist,comparestatus);
		
		
		//public program gate
		for(WebElement temp : programs){
			if(temp.getText().equals("Public")){
				temp.click();
				break;
			}
		}
		
		String programname = driver.findElementById("com.sphero.sprk:id/dialog_title").getText();
		driver.findElementById("com.sphero.sprk:id/share_button"); //check share button is visible
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public");
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		System.out.println(driver.findElementById("com.sphero.sprk:id/dialog_title").getText());
		clickButton("yes"); //continue
		leaveCanvas();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public");
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		clickButton("no"); //cancel
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		driver.findElementById("com.sphero.sprk:id/neutral_action").click(); //make a copy
		clickButton("save");
		leaveCanvas();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public");
		clickButton("x");
		
		//check name of copy
		String copyname = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView//*[@resource-id='com.sphero.sprk:id/program_name' and @index='0']")).getText();
		Assert.assertEquals( copyname, "Copy of " + programname );
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView//*[@resource-id='com.sphero.sprk:id/program_name' and @index='0']")).click();
		//check status of copy
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		//delete copy
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.3), 500);
		driver.findElementById("com.sphero.sprk:id/delete_button").click();
		clickButton("delete");
		
		//in review gate
		for(WebElement temp : programs){
			if(temp.getText().equals("In Review")){
				temp.click();
				break;
			}
		}
		programname = driver.findElementById("com.sphero.sprk:id/dialog_title").getText();
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/share_button").size(),0); //check share button is invisible
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		clickButton("yes"); //continue
		leaveCanvas();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		clickButton("no"); //cancel
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		driver.findElementById("com.sphero.sprk:id/neutral_action").click();
		clickButton("save");
		leaveCanvas();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		clickButton("x");
		
		//check name of copy
		copyname = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView//*[@resource-id='com.sphero.sprk:id/program_name' and @index='0']")).getText();
		Assert.assertEquals( copyname, "Copy of " + programname );
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView//*[@resource-id='com.sphero.sprk:id/program_name' and @index='0']")).click();
		//check status of copy
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		//delete copy
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		driver.swipe((int)(screenWidth*0.75), (int)(screenHeight*0.5), (int)(screenWidth*0.75), (int)(screenHeight*0.3), 500);
		driver.findElementById("com.sphero.sprk:id/delete_button").click();
		clickButton("delete");
		
		//private no gate
		for(WebElement temp : programs){
			if(temp.getText().equals("Private")){
				temp.click();
				break;
			}
		}
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		leaveCanvas();
		clickButton("x");
		
		//rejected no gate
		for(WebElement temp : programs){
			if(temp.getText().equals("Rejected")){
				temp.click();
				break;
			}
		}
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Private");
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/share_button").size(),0); //check share button is invisible
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		leaveCanvas();
		clickButton("x");
		
		
	
	}
	
	
}
