package com.manLoader.android.activities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manLoader.android.adapter.SearchAdapter;
import com.manLoader.core.buscaDown.controller.MangaControler;
import com.manLoader.pojo.SearchResult;
import com.manLoader.utils.MyLog;
import com.mangoDownload.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity {

	public static final String BUNDLE_SEARCH_RESULT = "Search_sresult";
	private SearchAdapter searchAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_search);

		init();
		build();
	}

	private void init() {
		searchAdapter = new SearchAdapter(getApplicationContext(),
				new ArrayList<SearchResult>());
	}

	private void build() {
		ListView searchList = (ListView) findViewById(R.id.search_searchResults);
		searchList.setAdapter(searchAdapter);

		searchList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						onListItemClick((SearchResult) searchAdapter
								.getItem(position));
					}
				});

		final EditText buscaEdit = (EditText) findViewById(R.id.search_searchBox);
		buscaEdit
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						try {
							if (event != null) {
								searchListUpdate(buscaEdit.getText().toString());
							}
						} catch (IOException e) {
							String message = e.getMessage();
							MyLog.getInstance().errorAndMessageDialog(
									SearchActivity.this,
									e.getClass().getSimpleName(), message);
							e.printStackTrace();
						}
						return true;
					}
				});

	}

	private void searchListUpdate(String params) throws IOException {
		List<SearchResult> results = MangaControler.searchManga(params);

		searchAdapter.setSearchResults(results);
		searchAdapter.notifyDataSetChanged();
	}

	private void onListItemClick(SearchResult item) {
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra(BUNDLE_SEARCH_RESULT, (Serializable) item);
		startActivity(intent);
		// MyLog.getInstance().debug(SearchAdapter.class.getName(),
		// "Item " + item + " clicked!!");
	}

}
