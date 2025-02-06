package com.eventmanager.event_management.Model;

import lombok.Getter;


@Getter
public class Breadcrumb {
    private final String name;
    private final String url;

    public Breadcrumb(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
