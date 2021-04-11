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

    // return a square submatrix from this
	public Matrix getSubmatrix(int startRow, int startCol, int dim){
		Matrix subM = new Matrix(dim);
		for (int i = 0; i<dim ; i++ )
			for (int j=0;j<dim ; j++ )
				subM.setV(i,j, m[startRow+i][startCol+j]);
		return subM;
	}


    // write this matrix as a submatrix from b (useful for the result of multiplication)
	public void putSubmatrix(int startRow, int startCol, Matrix b){
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
		for (int i = 0; i < a.n; i++) {
			for (int j = 0; j < a.n; j++) {
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


    // Strassen multiplication
	/* public Matrix multStrassen(Matrix b, int cutoff){
        //TODO
	} */
}




public class izziv{
	public static void main(String[] args) {
		Matrix a = new Matrix(4);
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

		a.mult(b).debug();

	}
}
