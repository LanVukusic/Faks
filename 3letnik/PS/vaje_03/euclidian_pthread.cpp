#include <chrono>
#include <iostream>
#include <pthread.h>
#include <math.h>

using namespace std;
#define N_THREADS 1
#define NUM_SIZE 10000
#define VECTOR_LENGTH 80000000

void *job(void *params);

pthread_barrier_t barrier;
pthread_t threads[N_THREADS];

int jobs[N_THREADS];
int vecX[VECTOR_LENGTH];
int vecY[VECTOR_LENGTH];
int vecOut[VECTOR_LENGTH];

int main(int argc, char const *argv[])
{
  // init barriers
  pthread_barrier_init(&barrier, NULL, N_THREADS);

  // create dummy data
  for (int i = 0; i < VECTOR_LENGTH; i++)
  {
    vecX[i] = (rand() % NUM_SIZE);
    vecY[i] = (rand() % NUM_SIZE);
  }

  auto timeStart = chrono::high_resolution_clock::now();
  for (int i = 0; i < N_THREADS; i++)
  {
    jobs[i] = i;
    pthread_create(&threads[i], NULL, job, (void *)&jobs[i]);
  }
  for (int i = 0; i < N_THREADS; i++)
  {
    pthread_join(threads[i], NULL);
  }
  auto stop = chrono::high_resolution_clock::now();
  auto timeStop = chrono::duration_cast<chrono::microseconds>(stop - timeStart);
  cout << "duration micro: " << timeStop.count() << endl;
  // cout << "OUT: " << sqrt(vecOut[0]) << endl;
  cout << "OUT: " << (vecOut[0]) << endl;
  return 0;
}

void *job(void *params)
{
  int locl = 0;
  int jobIndex = *(int *)params;
  for (int i = jobIndex; i < VECTOR_LENGTH; i += N_THREADS)
  {
    locl += (int)pow((vecX[i] - vecY[i]), 2); // bols bi blo samo mnozenje
  }
  // barrier here so all threads calculate their job
  pthread_barrier_wait(&barrier);

  // to ni good.
  for (int depth = 0; depth < ceil(log2(VECTOR_LENGTH)); depth++)
  {
    // for each depth, each thread gets its own job to calculate what it needs
    int p2d = (int)pow(2, depth);
    for (int i = jobIndex + p2d; i < VECTOR_LENGTH; i += (N_THREADS)*p2d)
    {
      int second = i + (int)pow(2, depth);
      if (i + p2d < VECTOR_LENGTH)
      {
        vecOut[i] = vecOut[i] + vecOut[second];
      }
    }
    // it waits for all the other jobs to complete it as well
    pthread_barrier_wait(&barrier);
  }

  return NULL;
}