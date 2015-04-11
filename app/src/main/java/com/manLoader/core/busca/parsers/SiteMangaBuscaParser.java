package com.manLoader.core.busca.parsers;

import java.io.IOException;
import java.util.List;

import com.manLoader.pojo.SearchResult;

/**
 * Classe utilizada como base para os Parsers(Objetos que interpretam o texto da
 * página HTML) da página de busca.
 * 
 * @author Kithkin
 * 
 */
public abstract class SiteMangaBuscaParser {

	/**
	 * Método para buscar um mangá no parser especificado pelo site.
	 * 
	 * @param termos
	 *            - Termos da busca(Ex: nome do mangá)
	 * @return Lista de objetos mangá com as informações do resultado da busca.
	 * @throws IOException
	 *             - Caso algum erro de conexão com o site ocorra.
	 */
	public abstract List<SearchResult> searchManga(String termos)
			throws IOException;

	// public Manga buscarMangaPorPageLink(String pageLink) throws IOException {
	// Manga manga = new Manga();
	// manga.setPageLink(pageLink);
	// manga.setName(pageLink);
	// manga.setParser(SiteMangaWebParser.createInstance(pageLink));
	// manga.setAuthor(manga.getParser().getMangaAuthor());
	// manga.setCapitulos(new MangaControler().parseMangaChapters(manga));
	// return manga;
	// }
	// Manga buscarMangaPorPageLink(String pageLink) throws IOException;

}
