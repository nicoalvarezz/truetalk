Feature: user service

  Scenario: Register user with valid request
    Given I have a valid email and password
    When I make a "POST" request to "register-user" endpoint
    Then The response body "message" is "User registered successfully"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"

  Scenario: Register user with invalid request
    Given I have an invalid email and password
    When I make a "POST" request to "register-user" endpoint
    Then The response body "message" is "Validation failed for some of the arguments. Make sure that all arguments are correct."
    Then The response body "status" is 400
    Then The response body "service" is "user-service"

  Scenario: Register user that already exists
    Given I have register user with email "somerandomemail@gmail.com" and password "password"
    When I make a "POST" request to "register-user" endpoint
    Then The response body "message" is "Unique constraint violated."
    Then The response body "status" is 409
    Then The response body "service" is "user-service"


  Scenario: Registration confirm with valid token
    Given I have a valid token for user confirm registration
    When I make a "GET" request to "registration-confirm" endpoint
    Then The response body "message" is "User confirmed successfully"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"

  Scenario: Registration confirm with empty token
    Given I have an empty token from user confirm registration
    When I make a "GET" request to "registration-confirm" endpoint
    Then The response body "message" is "Confirmation token cannot be null"
    Then The response body "status" is 401
    Then The response body "service" is "user-service"

  Scenario: Registration confirm with expired token
    Given I have a expired token from user confrim registration
    When I make a "GET" request to "registration-confirm" endpoint
    Then The response body "message" is "Confirmation token has expired"
    Then The response body "status" is 401
    Then The response body "service" is "user-service"

  Scenario: Receive a valid user profile from alethia service
    Given I have received a valid user profile from alethia service
    When I make a "POST" request to "receive-user-profile" endpoint
    Then The response body "message" is "User profile information received and user created"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"

  Scenario: Receive an invalid user profile from alethia service
    Given I have received an invalid user profile from alethia service
    When I make a "POST" request to "receive-user-profile" endpoint
    Then The response body "message" is "Validation failed for some of the arguments. Make sure that all arguments are correct."
    Then The response body "status" is 400
    Then The response body "service" is "user-service"

  Scenario: Login user with valid request
    Given I have registered and verified a valid user
    When I make a "POST" request to "login" endpoint
    Then The response body "message" is "token generated successfully"
    Then The response body "status" is 200
    Then The response body "data" should contain the field "token"
    Then The response body "service" is "user-service"

  Scenario: Login user with invalid request
    Given I have registered and verified a valid user
    When I make a "POST" request to "login" endpoint
    Then The response body "message" is "Validation failed for some of the arguments. Make sure that all arguments are correct."
    Then The response body "status" is 400
    Then The response body "service" is "user-service"
    And The response body does not contain "data"

  Scenario: Login user with non existent user
    Given I have not registered and verified any user
    When I make a "POST" request to "login" endpoint
    Then the response body "message" is "Invalid user"
    Then The response body "status" is 401
    Then The response body "service" is "user-service"
    And The response body does not contain "data"

  Scenario: Follow user with valid user data
    Given I have registered and verified a valid user
    When I make a "POST" request to "follow" endpoint
    Then The response body "message" is "User successfully followed"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"

  Scenario: Unfollow user with valid user data
    Given I have registered and verified a valid user
    When I make a "POST" request to "unfollow" endpoint
    Then The response body "message" is "User successfully unfollowed"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"

  Scenario: List followees with valid user
    Given I have two users that follow each other
    When I make a "GET" request to "list-followees" endpoint
    Then The response body "message" is "list of followees retrived successfully"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"
    Then The response body "data" should contain the field "followees"

  Scenario: List followees with non existent user
    When I make a "GET" request to "list-followees" endpoint
    Then The response body "message" is "Validation failed for some of the arguments. Make sure that all arguments are correct."
    Then The response body "status" is 400
    Then The response body "service" is "user-service"
    And The response body does not contain "data"

  Scenario: List followees from a user with no followers
    Given I have two users that do not follow each other
    When I make a "GET" request to "list-followees" endpoint
    Then The response body "message" is "list of followees retrived successfully"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"
    Then The response body "data" should contain the field "followees"
    And The response body "data" that contains the filed "followees" list should be empty

  Scenario: Request user profile for a valid user
    Given I have registered and verified a valid user
    When I make a "GET" request to "user-profile" endpoint
    Then The response body "message" is "User profile information"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"
    Then The response body "data" should contain the field "name"
    Then The response body "data" should contain the field "country"
    Then The response body "data" should contain the field "language"

  Scenario: Request user profile for an invalid user
    Given I have not registered and verified any user
    When I make a "GET" request to "user-profile" endpoint
    Then The response body "message" is "Validation failed for some of the arguments. Make sure that all arguments are correct."
    Then The response body "status" is 400
    Then The response body "service" is "user-service"
    And The response body does not contain "data"

  Scenario: Request user profile for a non existent user
    Given I have not registered and verified any user
    When I make a "GET" request to "user-profile" endpoint
    Then The response body "message" is "User not found"
    Then The response body "status" is 404
    Then The response body "service" is "user-service"
    And The response body does not contain "data"

  Scenario: Find valid user
    Given I have registered and verified a valid user
    When I make a "GET" request to "find-user" endpoint
    Then The response body "message" is "User found"
    Then The response body "status" is 200
    Then The response body "service" is "user-service"
    Then The response body "data" should contain the field "uuid"

  Scenario: Find non existent user
    Given I have not registered and verified any user
    When I make a "GET" request to "find-user" endpoint
    Then The response body "message" is "User found"
    Then The response body "status" is 404
    Then The response body "service" is "user-service"
    And The response body does not contain "data"
    And The response body does not contain "data"



