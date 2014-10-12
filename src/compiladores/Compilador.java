package compiladores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static compiladores.Scanner.scanner;

public class Compilador {

	public static ArrayList<Token> listaTokens;

	public static void main (String[] args) {
		//"/home/popoto/workspace/Compiladores/src/compiladores/oi.txt"
		//java -jar /home/popoto/workspace/Compiladores/src/compiladores/run.jar /home/popoto/git/Compiladores/Compiladores/src/compiladores/oi2.txt
		///home/popoto/Dropbox/Programação/Compiladores UNICAP 2014.1/Carlos/PANCADA2.C
		//Sempre que alterar os arquivos, salvar um runnable novo
		try 
		{
			String file = new String ("/home/popoto/git/Compiladores/Compiladores/src/compiladores/oi2.txt");
			listaTokens = scanner(new FileInputStream(file));
			compiladores.Parser.parser(listaTokens);
			System.out.println("Compilado com sucesso!");
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Erro abrindo o arquivo");
		}
	}
}