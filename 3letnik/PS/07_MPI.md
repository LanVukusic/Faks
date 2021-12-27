# MPI

```c
  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD, &myid);
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  MPI_Finalize();
```

## Point to point  
Gre za komunikacijo med dvema procesoma naenkrat.  
Uporabljamo funkciji `MPI_recv()` in `MPI_send()`. Imamo blokirajoče in takojšnje (imidiate) / neblokirajoče.  
- Obstaja  `MPI_Bsend()`, ta funkcija bo podatke dala skoz medpomnilnik. kdaj se to zgodi se ne sekiramo.  
- Druga opcija je `MPI_Ssend()`. Synchronus send.
- `MPI_Recv`, je samo en. Ne glede na to kako pošiljamo.

Neblokirajoče: 
- `MPI_Isend()`, je imidiate send.
  - `MPI_Ibsend()` za majhne podatke
  - `MPI_Issend()` za velike podatke

Seveda obstaja tudi fancy : `MPI_Sendrcv(...ful parametrov...)`

bariere: `MPI_Barrier(MPI_COMM_WORLD)`  
čas: `MPI_Wtime()` je nek timestamp.

## skupinska komunikacija
Komunikacija je blokirajoča, sodelujejo pa **vsi** procesi.
- `MPI_Broadcast()`