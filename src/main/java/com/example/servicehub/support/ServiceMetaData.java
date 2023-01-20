package com.example.servicehub.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ServiceMetaData {
    private String siteName;
    private String title;
    private String url;
    private String image;
    private String description;

    public void setAttributeByProperty(String property, String content){
        switch (property){
            case "og:site_name" : this.siteName = content;return;
            case "og:title" : this.title = content;return;
            case "og:url" : this.url = content;return;
            case "og:image" : this.image = content;return;
            case "og:description" : this.description = content;return;
        }
    }

    public void setAttributeByName(String name , String content){
        switch (name){
            case "site_name" :
                if(this.siteName != null) return;
                this.siteName = content;return;
            case "title" :
                if(this.title != null) return;
                this.title = content;return;
            case "url" :
                if(this.url != null) return;
                this.url = content;return;
            case "image" :
                if(this.image != null) return;
                this.image = content;return;
            case "description" :
                if(this.description != null) return;
                this.description = content;return;
        }
    }

    public String getSiteName() {
        return siteName == null ? "" :siteName;
    }

    public String getTitle() {
        return title == null ? "" :title;
    }

    public String getUrl() {
        return url == null ? "" :url;
    }

    public String getImage(){
        if(this.image != null && this.image.contains("http")) return this.image;
        return null;
    }

    public String getDescription() {
        return description == null ? "" :description;
    }
}
