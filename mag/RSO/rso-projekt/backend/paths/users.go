package paths

import (
	"encoding/json"
	"fmt"
	"net/http"
	dbclient "rso/m/dbClient"
	"rso/m/failer"
	"rso/m/pralnicaDb"

	"github.com/failsafe-go/failsafe-go"
	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgtype"
)

// SearchUsers searches users based on text input.
func GetUsers(w http.ResponseWriter, r *http.Request) {
	users, err := dbclient.Client.GetUsers(r.Context())
	if err != nil {
		fmt.Printf(err.Error())
		http.Error(w, "Unable to search users", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(users)
}

// SearchUsers searches users based on text input.
func GetSelf(w http.ResponseWriter, r *http.Request) {
	users, err := dbclient.Client.GetUsers(r.Context())
	if err != nil {
		fmt.Printf(err.Error())
		http.Error(w, "Unable to search users", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(users[0]) // TODO: to nedela se
}

// SearchUsers searches users based on text input.
func GetUser(w http.ResponseWriter, r *http.Request) {
	userUUID := chi.URLParam(r, "uuid")
	var pgUUID pgtype.UUID
	parsedUUID, err := uuid.Parse(userUUID)
	if err != nil {
		http.Error(w, "Invalid UUID string: %v", http.StatusInternalServerError)
	}
	pgUUID.Bytes = parsedUUID
	pgUUID.Valid = true

	users, err := dbclient.Client.GetUser(r.Context(), pgUUID)
	if err != nil {
		http.Error(w, "Unable to search users", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(users)
}

// SearchUsers searches users based on text input.
func SearchUsers(w http.ResponseWriter, r *http.Request) {
	searchText := r.URL.Query().Get("search_text")

	var pgSearch pgtype.Text
	pgSearch.String = searchText
	pgSearch.Valid = true

	// search function
	f := func() (any, error) {
		// return dbclient.Client.SearchUsers(r.Context(), pgSearch)
		return nil, fmt.Errorf("ojoj")
	}

	// failsafe iwth circuit breaker
	users, err := failsafe.Get[any](f, failer.Breaker)

	if err != nil {
		http.Error(w, "Unable to search users", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(users)
}

// ConfirmUser updates a user's 'confirmed' status.
func ConfirmUser(w http.ResponseWriter, r *http.Request) {
	userUUID := chi.URLParam(r, "uuid")
	fmt.Println(r.RequestURI)
	fmt.Println("User id: " + userUUID)
	var pgUUID pgtype.UUID
	parsedUUID, err := uuid.Parse(userUUID)
	if err != nil {
		http.Error(w, fmt.Sprintf("Invalid UUID string: %v", userUUID), http.StatusInternalServerError)
	}
	pgUUID.Bytes = parsedUUID
	pgUUID.Valid = true

	err = dbclient.Client.ConfirmUser(r.Context(), pgUUID)
	if err != nil {
		http.Error(w, "Unable to confirm user", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// BanUser disables a user.
func BanUser(w http.ResponseWriter, r *http.Request) {
	userUUID := chi.URLParam(r, "uuid")
	var pgUUID pgtype.UUID
	parsedUUID, err := uuid.Parse(userUUID)
	if err != nil {
		http.Error(w, "Invalid UUID string: %v", http.StatusInternalServerError)
	}
	pgUUID.Bytes = parsedUUID
	pgUUID.Valid = true

	err = dbclient.Client.BanUser(r.Context(), pgUUID)
	if err != nil {
		http.Error(w, "Unable to ban user", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// EditUser updates a user's information.
func EditUser(w http.ResponseWriter, r *http.Request) {
	var user pralnicaDb.EditUserParams
	if err := json.NewDecoder(r.Body).Decode(&user); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	err := dbclient.Client.EditUser(r.Context(), pralnicaDb.EditUserParams{
		Uuid:    user.Uuid,
		Phone:   user.Phone,
		Name:    user.Name,
		Surname: user.Surname,
		Room:    user.Room,
		Email:   user.Email,
		Role:    user.Role,
	})

	if err != nil {
		http.Error(w, "Unable to edit user", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}
