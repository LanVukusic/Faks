FROM docker.io/library/golang:1.23 AS build

WORKDIR /backend

COPY go.mod go.sum ./
RUN go mod download

COPY ./ ./

ENV URL zaanimivo.xyz

RUN CGO_ENABLED=0 GOOS=linux go build -o /backend

RUN chmod +x /backend
FROM debian:bullseye-slim AS release

WORKDIR /backend

COPY --from=build /backend /backend
# COPY --from=build /rso-stats/defaults.env /defaults.env

EXPOSE 1234

# USER nonroot:nonroot


# ENTRYPOINT [ "/bin/ls",  "-l", "backend" ]
ENTRYPOINT [ "/backend/m" ]