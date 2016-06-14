package start;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class androidInterface implements clickableObjects {
	
	static AndroidDriver driver;
	WebDriverWait wait;
	List<WebElement> allTextView;
	
	//device screen
	int screenHeight;
	int screenWidth;
	
	String connect = "hey";

	public void clickNavBar(String item){
		
		List <WebElement> allNavBar;
		List <WebElement> allTabs;
		
		//click on the nav bar item
		allNavBar = driver.findElements(By.id("com.sphero.sprk:id/bottom_navigation_small_container"));
	
		if (item.toLowerCase().equals("home")){
			allNavBar.get(0).click();
			
			}
		else if (item.toLowerCase().equals("programs")){
			allNavBar.get(1).click();
		}
		else if (item.toLowerCase().equals("activities")){
			allNavBar.get(2).click();
			
		}
		else if (item.toLowerCase().equals("drive")){
			allNavBar.get(3).click();
		}
		else{
			System.out.println("Not a valid item in the nav bar");
		}
		
		System.out.println("Clicked " + item + " in nav bar");
		
		//chcek the name of the button is shown to match expected
		
		if(driver.findElementsById("com.sphero.sprk:id/programs_onboarding_overlay").size()==0){
			driver.findElementById("com.sphero.sprk:id/bottom_navigation_small_item_title").getText().toLowerCase().equals(item);
		}
		
		allTabs = driver.findElements(By.xpath("//android.support.v7.a.d/android.widget.TextView"));
		
		//verify expected tabs
		if(item.toLowerCase().equals("account")){
			allTabs.get(0).getText().toLowerCase().equals("feed");
			allTabs.get(1).getText().toLowerCase().equals("profile");
			allTabs.get(2).getText().toLowerCase().equals("settings");
		}
		else if(item.toLowerCase().equals("programs")){
			allTabs.get(0).getText().toLowerCase().equals("my programs");
			allTabs.get(1).getText().toLowerCase().equals("sphero");
			allTabs.get(2).getText().toLowerCase().equals("community");
		}
		else if(item.toLowerCase().equals("activities")){
			allTabs.get(0).getText().toLowerCase().equals("my activities");
			allTabs.get(1).getText().toLowerCase().equals("sphero");
			allTabs.get(2).getText().toLowerCase().equals("community");
			//allTabs.get(3).getText().toLowerCase().equals("innovators");
		}
	}
	
	public void clickTab(String s){
		s = s.toLowerCase();
		List <WebElement> tabs = driver.findElements(By.xpath("//android.support.v7.a.d/android.widget.TextView"));
		for(WebElement temp:tabs){
			if (temp.getText().toLowerCase().equals(s)){
				temp.click();
				System.out.println("Clicked " + s + " tab");
				if(s.equals("profile")){
					wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/profile_image")));
				}
				Assert.assertEquals(temp.isSelected(), true);
				break;
			}
		}

		
	}
	
	public void clickMenu(){
		driver.findElementByXPath("//*[@class='android.widget.ImageButton' and @index='0']").click();	
		System.out.println("Clicked on menu");
		//Verify the pocket nav is open by checking settings exists
		driver.findElement(By.id("com.sphero.sprk:id/settings"));
		allTextView = driver.findElements(By.id("com.sphero.sprk:id/menu_item_text")); 
	}
	public void clickMenuItem(String s){
		s = s.toLowerCase();
		for (WebElement temp : allTextView) {
			System.out.println(temp.getText());
			if(temp.getText().toLowerCase().equals(s)){
				temp.click();
				System.out.println("Clicked " + s);
				break;
			}	
		}
		//Use a different method to check screen for drive for Tablets since it's a drawer
		if(s.toLowerCase().equals("drive") && screenWidth>screenHeight){
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/background")));
			System.out.println("Tablet with Drive drawer appeared");
		}
		else{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/toolbar_title")));
			//Verify screen is Explore
			String screen = driver.findElementById("com.sphero.sprk:id/toolbar_title").getText();
			Assert.assertEquals(screen.toLowerCase(),s.toLowerCase());
			System.out.println(s + " screen was found");
		}
	}
	
	public void clickButton(String s){
		//Find dialog
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/button_content")));
		s = s.toLowerCase();
		
		String[] positive = {"yes", "ok","okay", "equals","positive","save","delete","done"};
		ArrayUtils.contains(positive,s);

		//Yes
		if(ArrayUtils.contains(positive,s)){
			if(driver.findElementsById("com.sphero.sprk:id/dialog_action").size()==1){
				driver.findElement(By.id("com.sphero.sprk:id/dialog_action")).click();
			}
			else{
			driver.findElementById("com.sphero.sprk:id/positive_action").click();
			}
		}
		//No
		else if(s.equals("no") || s.equals("cancel")  || s.equals("negative")){
			driver.findElementById("com.sphero.sprk:id/negative_action").click();
		}
		//Sign in
		else if(s.equals("sign in") || s.equals("signin")){
			
			if(driver.findElementsByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//*[@resource-id='com.sphero.sprk:id/sign_in_button']").size()==0){
				driver.findElementById("com.sphero.sprk:id/sign_in_button").click();
			}
			else{
				driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/fragment_container']//*[@resource-id='com.sphero.sprk:id/sign_in_button']").click();
			}
		}
		//Sign out
		else if(s.equals("sign out") || s.equals("signout")){
			driver.findElementById("com.sphero.sprk:id/sign_out_button").click();
		}
		else if(s.equals("sign up") || s.equals("signup")){
			driver.findElementById("com.sphero.sprk:id/sign_up_button").click();
		}
		else if(s.equals("back") || s.equals("close") || s.equals("x")){
			if(driver.findElementsById("com.sphero.sprk:id/nav_button").size()==0){
				driver.findElementById("com.sphero.sprk:id/dialog_close").click();
			}
			else{
			driver.findElementById("com.sphero.sprk:id/nav_button").click();
			}
		}
		else if(s.equals("continue")){
			driver.findElementById("com.sphero.sprk:id/continue_button").click();
		}
		else if(s.equals("sphero")|| s.equals("choose sphero")){
			//edit button
			if(driver.findElementsById("com.sphero.sprk:id/sphero_button").size()==1){
				driver.findElementById("com.sphero.sprk:id/sphero_button").click();
			}
			//connect robot button
			else if(driver.findElementsById("com.sphero.sprk:id/choose_sphero_button").size()==1){
				driver.findElementById("com.sphero.sprk:id/choose_sphero_button").click();
			}
			//filter button
			else{
				driver.findElementById("com.sphero.sprk:id/filter_sphero").click();
			}
		}
		else if(s.equals("bb8")|| s.equals("choose bb8")){
			//edit button
			if(driver.findElementsById("com.sphero.sprk:id/bb8_button").size()==1){
				driver.findElementById("com.sphero.sprk:id/bb8_button").click();
			}
			//connect robot button
			else if(driver.findElementsById("com.sphero.sprk:id/choose_bb8_button").size()==1){
				driver.findElementById("com.sphero.sprk:id/choose_bb8_button").click();
			}
			//filter button
			else{
				driver.findElementById("com.sphero.sprk:id/filter_bb8").click();
			}
		}
		else if(s.equals("ollie")|| s.equals("choose ollie")){
			//edit button
			if(driver.findElementsById("com.sphero.sprk:id/ollie_button").size()==1){
				driver.findElementById("com.sphero.sprk:id/ollie_button").click();
			}
			//connect robot button
			else if(driver.findElementsById("com.sphero.sprk:id/choose_ollie8_button").size()==1){
				driver.findElementById("com.sphero.sprk:id/choose_ollie_button").click();
			}
			//filter button
			else{
				driver.findElementById("com.sphero.sprk:id/filter_ollie").click();
			}
		}
		else if(s.equals("sprk+")){
			driver.findElementById("com.sphero.sprk:id/choose_sprk_plus_button").click();
		}
		else if(s.equals("connect robot")){
			driver.findElementById("com.sphero.sprk:id/connect_robot_button").click();
		}
		
		System.out.println("Clicked " + s + " button");
		
	}
	
	public void sendKeys(String s){
		driver.findElementById("com.sphero.sprk:id/edit_text").sendKeys(s);
	}
	public void checkToolBarTitle(String s){
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/toolbar_title")));
		String screen = driver.findElementById("com.sphero.sprk:id/toolbar_title").getText();
		Assert.assertEquals(screen.toLowerCase(),s.toLowerCase());
	}
	
	//Build
	public void clickAddNewProgram(){
		driver.findElementById("com.sphero.sprk:id/floating_add_new_program_button").click();
		//Verify text field appears
		driver.findElementById("com.sphero.sprk:id/edit_text");
	}
	public void clickMyProgramsTab(){
		driver.findElementByXPath("//*[@class='android.support.v7.a.d' and @index='0']").click();
		driver.findElementById("com.sphero.sprk:id/program_image");
		driver.findElementById("com.sphero.sprk:id/floating_add_new_program_button");
	}
	public void clickSampleProgramsTab(){
		driver.findElementByXPath("//*[@class='android.support.v7.a.d' and @index='1']").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
	}
	public void clickProgramEdit(){
		driver.findElementById("com.sphero.sprk:id/overflow_menu").click();
		
	}
	public void clickSave(){
		driver.findElementById("com.sphero.sprk:id/dialog_action").click();
	}
	
	
	//Shared between build and explore
	public void clickProgram(){
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		if(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']//*[@resource-id='com.sphero.sprk:id/program_name']").getText().equals("Canvas Tutorial")){
			System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='1']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
			driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='1']").click();
		}
		else{
			System.out.println(driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[@index='0']//*[@resource-id='com.sphero.sprk:id/program_name']").getText());
			driver.findElementById("com.sphero.sprk:id/program_image").click();
		}
		System.out.println("Clicked a program");
	}
	public void clickCopy(){
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		driver.findElementById("com.sphero.sprk:id/make_a_copy_button").click();
		System.out.println("Clicked Copy");
	}
	public void clickView(){
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		//Click on View
		driver.findElementById("com.sphero.sprk:id/view_button").click();
		System.out.println("Clicked View");
	}
	public void clickClose(){
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/dialog_title")));
		driver.findElementById("com.sphero.sprk:id/dialog_close").click();
		System.out.println("Clicked close");
	}
	public void closeSignIn(){
		
	}

	//Explore
	public void clickExploreTab(){
		driver.findElementByXPath("//*[@class='android.support.v7.a.d' and @index='0']").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		System.out.println("Explore tab clicked");
	}
	public void clickMediaTab(){
		driver.findElementByXPath("//*[@class='android.support.v7.a.d' and @index='1']").click();
		driver.findElementById("com.sphero.sprk:id/floating_add_new_media_button");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/program_image")));
		System.out.println("Media tab clicked");
	}
	public void clickTwitterTab(){
		driver.findElementByXPath("//*[@class='android.support.v7.a.d' and @index='2']").click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sphero.sprk:id/tw__tweet_view")));
		System.out.println("Twitter tab clicked");
	}
	public void clickAddMedia(){
		driver.findElementById("com.sphero.sprk:id/floating_add_new_media_button").click();
		//Check result add media or user gate??
		System.out.println("Add Media button clicked");
	}
	
	//Sign in
	public void clickPocketNavSignIn(){
		driver.findElementById("com.sphero.sprk:id/profile_header_not_signed_in").click();
		//Wait for sign in container to appear
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.sphero.sprk:id/fragment_container")));
		System.out.println("Clicked on Pocket Nav Sign in");
	}
    public void clickSignIn(Boolean actual){
        driver.findElementById("com.sphero.sprk:id/sign_in_button").click();
        System.out.println("Clicked on Sign in button");
        if(actual == true){
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/progress")));
            System.out.println("Sign in progress indicator appeared");
        }
    }
	public void clickProfile(){
		driver.findElementById("com.sphero.sprk:id/profile_header_signed_in").click();
		//Verify sign out button, profile pic exist
		driver.findElementById("com.sphero.sprk:id/sign_out_button");
		driver.findElementById("com.sphero.sprk:id/profile_image");
	}
	public void clickCloseSignIn(){
		//driver.findElementById("com.sphero.sprk:id/close_button").click();
		driver.findElementById("com.sphero.sprk:id/nav_button").click();
	}
	public void clickSignOut(){
		driver.findElementById("com.sphero.sprk:id/sign_out_button").click();
		System.out.println("Clicked on Sign out button");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/content")));
		System.out.println("Sign confirmation appeared");
	}
	
	//Pocketnav
	public void checkConnectRobot(){
		driver.findElementById("com.sphero.sprk:id/connect_robot_button").click();
		//Verify robot buttons exists
		driver.findElementById("com.sphero.sprk:id/choose_sphero_button");
		driver.findElementById("com.sphero.sprk:id/choose_sprk_plus_button");
		driver.findElementById("com.sphero.sprk:id/choose_ollie_button");
		driver.findElementById("com.sphero.sprk:id/choose_bb8_button");
		System.out.println("Connect Sphero clicked and elements found");
	}
	
	public void chooseSphero(){
		driver.findElementById("com.sphero.sprk:id/choose_sphero_button").click();
		//Verify title and close button exists
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect Sphero']");
		driver.findElementById("com.sphero.sprk:id/dialog_close");
		driver.findElementById("com.sphero.sprk:id/find_and_connect_button");
		System.out.println("Connect Sphero clicked and elements found");
	}
	public void chooseOllie(){
		driver.findElementById("com.sphero.sprk:id/choose_ollie_button").click();
		//Verify title and close button exists
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect Ollie']");
		driver.findElementById("com.sphero.sprk:id/dialog_close");
		System.out.println("Connect Ollie clicked and elements found");
	}
	public void chooseBB8(){
		driver.findElementById("com.sphero.sprk:id/choose_bb8_button").click();
		//Verify title and close button exists
		driver.findElementByXPath("//*[@resource-id='com.sphero.sprk:id/dialog_title' and @text='Connect BB-8']");
		driver.findElementById("com.sphero.sprk:id/dialog_close");
		System.out.println("Connect BB8 clicked and elements found");
	}
	public void clickSettings(){
		
	}
	

}
