/**
 * izziv08
 */
public class izziv08 {

    // premade complex class
    class Complex{
        double re;
        double im;
    
        public Complex(double real, double imag) {
            re = real;
            im = imag;
        }
    
        public String toString() {
            double tRe = (double)Math.round(re * 100000) / 100000;
            double tIm = (double)Math.round(im * 100000) / 100000;
            if (tIm == 0) return tRe + "";
            if (tRe == 0) return tIm + "i";
            if (tIm <  0) return tRe + "-" + (-tIm) + "i";
            return tRe + "+" + tIm + "i";
        }
    
        public Complex conj() {
            return new Complex(re, -im);
        }
    
        // sestevanje
        public Complex plus(Complex b) {
            Complex a = this;
            double real = a.re + b.re;
            double imag = a.im + b.im;
            return new Complex(real, imag);
        }
    
        // odstevanje
        public Complex minus(Complex b) {
            Complex a = this;
            double real = a.re - b.re;
            double imag = a.im - b.im;
            return new Complex(real, imag);
        }
    
        // mnozenje z drugim kompleksnim stevilom
        public Complex times(Complex b) {
            Complex a = this;
            double real = a.re * b.re - a.im * b.im;
            double imag = a.re * b.im + a.im * b.re;
            return new Complex(real, imag);
        }
    
        // mnozenje z realnim stevilom
        public Complex times(double alpha) {
            return new Complex(alpha * re, alpha * im);
        }
    
        // reciprocna vrednost kompleksnega stevila
        public Complex reciprocal() {
            double scale = re*re + im*im;
            return new Complex(re / scale, -im / scale);
        }
    
        // deljenje
        public Complex divides(Complex b) {
            Complex a = this;
            return a.times(b.reciprocal());
        }
    
        // e^this
        public Complex exp() {
            return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
        }
    
    
        //potenca komplesnega stevila
        public Complex pow(int k) {
    
            Complex c = new Complex(1,0);
            for (int i = 0; i <k ; i++) {
                c = c.times(this);
            }
            return c;
        }
    
    }


    int[] RecursiveFFT(int[] a){
        int n =  a.length;
        if(n==1){
            return a;
        }

        int sn = Math.floorDiv(n, 2);
        int ln = n - sn;

        int[] ys = new int[sn];
        int[] yl = new int[ln];

        for (int i = 0; i < n; i ++) {
            if(i/2 % 2 == 0){
                ys[i] = a[i];
            }else{
                yl[i] = a[i];
            }
        }


        return a;
    }


    public static void main(String[] args) {
        // povečaj to številko vsakič ko učilnica hoče novo verzijo da jo potem mogoče prevede
        int poskusi = 0;
        
        int[] a = new int[]{1,1};
        int[] b = new int[]{1,1};


    }
}