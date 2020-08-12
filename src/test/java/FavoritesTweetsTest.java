import EnvRead.EnvReader;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class FavoritesTweetsTest {
    @Test
    public void shouldLikeATweetGivenAnID() {
        String tweetText = "Do you like me? v1.0";

        ValidatableResponse createTweetResponse = given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        String tweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathFavorites())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetId)
                .log().all()
                .post("/create.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldDislikeALikedTweetGivenAnID() {
        String tweetText = "Do you like me? v2.0";

        ValidatableResponse createTweetResponse = given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        String tweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathFavorites())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetId)
                .log().all()
                .post("/create.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathFavorites())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetId)
                .log().all()
                .post("/destroy.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void shouldNotDislikeANonLikedTweetGivenAnID() {
        String tweetText = "You don't like me :( v2.0";

        ValidatableResponse createTweetResponse = given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        String tweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathFavorites())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetId)
                .log().all()
                .post("/destroy.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void shouldReturnAllLikedTweetsGivenAScreenName() {
        given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathFavorites())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParams("screen_name", EnvReader.getScreenName())
                .log().all()
                .get("/list.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldLikeARetweetedTweetGivenATweetID() {
        String tweetText = "Retweet me & like me maaan ! v2.0";

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

        String RetweetedID = retweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathFavorites())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", RetweetedID)
                .log().all()
                .post("/create.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }

}
