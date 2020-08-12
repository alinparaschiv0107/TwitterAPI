import EnvRead.EnvReader;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GetTweetTest {

    @Test
    public void shouldRetrieveTweetGivenAnTweetID() {
        String tweetText = "Get me!v5.0";

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

        String tweetId = createTweetResponse.extract().jsonPath().getString("id_str");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetId)
                .log().all()
                .get("/show.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        // DO SOME CLEANING
        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .post("/destroy/" + tweetId + ".json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldNotRetrieveAnythingGivenAnEmptyID() {
        String tweetID = "";
        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetID)
                .log().all()
                .get("/show.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldNotRetrieveAnythingGivenATextID() {
        String tweetID = "ThisIsATweetID";

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", tweetID)
                .log().all()
                .get("/show.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldRetrieveMultipleTweetsGivenMultipleIDs() {
        String tweetText1 = "Get me with my friends (first) !v1.0";
        String tweetText2 = "Get me with my friends (second) !v1.0";
        String tweetText3 = "Get me with my friends (third) !v1.0";

        ValidatableResponse createTweetResponse1 = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText1)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        ValidatableResponse createTweetResponse2 = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText2)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        ValidatableResponse createTweetResponse3 = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("status", tweetText3)
                .log().all()
                .post("/update.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        String tweetId1 = createTweetResponse1.extract().jsonPath().getString("id_str");
        String tweetId2 = createTweetResponse2.extract().jsonPath().getString("id_str");
        String tweetId3 = createTweetResponse3.extract().jsonPath().getString("id_str");

        String concatIDs = tweetId1 + "," + tweetId2 + "," + tweetId3;

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePathStatus())
                .auth().oauth(EnvReader.getApiKey(), EnvReader.getApiSecret(), EnvReader.getAccessToken(), EnvReader.getAccessSecret())
                .queryParam("id", concatIDs)
                .log().all()
                .get("/lookup.json")
                .prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

    }

}
