package com.manLoader.android.adapter;

import java.util.ArrayList;
import java.util.List;

import com.manLoader.pojo.Manga;
import com.manLoader.pojo.SearchResult;
import com.manLoader.utils.ViewUtils;
import com.mangoDownload.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SearchAdapter extends BaseAdapter {

	private Context context;
	private List<SearchResult> searchResults = new ArrayList<SearchResult>();

	public SearchAdapter(Context context, List<SearchResult> results) {
		this.context = context;
		this.searchResults = results;
	}

	@Override
	public int getCount() {
		return searchResults.size();
	}

	@Override
	public Object getItem(int position) {
		return searchResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View contentView = View.inflate(context, R.layout.item_search, null);
		SearchResult result = searchResults.get(position);
		ViewUtils.setTextinTextView(contentView, result.getName(),
				R.id.search_item_mangaName);
		ViewUtils.setTextinTextView(contentView, result.getLink() + "",
				R.id.search_info_mangaSize);

		return contentView;
	}

	public void setSearchResults(List<SearchResult> results) {
		this.searchResults = results;
	}

	public List<SearchResult> getSearchResults() {
		return searchResults;
	}

}
