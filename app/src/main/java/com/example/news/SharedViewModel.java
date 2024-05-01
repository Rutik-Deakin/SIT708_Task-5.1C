package com.example.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.NewsItem;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<NewsItem> selectedNewsItem = new MutableLiveData<>();

    public void selectNewsItem(NewsItem item) {
        selectedNewsItem.setValue(item);
    }

    public LiveData<NewsItem> getSelectedNewsItem() {
        return selectedNewsItem;
    }
}
