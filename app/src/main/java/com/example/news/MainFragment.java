package com.example.news;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView recyclerView, recycler_view_news;
    private List<NewsItem> imageList;
    private List<NewsItem> newsItemList;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_top_news);
        recycler_view_news = rootView.findViewById(R.id.recycler_view_news);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Set up RecyclerView with a horizontal layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize imageList with an empty ArrayList
        imageList = new ArrayList<NewsItem>();

        // Create and set the adapter
        TopNewsAdapter adapter = new TopNewsAdapter(imageList);
        recyclerView.setAdapter(adapter);

        // Call the NewsAPIService to fetch news items
        NewsAPIService newsAPIService = new NewsAPIService();
        newsAPIService.fetchTopHeadlines(new NewsAPIService.NewsCallback() {
            @Override
            public void onNewsLoaded(List<NewsItem> imageUrls) {
                // Update imageList with the loaded image URLs
                imageList.clear(); // Clear existing URLs
                imageList.addAll(imageUrls); // Add loaded URLs
                Log.d("ImageListLength", "Length of @@@@@@@@: " + imageList.size());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure, e.g., show error message
                Log.e("NewsAPIService", errorMessage);
            }
        });


        // NEWS
        newsItemList = new ArrayList<>();
        // Call the NewsAPIService to fetch news items
        newsAPIService.getNews("India", new NewsAPIService.NewsCallback() {
            @Override
            public void onNewsLoaded(List<NewsItem> loadedNewsItems) {
                // Update newsItemList with the loaded news items
                newsItemList.clear(); // Clear existing items
                newsItemList.addAll(loadedNewsItems); // Add loaded items
                Log.d("NewsItemListLength", "Length of newsItemList: " + newsItemList.size());
                NewsAdapter newsAdapter = new NewsAdapter(newsItemList, new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(NewsItem item) {
                        viewModel.selectNewsItem(item);
                        // Handle item click here, for example, replace the fragment
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        // Replace the current fragment with a new one
                        ViewNews newFragment = new ViewNews();
                        fragmentTransaction.replace(R.id.fragmentContainer, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }, R.layout.item_news);
                recycler_view_news.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure, e.g., show error message
                Log.e("NewsAPIService", errorMessage);
            }
        });

        // Create and set the adapter
        GridLayoutManager newsLayoutManager = new GridLayoutManager(getContext(), 2);
        recycler_view_news.setLayoutManager(newsLayoutManager);

        return rootView;
    }
}
