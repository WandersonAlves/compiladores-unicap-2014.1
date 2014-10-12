package compiladores;

public class Token {
	private int tokenIdentificador;
	private int linha;
	private int coluna;
	private String tokenString;
	
	
	public String getTokenString (){
		return tokenString;
	}
	public int getTokenIdentificador (){
		return tokenIdentificador;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public void setTokenIdentificador(int tokenIdentificador) {
		this.tokenIdentificador = tokenIdentificador;
	}
	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}
	
	public Token (int tokenIdentificador, String tokenString, int linha, int coluna)
	{
		this.tokenIdentificador = tokenIdentificador;
		this.tokenString = tokenString;
		this.linha = linha;
		this.coluna = coluna;
	}
	public Token(int tokenIdentificador, char charAt, int linha, int coluna) {
		// TODO Auto-generated constructor stub
		this.tokenString = "";
		this.tokenIdentificador = tokenIdentificador;
		this.tokenString += charAt;
		this.linha = linha;
		this.coluna = coluna;
	}

}
