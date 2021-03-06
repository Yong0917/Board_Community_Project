package yong.board.api;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yong.board.vo.MovieVO;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieApiClient {

    private final RestTemplate restTemplate;

    @Value("${movieApi.ID}")
    private String CLIENT_ID;

    @Value("${movieApi.SECRET}")
    private String CLIENT_SECRET;

    @Value("${movieApi.URL}")
    private String OpenNaverMovieUrl_getMovies;

    public List<MovieVO> requestMovie(String keyword) {
        final HttpHeaders headers = new HttpHeaders(); // 헤더에 key들을 담아준다.

        headers.set("X-Naver-Client-Id", CLIENT_ID);        //header에 clinetId
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);    //header에 clinetSecretkey
        final HttpEntity<String> entity = new HttpEntity<>(headers);


        String response = restTemplate.exchange(OpenNaverMovieUrl_getMovies, HttpMethod.GET, entity, String.class, keyword).getBody();  //Get형식으로 호출
        //api호출

        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(response);      //JsonArray형식으로 바꿔줌
            JSONArray item = (JSONArray) obj.get("items");

            List<MovieVO> list = null;
            list = new ArrayList<MovieVO>();        //json을 Vo의 list형태로 맞춰줌

            for (int i = 0; i < item.size(); i++) {

                JSONObject tmp = (JSONObject) item.get(i);

                //builder 패턴으로 수정           VO맵핑
                MovieVO m = MovieVO.builder()
                        .title((String) tmp.get("title"))
                        .link((String) tmp.get("link"))
                        .image((String) tmp.get("image"))
                        .subtitle((String) tmp.get("subtitle"))
                        .pubDate((String) tmp.get("pubDate"))
                        .director((String) tmp.get("director"))
                        .actor((String) tmp.get("actor"))
                        .userRating((String) tmp.get("userRating"))
                        .build();

                if (m != null)
                    list.add(m);
            }

            return list;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


}
