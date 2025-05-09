package failer

import (
	"time"

	"github.com/failsafe-go/failsafe-go/circuitbreaker"
)

var Breaker circuitbreaker.CircuitBreaker[any]

func init() {
	// Opens after 5 failures, half-opens after 1 minute, closes after 2 successes
	Breaker = circuitbreaker.Builder[any]().
		WithFailureThreshold(5).
		WithDelay(30 * time.Second).
		WithSuccessThreshold(2).
		Build()
}
