package paths

import (
	"encoding/json"
	"fmt"
	"net/http"
	dbclient "rso/m/dbClient"
	"rso/m/pralnicaDb"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgtype"
)

// GetUserReservations retrieves a user's reservations.
func GetUserReservations(w http.ResponseWriter, r *http.Request) {
	userUUID := chi.URLParam(r, "uuid")
	var pgUUID pgtype.UUID
	parsedUUID, err := uuid.Parse(userUUID)
	if err != nil {
		http.Error(w, "Invalid UUID string: %v", http.StatusInternalServerError)
	}
	pgUUID.Bytes = parsedUUID
	pgUUID.Valid = true

	reservations, err := dbclient.Client.GetUserReservations(r.Context(), pgUUID)

	if err != nil {
		http.Error(w, "Unable to fetch reservations", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(reservations)
}

// GetReservationsByMonth retrieves all reservations for a specific month.
func GetReservationsByMonth(w http.ResponseWriter, r *http.Request) {
	month := chi.URLParam(r, "month")
	// Define the layout
	layout := "2006-01-02T15:04:05.000Z"

	// Parse the date
	parsedTime, err := time.Parse(layout, month)
	if err != nil {
		http.Error(w, "rror parsing date:", http.StatusBadRequest)

		return
	}

	var pgMonth pgtype.Date
	if err := pgMonth.Scan(parsedTime); err != nil {
		http.Error(w, "Invalid month format", http.StatusBadRequest)
		return
	}

	reservations, err := dbclient.Client.GetReservationsByMonth(r.Context(), pgMonth)
	if err != nil {
		http.Error(w, "Unable to fetch reservations", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(reservations)
}

// DeleteReservation deletes a specific reservation.
func DeleteReservation(w http.ResponseWriter, r *http.Request) {
	reservationID := chi.URLParam(r, "id")

	id, err := strconv.Atoi(reservationID)
	if err != nil {
		http.Error(w, "Invalid reservation ID. Not a number", http.StatusInternalServerError)
		return
	}

	err = dbclient.Client.DeleteReservation(r.Context(), int32(id))
	if err != nil {
		http.Error(w, "Unable to delete reservation", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// AddReservation adds a new reservation.
func AddReservation(w http.ResponseWriter, r *http.Request) {
	var req struct {
		UUID   string `json:"uuid"`
		Date   string `json:"date"`
		Termin int    `json:"termin"`
		Washer int    `json:"washer"`
	}

	// Parse request body
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Parse and validate UUID
	parsedUUID, err := uuid.Parse(req.UUID)
	if err != nil {
		http.Error(w, "Invalid UUID format", http.StatusBadRequest)
		return
	}
	var pgUUID pgtype.UUID
	pgUUID.Bytes = parsedUUID
	pgUUID.Valid = true

	// Parse and validate date
	// layout := "2006-01-02T15:04:05.000Z"
	layout := time.RFC3339
	parsedDate, err := time.Parse(layout, req.Date)
	if err != nil {
		http.Error(w, fmt.Sprintf("Invalid date format. Expected : %s", layout), http.StatusBadRequest)
		return
	}
	var pgDate pgtype.Date
	if err := pgDate.Scan(parsedDate); err != nil {
		http.Error(w, "Error parsing date", http.StatusBadRequest)
		return
	}

	// Add reservation to the database
	reservation, err := dbclient.Client.AddReservation(r.Context(), pralnicaDb.AddReservationParams{
		Date:   pgDate,
		Termin: int32(req.Termin),
		Washer: int32(req.Washer),
		Uuid:   pgUUID,
	})
	if err != nil {
		http.Error(w, "Unable to add reservation", http.StatusInternalServerError)
		return
	}

	// Return the added reservation
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(reservation)
}
