// http: // www.physics.rutgers.edu/~haule/509/MPI_Guide_C++.pdf

#include "openssl/md5.h"
#include <iostream>
#include <fstream>
#include <string.h>
#include <math.h>
#include <iomanip> // for std::setw and std::setfill
#include <mpi.h>

// well this is just necesarry
#define smooth_yoink calloc
#define yoink malloc
#define yeet free

int world_size;
int my_id;



int main(int argc, char **argv)
{
  MPI::Init(argc, argv);
  world_size = MPI::COMM_WORLD.Get_size();
  my_id = MPI::COMM_WORLD.Get_rank();
  unsigned int str_size = world_size*2*sizeof(char);

  if (my_id == 0)
  {
    char start_str[] = "0";
    MPI::COMM_WORLD.Send(&start_str, str_size, MPI::CHAR, my_id+1, 0);
    std::cout << "Master sent" << std::endl;

    char msg_buff[str_size];
    MPI::COMM_WORLD.Recv(&msg_buff, str_size, MPI::CHAR, world_size-1, 0);
    std::cout << my_id << " @ " << msg_buff << std::endl;
  }
  else
  {
    // i start the messaging
    char msg_buff[str_size];
    MPI::COMM_WORLD.Recv(&msg_buff, str_size, MPI::CHAR, my_id - 1, 0);
    std::cout <<  my_id << " @ " << msg_buff << std::endl;
    int _ = strlen(msg_buff);
    msg_buff[_++] = '-';
    msg_buff[_++] = my_id+'0';
    msg_buff[_++] = '\0';

    MPI::COMM_WORLD.Send(msg_buff, str_size, MPI::CHAR, (my_id+1)%world_size, 0);
  }

  MPI::Finalize(); // clean after yourself MPI!
  return 0;
}


