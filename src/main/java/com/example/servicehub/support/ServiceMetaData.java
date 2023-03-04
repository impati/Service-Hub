package com.example.servicehub.support;

import com.example.servicehub.util.ProjectUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMetaData {

    private final String defaultLogoURI = "/file/logo/default.png";

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
                this.description = content;
        }
    }

    public String getSiteName() {
        return getNullOrBlank(this.siteName);
    }

    public String getTitle() {
        return getNullOrBlank(this.title);
    }

    public String getUrl() {
        return getNullOrBlank(this.url);
    }


    /**
     * service 의 로고의 URL 이 있다면 반환하고 없다면 해당 서버의 Default Logo URL 을 반환한다.
     * @return 로고 url
     */

    public String getImageUrl(){
        if(isUrl(getNullOrBlank(this.image))) return this.image;
        return ProjectUtils.getDomain() + defaultLogoURI;
    }

    public String getDescription() {
        return getNullOrBlank(this.description);
    }

    private String getNullOrBlank(String str){
        if(str == null) return "";
        return str;
    }

    private boolean isUrl(String path){
        if(path.contains("http"))return true;
        return false;
    }
}
