package com.example.news;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewNews extends Fragment {

    private SharedViewModel viewModel;
    private TextView newsTitle, newsDescription;
    private ImageView newsImage;
    private List<NewsItem> newsItemList;
    private RecyclerView recycler_view_related_news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_news, container, false);

        newsTitle = rootView.findViewById(R.id.news_title);
        newsDescription = rootView.findViewById(R.id.news_description);
        newsImage = rootView.findViewById(R.id.news_image);
        recycler_view_related_news = rootView.findViewById(R.id.recycler_view_related_news);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        NewsItem selectedNewsItem = viewModel.getSelectedNewsItem().getValue();

        // Check if the selectedNewsItem is not null and use it
        if (selectedNewsItem != null) {
            // Use the selectedNewsItem to set the text of the TextView
            newsTitle.setText(selectedNewsItem.getTitle());
            newsDescription.setText(selectedNewsItem.getDescription());
            Picasso.get().load(selectedNewsItem.getImageUrl()).into(newsImage);
        }

        newsItemList = new ArrayList<>();

        // Call the NewsAPIService to fetch news items
        NewsAPIService newsAPIService = new NewsAPIService();
        newsAPIService.getNews(selectedNewsItem.getSource(), new NewsAPIService.NewsCallback() {
            @Override
            public void onNewsLoaded(List<NewsItem> loadedNewsItems) {
                // Update newsItemList with the loaded news items
                newsItemList.clear(); // Clear existing items
                newsItemList.addAll(loadedNewsItems); // Add loaded items

                // Set up the adapter with the loaded news items
                NewsAdapter newsAdapter = new NewsAdapter(newsItemList, new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(NewsItem item) {
                        viewModel.selectNewsItem(item);
                    }
                }, R.layout.related_news_item);

                // Set layout manager for the RecyclerView
                recycler_view_related_news.setLayoutManager(new LinearLayoutManager(getContext()));

                // Set adapter to the RecyclerView
                recycler_view_related_news.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure
                Log.e("ViewNews", "Failed to load news: " + errorMessage);
                // Display a toast message to inform the user about the error
                Toast.makeText(getContext(), "Failed to load news: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("PROBLEM!!!!!!!!!!!", "true");
        return rootView;
    }
}


