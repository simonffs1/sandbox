package start;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Workbook extends communityActivities {

	@Test
	public void ActivityAssociation(){
		
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		clickNavBar("Activities");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/image")));
		
		String activityname = driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']//*[@resource-id='com.sphero.sprk:id/name']").getText();
		System.out.println(activityname);
		driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		String stepbutton = driver.findElementById("com.sphero.sprk:id/view_steps_button").getText();
		Assert.assertEquals(stepbutton, "Continue Activity");
		
		String username = driver.findElementById("com.sphero.sprk:id/name").getText();
		driver.findElementsById("com.sphero.sprk:id/progress_section");
		
		clickButton("x");
		
		if(username.equals("Sphero")){
			clickTab("Sphero");
		}
		else{
			clickTab("Community");
		}
		
		//relative layout is used in community...
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		
		for(int i=0;i<4;i++){
			if(driver.findElementsByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@text='"+activityname +"']").size()==1){
				System.out.println("Card " + i + " is Default Program");
				//System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
				driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']").click();
				break;
			}
			else{
				System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
				System.out.println("Card " + i + " is not " + activityname);
			}
			//if no program is clicked, scroll down and reset index to 0
			if(i == 3){
				driver.swipe((int)(screenWidth*0.50), (int)(screenHeight*0.5), (int)(screenWidth*0.50), (int)(screenHeight*0.25), 500);
				i = -1;
			}
		}

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/view_steps_button").getText(), "Continue Activity");
		driver.findElementsById("com.sphero.sprk:id/progress_section");
		clickButton("x");
		
		System.out.println("Program association confirmed for program " + activityname);
		
		
	}
	
	@Test
	public void findActivity(){
		/*
		clickNavBar("Activities");
		clickTab("Community");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		
		while(driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/program_name' and text='Default Program']").size()==0){
			driver.swipe((int)(screenWidth*0.50), (int)(screenHeight*0.5), (int)(screenWidth*0.50), (int)(screenHeight*0.30), 700);
		}
		System.out.println("Found Default Program");
		*/
		
		clickNavBar("Activities");
		clickTab("Community");
		
		for(int i=0;i<4;i++){
			if(driver.findElementsByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@text='Default Program']").size()==1){
				System.out.println("Card " + i + " is Default Program");
				//System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
				driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']").click();
				break;
			}
			else{
				System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
				System.out.println("Card " + i + " is not default program");
			}
			//if no program is clicked, scroll down and reset index to 0
			if(i == 3){
				driver.swipe((int)(screenWidth*0.50), (int)(screenHeight*0.5), (int)(screenWidth*0.50), (int)(screenHeight*0.25), 500);
				i = -1;
			}
		}
	}
	
	@Test
	public void newActivity(){
		
		if(signedIn==false){
			signIn("ffsi4","yyyyyy","instructor",true,false,false);
		}
		clickNavBar("Activities");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/image")));
		
		Set <String> activity = new HashSet<String>();
		
		//String activityname = driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.FrameLayout[@index='0']//*[@resource-id='com.sphero.sprk:id/name']").getText();
	
		List <WebElement> activitynames =  driver.findElementsById("com.sphero.sprk:id/name");
		
		for(WebElement temp:activitynames){
			activity.add(temp.getText());
		}
		
		clickTab("Community");
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		
		//String program = "";
		
		//find a program not matching anything in workbook
		for(int i=0;i<4;i++){
			if(activity.contains(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText())==false){
				System.out.println("Card " + i + " is not in Workbook");
				System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
				driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='"+i+"']").click();
				break;
			}
			else{
				
				System.out.println("Card " + i + " is already in workbook");
			}
			//if no program is clicked, scroll down and reset index to 0
			if(i == 3){
				scrollVertical("down",5000);
				i = 0;
			}
		}
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		Assert.assertEquals(driver.findElementById("com.sphero.sprk:id/view_steps_button").getText(), "View Steps");
		Assert.assertEquals(driver.findElementsById("com.sphero.sprk:id/progress_section").size(), 0);
		driver.findElementById("com.sphero.sprk:id/view_steps_button").click();
		driver.findElementByXPath("//android.widget.TextView[@text='0% Completed']");
		int numSteps = driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/page_indicator_container']//android.widget.ImageView").size();
		System.out.println("There are " + numSteps + " steps");
		
		boolean select = false;
		int numPages = 0;
		
		if(numSteps==1){
			select = driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/page_indicator_container']//android.widget.ImageView[@index='0']").isSelected();
			Assert.assertEquals(select, true);
			numPages = driver.findElementsByXPath("//android.support.v4.view.ViewPager/android.widget.FrameLayout").size();
			Assert.assertEquals(numPages, 1);
			System.out.println("Only one step");
		}
		else{
			for(int i=0;i<numSteps;i++){
				select = driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/page_indicator_container']//android.widget.ImageView[@index='" + i + "']").isSelected();
				Assert.assertEquals(select, true);
				driver.swipe((int)(screenWidth*0.5), (int)(screenHeight*0.5), (int)(screenWidth*0.25), (int)(screenHeight*0.5), 200);
			}
			if(driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/page_indicator_container']//android.widget.ImageView[@index='" + (numSteps-1) + "']").isSelected()==true){
				numPages = driver.findElementsByXPath("//android.support.v4.view.ViewPager/android.widget.FrameLayout").size();
				Assert.assertEquals(numPages, 2); //at the end
				System.out.println("At the last step");
			}
		}
		
		driver.findElementById("com.sphero.sprk:id/close_button").click();
		
		
		clickButton("x");
		
		;
		
	}
	
}
