package compiladores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//VERSÃO 0.1 (Start) 27/02/2014 02:45 reconhecimento de inteiros e floats ok, contagem de linha e erro ok
//VERSÃO 0.3.6 09/03/2014 reconhecimento de inteiros, floats, caracteres, comentarios tudo ok, falta implementar os outros identificadores
//VERSÃO 1.1 14/03/2014 Fresh Start: reconhecimento de inteiros, floats, variaveis e reservadas, comentarios simples
//VERSÃO 1.4 16/03/2014 Scanner finalizado, falta colocar os closes quando ouver error
//VERSÃO 1.7 24/03/2014 Varios bugfixes
//VERSÃO 1.7.2 26/03/2014 Corrigido bug de não captura do token ','

public class Scanner {
	public static boolean isNumber(String texto) {
		if (texto == null)
			return false;
		for (char letra : texto.toCharArray())
			if (!Character.isDigit(letra))
				return false;
		return true;

	}

	public static boolean isWord(String texto) {
		if (texto == null)
			return false;
		for (char letra : texto.toCharArray())
			if (!Character.isAlphabetic(letra))
				return false;
		return true;

	}

	public static boolean isVar(char texto) {
		if (Character.isAlphabetic(texto))
			return true;
		else if (Character.isDigit(texto))
			return true;
		else if (texto == '_')
			return true;
		else
			return false;
	}
	public static boolean isValid (char texto){
		if (Character.isAlphabetic(texto) || Character.isDigit(texto) || texto == '_' || texto == '<' || texto =='>' || texto =='=' || texto  == '!' || texto == ',' || texto == '.' || texto == ';' || texto == '*' || texto == '+' || texto == '-' || texto == '/')
			return true;
		else
			return false;
	}
	private static void igualdadeFixer (ArrayList<Token> tokens){

		for (int tokenAtual = 0; tokenAtual < tokens.size(); tokenAtual++){
			if (tokens.get(tokenAtual).getTokenIdentificador() == Identificadores.ATRIBUIÇÃO){
				
				tokenAtual++;
				if (tokens.get(tokenAtual).getTokenIdentificador() == Identificadores.ATRIBUIÇÃO)
				{
					
					tokens.remove(tokenAtual);
					tokenAtual--;
					tokens.get(tokenAtual).setTokenIdentificador(Identificadores.IGUALDADE);
					tokens.get(tokenAtual).setTokenString("==");
				}
			}
		}
		
	}
	/*public static boolean notValid(char texto){
		if (Character.isAlphabetic(texto) || Character.isDigit(texto) || (char)valorTokenAtual ==)
	}*/
	//FIXME Concertar comentario composto
	public static ArrayList<Token> scanner(InputStream file) {
		ArrayList<Token> lista = new ArrayList<Token>();
		try {

			int valorTokenAtual;
			int linha = 1;
			int coluna = 0;
			char commentHelper;
			String valorTokenFixo = "";
			BufferedReader input = new BufferedReader(new InputStreamReader(file));

			// INICIO SCANNER
			// Ler enquanto o caracter for -1 *pois o retorno de input.read é
			// int e -1 é EOF*
			while ((valorTokenAtual = input.read()) != -1) {
				coluna++;
				if (valorTokenAtual != '_' && valorTokenAtual != -1 && !Character.isWhitespace((char)valorTokenAtual) && !Character.isDigit((char)valorTokenAtual) && !Character.isAlphabetic((char)valorTokenAtual) && (char)valorTokenAtual !='<' && (char)valorTokenAtual !='>' && (char)valorTokenAtual !='=' && (char)valorTokenAtual !='!' && (char)valorTokenAtual !='+'&& (char)valorTokenAtual !='-'&& (char)valorTokenAtual !='*'&& (char)valorTokenAtual !='/'&& (char)valorTokenAtual !='('&& (char)valorTokenAtual !=')'&& (char)valorTokenAtual !='{'&& (char)valorTokenAtual !='}'&& (char)valorTokenAtual !='.'&& (char)valorTokenAtual !=','&& (char)valorTokenAtual !=';' && (char)valorTokenAtual != '\'')
					{
						System.out.println("Erro, caracter invalido encontrado na linha: "+linha+" e coluna: "+coluna+" token: "+(char)valorTokenAtual);
						System.exit(-1);
					}
				if ((char) valorTokenAtual == '\n') {
					linha++;
					coluna = 0;
				}
				if ((char) valorTokenAtual == '\t') {
					coluna+=4;
				}
				if ((char) valorTokenAtual == '=') {
					lista.add(new Token(30, (char) valorTokenAtual, linha,
							coluna));
					valorTokenFixo = "";
					continue;
				} else if ((char) valorTokenAtual == '+') {
					lista.add(new Token(31, (char) valorTokenAtual, linha,
							coluna));
					valorTokenFixo = "";
					continue;
				} else if ((char) valorTokenAtual == '-') {
					lista.add(new Token(32, (char) valorTokenAtual, linha,
							coluna));
					valorTokenFixo = "";
					continue;
				} else if ((char) valorTokenAtual == '*') {
					lista.add(new Token(33, (char) valorTokenAtual, linha,
							coluna));
					valorTokenFixo = "";
					continue;
				} else if ((char) valorTokenAtual == ';') {
					lista.add(new Token(22, (char) valorTokenAtual, linha,
							coluna));
					valorTokenFixo = "";
					continue;
				} else if ((char) valorTokenAtual == ',') {
					lista.add(new Token(23, (char) valorTokenAtual, linha,
							coluna));
					valorTokenFixo = "";
					continue;
				}
				// ADICIONAR VERIFICAÇÃO DOS COMENTARIOS
				// AQUIIIIIIIIIIIIIIIIII!!!
				else if ((char) valorTokenAtual == '/') {
					// Marca o local no arquivo onde apareceu a primeira / e
					// avança com o ponteiro
					input.mark(0);
					commentHelper = (char) valorTokenAtual;
					valorTokenAtual = input.read();
					coluna++;
					if ((char) valorTokenAtual == '\n') {
						linha++;
						coluna = 0;
					}
					if ((char) valorTokenAtual == '\t') {
						coluna+=4;
					}
					// Se o proximo ponteiro for tbm uma / indica inicio de
					// comentarios simples
					if ((char) valorTokenAtual == '/')
						while ((char) valorTokenAtual != '\n') {
							valorTokenFixo = "";
							valorTokenAtual = input.read();
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							if ((char) valorTokenAtual == '\t') {
								coluna+=4;
							}
							if (valorTokenAtual == -1)
								break;
							
						}
					else if ((char) valorTokenAtual == '*') {
						while (!valorTokenFixo.contains("*/")) {
							//Fim do arquivo
							if (valorTokenAtual == -1) {
								System.out
										.println("Erro, comentario composto invalido, começando na linha: "
												+ linha);
								System.exit(1);
								break;
							}
							valorTokenFixo += (char) valorTokenAtual;
							if (valorTokenFixo.contains("*/"))
									break;
							valorTokenAtual = input.read();
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							if ((char) valorTokenAtual == '\t') {
								coluna+=4;
							}
						}
						valorTokenFixo = "";
					}
					// Caso o proximo não seja uma / reseta o input para a barra
					// e adiciona ela como divisão
					else {
						input.reset();
						coluna--;
						valorTokenAtual = input.read();
						lista.add(new Token(34, commentHelper, linha, coluna));
						valorTokenFixo = "";
					}
				}
				// Se o caracterer lido for digito
				if (Character.isDigit((char) valorTokenAtual)) {
					// Enquando for digito, adicione ao buffer
					while (Character.isDigit((char) valorTokenAtual)) {
						valorTokenFixo += (char) valorTokenAtual;
						valorTokenAtual = input.read();
						coluna++;
						if ((char) valorTokenAtual == '\n') {
							linha++;
							coluna = 0;
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							valorTokenFixo = "";
							break;
						}
						if ((char) valorTokenAtual == '\t') {
							coluna+=4;
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							valorTokenFixo = "";
							break;
						}
						if ((char) valorTokenAtual == '=') {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							lista.add(new Token(30, (char) valorTokenAtual,
									linha, coluna));
							valorTokenFixo = "";
							break;
						} else if ((char) valorTokenAtual == '+') {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							lista.add(new Token(31, (char) valorTokenAtual,
									linha, coluna));
							valorTokenFixo = "";
							break;
						} else if ((char) valorTokenAtual == '-') {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							lista.add(new Token(32, (char) valorTokenAtual,
									linha, coluna));
							valorTokenFixo = "";
							break;
						} else if ((char) valorTokenAtual == '*') {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							lista.add(new Token(33, (char) valorTokenAtual,
									linha, coluna));
							valorTokenFixo = "";
							break;
						} else if ((char) valorTokenAtual == ';') {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							lista.add(new Token(22, (char) valorTokenAtual,
									linha, coluna));
							valorTokenFixo = "";
							break;
						} else if ((char) valorTokenAtual == ',') {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							lista.add(new Token(23, (char) valorTokenAtual,
									linha, coluna));
							valorTokenFixo = "";
							break;
						} else if ((char) valorTokenAtual == '/') {
							// Marca o local no arquivo onde apareceu a primeira
							// / e avança com o ponteiro
							input.mark(0);
							valorTokenAtual = input.read();
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							if ((char) valorTokenAtual == '\t') {
								coluna+=4;
							}
							// Se o proximo ponteiro for tbm uma / indica inicio
							// de comentarios simples
							if ((char) valorTokenAtual == '/') {
								lista.add(new Token(1, valorTokenFixo, linha,
										coluna));
								while ((char) valorTokenAtual != '\n') {
									valorTokenFixo = "";
									valorTokenAtual = input.read();
									coluna++;
									if (valorTokenAtual == -1)
										break;
								}
							}
							
							// Caso o proximo não seja uma / reseta o input para
							// a barra e adiciona ela como divisão
							else {
//								input.reset();
								lista.add(new Token(1, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(34, '/', linha, coluna));
								valorTokenFixo = "";
								break;
							}

						}
						if ((char) valorTokenAtual == '\n') {
							linha++;
							coluna = 0;
						}
						if ((char) valorTokenAtual == '\t') {
							coluna+=4;
						}
//						 Se um ponto, preparar para armazenar float
						if ((char) valorTokenAtual == '.') {
							valorTokenFixo += (char) valorTokenAtual;
							valorTokenAtual = input.read();
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							if ((char) valorTokenAtual == '\t') {
								coluna+=4;
							}
							if (Character.isDigit((char) valorTokenAtual)) {
								while (Character
										.isDigit((char) valorTokenAtual)) {
									valorTokenFixo += (char) valorTokenAtual;
									valorTokenAtual = input.read();
									coluna++;
									if ((char) valorTokenAtual == '=') {
										lista.add(new Token(2, valorTokenFixo, linha,
												coluna));
										lista.add(new Token(30,
												(char) valorTokenAtual, linha,
												coluna));
										valorTokenFixo = "";
										break;
									}
									else if ((char) valorTokenAtual == '/') {
										lista.add(new Token(2, valorTokenFixo, linha,
												coluna));
										lista.add(new Token(34,
												(char) valorTokenAtual, linha,
												coluna));
										valorTokenFixo = "";
										break;
									}
									else if ((char) valorTokenAtual == '*') {
										lista.add(new Token(2, valorTokenFixo, linha,
												coluna));
										lista.add(new Token(33,
												(char) valorTokenAtual, linha,
												coluna));
										valorTokenFixo = "";
										break;
									}
									 else if ((char) valorTokenAtual == ';') {
											lista.add(new Token(2, valorTokenFixo, linha,
													coluna));
											lista.add(new Token(22, (char) valorTokenAtual,
													linha, coluna));
											valorTokenFixo = "";
											break;
									 }
									if (!Character
											.isDigit((char) valorTokenAtual)) {
										lista.add(new Token(2, valorTokenFixo,
												linha, coluna));
										valorTokenFixo = "";
										break;
									}
								}
							}
							// Mensagem de erro
							else {
								System.out
										.println("Erro, float mal formado na linha "
												+ linha
												+ " e coluna "
												+ coluna
												+ " Token: " + valorTokenFixo);
								valorTokenFixo = "";
								System.exit(1);
								break;
							}
						}
						// Problema aqui: em segmetos do tipo
						// <numeros><caracters> ignora-se os numeros e O
						// PRIMEIRO CHAR DOS CARACTEREs
						// Assim invalidando o proximo token com uma letra a
						// menos
						// SOLVED
						else if (!Character.isDigit((char) valorTokenAtual)
								&& valorTokenAtual != 10 && valorTokenAtual != -1) {
							lista.add(new Token(1, valorTokenFixo, linha,
									coluna));
							valorTokenFixo = "";
						}
					}
				}

				// Se o caracter lido for caracter
				if (Character.isAlphabetic((char) valorTokenAtual)
						|| (char) valorTokenAtual == '_') {
					valorTokenFixo += (char) valorTokenAtual;
					valorTokenAtual = input.read();
					coluna++;
					if ((char) valorTokenAtual == '\n') {
						linha++;
						coluna = 0;
					}
					if ((char) valorTokenAtual == '\t') {
						coluna+=4;
					}
					if ((char) valorTokenAtual == '=') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(30, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if ((char) valorTokenAtual == '+') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(31, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if ((char) valorTokenAtual == '-') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(32, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if ((char) valorTokenAtual == '*') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(33, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if ((char) valorTokenAtual == '/') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(34, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if ((char) valorTokenAtual == ';') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(22, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if ((char) valorTokenAtual == ',') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						lista.add(new Token(23, (char) valorTokenAtual, linha,
								coluna));
						valorTokenFixo = "";
					} else if (isVar((char) valorTokenAtual)) {
						while (isVar((char) valorTokenAtual)) {
							valorTokenFixo += (char) valorTokenAtual;
							valorTokenAtual = input.read();
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
								if (valorTokenFixo.equals("int")) {
									// Adiciona um identificador INT
									lista.add(new Token(11, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("float")) {
									// Adiciona um identificador FLOAT
									lista.add(new Token(12, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("char")) {
									// Adiciona um identificador CHAR
									lista.add(new Token(15, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("for")) {
									// Adiciona um identificador FOR
									lista.add(new Token(16, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("do")) {
									// Adiciona um identificador DO
									lista.add(new Token(17, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("while")) {
									// Adiciona um identificador WHILE
									lista.add(new Token(18, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("else")) {
									// Adiciona um identificador ELSE
									lista.add(new Token(19, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("if")) {
									// Adiciona um identificador IF
									lista.add(new Token(21, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("main")) {
									// Adiciona um identificador MAIN
									lista.add(new Token(100, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else {
									lista.add(new Token(20, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								}
							}

							if ((char) valorTokenAtual == '=') {
								lista.add(new Token(20, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(30, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							if ((char) valorTokenAtual == '+') {
								lista.add(new Token(20, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(31, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							if ((char) valorTokenAtual == ',') {
								lista.add(new Token(20, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(23, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							if ((char) valorTokenAtual == ';') {
								lista.add(new Token(20, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(22, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							if ((char) valorTokenAtual == '/') {
								// Marca o local no arquivo onde apareceu a
								// primeira / e avança com o ponteiro
								input.mark(0);
								valorTokenAtual = input.read();
								coluna++;
								if ((char) valorTokenAtual == '\n') {
									linha++;
									coluna = 0;
								}
								if ((char) valorTokenAtual == '\t') {
									coluna+=4;
								}
								// Se o proximo ponteiro for tbm uma / indica
								// inicio de comentarios simples
								if ((char) valorTokenAtual == '/') {
									if (valorTokenFixo.equals("int")) {
										// Adiciona um identificador INT
										lista.add(new Token(11, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("float")) {
										// Adiciona um identificador FLOAT
										lista.add(new Token(12, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("char")) {
										// Adiciona um identificador CHAR
										lista.add(new Token(15, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("for")) {
										// Adiciona um identificador FOR
										lista.add(new Token(16, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("do")) {
										// Adiciona um identificador DO
										lista.add(new Token(17, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("while")) {
										// Adiciona um identificador WHILE
										lista.add(new Token(18, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("else")) {
										// Adiciona um identificador ELSE
										lista.add(new Token(19, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("if")) {
										// Adiciona um identificador IF
										lista.add(new Token(21, valorTokenFixo,
												linha, coluna));
									} else if (valorTokenFixo.equals("main")) {
										// Adiciona um identificador MAIN
										lista.add(new Token(100, valorTokenFixo,
												linha, coluna));
									} else {
										lista.add(new Token(20, valorTokenFixo,
												linha, coluna));
									}
							
									while ((char) valorTokenAtual != '\n') {
										valorTokenFixo = "";
										valorTokenAtual = input.read();
										coluna++;
										if ((char) valorTokenAtual == '\n') {
											linha++;
											coluna = 0;
										}
										if ((char) valorTokenAtual == '\t') {
											coluna+=4;
										}
									}
								}
								// Caso o proximo não seja uma / reseta o input
								// para a barra e adiciona ela como divisão
								else {
									input.reset();
									coluna--;
									lista.add(new Token(20, valorTokenFixo,
											linha, coluna));
									lista.add(new Token(34, '/', linha, coluna));
									valorTokenFixo = "";
									break;
								}

							}
							if (!isVar((char) valorTokenAtual)
									|| (char) valorTokenAtual == '\n') {
								if (valorTokenFixo.equals("int")) {
									// Adiciona um identificador INT
									lista.add(new Token(11, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("float")) {
									// Adiciona um identificador FLOAT
									lista.add(new Token(12, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("char")) {
									// Adiciona um identificador CHAR
									lista.add(new Token(15, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("for")) {
									// Adiciona um identificador FOR
									lista.add(new Token(16, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("do")) {
									// Adiciona um identificador DO
									lista.add(new Token(17, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("while")) {
									// Adiciona um identificador WHILE
									lista.add(new Token(18, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("else")) {
									// Adiciona um identificador ELSE
									lista.add(new Token(19, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("if")) {
									// Adiciona um identificador IF
									lista.add(new Token(21, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenFixo.equals("main")) {
									// Adiciona um identificador MAIN
									lista.add(new Token(100, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								} else if (valorTokenAtual != 10) {
									lista.add(new Token(20, valorTokenFixo,
											linha, coluna));
									valorTokenFixo = "";
									break;
								}
							}
						}
					} else if (!isVar((char) valorTokenAtual)
							|| (char) valorTokenAtual == '\n') {
						lista.add(new Token(20, valorTokenFixo, linha, coluna));
						valorTokenFixo = "";
					}
				}
				// Floats começando com o .
				if ((char) valorTokenAtual == '.') {
					valorTokenFixo += (char) valorTokenAtual;
					valorTokenAtual = input.read();
					coluna++;
					if ((char) valorTokenAtual == '\n') {
						linha++;
						coluna = 0;
					}
					if ((char) valorTokenAtual == '\t') {
						coluna+=4;
					}

					if (Character.isDigit((char) valorTokenAtual)) {
						while (Character.isDigit((char) valorTokenAtual)) {
							valorTokenFixo += (char) valorTokenAtual;
							valorTokenAtual = input.read();
							coluna++;
							if ((char) valorTokenAtual == ';') {
									lista.add(new Token(2, valorTokenFixo, linha,
											coluna));
									lista.add(new Token(22, (char) valorTokenAtual,
											linha, coluna));
									valorTokenFixo = "";
									break;
							}
							else if ((char) valorTokenAtual == '*') {
								lista.add(new Token(2, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(33, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							else if ((char) valorTokenAtual == '/') {
								lista.add(new Token(2, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(34, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							else if ((char) valorTokenAtual == '+') {
								lista.add(new Token(2, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(31, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							else if ((char) valorTokenAtual == '-') {
								lista.add(new Token(2, valorTokenFixo, linha,
										coluna));
								lista.add(new Token(32, (char) valorTokenAtual,
										linha, coluna));
								valorTokenFixo = "";
								break;
							}
							if (!Character.isDigit((char) valorTokenAtual)) {
								lista.add(new Token(2, valorTokenFixo, linha,
										coluna));
								valorTokenFixo = "";
								break;
							}
						}
					} else {
						System.out.println("Erro, float mal formado na linha "
								+ linha + " e coluna " + coluna + " Token: "
								+ valorTokenFixo);
						valorTokenFixo = "";
						System.exit(1);
					}
				}
				if (valorTokenAtual == 39) {
					valorTokenAtual = input.read();
					coluna++;
					if ((char) valorTokenAtual == '\n') {
						linha++;
						coluna = 0;
					}
					if ((char) valorTokenAtual == '\t') {
						coluna+=4;
					}
					if (Character.isDigit((char) valorTokenAtual)
							|| Character.isAlphabetic((char) valorTokenAtual)) {
						valorTokenFixo += (char) valorTokenAtual;
						valorTokenAtual = input.read();
						coluna++;
						if ((char) valorTokenAtual == '\n') {
							linha++;
							coluna = 0;
						}
						if ((char) valorTokenAtual == '\t') {
							coluna+=4;
						}
						if (valorTokenAtual == 39) {
							lista.add(new Token(5, valorTokenFixo, linha,
									coluna));
							valorTokenFixo = "";
						} else {
							System.out
									.println("Erro, char mal formado na linha: "
											+ linha
											+ " coluna: "
											+ coluna
											+ " Token: " + valorTokenFixo);
							valorTokenFixo = "";
							System.exit(1);
						}
					} else {
						System.out.println("Erro, char mal formado na linha: "
								+ linha + " coluna: " + coluna + " Token: "
								+ valorTokenFixo);
						valorTokenFixo = "";
						System.exit(1);
					}
				}
				if ((char) valorTokenAtual == '<'|| (char) valorTokenAtual == '>'|| (char) valorTokenAtual == '!'
						|| (char) valorTokenAtual == '('|| (char) valorTokenAtual == ')'|| (char) valorTokenAtual == '{'
						|| (char) valorTokenAtual == '}'|| (char) valorTokenAtual == ',') {
					if ((char) valorTokenAtual == '<') {
						input.mark(0);
						if ((valorTokenAtual = input.read()) != '=') {
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							lista.add(new Token(35, '<', linha, coluna));
							input.reset();
							coluna--;
						} else {
							lista.add(new Token(38, "<=", linha, coluna));
						}
					}
					if ((char) valorTokenAtual == '>') {
						input.mark(0);
						if ((valorTokenAtual = input.read()) != '=') {
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							lista.add(new Token(36, '>', linha, coluna));
							input.reset();
							coluna--;
						} else {
							lista.add(new Token(39, ">=", linha, coluna));
						}
					} else if ((char) valorTokenAtual == '!') {
						if ((valorTokenAtual = input.read()) != '=') {
							coluna++;
							if ((char) valorTokenAtual == '\n') {
								linha++;
								coluna = 0;
							}
							System.out
									.println("Erro, caracter '!' requer '=' em seguida na linha: "
											+ linha + " coluna: " + coluna);
							valorTokenFixo += (char) valorTokenAtual;
							System.exit(1);
						} else {
							lista.add(new Token(37, "!=", linha, coluna));
						}
					} else if ((char) valorTokenAtual == '(') {
						lista.add(new Token(28, '(', linha, coluna));
					} else if ((char) valorTokenAtual == ')') {
						lista.add(new Token(29, ')', linha, coluna));
					} else if ((char) valorTokenAtual == '{') {
						lista.add(new Token(26, '{', linha, coluna));
					} else if ((char) valorTokenAtual == '}') {
						lista.add(new Token(27, '}', linha, coluna));
					}

				}
				
				
			}
			igualdadeFixer(lista);

			// APENAS PARA DEBUG E TAL
			for (int i = 0; i < lista.size(); i++) {
				if (lista.get(i).getTokenIdentificador() == 1)
					System.out.print("int: ");
				else if (lista.get(i).getTokenIdentificador() == 2)
					System.out.print("float: ");
				else if (lista.get(i).getTokenIdentificador() == 5)
					System.out.println("char: ");
				else if (lista.get(i).getTokenIdentificador() == 15)
					System.out.print("CHAR: ");
				else if (lista.get(i).getTokenIdentificador() == 16)
					System.out.print("FOR: ");
				else if (lista.get(i).getTokenIdentificador() == 17)
					System.out.print("DO: ");
				else if (lista.get(i).getTokenIdentificador() == 18)
					System.out.print("WHILE: ");
				else if (lista.get(i).getTokenIdentificador() == 19)
					System.out.print("ELSE: ");
				else if (lista.get(i).getTokenIdentificador() == 21)
					System.out.print("IF: ");
				else if (lista.get(i).getTokenIdentificador() == 100)
					System.out.print("MAIN: ");
				else if (lista.get(i).getTokenIdentificador() == 11)
					System.out.print("INT: ");
				else if (lista.get(i).getTokenIdentificador() == 12)
					System.out.print("FLOAT: ");
				else if (lista.get(i).getTokenIdentificador() == 20)
					System.out.print("variavel: ");
				else if (lista.get(i).getTokenIdentificador() == 22)
					System.out.print("ponto e virgula: ");
				else if (lista.get(i).getTokenIdentificador() == 23)
					System.out.print("virgula: ");
				else if (lista.get(i).getTokenIdentificador() == 26)
					System.out.print("abre colchete: ");
				else if (lista.get(i).getTokenIdentificador() == 27)
					System.out.print("fecha colchete: ");
				else if (lista.get(i).getTokenIdentificador() == 28)
					System.out.print("abre parentese: ");
				else if (lista.get(i).getTokenIdentificador() == 29)
					System.out.print("fecha parentese: ");
				else if (lista.get(i).getTokenIdentificador() == 30)
					System.out.print("Atribuição: ");
				else if (lista.get(i).getTokenIdentificador() == 31)
					System.out.print("soma: ");
				else if (lista.get(i).getTokenIdentificador() == 32)
					System.out.print("subtração: ");
				else if (lista.get(i).getTokenIdentificador() == 33)
					System.out.print("multiplicação: ");
				else if (lista.get(i).getTokenIdentificador() == 34)
					System.out.print("divisão: ");
				else if (lista.get(i).getTokenIdentificador() == 35)
					System.out.print("menor: ");
				else if (lista.get(i).getTokenIdentificador() == 36)
					System.out.print("maior: ");
				else if (lista.get(i).getTokenIdentificador() == 37)
					System.out.print("diferença: ");
				else if (lista.get(i).getTokenIdentificador() == 38)
					System.out.print("MenorIgual: ");
				else if (lista.get(i).getTokenIdentificador() == 39)
					System.out.print("MaiorIgual: ");
				else if (lista.get(i).getTokenIdentificador() == 40)
					System.out.print("Igualdade: ");
				System.out.println(lista.get(i).getTokenString() + " [" + i
						+ "]");
			}

		} catch (IOException e) {
			System.out.println("Erro ao abrir arquivo!");
			System.exit(1);

		}
		return lista;

	}
}
