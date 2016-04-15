package com.victor.polskavisa;

public class ListItemObject implements ListItemInterface {

    private final int picture;
    private final String title;
    private final String titleDetail;
    private final int actPicture;
    private final boolean heading;

    public ListItemObject(int picture, String title, String titleDetail, int actPicture, boolean heading) {
        this.picture = picture;
        this.title = title;
        this.titleDetail = titleDetail;
        this.actPicture = actPicture;
        this.heading = heading;
    }

    public int getPicture() {
        return picture;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleDetail() {
        return titleDetail;
    }

    public int getActPicture() {
        return actPicture;
    }

    public boolean isHeading() {
        return heading;
    }
}
