package com.manLoader.utils;

public class HtmlParser {

	public String[] parseTD(CppString source) {
		int initialIndex = source.indexOf("<td>");
		int finalIndex = source.indexOf("</tr>");
		source = source.substring(initialIndex, finalIndex);

		String[] resultsArray = source.split("</td>");
		for (int i = 0; i < resultsArray.length; i++) {
			resultsArray[i] = resultsArray[i].replace("<td>", "");
		}

		return resultsArray;
	}

	/**
	 * 
	 *
	 * @param source
	 * @return link no 'href' da proxima tag <a> encontrada no c�digo html
	 *         source.
	 */
	public String parseNextAnchorHref(String source) {

		int initialIndex = source.indexOf("<a");
        int endingIndex = source.indexOf(">",initialIndex);
		source = source.substring(initialIndex,endingIndex);

		int hrefIndex = source.indexOf("href");
		source = source.substring(hrefIndex);
		String[] link = source.split("\"");
		return link[1];
	}

	public String parseNextAnchorText(String source) {
		int initialIndex = source.indexOf("<a");
        int endingIndex = source.indexOf("</a>");
        source = source.substring(initialIndex,endingIndex);

		int closureIndex = source.indexOf(">");

		source = source.substring(closureIndex + 1);
		return source;
	}

	public String parseNextImageSrc(String source) {
		int initialIndex = source.indexOf("<img");
        //int endingIndex = source.indexOf(">",initialIndex);
        source = source.substring(initialIndex);

		int hrefIndex = source.indexOf("src");
		source = source.substring(hrefIndex);
		String[] link = source.split("\"");
		return link[1];

	}

	/**
	 * Get the text between especified patterns on a html string, making all the
	 * necessary formating for the tags p and br
	 * 
	 * @param sourceBuffer
	 *            - Html-formated source.
	 * @param initialPattern
	 *            - Pattern where begins the desired text. Non-included on it.
	 * @param finishingPattern
	 *            - Pattern where ends the desired text. Non-included on it.
	 * @return Desired text between the patterns.
	 */
	public String getTextBetweenPatterns(StringBuffer sourceBuffer, String initialPattern,
			String finishingPattern) {
		int initialIndex = sourceBuffer.indexOf(initialPattern)
				+ initialPattern.length();
        int endingIndex = sourceBuffer.indexOf(finishingPattern,initialIndex);
		String source = sourceBuffer.substring(initialIndex,endingIndex);

		source.replaceAll("<br>", "\n");
		source.replaceAll("<br/>", "\n");
		source.replaceAll("<br />", "\n");
		source.replaceAll("<p>", "/t");
		source.replaceAll("</p>", "");

		return source;
	}

	/**
	 * Verifica se um dado link contem a indicacao de protocolo http, e se nao
	 * houver, a coloca.
	 * 
	 * @param listLink
	 *            - link a ser verificado.
	 * @return listLink, se estiver valido, ou http://www.+listlink se nao
	 *         estiver.
	 */
	public String verifyLink(String listLink) {
		if (!listLink.contains("http://")) {
			listLink = "http://" + listLink;
		}
		return listLink;
	}

	public String getAuthorityFromLink(String link) {
		link = link.replace("http://", "");
		return link.split("/")[0];

	}

}
