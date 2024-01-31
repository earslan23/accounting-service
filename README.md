# **accounting-service**

`to set-up db`

`run docker-compose.yml`

if execute not visible install docker plugin
example api curls

Get-products
curl --location 'localhost:8282/products/v1'

curl -X 'GET' \
'http://localhost:8282/products/v1' \
-H 'accept: */*'


run profiles test (hibernate-statistics :false)
mvn spring-boot:run -Dspring-boot.run.profiles=test

**to build project**
`mvn clean compile install`

**start project**
`run or debug AccountingServiceApplication.java class`

**api document url**
`http://localhost:8282/swagger-ui/index.html`


