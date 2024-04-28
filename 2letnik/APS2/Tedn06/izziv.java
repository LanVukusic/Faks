import java.util.Scanner;


class Matrix {

	private int[][] m;

	public int n; //only square matrices

	public Matrix(int n){
		this.n = n;
		m = new int[n][n];
	}

    //set value at i,j
	public void setV(int i, int j, int val){
		m[i][j] = val;
	}


    // get value at index i,j
	public int v(int i, int j){
		return m[i][j];
	}

    // write this matrix as a submatrix from b (useful for the result of multiplication)
	public void putSubmatrix(int startCol, int startRow, Matrix b){
		for (int i = 0; i<b.n ; i++ )
			for (int j=0;j<b.n ; j++ )
				setV(startRow+i,startCol+j, b.v(i,j));
	}


    // matrix addition
	public Matrix sum(Matrix b){
		Matrix c = new Matrix(n);
		for(int i = 0; i< n;i++){
			for(int j = 0; j<n;j++){
				c.setV(i, j, m[i][j]+b.v(i, j));
			}
		}
		return c;
	}

    // matrix subtraction
	public Matrix sub(Matrix b){
		Matrix c = new Matrix(n);
		for(int i = 0; i< n;i++){
			for(int j = 0; j<n;j++){
				c.setV(i, j, m[i][j]-b.v(i, j));
			}
		}
		return c;
	}

	// debug matrix
	public void debug(){
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				System.out.print(this.v(i, j)+" ");
			}
			System.out.println();
		}
	}

	//simple multiplication
	public Matrix mult(Matrix b){
		Matrix out = new Matrix(this.n);

        for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				for (int k = 0; k < this.n; k++) {
					out.m[i][j] += this.v(i, k) * b.v(k, j);
				}
			}
		}

		return out;
	}

    // return a square submatrix from this
	public Matrix getSubmatrix(int startRow, int startCol, int dim){
		Matrix subM = new Matrix(dim);
		for (int i = 0; i<dim ; i++ )
			for (int j=0;j<dim ; j++ )
				subM.setV(i,j, m[startRow+i][startCol+j]);
		return subM;
	}

	public Matrix slice(int s){
		Matrix out = new Matrix(this.n/2);
		int startx = 0;
		int starty = 0;
		switch (s) {
			case 11:
				startx  = 0;
				starty  = 0;
				break;
			
			case 21:
				startx  = this.n/2;
				starty  = 0;
				break;
			
			case 12:
				startx  = 0;
				starty  = this.n/2;
				break;

			case 22:
				startx  = this.n/2;
				starty  = this.n/2;
				break;
		}

		for (int i = startx; i < startx + this.n/2; i++) {
			for (int j = starty; j < starty + this.n/2; j++) {
				out.setV(i-startx, j-starty, this.v(i, j));
			}
		}
		return out;
	}

	public int allSum(){
		Matrix a = this;
		int out = 0;
		for (int i = 0; i < a.n; i++) {
			for (int j = 0; j < a.n; j++) {
				out += a.v(i,j);
			}
		}
		return out;
	}


    // Strassen multiplication
	public Matrix multStrassen(Matrix b, int cutoff){
		Matrix a = this;
		if(b.n == cutoff){
			return a.mult(b);
		}
		
		Matrix m1 = a.slice(11).sum(a.slice(22)).multStrassen(b.slice(11).sum(b.slice(22)), cutoff);
		Matrix m2 = a.slice(21).sum(a.slice(22)).multStrassen(b.slice(11), cutoff);
		Matrix m3 = a.slice(11).multStrassen(b.slice(12).sub(b.slice(22)), cutoff);
		Matrix m4 = a.slice(22).multStrassen(b.slice(21).sub(b.slice(11)), cutoff);
		Matrix m5 = a.slice(11).sum(a.slice(12)).multStrassen(b.slice(22), cutoff);
		Matrix m6 = a.slice(21).sub(a.slice(11)).multStrassen(b.slice(11).sum(b.slice(12)), cutoff);
		Matrix m7 = a.slice(12).sub(a.slice(22)).multStrassen(b.slice(21).sum(b.slice(22)), cutoff);

		System.out.println("m1: " + m1.allSum());
		System.out.println("m2: " + m2.allSum());
		System.out.println("m3: " + m3.allSum());
		System.out.println("m4: " + m4.allSum());
		System.out.println("m5: " + m5.allSum());
		System.out.println("m6: " + m6.allSum());
		System.out.println("m7: " + m7.allSum());


		Matrix c11 = m1.sum(m4).sub(m5).sum(m7);
		Matrix c21 = m3.sum(m5);
		Matrix c12 = m2.sum(m4);
		Matrix c22 = m1.sub(m2).sum(m3).sum(m6);

		Matrix out = new Matrix(a.n);
		out.putSubmatrix(0,0,c11);
		out.putSubmatrix(0,out.n/2,c12);
		out.putSubmatrix(out.n/2,0,c21);
		out.putSubmatrix(out.n/2,out.n/2,c22);

		return out;
	}
}




public class izziv{
	public static void main(String[] args) {
		/* Matrix a = new Matrix(4);
		Matrix b = new Matrix(4);
		

		for (int i = 0; i < a.n; i++) {
			for (int j = 0; j < a.n; j++) {
				a.setV(i, j, i+j);
			}
		}

		for (int i = 0; i < b.n; i++) {
			for (int j = 0; j < b.n; j++) {
				b.setV(i, j, i+j);
			}
		}
		a.debug(); 
		a.multStrassen(a, 1).debug();
		a.mult(a).debug(); */

		Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
		int cutoff = scanner.nextInt();

		Matrix a = new Matrix(n);
		Matrix b = new Matrix(n);

		for (int i = 0; i < a.n; i++) {
			for (int j = 0; j < a.n; j++) {
				a.setV(i, j, scanner.nextInt());
			}
		}

		for (int i = 0; i < b.n; i++) {
			for (int j = 0; j < b.n; j++) {
				b.setV(i, j, scanner.nextInt());
			}
		}

		a.multStrassen(b, cutoff).debug();

	}
}
