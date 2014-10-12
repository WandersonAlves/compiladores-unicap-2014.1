package compiladores;

import java.util.ArrayList;

public class errorHelperImpl {
	public static void errorHelper(ArrayList<Token> token, int status, int posTokenAtual) {
		if (status == 1) {
			System.out.println("Erro, tipo invalido. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(1);
		} else if (status == 2) {
			System.out
			.println("Erro, iniciação de codigo invalida, inicie com 'int main ()'. Linha: "
					+ token.get(posTokenAtual).getLinha()
					+ " Coluna: "
					+ token.get(posTokenAtual).getColuna()
					+ " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(2);
		} else if (status == 3) {
			System.out.println("Erro, falta fechar o bloco. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(3);
		} else if (status == 4) {
			System.out.println("Erro, bloco de comandos invalido. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(4);
		} else if (status == 5) {
			System.out
			.println("Erro, esperava-se uma virgula entre as variaveis. Linha: "
					+ token.get(posTokenAtual).getLinha()
					+ " Coluna: "
					+ token.get(posTokenAtual).getColuna()
					+ " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(5);
		} else if (status == 6) {
			System.out
			.println("Erro, faltou encerrar linha de codigo com ';'. Linha: "
					+ token.get(posTokenAtual).getLinha()
					+ " Coluna: "
					+ token.get(posTokenAtual).getColuna()
					+ " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(6);
		} else if (status == 7) {
			System.out.println("Erro, esperava-se uma variavel. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(7);
		} else if (status == 8) {
			System.out
			.println("Erro, faltou encerrar linha de codigo com ';'. Linha: "
					+ token.get(posTokenAtual).getLinha()
					+ " Coluna: "
					+ token.get(posTokenAtual).getColuna()
					+ " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(8);
		} else if (status == 9) {
			System.out.println("Erro, identificador invalido. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(9);
		} else if (status == 10) {
			System.out.println("Erro, expressão invalida. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(10);
		} else if (status == 11) {
			System.out.println("Erro, atribuição invalida. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(11);
		} else if (status == 12) {
			System.out.println("Erro, expressão relacional invalida. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(12);
		} else if (status == 13) {
			System.out.println("Erro, esperava-se atribuição. Linha: "
					+ token.get(posTokenAtual).getLinha() + " Coluna: "
					+ token.get(posTokenAtual).getColuna() + " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(13);
		} else if (status == 14) {
			System.out
			.println("Erro, iniciação de expressão relacional invalida. Linha: "
					+ token.get(posTokenAtual).getLinha()
					+ " Coluna: "
					+ token.get(posTokenAtual).getColuna()
					+ " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(14);
		} else if (status == 15) {
			System.out
			.println("Erro, declaração dentro de comando Linha: "
					+ token.get(posTokenAtual).getLinha()
					+ " Coluna: "
					+ token.get(posTokenAtual).getColuna()
					+ " Token: "
					+ token.get(posTokenAtual).getTokenString());
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(15);
		}
		else if (status == 16) {
			System.out
			.println("Erro, variaveis repetidas");
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(16);
		}
		else if (status == 200) {
			System.out.println("BUGGG!!! *em termLinha*");
			System.out.println("Tokens lidos: "+posTokenAtual);
			System.exit(200);
		}
	}
}
