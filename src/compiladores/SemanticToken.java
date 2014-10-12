package compiladores;

public class SemanticToken <E>{
	private int tokenIdentificador;
	private E tokenString;
	private char operation;
	
	public int getTokenIdentificador() {
		return tokenIdentificador;
	}
	public void setTokenIdentificador(int tokenIdentificador) {
		this.tokenIdentificador = tokenIdentificador;
	}
	public E getTokenString() {
		return tokenString;
	}
	public void setTokenString(E tokenString) {
		this.tokenString = tokenString;
	}
	public char getOperation() {
		return operation;
	}
	public void setOperation(char operation) {
		this.operation = operation;
	}

}
