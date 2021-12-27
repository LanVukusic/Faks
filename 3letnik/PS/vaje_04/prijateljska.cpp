#include <chrono>
#include <iostream>
#include <omp.h>

using namespace std;
#define N_THREADS 32
#define N_BUCKETS 4

int sumDiv(int N);
int job(int i);

int nIters;

int main(int argc, char const *argv[])
{
  // number of iters, read from stdin
  nIters = atoi(argv[1]);
  cout << "INPUT: " << nIters << endl;

  auto timeStart = chrono::high_resolution_clock::now();
  int out = 0;
#pragma omp parallel num_threads(N_THREADS)
#pragma omp for schedule(dynamic, N_BUCKETS) reduction(+ \
                                                       : out)
  for (int i = 0; i < nIters; i++)
  {
    out += job(i);
  }

  auto stop = chrono::high_resolution_clock::now();
  auto timeStop = chrono::duration_cast<chrono::microseconds>(stop - timeStart);
  cout << "num: " << out << endl;
  cout << "duration micro: " << timeStop.count() << endl;
  return 0;
}

int sumDiv(int N)
{
  int sum = 0;
  for (int i = 1; i <= N / 2 + 1; i++)
  {
    if ((N % i) == 0)
    {
      sum += i;
    }
  }
  return sum;
}

int job(int i)
{

  int vsota = sumDiv(sumDiv(i));
  if (i == vsota)
  {
    return (vsota * 2);
  }
  return 0;
}