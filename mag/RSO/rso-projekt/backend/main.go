package main

import (
	"fmt"
	"net/http"
	dbclient "rso/m/dbClient"
	"rso/m/docs"
	"rso/m/paths"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/go-chi/docgen"
	"github.com/prometheus/client_golang/prometheus/promhttp"

	httpSwagger "github.com/swaggo/http-swagger"

	chiprometheus "github.com/766b/chi-prometheus"

	healthcheck "github.com/pmatteo/chi-healthcheck-middleware"
)

const PORT = 1234

// func rabbit() {
// 	connStr := fmt.Sprintf("amqp://%s:%s@%s:5672/", "zajec", "korenje", "localhost")
// 	// connStr := fmt.Sprintf("amqp://%s:%s@%s:15672/", os.Getenv("USR"), os.Getenv("PASS"), os.Getenv("HOST"))
// 	fmt.Println(connStr)
// 	conn, err := rabbitmq.NewConn(
// 		connStr,
// 		rabbitmq.WithConnectionOptionsLogging,
// 	)
// 	if err != nil {
// 		log.Fatal(err)
// 	}
// 	defer conn.Close()

// 	publisher, err := rabbitmq.NewPublisher(
// 		conn,
// 		rabbitmq.WithPublisherOptionsLogging,
// 		rabbitmq.WithPublisherOptionsExchangeName("events"),
// 		rabbitmq.WithPublisherOptionsExchangeDeclare,
// 	)
// 	if err != nil {
// 		log.Fatal(err)
// 	}
// 	defer publisher.Close()

// 	err = publisher.Publish(
// 		[]byte("hello, world"),
// 		[]string{"my_routing_key"},
// 		rabbitmq.WithPublishOptionsContentType("application/json"),
// 		rabbitmq.WithPublishOptionsExchange("/"),
// 	)
// 	if err != nil {
// 		log.Println(err)
// 	}
// 	for {

// 		// body := "{'termins':9}"
// 		time.Sleep(2 * time.Second)
// 	}
// }

func main() {
	r := chi.NewRouter()

	r.Use(middleware.Logger)
	r.Use(cors.Handler(cors.Options{
		// AllowedOrigins:   []string{"https://foo.com"}, // Use this to allow specific origin hosts
		AllowedOrigins: []string{"https://*", "http://*"},
		// AllowOriginFunc:  func(r *http.Request, origin string) bool { return true },
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	m := chiprometheus.NewMiddleware("serviceName")
	r.Use(m)

	// Set up the health check middleware with different configurations
	healthMiddleware := healthcheck.NewHealthChecker(
		healthcheck.WithEndpointDefaultProbe("/health"), // Adds a default health check endpoint at /health
	)
	r.Use(healthMiddleware)

	r.Handle("/metrics", promhttp.Handler())
	r.Mount("/swagger", httpSwagger.WrapHandler)

	r.Get("/health", paths.Health)

	r.Get("/reservations/user/{uuid}", paths.GetUserReservations)
	r.Get("/reservations/month/{month}", paths.GetReservationsByMonth)
	r.Post("/reservations", paths.AddReservation)
	r.Delete("/reservations/{id}", paths.DeleteReservation)

	r.Get("/users/search", paths.SearchUsers)
	r.Get("/users", paths.GetUsers)
	r.Get("/users/self", paths.GetSelf)
	r.Get("/users/{uuid}", paths.GetUser)
	r.Post("/users/{uuid}", paths.EditUser)
	r.Post("/users/confirm/{uuid}", paths.ConfirmUser)
	r.Post("/users/ban/{uuid}", paths.BanUser)
	r.Post("/users/{uuid}", paths.EditUser)

	docgen.PrintRoutes(r)
	docs.WriteDocsMarkdown(r)
	docs.WriteDocsJSON(r)

	fmt.Printf("Serving on: %d\n", PORT)
	dbclient.InitDbClient()

	http.ListenAndServe(fmt.Sprintf(":%d", PORT), r)
	for {
		time.Sleep(100 * time.Second)
	}
}
