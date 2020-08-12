import EnvRead.EnvReader;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PostTweetTest {
    @Test
    public void shouldCreateTweetGivenAuthCredentials() {
        String tweetText = "Alin C first Tweet from RA";

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    /**
     * Response is 403 forbidden but i think it could be 400 Bad Request ?
     */
    @Test
    public void shouldNOTCreateTweetGivenNoTextAsParam() {
        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", "")
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void shouldNotCreateTweetsWithSameStatusGivenAuthCredetials() {
        String tweetText = "Test2 Tweet";

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }


}
