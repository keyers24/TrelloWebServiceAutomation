package services;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.List;


public class trelloOperationsService extends baseTest {

    @Test
    public void serviceTest() {

        RestAssured.baseURI=URL;

        String boardName = "New Board";
        String boardId = createBoard(boardName);

        String listName="List Test";
        String listIdd=createList(listName,boardId);

        String cardName = "New Card Test";
        String cardId1=createCard(listIdd,cardName);

        String cardName2 = "New Card Test2";
        String cardId2=createCard(listIdd,cardName2);

        String updateCardID=updateCard(cardId2) ;

        deleteAllCardsInList(listIdd);

        deleteBoard(boardId);

    }

    public  String createBoard(String boardName) {
        Response boardResponse =RestAssured
                .given()
                .queryParam("name", boardName)
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .post("boards/");
        boardResponse
                .then()
                .statusCode(200);
        System.out.println("Board Create successfully.");
        return boardResponse.path("id");

    }
    public  String createList(String listName,String boardId) {
        Response listResponse =RestAssured
                        .given()
                        .queryParam("name", listName)
                        .queryParam("idBoard",boardId)
                        .queryParam("key", key)
                        .queryParam("token", token)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("lists/");
        listResponse
                .then()
                .statusCode(200);
        System.out.println("List Create successfully.");
        return listResponse.path("id");

    }

    public  String  createCard(String listID, String cardName) {
        Response cardResponse=RestAssured
                        .given()
                        .queryParam("name", cardName)
                        .queryParam("idList", listID)
                        .queryParam("key", key)
                        .queryParam("token", token)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("cards");
        cardResponse
                .then()
                .statusCode(200);
        System.out.println("Card Create successfully.");
        return cardResponse.path("id");

    }
    public  String  updateCard(String cardId) {
        Response cardResponse=RestAssured
                .given()
                .queryParam("name", "HGET")
                .queryParam("id", cardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .put("cards/"+cardId);
        cardResponse
                .then()
                .statusCode(200);
        System.out.println("Card Update successfully.");
        return cardResponse.path("id");

    }
    public  void  deleteCard(String cardId) {
                RestAssured
                .given()
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .delete("cards/"+cardId)
                .then()
                .statusCode(200);
        System.out.println("Card deleted successfully.");

    }
    public  void deleteAllCardsInList(String listId) {
        Response response = RestAssured
                .given()
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .get("lists/" + listId + "/cards");

        JsonPath jsonPath = response.jsonPath();
        List<String> cardIds = jsonPath.getList("id");

        for (String cardId : cardIds) {
            deleteCard(cardId);
        }

        System.out.println("All cards deleted successfully.");
    }

    public  void  deleteBoard(String boardID) {
        RestAssured
                .given()
                .queryParam("key", key)
                .queryParam("token", token)
                .when()
                .delete("boards/"+boardID)
                .then()
                .statusCode(200);
        System.out.println("Board deleted successfully.");

    }

}

