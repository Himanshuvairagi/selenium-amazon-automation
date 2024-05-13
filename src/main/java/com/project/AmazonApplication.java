package com.project;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AmazonApplication {

	public static void main(String[] args) throws InterruptedException {

		AmazonApplication amazone1 = new AmazonApplication();

		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Email or mobile phone number for Amazon login - ");
		String phoneNumber = sc.next();
		System.out.print("Enter password - ");
		String password = sc.next();
		sc.nextLine();
		System.out.print("Enter a product name which you want to search- ");
		String searchProduct = sc.nextLine();
		System.out.print("How many products you want to add- ");
		int addQuanity = sc.nextInt();
		sc.nextLine();
		System.out.print("Which products you want to delete from card- ");
		String deleteItemFromCard = sc.nextLine();

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();

		driver.get("https://www.amazon.in/");

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		amazone1.login(driver, phoneNumber, password);
		amazone1.addToCard(driver, searchProduct, addQuanity);
		Thread.sleep(2000);

		amazone1.deleteProductFromCard(driver, deleteItemFromCard);

		Thread.sleep(2000);
		amazone1.addAddress(driver);

	}

	public void login(WebDriver driver, String phoneNumber, String password) {

		driver.findElement(By.id("nav-link-accountList-nav-line-1")).click();
		driver.findElement(By.id("ap_email")).sendKeys(phoneNumber);
		driver.findElement(By.cssSelector("input[id='continue']")).click();
		driver.findElement(By.id("ap_password")).sendKeys(password);
		driver.findElement(By.id("signInSubmit")).click();

	}

	public void addToCard(WebDriver driver, String searchProduct, int addQuanity) throws InterruptedException {

		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchProduct);

		driver.findElement(By.id("nav-search-submit-button")).click();

		String[] productDetail = searchProduct.split(" ");

		List<WebElement> searchResults = driver.findElements(By.xpath(
				"//h2[@class='a-size-mini a-spacing-none a-color-base s-line-clamp-2']/a/span[@class='a-size-medium a-color-base a-text-normal']"));

		int addedQuantity = 0;

		for (WebElement webElement : searchResults) {

			String productTitle = webElement.getText();

			boolean allStringsFound = true;

			for (String stringSearching : productDetail) {
				String lowerCaseStringSearching = stringSearching.toLowerCase();
				String lowerCaseProductTitle = productTitle.toLowerCase();

				if (!lowerCaseProductTitle.contains(lowerCaseStringSearching)) {
					allStringsFound = false;
					break;
				}
			}

			if (allStringsFound) {
				try {
					WebElement addToCartElement = webElement.findElement(By.xpath("./../../../..//span/button"));

					addToCartElement.click();
					addedQuantity++;

				} catch (Exception e) {
				}

				if (addedQuantity == addQuanity) {
					break;
				}
			}
			Thread.sleep(2000);
		}
		System.out.println("Total items added to cart :: " + addedQuantity);
	}

	public void deleteProductFromCard(WebDriver driver, String deleteItemFromCard) throws InterruptedException {
		driver.findElement(By.id("nav-cart-count")).click();
		Thread.sleep(2000);
		List<WebElement> addedCardItems = driver.findElements(By.xpath(
				"//div[@data-name='Active Items']//span[@class='a-truncate sc-grid-item-product-title a-size-base-plus']"));
		boolean productFoundForDelete = true;

		String[] arrDeleteItemFromCard = deleteItemFromCard.split(" ");
		int deleteQuantity = 0;

		for (WebElement element : addedCardItems) {
			String deleteProductTitle = element.getText();
			boolean allDeleteStringFound = true;

			for (String searchString : arrDeleteItemFromCard) {
				String lowerCaseSearchString = searchString.toLowerCase();
				String lowerCaseDeleteProductTitle = deleteProductTitle.toLowerCase();
				if (!lowerCaseDeleteProductTitle.contains(lowerCaseSearchString)) {
					allDeleteStringFound = false;
					break;
				}
			}
			if (allDeleteStringFound) {
				WebElement deleteProduct = element.findElement(
						By.xpath("./ancestor::div[@class='sc-item-content-group']/descendant::input[@value='Delete']"));
				deleteProduct.click();
				deleteQuantity++;
				productFoundForDelete = false;
			}
			if (deleteQuantity >= 1) {
				break;
			}
		}

		if (productFoundForDelete) {
			System.out.println("Product not found in the card for delete.");
		}

	}

	public void addAddress(WebDriver driver) throws InterruptedException {
		driver.findElement(By.id("sc-buy-box-ptc-button")).click();
		driver.findElement(By.id("add-new-address-popover-link")).click();
		driver.findElement(By.id("address-ui-widgets-enterAddressFullName")).sendKeys("Himanshu");
		driver.findElement(By.id("address-ui-widgets-enterAddressPhoneNumber")).sendKeys("9680201344");
		driver.findElement(By.id("address-ui-widgets-enterAddressPostalCode")).sendKeys("122010");
		driver.findElement(By.id("address-ui-widgets-enterAddressLine1")).sendKeys("Hub, K10A, DLF Cyber City");
		driver.findElement(By.id("address-ui-widgets-enterAddressLine2")).sendKeys("DLF Phase 3, Sector 24");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@aria-labelledby='address-ui-widgets-form-submit-button-announce']"))
				.click();
	}

}
