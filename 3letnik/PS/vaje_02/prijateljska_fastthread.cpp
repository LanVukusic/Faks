#include <chrono>
#include <iostream>
#include <pthread.h>

using namespace std;
#define N_THREADS 4

int sumDiv(int N);
void *job(void *params);

pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
pthread_t threads[N_THREADS];

#define N_ITERS 30000
int sumParov = 0;
int finished = 0;
int jobs[N_THREADS];
int cahce[20 * N_ITERS];

int main(int argc, char const *argv[])
{
    // number of iters, read from stdin
    cout << "INPUT: " << N_ITERS << endl;

    auto timeStart = chrono::high_resolution_clock::now();
    for (int i = 0; i < 10 * N_ITERS; i++)
    {
        cahce[i] = -1;
    }

    for (int i = 0; i < N_THREADS; i++)
    {
        jobs[i] = i;
        pthread_create(&threads[i], NULL, job, (void *)&jobs[i]);
    }
    while (finished != N_THREADS)
    {
    }
    auto stop = chrono::high_resolution_clock::now();
    auto timeStop = chrono::duration_cast<chrono::microseconds>(stop - timeStart);
    cout << "num: " << sumParov << endl;
    cout << "duration micro: " << timeStop.count() << endl;
    return 0;
}

int sumDiv(int N)
{
    if (cahce[N] != -1)
    {
        return cahce[N];
    }

    int sum = 0;
    for (int i = 1; i <= N / 2 + 1; i++)
    {
        if ((N % i) == 0)
        {
            sum += i;
        }
    }
    cahce[N] = sum;
    return sum;
}

void *job(void *params)
{
    int tempSum = 0;
    int jobIndex = *(int *)params;
    for (int i = jobIndex; i < N_ITERS; i += N_THREADS)
    {
        int vsota = sumDiv(sumDiv(i));
        if (i == vsota)
        {
            tempSum += (vsota + i);
        }
    }

    pthread_mutex_lock(&lock);
    sumParov += tempSum;
    finished++;
    pthread_mutex_unlock(&lock);
    return NULL;
}