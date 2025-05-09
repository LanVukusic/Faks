package paths

import (
	"encoding/json"
	"net/http"
)

// Health retrieves a user's reservations.
func Health(w http.ResponseWriter, r *http.Request) {

	json.NewEncoder(w).Encode("outcome: UP:")
}
