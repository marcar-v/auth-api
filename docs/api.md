---
title: Auth Service main changes

---

# Auth Service main changes

## App API

- Remove ID & password from User entity

## Auth Service API

### Init service layers

1. `User` entity and repository.
2. `Session` entity and repository.
3. `User` controller.
4. `Auth` controller.
5. `UserAuth` DTOs and services.

### Auth API entities definitions

#### User
```
Email: String -> Email address format
Password: String -> Hash of the password
```

#### Session
```
ID: String -> Internal UUID to identify the sessions
Email: String -> Identify the user owner of the session
AuthToken: String -> Will be updated during refresh flow and works as authentication proof
RefresToken: String -> Will be updated during refresh flow, required to get a new AuthToken
Expiration: Datetime -> Will be updated during refresh flow, defines where the current AuthToken should be refreshed
```

#### Password recovery request
```
Email: String -> Identify the user owner of the request
RecoveryCode: String -> Only one per email, randomly generated.  
Expiration: Datetime -> Indicates when the recovery code is considered invalid.
```

### Auth API endpoints definition

#### Create user

Creates a new `User` entity with the data provided by the user.

| URI       | Method | Headers |
|:----------|:------:|:-------|
| `/users`  | `POST` |        |

##### Body
```json
{
  "email": "user1@email.com",
  "password": "hash(password)"
}
```

##### Status codes and errors
- 201 - Created
- 400 - Bad request
- 409 - Conflict
- 500 - Internal server error

#### Delete user

Remove an existing `User` entity from the database (based on the email in the provided auth token), and every session that belongs to that user. Require a valid (existing and not expired) auth token.

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/users` | `DELETE` | `Authorization: Bearer <user_auth_token>` |

##### Status codes and errors
- 200 - Ok
- 403 - Forbidden
- 500 - Internal server error


#### Password recovery

Sends an email to an existing `User` with a random code. Creates a new `RecoveryRequest` entity if there is not any other one for the same email address. It will be deleted once the code is attempted to be used (no matters if it success or not).

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/users/password` | `POST` |  |

##### Body
```json
{
  "email": "user1@email.com" // where the auth code will be sent
}
```

##### Status codes and errors
- 200 - Ok
- 400 - Bad request
- 500 - Internal server error

#### Password change

Checks if the code is valid for an existing `RecoveryRequest` entity. If so, updates the password of the referenced `User` entity. In any case, the `RecoveryRequest` should be deleted if it exists.

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/users/password` | `PUT` |  |

##### Body
```json
{
  "password": "hash(password)",
  "recovery_code": "123456" // received by email 
}
```

##### Status codes and errors
- 200 - Ok
- 400 - Bad request
- 500 - Internal server error

#### Request auth token

Generates a new auth token, refresh token and expiration tuple and stores them as a new `Session` entity.

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/auth` | `POST` |  |

##### Body
```json
{
  "email": "user1@email.com",
  "password": "hash(password)"
}
```

##### Response
```json
{
  "auth_token": "lalala", // jwt
  "refresh_token": "lololo", // token random
  "expiration": "2026-07-22 10:10+2T000"
}
```

##### Status codes and errors
- 200 - Ok
- 400 - Bad request
- 500 - Internal server error

#### Refresh auth token

Checks if the provided refresh token exists and if so, repeats the request auth token flow overwriting the previous one.

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/auth` | `PUT` |  |

##### Body
```json
{
  "refresh_token": "lololo" // token random
}
```

##### Response
```json
{
  "auth_token": "lalala", // jwt
  "refresh_token": "lololo", // token random
  "expiration": "2026-07-22 10:10+2T000"
}
```

##### Status codes and errors
- 200 - Ok
- 400 - Bad request
- 500 - Internal server error

#### Check auth token

Returns if the provided auth token still valid (exists, belongs to a registered user and it is not expired).

Decode the email from the auth token provided and:
1. Checks if it exist as `User` entity.
2. Checks if exists a `Session` for that email with that auth token.
3. Checks if that `Session` still valid.

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/auth` | `GET` | `Authorization: Bearer <user_auth_token>` |

##### Status codes and errors
- 200 - Ok
- 400 - Bad request
- 500 - Internal server error

#### Close session

Deletes every single `Session` entity related to the email behind the provided auth token, but only for valid auth tokens.

| URI               |  Method  | Headers                |
|:------------------|:--------:|:-----------------------|
| `/auth` | `DELETE` | `Authorization: Bearer <user_auth_token>` |

##### Status codes and errors
- 200 - Ok
- 400 - Bad request
- 500 - Internal server error