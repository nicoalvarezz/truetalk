Feature: post service

  Scenario: Save post with valid request
    Given I have a valid request for a post
    When I make a "POST" request to "save-post" endpoint
    Then The response body "message" is "Post saved successfully"
    Then The response body "status" is 200
    Then The response body "service" is "post-service"

  Scenario: Save post with invalid request
    Given I have a valid request for a post
    When I make a "POST" request to "save-post" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"


  Scenario: List followee posts with valid request
    Given I have a valid request to list followee posts
    When I make a "GET" request to "followee-posts" endpoint
    Then The response body "message" is "List of followee post retrieved successfully"
    Then The response body "status" is 200
    Then The response body "data" should contain the field "followee_posts"

  Scenario: List followee posts with invalid request
    Given I have a valid request to list followee posts
    When I make a "POST" request to "followee-posts" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"
    And The response body does not contain "data"

  Scenario: List user posts with valid request
    Given I have a valid request to list user posts
    When I make a "GET" request to "user-posts" endpoint
    Then The response body "message" is "user posts retrieve successfully"
    Then The response body "status" is 200
    Then The response body "service" is "post-service"
    Then The response body "data" should contain the field "user_posts"

  Scenario: List user posts with invalid request
    Given I have a valid request to list user posts
    When I make a "POST" request to "user-posts" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"
    And The response body does not contain "data"

  Scenario: Like post with valid request
    Given I have a valid request to like a post
    When I make a "PUT" request to "like" endpoint
    Then The response body "message" is "Post liked successfully"
    Then The response body "status" is 200
    Then The response body "service" is "post-service"

  Scenario: Like post with invalid request
    Given I have an invalid request to like a post
    When I make a "PUT" request to "like" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"

  Scenario: Unlike post with valid request
    Given I have a valid request to like a post
    When I make a "PUT" request to "unlike" endpoint
    Then The response body "message" is "Post unliked successfully"
    Then The response body "status" is 200
    Then The response body "service" is "post-service"

  Scenario: Unlike post with invalid request
    Given I have an invalid request to like a post
    When I make a "PUT" request to "unlike" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"

  Scenario: Save comment with valid request
    Given I have a valid request for a comment
    When I make a "POST" request to "comment" endpoint
    Then The response body "message" is "Post saved successfully"
    Then The response body "status" is 200
    Then The response body "service" is "post-service"

  Scenario: Save comment with invalid request
    Given I have a valid request for a comment
    When I make a "POST" request to "comment" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"

  Scenario: List likes from a post with valid request
    Given I have a valid post id
    When I make a "POST" request to "likes" endpoint
    Then The response body "message" is "Post likes retrieved successfully"
    Then The response body "status" is 200
    Then The response body "service" is "post-service"
    Then The response body "data" should contain the field "post_likes"

  Scenario: List likes from a post with invalid request
    Given I have a invalid post id
    When I make a "POST" request to "likes" endpoint
    Then The response body "message" is "Sorry, the method argument provided is not valid. Please check your input and try again."
    Then The response body "status" is 400
    Then The response body "service" is "post-service"
    Then The response body "data" should contain the field "post_likes"
    And The response body does not contain "data"



