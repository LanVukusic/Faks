#include <chrono>
#include <iostream>
#include <omp.h>
#include <math.h>

using namespace std;

#define N_THREADS 1  // number of threads
#define EPSILON 1e-8 // precision we want our result to be accurate on; DEFAULT 1e-8
#define START 0      // start of integral interval
#define END 50       // end of integral interval

// aux functions
float integrate(float intervalStart, float intervalEnd);
float func(float x);
float trapesoid(float intervalStart, float intervalEnd);

// main
int main(int argc, char const *argv[])
{
  // timing
  auto timeStart = chrono::high_resolution_clock::now();
  // MEASURING INTERVAL START
  float result = integrate(START, END);
  // MEASURING INTERVAL END
  auto stop = chrono::high_resolution_clock::now();
  auto timeStop = chrono::duration_cast<chrono::microseconds>(stop - timeStart);
  cout << "duration micro: " << timeStop.count() << endl
       << "Result: " << result << endl;
  return 0;
}
// function we are integrating. Change to any single input real number function
float func(float x)
{
  return sin(x * x);
}

// aproximation function for the integral on the interval
float trapesoid(float intervalStart, float intervalEnd)
{
  return (intervalEnd - intervalStart) * ((func(intervalStart) + func(intervalEnd)) / 2);
}

// recursive threaded function. aka job
float integrate(float intervalStart, float intervalEnd)
{
  float out = 0;

  // compute integral with two seperate precisions
  // single precision
  float singlePointPrec = trapesoid(intervalStart, intervalEnd);
  // double precision
  float midPoint = (intervalStart + intervalEnd) / 2;
  float doublePointPrec = trapesoid(intervalStart, midPoint) + trapesoid(midPoint, intervalEnd);
  // compare precision to desired epsilon
  if (fabs(doublePointPrec - singlePointPrec) <= EPSILON)
  {
    out = doublePointPrec;
  }
  else
  {
    float first, second; // PRAŠI NA VAJAH????? A TO JE TREBA DEFINIRAT POSEBI

#pragma omp task shared(first) firstprivate(midPoint) firstprivate(intervalStart)
    first = integrate(intervalStart, midPoint);
#pragma omp task shared(second) firstprivate(midPoint) firstprivate(intervalEnd)
    second = integrate(midPoint, intervalEnd);
#pragma omp taskwait
    first += second;

    out = first;
  }
  return out;
}

// LITERATURE
// https://www.py4u.net/discuss/88423
// https://en.wikibooks.org/wiki/OpenMP/Tasks
// https://www.nersc.gov/assets/Uploads/SC16-Programming-Irregular-Applications-with-OpenMP.pdf
//
// COMPILE OPTIONS
// g++ integral.cpp -O3 -o vn -fopenmp
//
// RUN
// OMP_NUM_THREADS=10 ./vn
