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
	
	@Test
	public void createProgram(){
		testLogTitle("Create a program");
		int initialprograms = countProgram();
		System.out.println(initialprograms);
		clickAddNewProgram();
		sendKeys("Program created by automation!");
		//Tap Save
		clickButton("yes");
		leaveCanvas();
		checkToolBarTitle("Build");
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
		testLogTitle("Copy My Programs while signed in");
		int numOfProg = countProgram();
		clickProgram();
		driver.findElementById("com.sphero.sprk:id/copy_button").click();
		clickButton("yes"); //confirm
		clickButton("yes"); //ok
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		//driver.findElementByName("Programming Tutorial");
		Assert.assertEquals(countProgram(), numOfProg+1);
		System.out.println("Program has been copied");
		//*check name 
	}
	
	@Test
	public void editMyProgram() throws Exception{
		
		
		testLogTitle("Edit My Programs while signed in");
		
		//Check Edit title
		//String fragmentTitle = driver.findElement(By.id("com.sphero.sprk:id/dialog_title")).getText();
		//Assert.assertEquals(fragmentTitle, "Edit");
		
		clickProgram();
		
		boolean wasPublic = false;
		uploadSuccess=false;
		driver.findElementById("com.sphero.sprk:id/edit_button").click();
		//aattach image
		driver.findElementById("com.sphero.sprk:id/add_media_button").click();
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
		
		//current status
		String currentStatus = driver.findElementById("com.sphero.sprk:id/status_text").getText();
		//Click switch and help icon
		driver.findElementById("com.sphero.sprk:id/program_public_switch").click(); //*Keep track of switch status later
		String postStatus = driver.findElementById("com.sphero.sprk:id/status_text").getText();
		if(driver.findElementById("com.sphero.sprk:id/program_public_switch").getText().equals("OFF")){
			wasPublic = true;
			Assert.assertEquals(postStatus, "Private");
		}
		else if (driver.findElementById("com.sphero.sprk:id/program_public_switch").getText().equals("ON")){
			Assert.assertEquals(postStatus, "In Review");
		}
		driver.findElementById("com.sphero.sprk:id/public_program_help").click();
		clickButton("yes");
		
		//Click robots
		driver.findElementById("com.sphero.sprk:id/choose_sphero").click();
		driver.findElementById("com.sphero.sprk:id/choose_ollie").click();
		driver.findElementById("com.sphero.sprk:id/choose_bb8").click();
		
		if(screenHeight>screenWidth){
			scrollVertical("down",500);
		}
		
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
		
		
		waitLong.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/toolbar_title")));
		//check if able to go back to build, if not upload failed
		if(driver.findElementsById("com.sphero.sprk:id/share_container").size()==0){
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

		//touch.press(10,(driver.manage().window().getSize().getHeight())/2);
		//*Add discard dialog test case here later
		//Tap the left most middle area of the screen (TABLET ONLY)
	}
	
	
	@Test 
	public void deleteMyProgramSignIn(){
		testLogTitle("Delete Programs while signed in");
		int numOfProg = countProgram();
		driver.findElementById("com.sphero.sprk:id/overflow_menu").click();
		//Check Edit title
		String fragmentTitle = driver.findElement(By.id("com.sphero.sprk:id/dialog_title")).getText();
		Assert.assertEquals(fragmentTitle, "Edit");
		driver.findElementById("com.sphero.sprk:id/delete_button").click();
		clickButton("yes");
		checkToolBarTitle("Build");
		Assert.assertEquals(countProgram(), numOfProg-1);
		System.out.println("Program has been deleted");
		//*check name
	}
	
	@Test
	public void deleteRenameProgramSignedOut(){
		testLogTitle("Delete Programs while signed in");
		int initialprograms = countProgram();
		System.out.println(initialprograms);
		driver.findElementById("com.sphero.sprk:id/overflow_menu").click();
		//Tap Rename
		driver.findElementByXPath("//*[@class='android.widget.LinearLayout' and @index='0']").click();
		driver.findElementById("com.sphero.sprk:id/edit_text").clear();
		sendKeys("Program renamed by automation!");
		clickButton("yes");
		driver.findElementById("com.sphero.sprk:id/overflow_menu").click();
		//Tap Delete and confirm
		driver.findElementByXPath("//*[@class='android.widget.LinearLayout' and @index='1']").click();
		clickButton("yes");
		System.out.println(countProgram());
		Assert.assertEquals(countProgram(),initialprograms-1);
		System.out.println("Program deleted successfully");
	}
	
	//@Test //(dependsOnMethods={'signIn'}
	public void deleteAllPrograms(){
		while (countProgram()>1){
			deleteMyProgramSignIn();
		}
		System.out.println("All programs deleted");
	}
	
	@Test 
	public void deleteAll(){
		testLogTitle("Delete Programs while signed in");
		int programCount = 0;
		while(countProgram()>1){
		driver.findElementById("com.sphero.sprk:id/overflow_menu").click();
		String fragmentTitle = driver.findElement(By.id("com.sphero.sprk:id/dialog_title")).getText();
		Assert.assertEquals(fragmentTitle, "Edit");
		driver.findElementById("com.sphero.sprk:id/delete_button").click();
		clickButton("yes");
		checkToolBarTitle("Build");
		programCount++;
		//Assert.assertEquals(countProgram(), numOfProg-1);
		}
		System.out.println("Deleted all "+programCount+" Programs");
	}
	
	@Test 
	public void deleteAllProgramsSignedOut() throws Exception{
		while (countProgram()>1){
			driver.findElementById("com.sphero.sprk:id/overflow_menu").click();
			driver.findElementByXPath("//*[@class='android.widget.LinearLayout' and @index='1']").click();
			driver.findElementById("com.sphero.sprk:id/buttonDefaultPositive").click();
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
	public void publicProgramGate(){
		
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
		dismissHint();
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
