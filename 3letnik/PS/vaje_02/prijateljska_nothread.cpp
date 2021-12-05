#include <chrono>
#include <iostream>
#include <pthread.h>

using namespace std;

int sumDiv(int N);

int main(int argc, char const *argv[])
{
    int sumParov = 0;
    auto start = chrono::high_resolution_clock::now();

    for (int i = 1; i < atoi(argv[1]); i++)
    {
        int vsota = sumDiv(sumDiv(i));
        if (i == vsota)
        {
            sumParov += (vsota + i);
        }
    }
    auto stop = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(stop - start);
    cout << "num: " << sumParov << endl;
    cout << "duration micro: " << duration.count() << endl;
    // X(sumParov);
    auto xd = [](int N) {

    };
    return 0;
}

int sumDiv(int N)
{
    int sum = 0;
    for (int i = 1; i <= N / 2 + 1; i++)
    {

        if ((N % i) == 0)
        {
            // cout << "num: " << i << " " << N << endl;
            sum += i;
        }
    }
    return sum;
}
