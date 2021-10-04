function H = naloga1(besedilo,p)
% besedilo - stolpicni vektor znakov (char)
% p  - stevilo poznanih predhodnih znakov; 0, 1, 2 ali 3.
%    p = 0 pomeni, da racunamo povprecno informacijo na znak
%        abecede brez poznanih predhodnih znakov: H(X1)
%    p = 1 pomeni, da racunamo povprecno informacijo na znak 
%        abecede pri enem poznanem predhodnemu znaku: H(X2|X1)
%    p = 2: H(X3|X1,X2)
%    p = 3: H(X4|X1,X2,X3)
%
% H - skalar; povprecna informacija na znak abecede 
%     z upostevanjem stevila poznanih predhodnih znakov p

    % preprocess
    %besedilo = ['B','E','s','E','d','i','l','o',]';
    B = lower(besedilo(isletter(besedilo)));
    
    if p == 0
        [uniqueArrN,ia,ic] = unique(B, "rows");
        y1 = grouptransform(B,B,@numel);
        y1 = y1(ia)/length(ic)
        H = -(y1' * log2(y1));
        return
    end
    
    
    WordMatrix = zeros(length(B), p+1);

    for i = 1:p+1
        WordMatrix(1:length(B)-(i-1),i) = B(i:end)';
    end

     
     
    
    ArrN = WordMatrix(1:length(B)-p, 1:p+1);
    [uniqueArrN,ia,ic] = unique(ArrN, "rows");
    y1 = grouptransform(ArrN,ArrN,@numel);
    y1 = y1(ia)/length(ic)
    
    ArrN0 = WordMatrix(1:length(B)-p+1, 1:p);
    [uniqueArrN2,ia,ic] = unique(ArrN0, "rows");
    y0 = grouptransform(ArrN0,ArrN0,@numel);
    y0 = y0(ia)/length(ic)
    
    h0 = -(y0' * log2(y0));
    h1 = -(y1' * log2(y1));
    H = h1-h0;
   




