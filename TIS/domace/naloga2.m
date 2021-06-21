function [izhod, R] = naloga2(vhod, nacin)
% Izvedemo kodiranje ali dekodiranje vektorja "vhod" 
% z algoritmom LZW. Zacetni slovar vsebuje vse 8-bitne 
% vrednosti (0-255). Najvecja dolzina slovarja je 4096.
%
% vhod  - stolpicni vektor
% nacin - zastavica, ki pove, ali kodiramo (0)  
%         ali dekodiramo (1)
% izhod - ce je nacin=0: izhod je kodiran vhod
%         ce je nacin=1: izhod je dekodiran vhod
% R     - kompresijsko razmerje
    %vhod = vhod.';
    if(nacin == 1)
        izhod = decompress(vhod);
        R = (length(vhod) * 12) / (length(izhod) * 8);
    else
        izhod = compress(vhod);
        R = (length(izhod) * 12) / (length(vhod) * 8);
    end
end

function [izhod] = compress(vhod)
    izhod = [];
    i  = 1;
    buff = "";
    dictIndex = 256;
    
    % create map aka dictionary
    dict = containers.Map(string(char([0:255])')', [0:255]);
    
    while i <= length(vhod)
        k = [char(buff), vhod(i)];
        if dict.isKey(string(k))
            buff = string(k);
        else
            izhod = [izhod dict(buff)];
            buff = vhod(i);
            %pogledamo da ni slovar ze pouhn
            if length(dict) > 4096
                continue
            end
            
            %dodamo novo robo v slovar 
            dict(string(k)) = dictIndex;
            dictIndex = dictIndex +1;
        end
        i = i + 1;
    end
    izhod = [izhod dict(buff)];
end

function [izhod] = decompress(vhod)
    izhod = [];
    % create dictionary
    dict = containers.Map([0:255], string(char([0:255])')');

    in = vhod(1);
    buff = dict(in);
    izhod = [izhod buff];
    K = buff; % tle bomo buildal nas string
    
    j = 256;
    i = 2;
    while i <= length(vhod)
        if(dict.isKey(vhod(i)))
            buff = dict(vhod(i));
        else
            buff = [K, K(1)];
        end
        izhod = [izhod buff];
        dict(j) = [K, buff(1)];
        j = j+1; % povecamo counter za naslednic ko bomo dajal stvar v slovar
        K = buff;
        i = i+1;
    end
    
end
