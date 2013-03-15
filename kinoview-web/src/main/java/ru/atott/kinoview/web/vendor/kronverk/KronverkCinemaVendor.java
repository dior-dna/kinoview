package ru.atott.kinoview.web.vendor.kronverk;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.atott.kinoview.web.to.FilmInfo;
import ru.atott.kinoview.web.vendor.Vendor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KronverkCinemaVendor implements Vendor {
    public static final String PLAZA_CINEMA_URL = "http://www.kronverkcinema.ru/schedule/city_id_11_delta_days_0.json";
    public static final Long RYAZAN_CITY_ID = 11L;

    private String baseUrl;
    private Long cityId;

    public KronverkCinemaVendor(String baseUrl, Long cityId) {
        this.baseUrl = baseUrl;
        this.cityId = cityId;
    }

    @Override
    public List<FilmInfo> getFilms(Date date) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            String url = baseUrl + "?_=" + date.getTime();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            String html = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            return parseContent(html);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private List<FilmInfo> parseContent(String html) throws IOException {
        List<FilmInfo> result = new ArrayList<FilmInfo>();
        JSONObject json = (JSONObject) JSONValue.parse(html);
        JSONArray movies = (JSONArray) json.get("movies");
        for (int i = 0; i < movies.size(); i++) {
            FilmInfo filmInfo = parseMovie((JSONObject) movies.get(i));
            if (filmInfo != null) {
                result.add(filmInfo);
            }
        }
        return result;
    }

    private FilmInfo parseMovie(JSONObject movie) throws IOException {
        if (!movieIsInCity((JSONArray) movie.get("city_list"))) {
            return null;
        }
        JSONArray sessions = (JSONArray) movie.get("sessions");
        if (sessions == null || sessions.size() == 0) {
            return null;
        }

        FilmInfo filmInfo = new FilmInfo();
        filmInfo.setEid((Long) movie.get("id"));
        filmInfo.setTitle((String) movie.get("name"));
        filmInfo.setImageUrl((String) movie.get("poster"));
        if (filmInfo.getImageUrl() != null) {
            filmInfo.setImageUrl("http://www.kronverkcinema.ru" + filmInfo.getImageUrl());
        }
        String filmUrl = "http://www.kronverkcinema.ru" + movie.get("url");
        fillFilm(filmInfo, filmUrl);
        return filmInfo;
    }

    private boolean movieIsInCity(JSONArray cityList) {
        if (cityList == null) return false;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityId.equals(cityList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void fillFilm(FilmInfo filmInfo, String filmUrl) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(filmUrl);
            HttpResponse response = httpClient.execute(httpGet);
            String html = IOUtils.toString(response.getEntity().getContent(), "Windows-1251");
            Document document = Jsoup.parse(html);
            Elements filmSpecs = document.select(".movie-specs .specs p");
            for (Element filmSpec: filmSpecs) {
                fillFilmSpec(filmInfo, filmSpec);
            }
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private void fillFilmSpec(FilmInfo filmInfo, Element spec) {
        String tag = spec.text();
        if (StringUtils.startsWith(tag, "Производство:")) {

        } else if (StringUtils.startsWith(tag, "Год выпуска:")) {

        } else if (StringUtils.startsWith(tag, "Жанр:")) {
            filmInfo.setGenre(StringUtils.substringAfter(tag, "Жанр:").trim().toLowerCase());
            filmInfo.setGenre(filmInfo.getGenre().replaceAll(" / ", ", "));
        } else if (StringUtils.startsWith(tag, "Режиссёр:")) {
            filmInfo.setDirector(StringUtils.substringAfter(tag, "Режиссёр:").trim());
        } else if (StringUtils.startsWith(tag, "В ролях:")) {
            filmInfo.setActors(StringUtils.substringAfter(tag, "В ролях:").trim());
        } else if (StringUtils.startsWith(tag, "Продолжительность:")) {
            String minutes = StringUtils.substringAfter(tag, "Продолжительность:").trim().toLowerCase();
            minutes = StringUtils.substringBefore(minutes, "мин.").trim();
            filmInfo.setDuration(Long.parseLong(minutes));
        }
    }
}
