# ktor-sample
ktor sample, implement simple login/register

This Reporitory under development ...

Implement simple login/register with ktor

```API
POST : http://localhost:8080/v1/users/signup

request body: {
    "username":"amid",
    "password":"123456"
}

POST: http://localhost:8080/v1/users/signin

request body: {
    "username":"amid",
    "password":"123456"
}

POST: http://localhost:8080/v1/users/logout

request body: {
    "token":"jwttoken"
}

```

Docker compose
--------
just run docker compose yml and test APIs

Auth: JWT
Deploy: Docker
Database: postgresql, jetbrain exposed
DI framework: koin
