package com.manLoader.core.webModule.parsers;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.manLoader.exceptions.ChapterNonExcizteException;
import com.manLoader.pojo.Chapter;
import com.manLoader.utils.CppString;
import com.manLoader.utils.HtmlParser;
import com.manLoader.utils.DownloadHelper;

/**
 * Html parser para informaçoes relativas ao manga que estao no site relativo ao
 * objeto.
 * 
 * @author Kithkin
 * 
 */
public abstract class SiteMangaWebParser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4422521434660790174L;
	/**
	 * Codigo fonte da pagina de lista de capitulos do manga.
	 */
	private CppString listSource;
	/**
	 * Link para a pagina de lista de capitulos do manga.
	 */
	protected String listUrl;
	protected boolean chapterPageProcessed;
	protected String[] chapterPageLinks;
	protected String[] chapterNames;
	protected String[] mangaTags;
	protected String mangaAuthor;
	protected String mangaDescription;
//	protected final HttpHelper httpHelper = new HttpHelper();

	protected SiteMangaWebParser(String listUrl) throws IOException {
		this.listUrl = listUrl;
		processChapterPage();
	}

	public static SiteMangaWebParser createInstance(String mangaPageLink)
			throws IOException {
		mangaPageLink = new HtmlParser().verifyLink(mangaPageLink);
		if (mangaPageLink.contains(MangafoxWebParser.MANGAFOX_AUTHORITY)) {
			return new MangafoxWebParser(mangaPageLink);

		}
		if (mangaPageLink.contains(MangareaderWebParser.MANGAREADER_AUTHORITY)) {
			return new MangareaderWebParser(mangaPageLink);
		}
		throw new IllegalArgumentException(
				"Link nao pertence a nenhum dos sites registrados na aplicacao");
	}

	/**
	 * M�todo que deve ser utilizado sempre que se desejar utilizar o
	 * listSource, por quest�es de desempenho.
	 * 
	 * @return
	 * @throws MalformedURLException
	 *             - caso listUrl esteja em um formato incorreto.
	 * @throws IOException
	 *             - caso ocorra algum erro de conex�o com o site.
	 */
	protected CppString getListSource() throws MalformedURLException, IOException {
		if (this.listSource == null) {
			this.listSource = DownloadHelper.downloadSource(listUrl);
		}
		return listSource;
	}

	/**
	 * M�todo utilizado para obter todos os links para imagens de um determinado
	 * cap�tulo.
	 * 
	 * @param chapter
	 *            - capitulo para qual deseja-se obter os links.
	 * @return Links para as todas as imagens das p�ginas do cap�tulo.
	 * @throws IOException
	 *             - caso ocorra algum erro de conex�o com o site.
	 * @throws ChapterNonExcizteException
	 *             - caso o cap�tulo n�o exista.
	 */
	public String[] getChapterImageLinks(Chapter chapter) throws IOException,
			ChapterNonExcizteException {
		List<String> imageLinks = new ArrayList<String>();
		String pageLink = chapter.getLink();
		while (pageLink != null) {
			String imageLink = getImageLink(pageLink);
			if (imageLink != null) {
				imageLinks.add(imageLink);
			}
			pageLink = nextPageLink(pageLink);
		}
		return imageLinks.toArray(new String[imageLinks.size()]);
	}

	/**
	 * 
	 * @return array de links para a primeira p�gina de cada cap�tulo do mang�.
	 * @throws IOException
	 *             - caso algum erro de conex�o com o site ocorra.
	 */
	public String[] getChapterPageLinks() {
		if (chapterPageLinks != null) {
			return chapterPageLinks;
		}
		return null;
	}

	public String[] getChapterNames() {
		if (chapterNames != null) {
			return chapterNames;
		}
		return null;
	}

	/**
	 * Retorna o link para a imagem de uma dada p�gina que a cont�m.
	 * 
	 * @param pageLink
	 *            - p�gina que contem a imagem de uma p�gina do mang�.
	 * @return link para a imagem da p�gina dada.
	 * @throws IOException
	 *             - caso oorra algum erro de conex�o com o site.
	 */
	public String getImageLink(String pageLink) throws IOException {
		CppString source = DownloadHelper.downloadSource(pageLink);
		String imageLink = getImageLinkBySource(source);
		if (imageLink != null) {
			return imageLink;
		}
		return null;
	}

	public abstract String[] processMangaTags() throws MalformedURLException,
			IOException;

	public abstract String processMangaAuthor() throws MalformedURLException,
			IOException;

	public abstract String getMangaCoverLink() throws MalformedURLException,
			IOException;

	public String getMangaDescription() {
		if (mangaDescription != null) {
			return mangaDescription;
		}
		return null;
	}

	/**
	 * M�todo auxiliar para se obter o link de uma imagem do mang�, dado o
	 * c�digo-fonte da p�gina que o cont�m.
	 * 
	 * @param source
	 *            - c�digo fonte da p�gina que cont�m a imagem.
	 * @return
	 * @throws IOException
	 */
	protected abstract String getImageLinkBySource(CppString source)
			throws IOException;

	protected abstract String getChapterName(StringBuffer source, String chapterLink)
			throws MalformedURLException, IOException;

	/**
	 * M�todo para conseguir o link da pr�xima p�gina html contendo a imagem
	 * seguinte do mang�, a partir da p�gina anterior.
	 * 
	 * @param pageLink
	 *            - link da p�gina anterior.
	 * @return link da p�gina seguinte a pageLink, se n�o existir, retorna null.
	 * @throws MalformedURLException
	 *             - caso pageLink esteja mal-formatada.
	 * @throws IOException
	 *             - caso ocorra algum erro de conex�o com o site.
	 * @throws ChapterNonExcizteException
	 *             - caso pageLink n�o seja uma p�gina de um cap�tulo de um
	 *             mang�.
	 */
	public abstract String nextPageLink(String pageLink)
			throws MalformedURLException, IOException,
			ChapterNonExcizteException;

	public abstract String getMangaCoverImageLink()
			throws MalformedURLException, IOException;


	protected abstract boolean processChapterPage() throws IOException;

	/**
	 * @return a descri��o do mang� que listUrl descreve.
	 * @throws MalformedURLException
	 *             - caso listUrl esteja em um formato inv�lido.
	 * @throws IOException
	 *             - caso ocorra algum erro de conex�o com o site.
	 */
	protected abstract String processMangaDescription()
			throws MalformedURLException, IOException;

    public void clearSource(){
        listUrl = null;
        System.gc();
    }


	public String[] getMangaTags() {
		return mangaTags;
	}

	// public void setMangaTags(String[] mangaTags) {
	// this.mangaTags = mangaTags;
	// }

	public String getMangaAuthor() {
		return mangaAuthor;
	}

	// public void setMangaAuthor(String mangaAuthor) {
	// this.mangaAuthor = mangaAuthor;
	// }
}
