## What is this repository for? ###

Test task for Monese Software Engineer :

● Sending money between two predefined accounts with a positive starting balance
● Requesting account balance and list of transactions

### Tech stack used behind this test project :
Note: Tried not to use Spring framework:

Similar old projects can be found in my gitHub repositories with Spring Boot implementation, in memory and with ui:

https://github.com/Boburmirzo/transferMoney
https://github.com/Boburmirzo/banktransactionsemulation
https://github.com/Boburmirzo/localbankemulation
https://github.com/Boburmirzo/banktransactionsemulationwithGUI

* Dropwizard as web service framework.
* Apache Hibernate for ORM.
* H2 in memory database. 
* Apache Maven as build tool.
* JUnit as a test framework
* Mockito for mocking.
* JaCoCo for code coverage library.

### Requirements: ###
Things you need to have installed to run the test project. 

 - Git
 - Apache Maven
 - JDK

### Running the application: ###

 - Clone the project.

    ```
    git clone https://github.com/Boburmirzoshihabcsedu09/monese-money-transfer
    ```
    
 - Go to the project.
    ```
    cd monese-money-transfer
    ```
- Run this command
	 ```
    bash scripts/run.sh
  ```
- Alternatively you can run the following commands too
   ```
   mvn clean package  
   java -jar target/money-transfer-1.0-SNAPSHOT.jar server config.yml
   ```
- The application is deployed at http://localhost:8080/

### How the application handles transaction and concurrent transfer
1. ```@UnitOfWork``` annotation over jersey resource method make sure the transaction atomicity.
2. ```Optimistic Lock``` is implemented by adding a version attribute in the entity class. That ensures if other transaction updates the account entity in the middle of a transfer it will no commit the current one by checking the version number.
### Available APIs

***Account API***: 

**POST**:
Before transfering amount you need to first create a source acccount and a destination account with balance. This you can do by send a post request ```/account```  endpoint with the following as a request body.

```javascript
{
  "name" : "Some Test Account",
  "balance" : 100.22
}
```
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `name` | `string` | **Required**. The name of the account |
| `balance` | `decimal` | **Required**. The initial balance of the amount, must be a positive decimal |

Corresponding cURL command: 
```
curl -X POST \
  http://localhost:8080/account/ \
  -H 'content-type: application/json' \
  -d '{
  "name" : "Some Test Account",
  "balance" : 100.22
}'
```
As a response you will get the following

```javascript
{
    "id": 1,
    "name": "Some Test Account",
    "balance": 100.22
}
```
Keep track of this id as it is needed to transfer money from one account to another. 

Available status code for this API endpoint : 

| Status Code | Description | Comment
| :--- | :--- |:--- |
| 200 | `OK` |If the applications successfully creates the account|
| 400 | `Bad Request` | If the json is malformed |
| 422 | `UNPROCESSABLE ENTITY` | If parameter constraints violates like giving negative balance|
| 500 | `INTERNAL SERVER ERROR` |If something bad happened in the server | 

**GET**:
You can fetch account informaton by sending a GET request with account id as a path parameter to the following endpoint. ```/account/{accountId}```   

Example request:

```
curl -X GET \
  http://localhost:8080/account/1 \
  -H 'content-type: application/json' \
 ```

As a response you will get the following

```javascript
{
    "id": 1,
    "name": "Some Test Account",
    "balance": 100.22
}
```
Available status code for this API endpoint : 

| Status Code | Description | Comment
| :--- | :--- |:--- |
| 200 | `OK` |If the applications successfully fetches the account|
| 600 | `NO ACCOUNT FOUND` | If no account found for this id |
| 500 | `INTERNAL SERVER ERROR` |If something bad happened in the server |

***Transfer API***: 

**POST**:
To make a transfer we need to define the source account, destination account and transfer amount first. Then a post request with this information to ```/transaction``` endpoint will make the transfer.

```javascript
{
	"fromAccountId": 2,
	"toAccountId": 1,
	"amount": 100
}
```
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `fromAccountId` | `long` | **Required**. The id of the source account |
| `toAccountId` | `long` | **Required**. The id of the destination account |
| `amount` | `decimal` | **Required**. The transfer amount, must be a positive decimal |

Corresponding cURL command: 
```
curl -X POST \
  http://localhost:8080/transaction/ \
  -H 'content-type: application/json' \
  -d '{
	"fromAccountId": 2,
	"toAccountId": 1,
	"amount": 100
}'
```
As a response you will get the following

```javascript
{
	"fromAccountId": 2,
	"toAccountId": 1,
	"amount": 100
}
```
Available status code for this API endpoint : 

| Status Code | Description | Comment
| :--- | :--- |:--- |
| 200 |`OK` |If the applications successfully transfer the amount|
| 400 |`Bad Request`| If the json is malformed |
| 422 |`UNPROCESSABLE ENTITY`| If parameter constraints violates like giving negative balance|
| 601 |`SOURCE ACCOUNT NOT FOUND`| If non exisiting source account id given|
| 602 |`DESTINATION ACCOUNT NOTFOUND`| If non exisiting destination account id given|
| 603 |`NOT ENOUGH BALANCE`| If the source account don't have enough balance|
| 604 |`ACCOUNT ALREADY UPDATED`| If the account is already updated by another transaction concurrently|
| 500 |`INTERNAL SERVER ERROR`|If something bad happened in the server | 

### Limitations
1. Load/Stress test of the application to see how it works with concurrent requests
2. Swagger integration for api documentation
3. Code Quality linters integration like sonar cloud.
4. More granular exception mapping. 

	 



	  
	  


