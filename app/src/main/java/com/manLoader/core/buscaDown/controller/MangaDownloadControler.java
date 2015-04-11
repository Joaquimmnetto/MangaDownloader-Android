package com.manLoader.core.buscaDown.controller;

import java.io.IOException;

import com.manLoader.core.download.runnables.DownloadChapterRunnable;
import com.manLoader.exceptions.ChapterNonExcizteException;
import com.manLoader.pojo.Chapter;
import com.manLoader.pojo.Manga;
import com.manLoader.utils.DownloadHelper;
import com.manLoader.utils.MyLog;

public class MangaDownloadControler {
	
	private int currentDownloading;

	public boolean addMangaToLibrary(Manga manga, int chapterDe, int chapterAte)
			throws IOException, ChapterNonExcizteException {
		int chapterNum = 1;
        DownloadHelper.getInstance().startLooping();

		for (Chapter chapter : manga.getCapitulos()) {
			if (chapterNum >= chapterDe && chapterNum <= chapterAte) {
				DownloadChapterRunnable downloadRunnable = new DownloadChapterRunnable(chapter);
				chapter.setId(manga.getName() + "-" + chapter.getNumero());
				MyLog.getInstance().chapter(chapter);
				currentDownloading = chapter.getNumero();
				downloadRunnable.run();
			}
			chapterNum++;
		}
		return true;
	}

	public boolean addMangaToLibrary(Manga manga) throws IOException,
			ChapterNonExcizteException {
		return addMangaToLibrary(manga, 1, Integer.MAX_VALUE);
	}
	
	public int getCurrentDownloading() {
		return currentDownloading;
	}
}
