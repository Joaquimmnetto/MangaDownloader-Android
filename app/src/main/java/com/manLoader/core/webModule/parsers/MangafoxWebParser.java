package com.manLoader.core.webModule.parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manLoader.exceptions.ChapterNonExcizteException;
import com.manLoader.utils.CppString;
import com.manLoader.utils.HtmlParser;
import com.manLoader.utils.DownloadHelper;

public class MangafoxWebParser extends SiteMangaWebParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4928678486797667267L;

	protected MangafoxWebParser(String listUrl) throws IOException {
		super(listUrl);
	}

	public static final String MANGAFOX_AUTHORITY = "mangafox.me";

	/**
	 * Busca os links para a primeira página de cada capítulo listado em
	 * listUrl.
	 */
	@Override
	public boolean processChapterPage() throws IOException {
		//
		String pattern = "<span class=\"miniedit\"></span></a>";
		CppString source = getListSource();
		mangaDescription = processMangaDescription();
		mangaAuthor = processMangaAuthor();
		mangaTags = processMangaTags();
		int initialIndex = source.indexOf(pattern) + pattern.length();
		if (initialIndex - pattern.length() < 0) {
			return false;
		}
		CppString sourceDatMatters = source.substring(initialIndex);
		List<String> links = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
        //TODO: better way to do this(without split)
		for (String candidate : sourceDatMatters.split("\"")) {
			if (candidate.contains("http://" + MANGAFOX_AUTHORITY + "/manga/")) {
				names.add(getChapterName(sourceDatMatters, candidate));
				links.add(candidate);
			}
			if (candidate
					.contains("<div id=\"discussion\" style=\"display:none\">")) {
				break;
			}
		}
		Collections.sort(links);
		chapterPageLinks = links.toArray(new String[links.size()]);
		chapterNames = names.toArray(new String[names.size()]);

		return true;
	}

	@Override
	public String processMangaAuthor() throws MalformedURLException,
			IOException {
		StringBuffer source = getListSource();
		Integer initialIndex = source.indexOf("/search/author/") - 10;
		return new HtmlParser().parseNextAnchorText(source
				.substring(initialIndex));
	}

	/**
	 * Busca em pageUrl o link da imagem da página pageUrl do mangá.
	 */
	public String getImageLink(String pageLink) throws IOException {
		StringBuffer source = DownloadHelper.downloadSource(pageLink);
		return getImageLinkBySource(source);
	}

	public String getImageLinkBySource(StringBuffer source) throws IOException {
		String pattern = "onclick=\"return enlarge()\"><img src=";

		int initialIndex = source.indexOf(pattern) + pattern.length();
		if (initialIndex - pattern.length() < 0) {
			return null;
		}
		String sourceDatMatters = source.substring(initialIndex,
				initialIndex + 200);

		return sourceDatMatters.split("\"")[1];
	}

	/**
	 * Busca e retorna o link para a próxima página do mangá, baseado na página
	 * atual(pageLink).Caso não haja próxima página, retorna null.
	 */
	@Override
	public String nextPageLink(String pageLink) throws MalformedURLException,
			IOException, ChapterNonExcizteException {
		HtmlParser parser = new HtmlParser();
		StringBuffer pageSource = DownloadHelper.downloadSource(pageLink);
		String pattern = "<select onchange=\"change_page(this)\" class=\"m\">";
		Integer initialIndex = pageSource.indexOf(pattern);
		if (initialIndex < 0) {
			throw new ChapterNonExcizteException();
		}
		String next = parser.parseNextAnchorHref(pageSource
				.substring(initialIndex));
		if (next.contains("javascript")) {
			return null;
		}
		String[] pageLinkSplitten = new String(pageLink).replace('/', ':')
				.split(":");
		next = pageLink.replaceFirst(
				pageLinkSplitten[pageLinkSplitten.length - 1], next);
		return next;

	}

	protected String processMangaDescription() throws MalformedURLException,
			IOException {
		String initialPattern = "<p class=\"summary\">";
		String finishingPattern = "</div>";
		return new HtmlParser().getTextBetweenPatterns(getListSource(),
				initialPattern, finishingPattern);
	}

	@Override
	public String getMangaCoverImageLink() throws MalformedURLException,
			IOException {
		String intialPattern = "<div class=\"cover\">";
		Integer intialIndex = getListSource().indexOf(intialPattern);
		return new HtmlParser().parseNextImageSrc(getListSource().substring(
				intialIndex));
	}

	@Override
	public String getChapterName(CppString sourceBuffer, String chapterLink)
			throws MalformedURLException, IOException {
		String source = new HtmlParser().getTextBetweenPatterns(sourceBuffer, chapterLink,
				"</div>");
		String pattern = "<span class=\"title nowrap\">";

		String[] candidates = source.split(pattern);
		if (candidates.length < 0) {
			String chapName = candidates[1].split("</span>")[0];
			return chapName;
		} else {
			return "";
		}

	}

	@Override
	public String getMangaCoverLink() throws MalformedURLException, IOException {
		StringBuffer source = getListSource();
		Integer initialIndex = source.indexOf("<div class=\"cover\">");
		return new HtmlParser().parseNextImageSrc(source.substring(
				initialIndex, initialIndex + 200));
	}

	@Override
	public String[] processMangaTags() throws MalformedURLException,
			IOException {
		String source = getListSource().toString();
		List<String> tags = new ArrayList<String>();

		Integer initialIndex = source
				.indexOf("http://mangafox.me/search/genres/");
		while (initialIndex > -1) {

			source = source.substring(initialIndex - 10);
			String tag = new HtmlParser().parseNextAnchorText(source);
			tags.add(tag);
			source = source.substring(source.indexOf(tag));
			initialIndex = source.indexOf("http://mangafox.me/search/genres/");
		}
		return tags.toArray(new String[tags.size()]);
	}

}
