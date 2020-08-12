import EnvRead.EnvReader;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class RetweetTest {

    @Test
    public void shouldRetweetTweetGivenTweetID() {
        String tweetText = "Retweet me maaan ! v1.0";

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
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void shouldUnretweetARetweetedTweetGivenTweetID() {
        String tweetText = "Unretweet me maaan ! v1.0";

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
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/unretweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldUnretweetANonRetweetedTweetGivenTweetID() {
        String tweetText = "I m a not retweeted ! Do you want to unretweet me ? v1.0";

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
                .post("/unretweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldNotDoubleRetweetATweetGivenTweetID(){
        String tweetText = "Double Retweet ! v1.0";

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
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void shouldReturnListOfRetweetsGivenTweetID(){
        String tweetText = "Retweet me maaan ! v4.0";

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
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .log().all()
                .get("/retweets/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void shouldNOTRetweetARetweetedTweetGivenTweetID() {
        String tweetText = "Double Retweet WHOOO ! v2.0";

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
                .post("/retweet/" + retweetID + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_FORBIDDEN );
    }

    @Test
    public void shouldReturnListOfUsersWhoRetweetedTweetGivenTweetID() {
        String tweetText = "Retweet me maaan ! v6.0";

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
                .post("/retweet/" + TweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .queryParam("id", TweetId)
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .log().all()
                .get("/retweeters/ids.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);


    }

    @Test
    public void shouldReturnListOfRetweetsFromCurentUserGivenAuthCredentials() {
        given()
                .baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .log().all()
                .get("/retweets_of_me.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

}
