package com.manLoader.core.webModule.parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.manLoader.utils.HtmlParser;
import com.manLoader.utils.DownloadHelper;


public class MangareaderWebParser extends SiteMangaWebParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7341013630074381746L;


	protected MangareaderWebParser(String listUrl) throws IOException {
		super(listUrl);
	}

	public static final String MANGAREADER_AUTHORITY = "www.mangareader.net";
	

	@Override
	public boolean processChapterPage() throws IOException {
		String pattern = "<div class=\"chico_manga\"></div>";
		String parada = "</html>";
		String reconhecerLink = "<a href=\"";
		String baseSplit = "<div class=\"chico_manga\"></div>";
		StringBuffer source = getListSource();

		int initialIndex = source.indexOf(pattern) + pattern.length();
		if (initialIndex - pattern.length() < 0) {
			return false;
		}
		String sourceDatMatters = source.substring(initialIndex);
		List<String> links = new ArrayList<String>();
		HtmlParser parser = new HtmlParser();
		for (String candidate : sourceDatMatters.split(baseSplit)) {
			if (candidate.contains(reconhecerLink)) {
				String candidateLink = "http://" + MANGAREADER_AUTHORITY
						+ parser.parseNextAnchorHref(candidate);
				if (!links.contains(candidateLink)) {
					links.add(candidateLink);
				}
			}
			if (candidate.contains(parada)) {
				break;
			}
		}
		this.sortChapters(links);
		chapterPageLinks = links.toArray(new String[links.size()]);
		return true;
	}

	@Override
	protected String getImageLinkBySource(StringBuffer pageSourceBuffer) throws IOException {
		String pattern = "<img id=\"img\"";
		String pattern2 = "src";
		Integer baseIndex = pageSourceBuffer.indexOf(pattern) + pattern.length();
		String pageSource = pageSourceBuffer.substring(baseIndex, baseIndex + 500);
		pageSource = pageSource.substring(pageSource.indexOf(pattern2));

		return pageSource.split("\"")[1];
	}

	@Override
	public String nextPageLink(String pageLink) throws MalformedURLException,
			IOException {
		StringBuffer pageSourceBuffer = DownloadHelper.downloadSource(pageLink);
		String pattern = "<div id=\"imgholder\">";
		Integer baseIndex = pageSourceBuffer.indexOf(pattern) + pattern.length();
		String pageSource = pageSourceBuffer.substring(baseIndex, baseIndex + 200);

		String next = "http://" + MANGAREADER_AUTHORITY
				+ new HtmlParser().parseNextAnchorHref(pageSource);
		if (!pageLink.split("/")[4].equalsIgnoreCase(next.split("/")[4])
				&& next != null) {
			return null;
		}
		return next;
	}
	
	private void sortChapters(List<String> chapters){
		List<String> dummy = new ArrayList<String>(chapters);
		for(String chapter:chapters){
			String[] splitten = chapter.split("/");
			Integer numChapter = Integer.valueOf(splitten[splitten.length-1]);
			dummy.set(numChapter-1,chapter);
			
		}
		chapters.clear();
		chapters.addAll(dummy);
	}

	@Override
	public String processMangaDescription() throws MalformedURLException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMangaCoverImageLink() throws MalformedURLException, IOException  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChapterName(StringBuffer source, String chapterLink)
			throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMangaCoverLink() throws MalformedURLException, IOException {
		getListSource();
		return null;
	}

	@Override
	public String[] processMangaTags() throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String processMangaAuthor() throws MalformedURLException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}


}
