package compiladores;

public class SymbolTable {
	private int tokenIdentificador;
	private String tokenString;
	private int scope;
	
	SymbolTable (int tokenIdentificador,String tokenString, int scope){
		this.setTokenIdentificador(tokenIdentificador);
		this.setTokenString(tokenString);
		this.setScope(scope);
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
	public String getTokenString() {
		return tokenString;
	}
	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}
	public int getTokenIdentificador() {
		return tokenIdentificador;
	}
	public void setTokenIdentificador(int tokenIdentificador) {
		this.tokenIdentificador = tokenIdentificador;
	}
}
