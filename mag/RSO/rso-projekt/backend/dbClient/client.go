package dbclient

import (
	"context"
	"fmt"
	"log"
	"os"
	"rso/m/pralnicaDb"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

var Client *pralnicaDb.Queries

func InitDbClient() {
	// s3Bucket := os.Getenv("S3_BUCKET")
	// secretKey := os.Getenv("SECRET_KEY")

	// Load database URL from environment variable
	databaseURL := fmt.Sprintf("postgres://postgres:%s@%s:5432/postgres", os.Getenv("PASSWORD"), os.Getenv("URL")) // Example: "postgres://user:password@localhost:5432/dbname"
	if databaseURL == "" {
		log.Fatal("DATABASE_URL environment variable is not set")
	}

	// Create a connection pool
	config, err := pgxpool.ParseConfig(databaseURL)
	if err != nil {
		log.Fatalf("Unable to parse database URL: %v", err)
	}

	// Customize pool settings (optional)
	config.MaxConns = 10
	config.MinConns = 2
	config.MaxConnLifetime = time.Hour

	// Connect to the database
	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		log.Fatalf("Unable to establish connection pool: %v", err)
	}

	// Test the connection
	// err = pool.Ping(context.Background())
	// if err != nil {
	// 	log.Fatalf("Unable to ping the database: %v", err)
	// }

	Client = pralnicaDb.New(pool)
}
