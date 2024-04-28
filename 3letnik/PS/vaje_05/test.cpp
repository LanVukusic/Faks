#include <stddef.h>
#include <iostream>

using namespace std;

#define CUTOFF 100 // arbitrary

static float parallel_sum(const float *, size_t);
static float serial_sum(const float *, size_t);

float sum(const float *a, size_t n)
{
  float r;

#pragma omp parallel
#pragma omp single nowait
  r = parallel_sum(a, n);
  return r;
}

static float parallel_sum(const float *a, size_t n)
{
  // base case
  if (n <= CUTOFF)
  {
    return serial_sum(a, n);
  }

  // recursive case
  float x, y;
  size_t half = n / 2;

#pragma omp task shared(x)
  x = parallel_sum(a, half);
#pragma omp task shared(y)
  y = parallel_sum(a + half, n - half);
#pragma omp taskwait
  x += y;

  return x;
}

static float serial_sum(const float *a, size_t n)
{
  // base cases
  if (n == 0)
  {
    return 0.;
  }
  else if (n == 1)
  {
    return a[0];
  }

  // recursive case
  size_t half = n / 2;
  return serial_sum(a, half) + serial_sum(a + half, n - half);
}

int main(int argc, char const *argv[])
{
  cout << "ojla buraz" << argc << endl;
  return 0;
}
