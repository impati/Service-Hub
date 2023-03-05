package com.example.servicehub.support;

import com.example.servicehub.util.ProjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServiceMetaData {

    private final String DEFAULT_LOGO_URL = ProjectUtils.getDomain() + "/file/logo/default.png";
    private final String DEFAULT = "default";

    private String siteName;
    private String title;
    private String url;
    private String image;
    private String description;

    public ServiceMetaData(String serviceUrl){
        this.url = serviceUrl;
        this.title = DEFAULT;
        this.siteName = DEFAULT;
        this.description = DEFAULT;
        this.image = DEFAULT_LOGO_URL;
    }

    public void setAttributeByProperty(String property, String content){
        if(content == null) return ;
        switch (property){
            case "og:site_name" : setSiteName(content); break;
            case "og:title" : setTitle(content); break;
            case "og:image" : setImage(content); break;
            case "og:description" : setDescription(content); break;
        }
    }

    public void setAttributeByName(String name , String content){
        if(content == null) return ;
        switch (name){
            case "site_name" :
            case "twitter:site" :
                setSiteName(content); break;
            case "title" :
            case "twitter:title" :
                setTitle(content); break;
            case "image" :
            case "twitter:image" :
                setImage(content); break;
            case "description" :
            case "twitter:description" :
                setDescription(content); break;
        }
    }

    private void setSiteName(String siteName) {
        if(this.siteName.equals(DEFAULT)) this.siteName = siteName;
    }

    private void setTitle(String title) {
        if(this.title.equals(DEFAULT)) this.title = title;
    }

    private void setImage(String image) {
        if(this.image.equals(DEFAULT)) this.image = image;
    }

    private void setDescription(String description) {
        if(this.description.equals(DEFAULT)) this.description = description;
    }
}
