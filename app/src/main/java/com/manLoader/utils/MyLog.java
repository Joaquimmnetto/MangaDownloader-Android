package com.manLoader.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.manLoader.pojo.Chapter;
import com.manLoader.pojo.Page;

public class MyLog {

	private static MyLog instance;

	public static MyLog getInstance() {
		if (instance == null) {
			instance = new MyLog();
		}
		return instance;
	}

	public synchronized void debug(Class<?> source, String info) {
		android.util.Log.d(source.getSimpleName(), info);
	}

	public synchronized void debug(String tag, String info) {
		android.util.Log.d(tag, info);
	}

	public synchronized void error(String tag, String info) {
		android.util.Log.e(tag, info);
	}

	public void error(Class<?> source, Exception exception) {
		error(source.getName(), exception.getClass().getName() + ":"
				+ exception.getMessage());
	}

	public AlertDialog errorAndMessageDialog(Context context, String tag,
			String message) {
		error(tag, message);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("A Error occoured");
		dialog.setMessage(message);

		return dialog.show();

	}

	public void general(String status) {
		android.util.Log.i("GENERAL", status);
	}

	public void page(Page page) {
		android.util.Log.i("PAGE", "Iniciando pagina " + page.getNumero());
	}

	public void chapter(Chapter chapter) {
		android.util.Log.i("CHAPTER",
				"Iniciando capitulo " + chapter.getNumero());
	}

}
