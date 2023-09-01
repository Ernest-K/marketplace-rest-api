# Marketplace Rest API
Simple Restful CRUD API for marketplace created using Spring Boot.

## Features
- User login and registration
- JSON Web Token based authentication
- Role-based authorization
- Pagination and sorting
- Global exception handling
- OpenApi 3 & Swagger UI


## Run Locally

Clone the project

```bash
  git clone https://github.com/Ernest-K/marketplace-rest-api.git
```

Go to the project directory

```bash
  cd marketplace-rest-api
```

Run the app using maven

```bash
  ./mvnw spring-boot:run
```

The app will start running at <http://localhost:8080>

You can change some properties in [application.properties](src/main/resources/application.properties) file:

+ change `spring.datasource.username` and `spring.datasource.password` as per your datasource installation
+ change `security.jwt.token.expiration` as token expiration time in ms


## API endpoints
The app defines following CRUD APIs.

### Auth
| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | /api/auth/login | Log in | [JSON](#login) |
| POST   | /api/auth/register | Sign up | [JSON](#register) |

### Users

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/users | Get all users (only for admins) | |
| GET    | /api/users/{userId} | Get user by *userId* | |
| GET    | /api/users/{userId}/offers | Get posts created by user with *userId* | |
| POST   | /api/users | Create user (only for admins) | [JSON](#createUserRequest) |
| PUT    | /api/users/{userId} | Update user (if profile belongs to logged in user or logged in user is admin) | [JSON](#updateUserRequest) |
| DELETE | /api/users/{username} | Delete user (for logged in user or admin) | |


### Offers

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/offers | Get all offers | |
| GET    | /api/offers?categoryName={categoryName} | Get all offers belonging to the category with *categoryName* | |
| GET    | /api/offers/{offerId} | Get offer by *offerId* | |
| GET    | /api/offers/count | Get the number of offers belonging to each category | |
| GET    | /api/offers?query={query} | Search for offers using a *query* | |
| POST    | /api/users/{userId}/offers | Create offer (by logged user) | [JSON](#createOfferRequest) |
| PUT    | /api/users/{userId}/offers/{offerId} | Update offer (if offer belongs to logged in user or logged in user is admin) | [JSON](#updateOfferRequest) |
| DELETE | /api/users/{userId}/offers/{offerId} | Delete offer (for logged in user or admin) | |

All offers get endpoints have pagination and sorting. To specify add a request parameter:
+ `pageNo` page number
+ `pageSize` page size
+ `sortBy` offer field e.g. id, name, price
+ `direction` sorting direction (asc or dsc)

### Categories

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/categories | Get all categories | |
| POST   | /api/categories | Create category (only by admins) | [JSON](#categoryRequest) |
| PUT    | /api/categories/{categoryId} | Update category (only for admins) | [JSON](#categoryRequest) |
| DELETE | /api/categories/{categoryId} | Delete offer (only for admins) | |


## Examples of valid JSON request bodies

##### <a id="login">Log in</a>
```json
{
  "username": "john_doe",
  "password": "secretpassword"
}
```

##### <a id="register">Register</a>
```json
{
  "username": "john_doe",
  "email": "john.doe@example.com",
  "password": "secretpassword"
}
```

##### <a id="createUserRequest">Create user</a>
```json
{
  "username": "john_doe",             // Required
  "email": "john.doe@example.com",    // Required
  "password": "secretpassword",       // Required
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890"
}
```


##### <a id="updateUserRequest">Update user</a>
```json
{
  "username": "john_doe",
  "email": "john.doe@example.com",
  "password": "secretpassword",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890"
}
```

##### <a id="createOfferRequest">Create offer</a>
```json
{
  "name": "Phone",                    // Required
  "description": "Latest smartphone", // Required
  "price": 123.45,                    // Required
  "categoryId": 1                     // Required
}
```

##### <a id="updateOfferRequest">Update offer</a>
```json
{
  "name": "Phone",                 
  "description": "Latest smartphone",
  "price": 123.45,                
  "categoryId": 1              
}
```

##### <a id="categoryRequest">Create or update category</a>
```json
{
  "name": "Electronics"   // Required
}
```
