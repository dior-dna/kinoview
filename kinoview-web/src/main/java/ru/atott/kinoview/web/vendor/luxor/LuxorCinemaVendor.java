package ru.atott.kinoview.web.vendor.luxor;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.atott.kinoview.web.to.FilmInfo;
import ru.atott.kinoview.web.vendor.Vendor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LuxorCinemaVendor implements Vendor {
    public static final Long LUXOR_RYAZAN_BARS_CINEMA_ID = 36L;
    public static final Long LUXOR_RYAZAN_CRUIZ_CINEMA_ID = 34L;

    private Long cinemaId;

    public LuxorCinemaVendor(Long cinemaId) {
        this.cinemaId = cinemaId;
    }

    @Override
    public List<FilmInfo> getFilms(Date date) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String dateString = dateFormat.format(date);

            HttpPost httpPost = new HttpPost("http://www.luxorfilm.ru/afisha/");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ctl00$contentPlaceHolderTop$drdCinemas", cinemaId.toString()));
            params.add(new BasicNameValuePair("ctl00$contentPlaceHolderTop$drdDay", dateString));
            params.add(new BasicNameValuePair("ctl00$contentPlaceHolderTop$DateSlider$hfMin", "9"));
            params.add(new BasicNameValuePair("ctl00$contentPlaceHolderTop$DateSlider$hfMax", "26"));
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            String html = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            return parseContent(html);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private List<FilmInfo> parseContent(String content) {
        Document document = Jsoup.parse(content);
        List<FilmInfo> result = new ArrayList<FilmInfo>();
        Elements filmContainers = document.select(".afisha_cont.cf .filmz_cont_l .film_container");
        for (int i = 0; i < filmContainers.size(); i++) {
            result.add(parseFilmContainer(filmContainers.get(i)));
        }
        return result;
    }

    private FilmInfo parseFilmContainer(Element container) {
        FilmInfo filmInfo = new FilmInfo();
        filmInfo.setTitle(container.select("h2>a").get(0).text());
        filmInfo.setImageUrl(container.select(".film_inf_cont .img_film img").get(0).attr("src"));
        Element infoTable = container.select(".table_film_info_cont table").get(0);
        filmInfo.setGenre(infoTable.select("td").get(1).text());
        String duration = infoTable.select("td").get(3).text();
        filmInfo.setDuration(parseDuration(duration));
        filmInfo.setDirector(infoTable.select("td").get(5).text());
        filmInfo.setActors(infoTable.select("td").get(7).text());
        return filmInfo;
    }

    private Long parseDuration(String duration) {
        String hours = StringUtils.substringBefore(duration.toLowerCase(), "ч.").trim();
        String minutes = StringUtils.substringBefore(StringUtils.substringAfter(duration.toLowerCase(), "ч."), "мин.").trim();
        return Long.parseLong(hours) * 60 + Long.parseLong(minutes);
    }
}
