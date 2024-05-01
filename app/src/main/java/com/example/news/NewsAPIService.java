package com.example.news;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class NewsAPIService {

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY = "473d43e0b55c4d48bc2eb5eaf0e08e12";

    public interface NewsCallback {
        void onNewsLoaded(List<NewsItem> newsItems);
        void onFailure(String errorMessage);
    }

    public void getNews(String query, NewsCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsAPI newsAPI = retrofit.create(NewsAPI.class);

        Call<NewsResponse> call = newsAPI.getNews(query, API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<NewsItem> newsItems = new ArrayList<>();
                    if (newsResponse != null && newsResponse.getArticles() != null) {
                        for (Article article : newsResponse.getArticles()) {
                            // Create a NewsItem object for each article
                            String imageUrl = article.getUrlToImage();
                            String title = article.getTitle();
                            String description = article.getDescription();
                            // Get source information
                            String sourceName = article.getSource().getName();
                            if (imageUrl != null) {
                                NewsItem newsItem = new NewsItem(imageUrl, title, description, sourceName);
                                newsItems.add(newsItem);
                            }
                        }
                        // Callback with the list of news items
                        callback.onNewsLoaded(newsItems);
                    }
                } else {
                    callback.onFailure("Failed to fetch news: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                callback.onFailure("Failed to fetch news: " + t.getMessage());
            }
        });
    }

    public void fetchTopHeadlines(NewsCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsAPI newsAPI = retrofit.create(NewsAPI.class);

        Call<NewsResponse> call = newsAPI.getTopHeadlines("us", "473d43e0b55c4d48bc2eb5eaf0e08e12");
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<NewsItem> newsItems = new ArrayList<>();
                    if (newsResponse != null && newsResponse.getArticles() != null) {
                        int count = 0;
                        for (Article article : newsResponse.getArticles()) {
                            // Create a NewsItem object for each article
                            String imageUrl = article.getUrlToImage();
                            String title = article.getTitle();
                            String description = article.getDescription();
                            // Get source information
                            String sourceName = article.getSource().getName();
                            if (imageUrl != null) {
                                NewsItem newsItem = new NewsItem(imageUrl, title, description, sourceName);
                                newsItems.add(newsItem);
                                count++;
//                                // Break the loop if 20 records are fetched
//                                if (count == 20) {
//                                    break;
//                                }
                            }
                        }
                        // Callback with the list of news items
                        callback.onNewsLoaded(newsItems);
                    }
                } else {
                    callback.onFailure("Failed to fetch top headlines: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                callback.onFailure("Failed to fetch top headlines: " + t.getMessage());
            }
        });
    }



}
