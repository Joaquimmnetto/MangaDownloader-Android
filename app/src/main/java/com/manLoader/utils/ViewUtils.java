package com.manLoader.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ViewUtils {

	public static void setTextinTextView(View parent, String text,
			int textViewRes) {
		((TextView) parent.findViewById(textViewRes)).setText(text);

	}

	public static void setTextinTextView(Activity acty,
			String text, int textViewRes) {
		((TextView) acty.findViewById(textViewRes)).setText(text);
		
	}

}
