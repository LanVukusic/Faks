include .env

dbgen:
	podman run --name sqlc_dbgen --replace -v ./:/src:Z  -w /src -it sqlc/sqlc generate

run:
	go run main.go