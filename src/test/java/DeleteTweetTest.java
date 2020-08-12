import EnvRead.EnvReader;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class DeleteTweetTest {

    @Test
    public void shouldDeleteTweetGivenTweetID() {
        String tweetText = "Delete Tweet";

        ValidatableResponse createTweetResponse =
                given()
                        .baseUri(EnvReader.getBaseUri())
                        .basePath(EnvReader.getBasePathStatus())
                        .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                        .queryParam("status", tweetText)
                        .log().all()
                        .post("/update.json")
                        .prettyPeek()
                        .then().statusCode(HttpStatus.SC_OK);

        String TweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/destroy/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldNotDeleteTweetGivenAWrongTweetID() {
        String tweetID = "15";

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/destroy/" + tweetID + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldDeleteRetweetedTweetGivenTweetID() {
        String tweetText = "Retweet & Delete WHOOO ! v1.0";

        ValidatableResponse createTweetResponse =
                given()
                        .baseUri(EnvReader.getBaseUri())
                        .basePath(EnvReader.getBasePathStatus())
                        .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                        .queryParam("status", tweetText)
                        .log().all()
                        .post("/update.json")
                        .prettyPeek()
                        .then().statusCode(HttpStatus.SC_OK);

        String TweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        ValidatableResponse retweetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        String retweetID = retweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/destroy/" + retweetID + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void shouldDeleteBothRetweetedAndOriginalGivenTweetID() {
        String tweetText = "Retweet & Delete WHOOO ! v2.0";

        ValidatableResponse createTweetResponse =
                given()
                        .baseUri(EnvReader.getBaseUri())
                        .basePath(EnvReader.getBasePathStatus())
                        .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                        .queryParam("status", tweetText)
                        .log().all()
                        .post("/update.json")
                        .prettyPeek()
                        .then().statusCode(HttpStatus.SC_OK);

        String TweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        ValidatableResponse retweetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/destroy/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }
}
