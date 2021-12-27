#include <chrono>
#include <iostream>
#include <omp.h>
#include <math.h>

using namespace std;

#define CUTOFF 4
#define EPSILON 1e-10 // precision we want our result to be accurate on; DEFAULT 1e-8
#define START 0       // start of integral interval
#define END 100       // end of integral interval

// aux functions
float integrate(float intervalStart, float intervalEnd, int d);
float integrateNothread(float intervalStart, float intervalEnd);
float func(float x);
float trapesoid(float intervalStart, float intervalEnd);

// main
int main(int argc, char const *argv[])
{
  // timing
  auto timeStart = chrono::high_resolution_clock::now();
  // MEASURING INTERVAL START
  float result;
#pragma omp parallel
  {
#pragma omp single
    // this whole scope is now considered as a single omp "thing"
    // nowait means that no bariers will be set, since we already have a "taskwait" before the return. this is where both tasks will merge
    {
      result = integrate(START, END, 0);
    }
  }
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
float integrate(float intervalStart, float intervalEnd, int d)
{
  if (d >= CUTOFF)
  {
    return integrateNothread(intervalStart, intervalEnd);
  }

  cout << omp_get_thread_num() << endl;

  // compute integral with two seperate precisions
  // single precision
  float singlePointPrec = trapesoid(intervalStart, intervalEnd);
  // double precision
  float midPoint = (intervalStart + intervalEnd) / 2;
  float doublePointPrec = trapesoid(intervalStart, midPoint) + trapesoid(midPoint, intervalEnd);
  // compare precision to desired epsilon
  if (fabs(doublePointPrec - singlePointPrec) <= EPSILON)
  {
    return doublePointPrec;
  }

  // paralell part
  float first, second;
#pragma omp task shared(first)
  {
    first = integrate(intervalStart, midPoint, d + 1);
  }

  // tale del dejansko izvaja prejsnja nit da ne caka too much
  second = integrate(midPoint, intervalEnd, d + 1);
#pragma omp taskwait
  first += second;

  return first;
}

// recursive single threaded
float integrateNothread(float intervalStart, float intervalEnd)
{
  // compute integral with two seperate precisions
  // single precision
  float singlePointPrec = trapesoid(intervalStart, intervalEnd);
  // double precision
  float midPoint = (intervalStart + intervalEnd) / 2;
  float doublePointPrec = trapesoid(intervalStart, midPoint) + trapesoid(midPoint, intervalEnd);
  // compare precision to desired epsilon
  if (fabs(doublePointPrec - singlePointPrec) <= EPSILON)
  {
    return doublePointPrec;
  }
  return integrateNothread(intervalStart, midPoint) + integrateNothread(midPoint, intervalEnd);
}

// LITERATURE
// https://www.py4u.net/discuss/88423
// https://en.wikibooks.org/wiki/OpenMP/Tasks
// https://www.nersc.gov/assets/Uploads/SC16-Programming-Irregular-Applications-with-OpenMP.pdf
//
// COMPILE OPTIONS
// g++ integral.cpp -O3  -o vn -fopenmp
//
// RUN
// OMP_NUM_THREADS=10 ./vn
