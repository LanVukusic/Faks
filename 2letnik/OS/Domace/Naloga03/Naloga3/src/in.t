# Preusmerjanje notranjih ukazov
dirmake test
dirchange test
echo "Gori na gori gori" >gori.txt
cpcat gori.txt
cpcat <gori.txt
cpcat <gori.txt >doli.txt
cpcat doli.txt
cpcat <doli.txt
cpcat gori.txt voli.txt
cpcat voli.txt
cpcat <voli.txt
unlink gori.txt
unlink doli.txt
unlink voli.txt
dirchange ..
dirremove test
