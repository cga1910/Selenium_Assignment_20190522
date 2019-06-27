package testSuite;

import base.*;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.List;

// TODO: 2019-06-27 Put all xpaths in the properties file?

public class Test_2019_0522_0627 extends Base_ {

  @Test (priority = 1)
  public void test1_getPage() throws InterruptedException {
    openBrowser(prop.getProperty("browser"));
    navigate(prop.getProperty("home_URL"));
    // Give time for manual login, if not using the automatic login below
    // Thread.sleep(60000);
  }

  @Test (priority = 2)
  public void test2_login() {
    String loginUser = prop.getProperty("loginUser");
    String loginPass = prop.getProperty("loginPass");
    List<WebElement> elementList;

    elementList = driver.findElements(By.xpath("//a[@data-tracking-control-name='guest_homepage-basic_nav-header-signin']"));
    if (elementList.size() == 0) { // No button --> assume normal login page
      driver.findElement(By.id("login-email")).sendKeys(loginUser);
      driver.findElement(By.id("login-password")).sendKeys(loginPass);
      driver.findElement(By.xpath("//input[@id='login-submit']")).click();
    } else { // Assume alternative login page
      elementList.get(0).click();
      driver.findElement(By.xpath("//input[@id='username']")).sendKeys(loginUser);
      driver.findElement(By.xpath("//input[@id='password']")).sendKeys(loginPass);
      driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
  }

  @Test (priority = 3)
  public void test3_navigateTo_myNetwork() {
    driver.findElement(By.xpath("//a[@data-link-to='mynetwork']")).click();
  }

  @Test (priority = 4)
  public void test4_navigateTo_contacts() {
    driver.findElement(By.xpath("//a[@data-control-name='connections']")).click();
  }

  @Test (priority = 5)
  public void test5_searchContacts() throws InterruptedException {
    List<WebElement> elementList;
    int queryHitCount;
    String searchString = prop.getProperty("contactQuery");

    driver.findElement(By.xpath("//input[@type='search']")).sendKeys(searchString);
    Thread.sleep(5000); // There is probably a better way to wait here
    queryHitCount = driver.findElements(By.xpath("//a[@class='mn-connection-card__link ember-view']")).size();
    System.out.println("-- Number of hits for \"" + searchString + "\": " + queryHitCount);

    elementList = driver.findElements(By.xpath("//a[@class='mn-connection-card__link ember-view']/span[2]"));
    try {
      for (WebElement element : elementList) {
        System.out.println(element.getText());
      }
    } catch (StaleElementReferenceException e) {
      //e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  @Test (priority = 6)
  void test6_findContactByPartialName() {
    String partialName = prop.getProperty("contactToFind");
    System.out.println("-- Searching for a span element containing the text: " + partialName);
    //String foundName = driver.findElement(By.xpath("//span[contains(text(),'"+partialName+"')]")).getText();
    String foundName = driver.findElement(By.xpath("//span[text()[contains(.,'"+partialName+"')]]")).getText();
    System.out.println("-- The found element contains the extracted text: " + foundName);
  }

  @AfterClass
  void afterClass() throws InterruptedException {
    System.out.println("-- @AfterClass: Waiting x seconds before quit");
    Thread.sleep(10000);
    driver.quit();
  }

}