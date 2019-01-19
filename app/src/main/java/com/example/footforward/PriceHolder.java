package com.example.footforward;

public class PriceHolder {
    private String articleTitle, articleContent, articleURL,articleImage;

    public PriceHolder(String articleTitle, String articleContent, String articleImage, String articleURL){
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleImage = articleImage;
        this.articleURL = articleURL;
    }

    public String getArticleTitle(){
        return articleTitle;
    }
    public void setArticleTitle(String articleTitle){
        this.articleTitle = articleTitle;
    }

    public String getArticleContent(){
        return articleContent;
    }
    public void setArticleContent(String articleContent){
        this.articleContent = articleContent;
    }

    public String getArticleImage(){
        return articleImage;
    }
    public void setArticleImage(String articleImage){
        this.articleImage = articleImage;
    }

    public String getArticleUrl(){
        return articleURL;
    }
    public void setArticleUrl(String articleURL){
        this.articleURL = articleURL;
    }
}