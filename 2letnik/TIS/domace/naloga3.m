function [izhod, crc] = naloga3(vhod, n)
  % Izvedemo dekodiranje binarnega niza vhod, ki je bilo
  % zakodirano s Hammingovim kodom dolzine n.
  % in poslano po zasumljenem kanalu.
  % Nad vhodom izracunamo vrednost crc po standardu CRC-8-CCITT.
  %
  % vhod  - binarni vektor y (vrstica tipa double)
  % n     - stevilo bitov v kodni zamenjavi
  % crc   - crc vrednost izracunana po CRC-8-CCITT 
  %         nad vhodnim vektorjem (sestnajstisko)
  % izhod - vektor podatkovnih bitov, dekodiranih iz vhoda

  % zračunamo podatke za hamminga
  l = length(vhod);
  m = log2(n + 1);
  k = n - m;
  chunks = l / n;
  
  % zračunamo H
  
  % zračunamo B
  potenceNum = 2.^[0:floor(log2(n))];
  B = [1:n];
  B(ismember(B, potenceNum)) = [];
  
  H = [flipud(dec2bin(B)') fliplr(dec2bin(potenceNum)')] - '0';

  for chunk = 1:chunks
    zacetk = (n*chunk - n + 1);
    konc = n*chunk;
    vhod_i = vhod(zacetk : konc);
    
    
    S = mod(vhod_i * H', 2)';
    [~,ib] = ismember(S.',H.','rows');
    e = zeros([n,1]);
    if ib ~= 0
        e(ib) = 1;
    end
    
    % xoramo 
    yHat = bitxor(e', vhod_i);
    
    start = k*chunk - k + 1;
    stop = k*chunk;
    %returnamo
    izhod(start : stop) = yHat(1 : k);
  end
    
    %izhod vn
    izhod = boolean(izhod);
  
  %CRC del
  buffer = boolean(zeros(1, 8)); %starting all zero buffer
  bufferCurR = NaN;
  for i = 1:l %to je od ENA do L .....kak font res...
      bufferCurR = boolean(zeros(1, 8));  %resetiramo trenutni buffer
      bufferCurR(2 : end) = buffer(1 : end-1);  %zamaknemo prešni buffer na našga nouga
      if bitxor(buffer(8), vhod(i)) %pogledamo če je XOR enak 1 in če je nardimo te 3 reči
          bufferCurR(1) = 1;
          bufferCurR(2) = ~buffer(1);  % to je negacija ~
          bufferCurR(3) = ~buffer(2);
      end
      buffer = bufferCurR; %u nasledni iteraciji mormo met pripravljen taprau buffer
  end
  
  crc = dec2hex(bin2dec(num2str(flip(bufferCurR))));  % Tle je še ene zlo transformaci da nardimo hex...
end