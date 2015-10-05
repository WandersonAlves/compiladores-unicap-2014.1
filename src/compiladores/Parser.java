package compiladores;

import java.util.ArrayList;
import java.util.Stack;

import static compiladores.errorHelperImpl.errorHelper;

/*
 [OK] <declaration> ::= <type> <identifier> {,<identifier>}* ;
 [OK] <type> ::= int | float | char
 [OK] <program> ::= int main"("")" <block>
 [OK] <block> ::= “{“ {<declaration>}* {<command>}* “}”
 [OK] <command> ::= <basic_command> | <iteration> | if "("<relational_expr>")" <command> {else <command>}?
 [OK] <basic_command> ::= <assign> | <block>
 [OK] <iteration> ::= while "("<relational_expr>")" <command> | do <command> while "("<relational_expr>")"";"
 [OK] <assign> ::= <identifier> "=" <arithmetic_expr> ";"
 [OK] <relational_expr> ::= <arithmetic_expr> <relational_operator> <arithmetic_expr>
 [OK] <arithmetic_expr> ::= <arithmetic_expr> "+" <term>   | <arithmetic_expr> "-" <term> | <term>
 [OK] <term> ::= <term> "*" <factor> | <term> “/” <factor> | <factor>
 [OK] <factor> ::= “(“ <arithmetic_expr> “)” | <identifier> | <float> | <integer> | <char>

 Removing Left-Recursion:

 -------<arithmetic_expr>-------
 A -> A+T | A-T | T

 A -> TA'
 A'-> e|+TA'|-TA'

 ------------<term>-------------
 T -> T*F | T/F | F

 T -> FT'
 T'-> e|*FT'|/FT'

 
 
 Existe atribuição no inicio? *No bloco* Atribuições do tipo char a = 'a' ou a = 'a'	 

 */

public class Parser {

	private static int posTokenAtual = 0;
	private static int scope = 0;
	private static Stack<SymbolTable> symbolTable = new Stack<SymbolTable>();
	@SuppressWarnings("rawtypes")
	private static SemanticToken E1 = new SemanticToken();
	@SuppressWarnings("rawtypes")
	private static SemanticToken E2 = new SemanticToken();
	private static int semanticType;
	private static int result;
	private static int resultFloat = 0;
	private static int assignType;

	private static void stackPrintList(Stack<SymbolTable> symbolTable){
		System.out.println();
		for (int i=0; i<symbolTable.size();i++){
			System.out.println("Token: "+symbolTable.get(i).getTokenString()+" Tipo: "+symbolTable.get(i).getTokenIdentificador()+" Scopo: "+symbolTable.get(i).getScope());
		}	
	}
	private static int compare (int E1, int E2){
//				char + char = char
//				char + int = erro
//				char + float = erro
//				int + char = erro
//				int + float = float
//				int + int = int
//				float + char = erro
//				float + int = float
//				float + float = float
		System.out.println("Compare: "+E1+" "+E2);
		if (E1 == 5 && E2 == 5)
			return 5;
		else if (E1 == 5 && E2 != 5){
			System.out.println("Char não pode ser atribuido a floats ou inteiros");
			System.exit(17);
		}
		else if (E1 == 1 && E2 == 5){
			System.out.println("Char não pode ser atribuido a floats ou inteiros");
			System.exit(17);
		}
		else if (E1 == 1 && E2 == 2)
			return 2;
		else if (E1 == 1 && E2 == 1)
			return 1;
		else if (E1 == 2 && E2 == 5){
			System.out.println("Char não pode ser atribuido a floats ou inteiros");
			System.exit(17);
		}
		else if (E1 == 2 && E2 == 1)
			return 2;
		else if (E1 == 2 && E2 == 2)
			return 2;
		return 0;
			
	}
	private static int stackTypeHelper (ArrayList<Token> token)
	{
		if (token.get(posTokenAtual-1).getTokenIdentificador() == 11)
			return 1;
		else if (token.get(posTokenAtual-1).getTokenIdentificador() == 12)
			return 2;
		else if (token.get(posTokenAtual-1).getTokenIdentificador() == 15)
			return 5;
		else
			return 0;
	}

	private static void stackVerificaToken (Stack<SymbolTable> symbolTable){
		if (symbolTable.size() == 1)
			return;
		else {
			for (int i = 0; i < symbolTable.size()-1; i++){
				if (symbolTable.get(i).getTokenString().equals(symbolTable.get(symbolTable.size()-1).getTokenString()) && symbolTable.get(i).getScope() == symbolTable.get(symbolTable.size()-1).getScope()){
					System.out.println("Variaveis repetidas encontradas no escopo "+scope);
					System.exit(17);
				}
			}
		}
	}
	private static void stackPopScope (Stack<SymbolTable> symbolTable){
		try {
			stackPrintList(symbolTable);
			while (symbolTable.peek().getScope() == scope){
				symbolTable.pop();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println();
			System.out.println("Tabela de simbolos vazia");
			System.out.println();
			
		}
			
	}
	private static boolean stackSearch (ArrayList<Token> token, Stack<SymbolTable> symbolTable){
		
		for (int i = symbolTable.size()-1; i >= 0; i--){
//			System.out.println("Indice: "+i+" "+symbolTable.get(i).getTokenString()+" COMPARE "+token.get(posTokenAtual).getTokenString());
			if (symbolTable.get(i).getTokenString().equals(token.get(posTokenAtual).getTokenString())){
//				E1 = symbolTable.get(i).getTokenIdentificador();
				semanticType = symbolTable.get(i).getTokenIdentificador();
				return true;
			}							
		}
		return false;
	}
	public static void parser(ArrayList<Token> listaTokens) {
		
		program(listaTokens);
		try {
			if (!listaTokens.get(posTokenAtual+1).equals(null))
			{
				System.out.println("Ainda existem comandos além do main");
				System.out.println("Tokens lidos: "+posTokenAtual);
				System.exit(-2000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Tokens lidos: "+posTokenAtual);
		}
		stackPrintList(symbolTable);
	}

	public static int getTokenIdentificador(ArrayList<Token> token) {
		try {
			return token.get(posTokenAtual).getTokenIdentificador();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errorHelper(token, 4, --posTokenAtual);
		}
		return posTokenAtual;
	}

	public static boolean commandHelper(ArrayList<Token> token) {
		if (getTokenIdentificador(token) == Identificadores.idIF)
			return true;
		else if (getTokenIdentificador(token) == Identificadores.idVARIAVEL)
			return true;
		else if (getTokenIdentificador(token) == Identificadores.idWHILE)
			return true;
		else if (getTokenIdentificador(token) == Identificadores.idDO)
			return true;
		else if (getTokenIdentificador(token) == Identificadores.openCOLCHETE)
			return true;
		else
			return false;
	}

	public static void block(ArrayList<Token> token) {
		//<block> ::= “{“ {<declaration>}* {<command>}* “}”
		if (getTokenIdentificador(token) == Identificadores.openCOLCHETE) {
			posTokenAtual++;
			scope++;
			do {
				while (type(token))
					declaration(token);
				while (commandHelper(token)){
					command(token);
					if (type(token))
						errorHelper(token, 15, posTokenAtual);
				}
				if (!commandHelper(token))
					break;
				
			} while (!commandHelper(token) || !type(token));
			if (getTokenIdentificador(token) == Identificadores.closeCOLCHETE) {
				posTokenAtual++;
				stackPopScope(symbolTable);
				scope--;				
			} else
				errorHelper(token, 4,posTokenAtual);
		} else
			errorHelper(token, 4,posTokenAtual);
	}

	public static void declaration(ArrayList<Token> token) {
		if (type(token)) {
			posTokenAtual++;	
			if (identifier(token)) {
				//Parte do semantico
				int tokenIdenficador = stackTypeHelper(token);
				symbolTable.push(new SymbolTable(tokenIdenficador,token.get(posTokenAtual).getTokenString(), scope));
				//////////
				posTokenAtual++;
				// Pilha de escopos
				// {,<identifier>}*
				if (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.VIRGULA) {
					posTokenAtual++;
					if (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.idVARIAVEL)
						while (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.idVARIAVEL) {
							//Semantico
							symbolTable.add(new SymbolTable(tokenIdenficador,token.get(posTokenAtual).getTokenString(), scope));
							////////////
							posTokenAtual++;
							if (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.VIRGULA) {
								posTokenAtual++;
								continue;
							} else if (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.pontoVIRGULA) {
								posTokenAtual++;
								break;
							} else if (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.idVARIAVEL)
								errorHelper(token, 5, posTokenAtual);
							else
								errorHelper(token, 6, posTokenAtual);
						}
					else
						errorHelper(token, 7, posTokenAtual);
				} else if (token.get(posTokenAtual).getTokenIdentificador() == Identificadores.pontoVIRGULA)
					posTokenAtual++;
				else
					errorHelper(token, 8, posTokenAtual);

			} else
				errorHelper(token, 9,posTokenAtual);
		} else
			errorHelper(token, 7,posTokenAtual);
		stackVerificaToken(symbolTable);
	}

	public static void command(ArrayList<Token> token) {
		// <command> ::= <basic_command> | <iteration> | if "("<relational_expr>")" <command> {else <command>}?
		//FIXME Resolver essa ultrapassagem no array por aqui
		if (identifier(token) || getTokenIdentificador(token) == Identificadores.openCOLCHETE){
			basic_command(token);
			// Quantos elses aninhados essa linguagem aceita?
			if (getTokenIdentificador(token) == Identificadores.idELSE){
				posTokenAtual++;
				command(token);
			}				
		}
		else if (getTokenIdentificador(token) == Identificadores.idWHILE || getTokenIdentificador(token) == Identificadores.idDO)
			iteration(token);
		else if (getTokenIdentificador(token) == Identificadores.idIF) {
			posTokenAtual++;
			if (getTokenIdentificador(token) == Identificadores.openPARENTESE) {
				posTokenAtual++;
				relational_expr(token);
				if (getTokenIdentificador(token) == Identificadores.closePARENTESE) {
					posTokenAtual++;
//					if (getTokenIdentificador(token) == Identificadores.openCOLCHETE)
						command(token);
//					else
//						errorHelper(token, 4,posTokenAtual);
					// Quantos elses aninhados essa linguagem aceita?
					if (getTokenIdentificador(token) == Identificadores.idELSE)
						command(token);
				} else
					errorHelper(token, 14,posTokenAtual);
			} else
				errorHelper(token, 14,posTokenAtual);

		}

	}

	public static void basic_command(ArrayList<Token> token) {
		// <basic_command> ::= <assign> | <block>
		if (identifier(token))
			assign(token);
		else if (getTokenIdentificador(token) == Identificadores.openCOLCHETE)
			block(token);
	}

	public static void iteration(ArrayList<Token> token) {
		// <iteration> ::= while "("<relational_expr>")" <command> | do
		// <command> while "("<relational_expr>")"";"
		if (getTokenIdentificador(token) == Identificadores.idWHILE) {
			posTokenAtual++;
			if (getTokenIdentificador(token) == Identificadores.openPARENTESE) {
				posTokenAtual++;
				relational_expr(token);
				if (getTokenIdentificador(token) == Identificadores.closePARENTESE) {
					posTokenAtual++;
//					if (getTokenIdentificador(token) == Identificadores.openCOLCHETE)
						command(token);
//					else
//						errorHelper(token, 4);
				} 
				else
					errorHelper(token, 10,posTokenAtual);
			} 
			else
				errorHelper(token, 10,posTokenAtual);
		} 
		else if (getTokenIdentificador(token) == Identificadores.idDO) {
			posTokenAtual++;
//			if (getTokenIdentificador(token) == Identificadores.openCOLCHETE)
				command(token);
//			else
//				errorHelper(token, 4,posTokenAtual);
			if (getTokenIdentificador(token) == Identificadores.idWHILE) {
				posTokenAtual++;
				if (getTokenIdentificador(token) == Identificadores.openPARENTESE) {
					posTokenAtual++;
					relational_expr(token);
					if (getTokenIdentificador(token) == Identificadores.closePARENTESE) {
						posTokenAtual++;
						if (getTokenIdentificador(token) == Identificadores.pontoVIRGULA) {
							posTokenAtual++;
						} else
							errorHelper(token, 6,posTokenAtual);
					} else
						errorHelper(token, 10,posTokenAtual);
				} else
					errorHelper(token, 10,posTokenAtual);
			}
		}

	}

	public static void relational_operator(ArrayList<Token> token) {
		if (getTokenIdentificador(token) == Identificadores.MENOR)
			posTokenAtual++;
		else if (getTokenIdentificador(token) == Identificadores.MAIOR)
			posTokenAtual++;
		else if (getTokenIdentificador(token) == Identificadores.IGUALDADE)
			posTokenAtual++;
		else if (getTokenIdentificador(token) == Identificadores.DIFERENCA)
			posTokenAtual++;
		else if (getTokenIdentificador(token) == Identificadores.MENORIGUAL)
			posTokenAtual++;
		else if (getTokenIdentificador(token) == Identificadores.MAIORIGUAL)
			posTokenAtual++;
		else
			errorHelper(token, 12,posTokenAtual);
	}

	public static void program(ArrayList<Token> token) {
		boolean status = false;
		if (getTokenIdentificador(token) == Identificadores.idINT) {
			posTokenAtual++;
			if (getTokenIdentificador(token) == Identificadores.idMAIN) {
				posTokenAtual++;
				if (getTokenIdentificador(token) == Identificadores.openPARENTESE) {
					posTokenAtual++;
					if (getTokenIdentificador(token) == Identificadores.closePARENTESE) {
						posTokenAtual++;
						status = true;
					} else
						errorHelper(token, 2,posTokenAtual);
				} else
					errorHelper(token, 2,posTokenAtual);
			} else
				errorHelper(token, 2,posTokenAtual);
		} else
			errorHelper(token, 2,posTokenAtual);
		if (status)
			block(token);
	}

	public static boolean identifier(ArrayList<Token> token) {
		if (getTokenIdentificador(token) == Identificadores.idVARIAVEL)
			return true;
		else
			return false;
	}

	public static boolean type(ArrayList<Token> token) {
		if (getTokenIdentificador(token) == Identificadores.idCHAR
				|| getTokenIdentificador(token) == Identificadores.idFLOAT
				|| getTokenIdentificador(token) == Identificadores.idINT)
			return true;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	public static void factor(ArrayList<Token> token) {
		// <factor> ::= “(“ <arithmetic_expr> “)” | <identifier> | <float> | <integer> | <char>
		if (getTokenIdentificador(token) == Identificadores.openPARENTESE) {
			posTokenAtual++;
			arithmetic_expr(token);
			if (getTokenIdentificador(token) == Identificadores.closePARENTESE)
				posTokenAtual++;
			else
				errorHelper(token, 10,posTokenAtual);
		} else if (identifier(token)) {
			if (stackSearch(token, symbolTable)){
				if (E1.getTokenIdentificador() != 0){
					System.out.println("STACK SEARCH");
					System.out.println("Fator: "+token.get(posTokenAtual).getTokenString());
					System.out.println("Tipo variavel: "+semanticType);
					
					E2.setTokenIdentificador(semanticType);
					E2.setTokenString(token.get(posTokenAtual).getTokenString());
					result = compare(E1.getTokenIdentificador(), E2.getTokenIdentificador());
					if (result == 2)
						resultFloat = result;
					System.out.println("Result: "+result);
					System.out.println();
					E1.setTokenIdentificador(0);
				}
				else{
					System.out.println("STACK SEARCH");
					System.out.println("Fator: "+token.get(posTokenAtual).getTokenString());
					System.out.println("Tipo variavel: "+semanticType);
					System.out.println();
					E1.setTokenIdentificador(semanticType);
					E1.setTokenString(token.get(posTokenAtual).getTokenString());
				}
				posTokenAtual++;
			}
					
			else{
				System.out.println("A variavel *"+token.get(posTokenAtual).getTokenString()+"* não foi encontrada em nenhum dos escopos.");
				System.exit(404);
			}
		}
		// As palavras reservadas de tipo ou valores dos respectivos tipos?
		else if (getTokenIdentificador(token) == Identificadores.FLOAT){
//			E1 = token.get(posTokenAtual).getTokenIdentificador();
			if (E1.getTokenIdentificador() != 0){
				
				System.out.println("Fator: "+token.get(posTokenAtual).getTokenString());
				
				E2.setTokenIdentificador(token.get(posTokenAtual).getTokenIdentificador());
				E2.setTokenString(token.get(posTokenAtual).getTokenString());
				result = compare(E1.getTokenIdentificador(), E2.getTokenIdentificador());
				if (result == 2)
					resultFloat = result;
				System.out.println("Result: "+result);
				System.out.println();
				E1.setTokenIdentificador(0);
			}
			else{
			E1.setTokenIdentificador(token.get(posTokenAtual).getTokenIdentificador());
			E1.setTokenString(token.get(posTokenAtual).getTokenString());
			}
			posTokenAtual++;
		}
		else if (getTokenIdentificador(token) == Identificadores.INT){
//			E1 = token.get(posTokenAtual).getTokenIdentificador();
			if (E1.getTokenIdentificador() != 0){
				System.out.println("Fator: "+token.get(posTokenAtual).getTokenString());
				
				E2.setTokenIdentificador(token.get(posTokenAtual).getTokenIdentificador());
				E2.setTokenString(token.get(posTokenAtual).getTokenString());
				result = compare(E1.getTokenIdentificador(), E2.getTokenIdentificador());
				if (result == 2)
					resultFloat = result;
				System.out.println("Result: "+result);
				System.out.println();
				E1.setTokenIdentificador(0);
			}
			else{
				E1.setTokenIdentificador(token.get(posTokenAtual).getTokenIdentificador());
				E1.setTokenString(token.get(posTokenAtual).getTokenString());
			}
			posTokenAtual++;
		}
		else if (getTokenIdentificador(token) == Identificadores.CHAR){
//			E1 = token.get(posTokenAtual).getTokenIdentificador();
			if (E1.getTokenIdentificador() != 0){
				System.out.println("Fator: "+token.get(posTokenAtual).getTokenString());
				
				E2.setTokenIdentificador(token.get(posTokenAtual).getTokenIdentificador());
				E2.setTokenString(token.get(posTokenAtual).getTokenString());
				result = compare(E1.getTokenIdentificador(), E2.getTokenIdentificador());
				if (result == 2)
					resultFloat = result;
				System.out.println("Result: "+result);
				System.out.println();
				E1.setTokenIdentificador(0);
			}
			else{
				E1.setTokenIdentificador(token.get(posTokenAtual).getTokenIdentificador());
				E1.setTokenString(token.get(posTokenAtual).getTokenString());
			}
			posTokenAtual++;
		}			
		else
			errorHelper(token, 11,posTokenAtual);
	}

	public static void arithmetic_expr(ArrayList<Token> token) {
		// <arithmetic_expr> ::= <arithmetic_expr> "+" <term> |
		// <arithmetic_expr> "-" <term> | <term>
		/*
		 * A -> TA' A'-> e|+TA'|-TA'
		 */
		term(token);
		arithmetic_exprLinha(token);
	}

	public static void arithmetic_exprLinha(ArrayList<Token> token) {
		if (getTokenIdentificador(token) == Identificadores.SOMA) {
			posTokenAtual++;
			term(token);
			arithmetic_exprLinha(token);
		} else if (getTokenIdentificador(token) == Identificadores.SUBTRAÇAO) {
			posTokenAtual++;
			term(token);
			arithmetic_exprLinha(token);
		} else;
		// vazio???
	}

	public static void term(ArrayList<Token> token) {
		/*
		 * T -> FT'
		 * T'-> e|*FT'|/FT'
		 */
		factor(token);
		termLinha(token);
	}

	public static void termLinha(ArrayList<Token> token) {
		try {
			if (getTokenIdentificador(token) == Identificadores.MULTIPLICACAO) {
				posTokenAtual++;
				factor(token);
				termLinha(token);
			} else if (getTokenIdentificador(token) == Identificadores.DIVISAO) {
				posTokenAtual++;
				factor(token);
				termLinha(token);
			} else;
			// vazio?
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			System.out.println("termLinha");
			errorHelper(token, 8,posTokenAtual);
		}

	}

	public static void relational_expr(ArrayList<Token> token) {
		// <relational_expr> ::= <arithmetic_expr> <relational_operator>
		// <arithmetic_expr>
		// FIXME EM ALGUM CANTO POR AQUI ESTÁ ACONTECENDO UM INCREMENTO A MAIS
		arithmetic_expr(token);
		relational_operator(token);
		arithmetic_expr(token);
	}

	public static void assign(ArrayList<Token> token) {
		// <assign> ::= <identifier> "=" <arithmetic_expr> ";"
		//FIXME Problema com o throw de erro
		if (identifier(token)) {
			if (stackSearch(token, symbolTable)){
				assignType = semanticType;
				posTokenAtual++;					
			}
			else{
				System.out.println("A variavel *"+token.get(posTokenAtual).getTokenString()+"* não foi encontrada em nenhum dos escopos.");
				System.exit(404);
			}
			if (getTokenIdentificador(token) == Identificadores.ATRIBUIÇÃO) {
				posTokenAtual++;
				arithmetic_expr(token);
				if (resultFloat == 2){
					result = compare (assignType, resultFloat);
					System.out.println(resultFloat);
				}
				else{
					result = compare (assignType, result);
					System.out.println(result);
				}
				if (getTokenIdentificador(token) == Identificadores.pontoVIRGULA) {
					posTokenAtual++;
				} else
					errorHelper(token, 8,posTokenAtual);
			} else
				errorHelper(token, 13,posTokenAtual);
		} else
			errorHelper(token, 9,posTokenAtual);
	}
}
