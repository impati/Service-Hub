package com.example.servicehub.support.crawl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMetaData {

	private static final String DEFAULT_LOGO_URL = "https://service-hub.org/file/logo/default.png";
	private static final String DEFAULT = "default";

	private String siteName;
	private String title;
	private String url;
	private String image;
	private String description;

	public ServiceMetaData(final String serviceUrl) {
		this.url = serviceUrl;
		this.title = DEFAULT;
		this.siteName = DEFAULT;
		this.description = DEFAULT;
		this.image = DEFAULT_LOGO_URL;
	}

	public void setAttributeByProperty(final String property, final String content) {
		if (content == null) {
			return;
		}

		switch (property) {
			case "og:site_name":
				setSiteName(content);
				break;
			case "og:title":
				setTitle(content);
				break;
			case "og:image":
				setImage(content);
				break;
			case "og:description":
				setDescription(content);
				break;
		}
	}

	public void setAttributeByName(final String name, final String content) {
		if (content == null) {
			return;
		}
		switch (name) {
			case "site_name":
			case "twitter:site":
				setSiteName(content);
				break;
			case "title":
			case "twitter:title":
				setTitle(content);
				break;
			case "image":
			case "twitter:image":
				setImage(content);
				break;
			case "description":
			case "twitter:description":
				setDescription(content);
				break;
		}
	}

	private void setSiteName(final String siteName) {
		if (this.siteName.equals(DEFAULT)) {
			this.siteName = siteName;
		}
	}

	private void setTitle(final String title) {
		if (this.title.equals(DEFAULT)) {
			this.title = title;
		}
	}

	private void setImage(final String image) {
		if (!image.contains("http")) {
			return;
		}

		if (this.image.equals(DEFAULT_LOGO_URL)) {
			this.image = image;
		}
	}

	private void setDescription(final String description) {
		if (this.description.equals(DEFAULT)) {
			this.description = description;
		}
	}
}
