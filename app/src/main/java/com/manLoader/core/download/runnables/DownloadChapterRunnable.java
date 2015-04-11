package com.manLoader.core.download.runnables;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import com.manLoader.core.webModule.parsers.SiteMangaWebParser;
import com.manLoader.exceptions.ChapterNonExcizteException;
import com.manLoader.pojo.Chapter;
import com.manLoader.pojo.Page;
import com.manLoader.utils.MyLog;

/**
 * Runnable para fazer download de um determinado cap�tulo.
 * 
 * @author Kithkin
 * 
 */
public class DownloadChapterRunnable implements Runnable {

	public static final int MAX_CONCURRENT_THREADS = 5;

	private Chapter chapter;
	private Boolean isThreaded;
	private Boolean isCanceled;
	private Semaphore semaphore;

	/**
	 * 
	 * @param chapter
	 *            - cap�tulo do qual se deseja fazer download.
	 */
	public DownloadChapterRunnable(Chapter chapter) {
		this.chapter = chapter;
		semaphore = new Semaphore(MAX_CONCURRENT_THREADS);
		isCanceled = false;
	}

	 @Override
	public void run() {
		String chapterLink = chapter.getLink();
		SiteMangaWebParser parser = chapter.getManga().getParser();
		
		try {
			int pageNum = 1;
			String pageLink = String.valueOf(chapterLink);
			while (pageLink != null) {
				if (isCanceled) {
					MyLog.getInstance().general("Operação Cancelada");
					return;
				}
				Page page = new Page();
				page.setId(chapter.getManga().getName() + "-"
						+ chapter.getNumero() + "-" + pageNum);
				page.setCapitulo(chapter);
				page.setNumero(pageNum);
				page.setImageLink(parser.getImageLink(pageLink));
				if (!chapter.getPaginas().contains(page)) {
					chapter.getPaginas().add(page);
				}
				MyLog.getInstance().debug(DownloadChapterRunnable.class, "Iniciando Task");
				MyLog.getInstance().page(page);

                new DownloadPageRunnable(page).run();
				//change to android standard
				pageLink = parser.nextPageLink(pageLink);
				pageNum++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ChapterNonExcizteException e) {
            e.printStackTrace();
        }
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

	}

	public void cancelDownload() {
		isCanceled = true;
	}

}
