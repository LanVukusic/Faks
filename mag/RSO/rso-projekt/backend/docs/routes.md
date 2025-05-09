# 

Welcome to the documentation for the RESTful API.

## Routes

<details>
<summary>`/health`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/health**
	- _GET_
		- [Health]()

</details>
<details>
<summary>`/metrics`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/metrics**
	- _*_
		- [InstrumentHandlerCounter.func1]()

</details>
<details>
<summary>`/reservations`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/reservations**
	- _POST_
		- [AddReservation]()

</details>
<details>
<summary>`/reservations/month/{month}`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/reservations/month/{month}**
	- _GET_
		- [GetReservationsByMonth]()

</details>
<details>
<summary>`/reservations/user/{uuid}`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/reservations/user/{uuid}**
	- _GET_
		- [GetUserReservations]()

</details>
<details>
<summary>`/reservations/{id}`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/reservations/{id}**
	- _DELETE_
		- [DeleteReservation]()

</details>
<details>
<summary>`/swagger`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/swagger**
	- _*_
		- [v5.(*Mux).Mount.func1]()

</details>
<details>
<summary>`/users`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/users**
	- _GET_
		- [GetUsers]()

</details>
<details>
<summary>`/users/ban/{uuid}`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/users/ban/{uuid}**
	- _POST_
		- [BanUser]()

</details>
<details>
<summary>`/users/confirm/{uuid}`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/users/confirm/{uuid}**
	- _POST_
		- [ConfirmUser]()

</details>
<details>
<summary>`/users/search`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/users/search**
	- _GET_
		- [SearchUsers]()

</details>
<details>
<summary>`/users/self`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/users/self**
	- _GET_
		- [GetSelf]()

</details>
<details>
<summary>`/users/{uuid}`</summary>

- [Logger]()
- [o-chi/cors.(*Cors).Handler-fm]()
- [66b/chi-prometheus.Middleware.handler-fm]()
- [github.com/pmatteo/chi-healthcheck-middleware.NewHealthChecker.func1]()
- **/users/{uuid}**
	- _GET_
		- [GetUser]()
	- _POST_
		- [EditUser]()

</details>

Total # of routes: 13
