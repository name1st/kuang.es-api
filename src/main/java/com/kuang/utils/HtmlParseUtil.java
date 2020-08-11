package com.kuang.utils;

import com.kuang.pojo.Content;
import com.kuang.pojo.MContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.Contended;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonders on 2020-07-16 17:41
 */
public class HtmlParseUtil {

    public static void main(String[] args) throws Exception {
        new HtmlParseUtil().parseJD("vue").forEach(System.out::println);
    }

    public List<Content> parseJD(String keywords) throws Exception {

        //获取请求，https://search.jd.com/Search?keyword=java
        // 需要联网
        String url = "https://search.jd.com/Search?keyword="+keywords;
        //解析网页（Jsoup返回的document 就是js 页面对象）
        Document document = Jsoup.parse(new URL(url), 30000);
        //所有js可以使用的方法都有
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        ArrayList<Content> goodList = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();


            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            goodList.add(content);

        }
        return goodList;
    }

public List<MContent> parseMT(String keywords) throws Exception {

//    String meituan = "https://sh.meituan.com/s/"+keyword;
    //获取请求，https://search.jd.com/Search?keyword=java
    // 需要联网
    String url = "https://ns.meituan.com/meishi/b25710/";
    //解析网页（Jsoup返回的document 就是js 页面对象）
    Document document = Jsoup.parse(new URL(url), 30000);
    //所有js可以使用的方法都有
//    Element element = document.getElementById("react");
    Elements elements = document.getElementsByClass("list-ul");
    Element element1 = elements.get(0);
    Elements liElements = element1.getElementsByTag("li");

    ArrayList<MContent> goodList = new ArrayList<>();
    for (Element el : liElements) {
        String img = el.getElementsByTag("img").eq(0).attr("src");
        String title = el.getElementsByTag("a").eq(1).text();
        String site = el.getElementsByClass("desc").eq(0).text();
        String price = el.getElementsByTag("span").eq(0).text();
        String eval = el.getElementsByTag("p").eq(0).text();


        MContent content = new MContent();
        content.setImg(img);
        content.setPrice(price);
        content.setTitle(title);
        content.setEval(eval);
        content.setSite(site);
        goodList.add(content);

    }
    return goodList;
}

 }
