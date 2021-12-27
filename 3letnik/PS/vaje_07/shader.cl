__kernel void hist(__global unsigned char *hist) {
  // globalni indeks elementa
  int width = get_global_size(0);
  int height = get_global_size(1);
  int i = get_global_id(0);
  int j = get_global_id(1);
  

 
}