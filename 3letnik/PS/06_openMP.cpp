#include "omp.h"
#include <iostream>

using namespace std;

int main()
{
#pragma omp parallel
    {
        int id = omp_get_thread_num();
        cout << id << endl;
    }
    return 0;
}