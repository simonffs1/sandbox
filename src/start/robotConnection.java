package start;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.NetworkConnectionSetting;
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
	public void navigateConnectRobot() throws Exception{
		testLogTitle("Navigate Connect Robot screens");

		clickNavBar("home");


		//check if robot is connected
		
		//Click Connect a Robot
		clickButton("connect robot");
		
		if(driver.findElementsById("com.sphero.sprk:id/dialog_title").size()==1){
			//bluetooth dialog appears, click yes
			System.out.println("Bluetooth Not Enable dialog appeared");
			clickButton("yes");
		}
		
		//check text
		driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView[@text='Choose Robot']");
		driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView[@text='Tap a Robot to Connect']");
		driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView[@text='Sphero/SPRK']");
		driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView[@text='SPRK+']");
		driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView[@text='BB-8']");
		driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView[@text='Ollie']");
		
		
		//check button layouts
		driver.findElementById("com.sphero.sprk:id/choose_sphero_button");
		driver.findElementById("com.sphero.sprk:id/choose_sprk_plus_button");
		driver.findElementById("com.sphero.sprk:id/choose_ollie_button");
		driver.findElementById("com.sphero.sprk:id/choose_bb8_button");
		System.out.println("All robot buttons found");
		
		//Click Sphero
		clickButton("Sphero");
		//check text
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect Sphero']");
		driver.findElementByXPath("//android.widget.TextView[@text='To wake, double tap with your fingers.']");
		driver.findElementByXPath("//android.widget.TextView[@text='Sphero not connecting?']");
		// close button exists
		driver.findElementById("com.sphero.sprk:id/tap_to_wake_image");
		driver.findElementById("com.sphero.sprk:id/find_and_connect_button");
		System.out.println("Connect Sphero elements found");
		clickButton("back");

		//Check SPRK+
		clickButton("sprk+");
		//check text
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect SPRK+']");
		driver.findElementByXPath("//android.widget.TextView[@text='To connect, hold your device to SPRK+']");
		driver.findElementByXPath("//android.widget.TextView[@text='Still having trouble?']");
		// close button exists
		driver.findElementById("com.sphero.sprk:id/sprk_plus_help_image");
		driver.findElementById("com.sphero.sprk:id/still_having_trouble_image");
		System.out.println("Connect SPRK+ elements found");
		clickButton("back");
		
		//Check BB-8
		clickButton("BB8");
		//check text
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect BB-8']");
		driver.findElementByXPath("//android.widget.TextView[@text='To connect, hold your device to BB-8']");
		//check 3 images
		List <WebElement> BB8images = driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView");
		Assert.assertEquals(BB8images.size(), 3);
		System.out.println("Connect BB-8 elements found");
		clickButton("back");
		
		//Check Ollie
		clickButton("Ollie");
		//check text
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect Ollie']");
		driver.findElementByXPath("//android.widget.TextView[@text='To connect, hold your device to Ollie']");
		driver.findElementByXPath("//android.widget.TextView[@text='Still having trouble?']");
		driver.findElementByXPath("//android.widget.TextView[@text='Wake Ollie']");
		driver.findElementByXPath("//android.widget.TextView[@text='Plug your Ollie into USB Power, look for a green light to indicate your robot has a full charge.']");
		driver.findElementByXPath("//android.widget.TextView[@text='Disconnect USB Cable']");
		driver.findElementByXPath("//android.widget.TextView[@text='Before playing with Ollie, you must disconnect it from its USB power source.']");
		//check 3 images
		List <WebElement> Ollieimages = driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//android.widget.ImageView");
		Assert.assertEquals(Ollieimages.size(), 3);
		System.out.println("Connect Ollie elements found");
		clickButton("back");
		clickButton("x");
		
		//Check drawer is hidden;
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/fragment_container").size(), 0);
		
	}
	
	@Test
	public void connectBB8(){
		clickNavBar("home");
		
		WebDriverWait waitFirmware = new WebDriverWait(driver,120);
		
		clickButton("connect robot");
		
		try{
			clickButton("yes");
		}
		catch(Exception f){
			System.out.println("Bluetooth already enabled");
		}
		
		driver.findElementById("com.sphero.sprk:id/choose_bb8_button").click();
		
		waitFirmware.until(ExpectedConditions.invisibilityOfElementWithText(By.id("com.sphero.sprk:id/dialog_title"), "Connect BB-8"));
		
		//check button
		
		System.out.println("Robot is connected");
	}
	
	@Test(dependsOnMethods={"connectRobot"})
	public void renameRobot(){
		testLogTitle("Rename Robot");
		
		clickNavBar("home");
		String originalName = driver.findElementById("com.sphero.sprk:id/robot_name").getText();
		driver.findElementById("com.sphero.sprk:id/edit_robot_name").click();
		driver.findElementById("com.sphero.sprk:id/edit_text").clear();
		driver.findElementById("com.sphero.sprk:id/edit_text").sendKeys("Auto!");
		driver.findElementById("com.sphero.sprk:id/buttonDefaultPositive").click();
		String newName = driver.findElementById("com.sphero.sprk:id/robot_name").getText();
		Assert.assertNotEquals(newName, originalName);
		System.out.println("Robot name changed");
	}
	
	@Test //(dependsOnMethods={"connectRobot"},groups="connection")
	public void firmwareUpdate() throws Exception{
		testLogTitle("Firmware update");
		
		WebDriverWait waitFirmware = new WebDriverWait(driver,120);
		//Go to Settings
		clickNavBar("home");
		//check robot connection
		
		clickTab("Settings");

		driver.findElementById("com.sphero.sprk:id/firmware_update_button").click();
		driver.findElementById("com.sphero.sprk:id/buttonDefaultPositive").click();
		
		System.out.println("Settings found, initiating firmware update");
		
		while(!driver.findElementByXPath("//android.support.v7.a.d/android.widget.TextView[@text='Feed']").isSelected()){
			Thread.sleep(1000);
			driver.findElementByXPath("//android.support.v7.a.d/android.widget.TextView[@text='Feed']").click();
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
	
	@Test
	public void disconnectRobot(){
		testLogTitle("Disconnect Robot");
		
		clickNavBar("home");
			//Click Connect a Robot
		driver.findElementById("com.sphero.sprk:id/header").click();
		System.out.println("Robot is connected");
		//Disconnect by tapping Sphero
		driver.findElementById("com.sphero.sprk:id/choose_sphero_button");
		clickButton("back");
		clickButton("x");
		driver.findElementById("com.sphero.sprk:id/connect_robot_button");
		System.out.println("Robot is disconnected");
	}
	
}
