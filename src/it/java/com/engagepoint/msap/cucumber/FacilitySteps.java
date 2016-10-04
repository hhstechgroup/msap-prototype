package com.engagepoint.msap.cucumber;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.getWebDriverLogs;
import static com.codeborne.selenide.Selenide.sleep;

public class FacilitySteps {

    private final UserStepDefs userStepDefs = new UserStepDefs();

    @When("^search '(.*)' in address search in modal window and apply$")
    public void search_in_address_search_modal_window_and_apply(String address) throws Throwable {
        userStepDefs.click_xpath_and_wait(".//*[@class='ch-modal']//*[@class='leaflet-pelias-input']");
        $(By.xpath(".//*[@class='ch-modal']//*[@class='leaflet-pelias-input']")).setValue(address);
        sleep(3000);
        $(By.xpath(".//*[@class='ch-modal']//ul/li/*[contains(text(),'" + address + "')]")).shouldBe(visible);
        userStepDefs.click_xpath_and_wait(".//*[@class='ch-modal']//ul/li/*[contains(text(),'" + address + "')]");
        userStepDefs.click_css_and_wait("[ng-click*='onApplyAddress()']");
        $("[ng-click*='onApplyAddress()']").waitUntil(disappear, 4000);
    }

    @When("^open facilities page$")
    public void open_facilities_page() throws Throwable {
        userStepDefs.click_xpath_and_wait(".//a[@href='#/facilities']");
        sleep(2000);
    }

    @When("^search '(.*)' in facility address search$")
    public void search_in_facility_address_search(String address) throws Throwable {
        userStepDefs.click_css_and_wait(".leaflet-pelias-input");
        $(".leaflet-pelias-input").setValue(address);
        userStepDefs.click_xpath_and_wait(".//a[@class='leaflet-pelias-search-icon']");
        sleep(1000);
        $(By.xpath(".//ul/li/*[contains(text(),'" + address + "')]")).shouldBe(visible);
        userStepDefs.click_xpath_and_wait(".//ul/li/*[contains(text(),'" + address + "')]");
    }

    @When("^search '(.*)' by facility name$")
    public void search_in_facility_by_facility_name(String address) throws Throwable {
        $("[ng-keypress='findAgenciesByTextQuery($event);']").shouldBe(visible);
        $("[ng-keypress='findAgenciesByTextQuery($event);']").setValue(address);
        sleep(1000);
    }

    @When("^click checkbox with text '(.*)' in Search filters$")
    public void click_label_with_text(String checkboxText) throws Throwable {
        $(By.xpath(".//*[class='form-type__checkbox_empty-label']/label[text()='" + checkboxText + "']")).shouldBe(visible);
        userStepDefs.click_xpath_and_wait(".//*[class='form-type__checkbox_empty-label']/label[text()='" + checkboxText + "']");
        sleep(1000);
    }

    @Then("^verify facility with address '(.*)' and name '(.*)' presents in the list$")
    public void verify_facility_with_address_and_name_presents_in_the_list(String facilityAddress, String facilityName) throws Throwable {
        $(By.xpath(".//span[contains(text(),'" + facilityAddress + "')]/ancestor::div[@class='ch-facility']/descendant::*[text()='" + facilityName + "']")).shouldBe(visible);
        $(By.xpath(".//*[text()='" + facilityName + "']/ancestor::div[@class='ch-facility']/descendant::button[text()='Ask Caseworker']")).shouldBe(visible);
    }

    @When("^do Ask About for facility with address '(.*)' and name '(.*)' and send letter$")
    public void do_Ask_About_for_facility_with_address_and_name_and_send_letter(String facilityAddress, String facilityName) throws Throwable {
        userStepDefs.click_xpath_and_wait(".//span[contains(text(),'" + facilityAddress + "')]/ancestor::div[@class='ch-facility']/descendant::*[text()='" + facilityName + "']/ancestor::div[@class='ch-facility']/descendant::button[text()='Ask Caseworker']");
        userStepDefs.click_css_and_wait("[ng-click='sendMail()']");
        $("[ng-click='sendMail()']").waitUntil(disappear, 4000);
        userStepDefs.click_xpath_and_wait(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']");
        $(By.xpath(".//*[contains(@class,'ch-alert-msg')]/*[text()='Message has been sent!']")).waitUntil(disappear, 4000);

    }

    @Then("^verify letter contains attachment$")
    public void verify_letter_contains_attachment() throws Throwable {
//todo verify attached file
    }

}
