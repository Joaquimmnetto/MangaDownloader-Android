package com.manLoader.core.busca.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.manLoader.core.webModule.parsers.MangafoxWebParser;
import com.manLoader.pojo.SearchResult;
import com.manLoader.utils.HtmlParser;
import com.manLoader.utils.DownloadHelper;

public class MangafoxBuscaParser extends SiteMangaBuscaParser {

	@Override
	public List<SearchResult> searchManga(String termos) throws IOException {
		List<SearchResult> results = new ArrayList<SearchResult>();
		HtmlParser parser = new HtmlParser();

		String termoNome = termos.replaceAll(" ", "+");
		String baseLink = "http://mangafox.me/search.php? name_method=cw& name="
				+ termoNome
				+ "& type=&	  author_method=cw &author= &artist_method=cw &artist=	  &genres%5BAction%5D=0 &genres%5BAdult%5D=0 &genres%5BAdventure%5D=0	  &genres%5BComedy%5D=0 &genres%5BDoujinshi%5D=0 &genres%5BDrama%5D=0	  &genres%5BEcchi%5D=0 &genres%5BFantasy%5D=0 &genres%5BGender+Bender%5D=0	  &genres%5BHarem%5D=0 &genres%5BHistorical%5D=0 &genres%5BHorror%5D=0	  &genres%5BJosei%5D=0 &genres%5BMartial+Arts%5D=0 &genres%5BMature%5D=0	  &genres%5BMecha%5D=0 &genres%5BMystery%5D=0 &genres%5BOne+Shot%5D=0	  &genres%5BPsychological%5D=0 &genres%5BRomance%5D=0	  &genres%5BSchool+Life%5D=0 &genres%5BSci-fi%5D=0 &genres%5BSeinen%5D=0	  &genres%5BShoujo%5D=0 &genres%5BShoujo+Ai%5D=0 &genres%5BShounen%5D=0	  &genres%5BShounen+Ai%5D=0 &genres%5BSlice+of+Life%5D=0	  &genres%5BSmut%5D=0 &genres%5BSports%5D=0 &genres%5BSupernatural%5D=0	  &genres%5BTragedy%5D=0 &genres%5BWebtoons%5D=0 &genres%5BYaoi%5D=0	  &genres%5BYuri%5D=0 &released_method=eq &released= &rating_method=eq	  &rating= &is_completed= &advopts=1";
		StringBuffer sauceBuffer = DownloadHelper.downloadSource(parser.verifyLink(baseLink));
		String pattern = "<tr>";



		int indexFirst = sauceBuffer.indexOf(pattern);
        String sauce = sauceBuffer.substring(indexFirst + 4);
		indexFirst = sauce.indexOf(pattern);

		do {
			sauce = sauce.substring(indexFirst + 4);
			indexFirst = sauce.indexOf(pattern);
			String name = parser.parseNextAnchorText(sauce);
			String link = parser.parseNextAnchorHref(sauce);
			int quantChap = Integer.parseInt(parser.parseTD(sauce)[3].trim());

			SearchResult manga = new SearchResult(name, quantChap, link,
					MangafoxWebParser.MANGAFOX_AUTHORITY);
			results.add(manga);

			// manga.setCapitulos(new ArrayList<Chapter>());
			// manga.setCapitulos(new
			// MangaControler().parseMangaChapters(manga));
			// manga.setParser(SiteMangaWebParser.createInstance(manga.getPageLink()));
			// manga.setFavorited(false);
			// manga.setStatus(MangaStatus.naoLido);
			// manga.setAuthor(manga.getParser().getMangaAuthor());
			// manga.setName(parser.parseNextAnchorText(sauce));

		} while (indexFirst > 0);

		return results;
	}

}
