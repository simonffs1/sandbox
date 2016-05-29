package start;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidKeyCode;

public class robotConnection extends Testscript {

	@Test
	public void showBluetoothDialog(){
		testLogTitle("Bluetooth dialog prompt");
		
		clickNavBar("Home");
		driver.findElementById("com.sphero.sprk:id/connect_robot_button").click();
		
		driver.findElementByName("Bluetooth Not Enabled");
		//dismiss bluetooth
		clickButton("no");
	}
	
	
	@Test 
	public void connectRobot() throws Exception{
		testLogTitle("Connect BB-8");
		
		WebDriverWait waitFirmware = new WebDriverWait(driver,120);
		clickNavBar("home");
		try{
			driver.findElementById("com.sphero.sprk:id/header");
			System.out.println("Robot already connected");
		}
		catch(Exception e){
		System.out.println("Robot not connected");
		//Click Connect a Robot
		clickConnectRobot();
		//Click Sphero
		try{
			chooseSphero();
		}
		catch(Exception f){
			Assert.fail("Missing elements in Connect Sphero");
		}
		clickClose();
		//Click Ollie
		
		try{
			chooseOllie();
		}
		catch(Exception g){
			Assert.fail("Missing elements in Connect Ollie");
		}
		clickClose();
		//Click BB-8
		try{
			chooseBB8();
		}
		catch(Exception h){
			Assert.fail("Missing elements in Connect BB-8");
		}
		
		waitFirmware.until(ExpectedConditions.invisibilityOfElementWithText(By.id("com.sphero.sprk:id/dialog_title"), "Connect BB-8"));
		clickMenu();
		
		driver.findElementById("com.sphero.sprk:id/header");
		System.out.println("Robot is connected");
		//Close pocket nav
		driver.pressKeyCode(AndroidKeyCode.BACK);
		}
	}
	
	@Test(dependsOnMethods={"connectRobot"})
	public void renameRobot(){
		testLogTitle("Rename Robot");
		
		clickMenu();
		String originalName = driver.findElementById("com.sphero.sprk:id/robot_name").getText();
		driver.findElementById("com.sphero.sprk:id/edit_robot_name").click();
		driver.findElementById("com.sphero.sprk:id/edit_text").clear();
		driver.findElementById("com.sphero.sprk:id/edit_text").sendKeys("Auto!");
		driver.findElementById("com.sphero.sprk:id/buttonDefaultPositive").click();
		String newName = driver.findElementById("com.sphero.sprk:id/robot_name").getText();
		Assert.assertNotEquals(newName, originalName);
		System.out.println("Robot name changed");
		driver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
	@Test //(dependsOnMethods={"connectRobot"},groups="connection")
	public void firmwareUpdate() throws Exception{
		testLogTitle("Firmware update");
		
		WebDriverWait waitFirmware = new WebDriverWait(driver,120);
		//Go to Settings
		driver.findElementByXPath("//*[@class='android.widget.ImageButton' and @index='0']").click();
		driver.findElementById("com.sphero.sprk:id/settings").click();
		String screen = driver.findElementById("com.sphero.sprk:id/toolbar_title").getText();;
		Assert.assertEquals(screen,"Settings");
		//Verified on Settings screen
		driver.findElementById("com.sphero.sprk:id/firmware_update_button").click();
		driver.findElementById("com.sphero.sprk:id/buttonDefaultPositive").click();
		
		System.out.println("Settings found, initiating firmware update");
		
		while(!driver.findElementById("com.sphero.sprk:id/toolbar_title").getText().equals("Build")){
			Thread.sleep(500);
			driver.pressKeyCode(AndroidKeyCode.BACK);
		}
		
		System.out.println("Firmware complete");
		/*		Thread.sleep(50000); //wait 40 seconds
		
		waitFirmware.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.sphero.sprk:id/toolbar_title")));
		screen = driver.findElementById("com.sphero.sprk:id/toolbar_title").getText();;
		Assert.assertEquals(screen,"Settings");
		System.out.println("Returned back to settings, firmware update complete");
		Thread.sleep(2000);
		driver.pressKeyCode(AndroidKeyCode.BACK); //return to build*/
		
		
	}
	
	@Test (dependsOnMethods={"firmwareUpdate"})
	public void disconnectRobot() throws Exception{
		testLogTitle("Disconnect BB-8");
		
		clickMenu();

		try{
			//Click Connect a Robot
			driver.findElementById("com.sphero.sprk:id/header").click();
			System.out.println("Robot is connected");
			//Disconnect by tapping Sphero
			chooseSphero();
			driver.findElementById("com.sphero.sprk:id/dialog_close").click();
			driver.findElementById("com.sphero.sprk:id/dialog_close").click();
			//Open pocketnav
			Thread.sleep(5000);
			clickMenu();
			driver.findElementById("com.sphero.sprk:id/header_not_connected");
			System.out.println("Robot is disconnected");
		}
		catch(Exception e){
			System.out.println("Robot already disconnected");
		}
		//Close pocketnav
		driver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
}
