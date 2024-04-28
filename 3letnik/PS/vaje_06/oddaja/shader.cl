__kernel void mandelbrot(__global unsigned char *buffOut) {
  // globalni indeks elementa
  int width = get_global_size(0);
  int height = get_global_size(1);
  int i = get_global_id(0);
  int j = get_global_id(1);

  int max_iteration = 800; // max stevilo iteracij
  int max = 255;           // max vrednost barvnega kanala
  if (get_global_id(0) == 0 && get_global_id(1) == 0) {
    printf("%d\n", get_local_size(0));
    printf("%d\n", get_local_size(1));
  }

  // izracun
  float x0 = (float)j / width * (float)3.5 - (float)2.5; // zacetna vrednost
  float y0 = (float)i / height * (float)2.0 - (float)1.0;
  float x = 0;
  float y = 0;
  float xtemp = 0;
  int iter = 0;

  // ponavljamo, dokler ne izpolnemo enega izmed pogojev
  while ((x * x + y * y <= 4) && (iter < max_iteration)) {
    xtemp = x * x - y * y + x0;
    y = 2 * x * y + y0;
    x = xtemp;
    iter++;
  }

  // izracunamo barvo (magic: http://linas.org/art-gallery/escape/smooth.html)
  int color = 1.0 + iter - log(log(sqrt(x * x + y * y))) / log(2.0);
  color = (8 * max * color) / max_iteration;
  if (color > max)
    color = max;
  // zapisemo barvo RGBA (v resnici little endian BGRA)

  buffOut[4 * i * width + 4 * j + 0] = color;       // Blue
  buffOut[4 * i * width + 4 * j + 1] = color / 2.0; // Green
  buffOut[4 * i * width + 4 * j + 2] = 0;           // Red
  buffOut[4 * i * width + 4 * j + 3] = 255;         // Alpha
}