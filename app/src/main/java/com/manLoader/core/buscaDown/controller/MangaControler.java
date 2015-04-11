package com.manLoader.core.buscaDown.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.manLoader.core.busca.parsers.MangafoxBuscaParser;
import com.manLoader.core.webModule.parsers.SiteMangaWebParser;
import com.manLoader.pojo.Chapter;
import com.manLoader.pojo.Manga;
import com.manLoader.pojo.Page;
import com.manLoader.pojo.SearchResult;
import com.manLoader.utils.ByteDownloadTask;
import com.manLoader.utils.FileDownloadTask;

public class MangaControler {

	

	private static List<Chapter> parseMangaChapters(Manga manga) {
		SiteMangaWebParser parser = manga.getParser();
		int chapterNum = 1;
		List<Chapter> capitulos = new ArrayList<Chapter>();
		for (String pageLink : parser.getChapterPageLinks()) {
			Chapter chapter = new Chapter();
			chapter.setManga(manga);
			chapter.setNome(parser.getChapterNames()[chapterNum - 1]);
			chapter.setLink(pageLink);
			chapter.setNumero(chapterNum);
			chapter.setPaginas(new ArrayList<Page>());
			capitulos.add(chapter);
			chapterNum++;
		}
		return capitulos;
	}

	public static List<SearchResult> searchManga(String termos)
			throws IOException {
		MangafoxBuscaParser mfParser = new MangafoxBuscaParser();
		// MangareaderBuscaParser mrParser = new MangareaderBuscaParser();
		List<SearchResult> resultados = new ArrayList<SearchResult>();

		resultados.addAll(mfParser.searchManga(termos));
		// resultados.addAll(mrParser.buscarManga(termos));

		return resultados;

	}
	
	public static Manga buildMangaFromSearchResult(SearchResult result) throws IOException, InterruptedException, ExecutionException{
		Manga manga = new Manga();
		SiteMangaWebParser parser = SiteMangaWebParser.createInstance(result.getLink()); 
		manga.setParser(parser);
		manga.setName(result.getName());
		manga.setPageLink(result.getLink());
		manga.setNumChapters(result.getNumChapters());
		manga.setCapitulos(parseMangaChapters(manga));
		manga.setAuthor(parser.getMangaAuthor());
		manga.setSinopse(parser.getMangaDescription());
        StringBuilder tags = new StringBuilder();
        String[] tagsArray = parser.getMangaTags();
        for(String tag:tagsArray){
            tags.append(tag + " ");
        }
		manga.setTags(tags.toString());


		ByteDownloadTask task = new ByteDownloadTask();
		task.execute(parser.getMangaCoverLink());
		byte[] sourceBytes = task.get();
		task.verifyException(sourceBytes);
		
		manga.setCoverImage(sourceBytes);
		
		return manga;
		
	}
	
//
//	public static Manga buscarMangaPorPageLink(String pageLink)
//			throws IOException {
//		MangafoxBuscaParser mfParser = new MangafoxBuscaParser();
//
//		return mfParser.buscarMangaPorPageLink(pageLink);
//
//	}

}
