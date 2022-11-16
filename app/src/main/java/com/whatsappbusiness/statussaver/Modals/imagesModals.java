package com.whatsappbusiness.statussaver.Modals;

public class imagesModals {

    String name;
    String path;
    String uri;
    int index;

    public imagesModals(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public imagesModals(String name, String path, int index) {
        this.name = name;
        this.path = path;
        this.index = index;
    }

    public imagesModals(String name, String path, String uri) {
        this.name = name;
        this.path = path;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
