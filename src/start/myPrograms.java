package start;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidKeyCode;

public class myPrograms extends Testscript {

	@Test
	public void SortBy(){
		clickNavBar("programs");
		driver.findElementById("com.sphero.sprk:id/spinner_text").click();
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/spinner_text' and @text='Date Oldest']").click();
	}
	
	//Unit test for counting programs
	public int countProgram(){
		String xpath = "//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout";
		List <WebElement> ListByXpath = driver.findElements(By.xpath(xpath));
		return ListByXpath.size();   
	}
	
	//@Test
	public void countPrograms(){
		String xpath = "//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout";
		List <WebElement> ListByXpath = driver.findElements(By.xpath(xpath));
		System.out.println(ListByXpath.size());
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
			//check for no proggrams message
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
		if(signedIn==true){
			driver.findElement(By.id("com.sphero.sprk:id/uploading_spinner"));
			System.out.println("Sync indicator found");
		}
	}
	
	
	@Test 
	public void copyMyProgram(){
		clickNavBar("Programs");
		
		testLogTitle("Copy My Programs while signed in");
		int numOfProg = countProgram();
		clickProgram(); //click first program
		driver.findElementById("com.sphero.sprk:id/copy_button").click();
		clickButton("save"); //confirm
		clickButton("ok"); //ok
		clickButton("x");
		
		//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		Assert.assertEquals(countProgram(), numOfProg+1);
		System.out.println("Program copied successfully");
		//*check name 
	}
	
	@Test
	public void editMyProgram() throws Exception{
		
		
		testLogTitle("Edit My Programs while signed in");
		
		
		clickNavBar("Programs");
		
		clickProgram();
		
		boolean wasPublic = false;
		uploadSuccess=false;
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		//aattach image
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
	public void deleteRenameProgramSignedOut() throws Exception{
		testLogTitle("Delete Programs while signed in");
		
		clickNavBar("programs");
		
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
	public void viewProgramStatus(){
		
		WebElement publicProgram = driver.findElement(By.id("com.sphero.sprk:id/program_image"));
		
		
		List<WebElement> programs = driver.findElements(By.xpath("//android.support.v7.widget.RecyclerView/descendant::android.widget.FrameLayout"));
		for(WebElement temp : programs){
			System.out.println(temp.getText());
		}
		
		checkPublicProgramOrder();
		
		//overflow button for public
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']//android.widget.ImageButton")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Public"); //check status
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/program_public_switch")).getAttribute("checked"), "true");//check switch status
		driver.findElement(By.id("com.sphero.sprk:id/share_button")); //check visibility of share btuton
		clickSphero();
		clickSave();
		clickButton("no");
		clickSphero();
		clickClose();
		
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='1']//android.widget.ImageButton")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "Rejected");
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/program_public_switch")).getAttribute("checked"), "false");
		clickClose();
		
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='2']//android.widget.ImageButton")).click();
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/status_text").getText(), "In Review");
		Assert.assertEquals(driver.findElement(By.id("com.sphero.sprk:id/program_public_switch")).getAttribute("checked"), "true");
		clickSphero();
		clickSave();
		clickButton("no");
		clickSphero();//undo
		clickClose();

	}
	
	public void checkPublicProgramOrder(){
		String program0 = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']//android.widget.TextView[@index='0']")).getText();
		String program1 = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='1']//android.widget.TextView[@index='0']")).getText();
		String program2 = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='2']//android.widget.TextView[@index='0']")).getText();
		String program3 = driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='3']//android.widget.TextView[@index='0']")).getText();
		
		//0 public, 1 rejected, pos 2 : in review , pos3: video
		Assert.assertEquals(program0, "public");
		Assert.assertEquals(program1, "rejected");
		Assert.assertEquals(program2, "inreview");
		Assert.assertEquals(program3, "video");
	}
	
	@Test
	public void publicProgramGate() throws Exception{
		
		checkPublicProgramOrder();
		//overflow button for public
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']")).click();
		clickButton("yes"); //go to canvas
		leaveCanvas();
		checkToolBarTitle("Build");
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']")).click();
		clickButton("no"); //cancel
		
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='1']")).click();
		leaveCanvas();
		checkToolBarTitle("Build");
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='2']")).click();
		clickButton("yes"); //continue
		leaveCanvas();
		checkToolBarTitle("Build");
		driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='2']")).click();
		clickButton("no"); //cancel	
		checkPublicProgramOrder();
	}
	
	//Check unauthenticated user gate prompt visibility
	@Test
	public void userGates(){
		
		testLogTitle("User gates from signed out");
		
		clickMenu();
		clickMenuItem("Explore");
		//goToActivity("explore");
		
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/toolbar_title")));
		
		//Click on the first program
		clickProgram();
		//Wait for the drawer to appear
		clickView();
		driver.findElementById("com.sphero.sprk:id/content");
		System.out.println("View User Gate Present");
		clickButton("no");
		System.out.println("Clicked Maybe Later");
		//Click on Make a Copy
		clickCopy();
		driver.findElementById("com.sphero.sprk:id/content");
		System.out.println("Copy User Gate Present");
		clickButton("no");
		System.out.println("Clicked Maybe Later");
		
		clickClose();
		clickMediaTab();
		clickAddMedia();
		driver.findElementById("com.sphero.sprk:id/content");
		System.out.println("Media User Gate Present");
		clickButton("no");
		clickMenu();
		clickMenuItem("build");
	}
	
}
