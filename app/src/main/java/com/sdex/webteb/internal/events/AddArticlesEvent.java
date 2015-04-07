package com.sdex.webteb.internal.events;

public class AddArticlesEvent {

    private int page;

    public AddArticlesEvent(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
