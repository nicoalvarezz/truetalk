Feature: alethia service

  Scenario: Trigger ID-Pal verification with valid request
    Given I have a valid email and a valid phoneNumber
    When I make a "POST" request to "trigger-verification" endpoint
    Then The response body "message" is "Verification link sent"
    Then The response body "status" is 200
    Then The response body "service" is "alethia-service"

  Scenario: Trigger ID-Pal verification with invalid request
    Given I have a invalid email and invalid phoneNUmber
    When I make a "POST" request to "trigger-verification" endpoint
    Then The response body "message" is "Validation failed for some of the arguments. Make sure that all arguments are correct"
    Then The response body "status" is "400"
    Then The response body "service" is "alethia-service"

  Scenario: Webhook Receiver with valid request
    Given I have a valid IdPalWebhook request
    When I make a "POST" request to "webhook-receiver" endpoint
    Then The response body "message" is "Webhook received"
    Then The response body "status" is 200
    Then The response body "service" is "alethia-service"

