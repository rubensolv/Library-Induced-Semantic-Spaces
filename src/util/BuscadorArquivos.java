package util;

import java.io.File;
import java.util.ArrayList;

public class BuscadorArquivos {
	
	/**
	 * Realiza a busca (recursiva) de todos os arquivos com o nome informado
	 * @param arquivo = File contendo o caminho que se deseja procurar.
	 * @param palavra = String com o nome que se deseja buscar
	 * @param lista = ArrayList<String> que retornará os arquivos encontrados
	 * @return Lista de Strings com todos os caminhos absolutos dos arquivos com o nome encontrado
	 */
	public static ArrayList<String> buscar(File arquivo, String palavra, ArrayList<String> lista) {
        if (arquivo.isDirectory()) {
            File[] subPastas = arquivo.listFiles();
            for (int i = 0; i < subPastas.length; i++) {
                lista = buscar(subPastas[i], palavra, lista);
                if (arquivo.getName().equalsIgnoreCase(palavra)) lista.add(arquivo.getAbsolutePath());
                else if (arquivo.getName().indexOf(palavra) > -1) lista.add(arquivo.getAbsolutePath());
            }
        }
        else if (arquivo.getName().equalsIgnoreCase(palavra)) lista.add(arquivo.getAbsolutePath());
        //else if (arquivo.getName().indexOf(palavra) > -1) lista.add(arquivo.getAbsolutePath());
        return lista;
    }
	
	public static ArrayList<String> buscarParcial(File arquivo, String palavra, ArrayList<String> lista) {
        if (arquivo.isDirectory()) {
            File[] subPastas = arquivo.listFiles();
            for (int i = 0; i < subPastas.length; i++) {
                lista = buscarParcial(subPastas[i], palavra, lista);
                if (arquivo.getName().equalsIgnoreCase(palavra)) lista.add(arquivo.getAbsolutePath());
                else if (arquivo.getName().contains(palavra)) lista.add(arquivo.getAbsolutePath());
            }
        }
        else if (arquivo.getName().equalsIgnoreCase(palavra)) lista.add(arquivo.getAbsolutePath());
        else if (arquivo.getName().contains(palavra)) lista.add(arquivo.getAbsolutePath());
        return lista;
    }

}
