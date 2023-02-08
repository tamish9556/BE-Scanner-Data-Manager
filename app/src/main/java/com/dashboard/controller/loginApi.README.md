login
-------
curl -X 'POST' \
'http://localhost:8080/auth/login' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"id": "string",
"password": "string",
"userName": "string"
}'

Example:

curl -X 'POST' \
'http://localhost:8080/auth/login' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"id": "",
"password": "g1234",
"userName": "Galit"
}'
