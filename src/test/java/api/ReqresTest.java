package api;

import api.pojo.ErrorClass;
import api.pojo.ResourceData;
import api.pojo.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;


public class ReqresTest {

    public final static String URL = "https://reqres.in/";

//Тест 1
//1.1. Используя сервис https://reqres.in/ получить список пользователей с первой страницы
//1.2. Убедиться что имена файлов-аватаров пользователей совпадают:
//1.3. Убедиться, что email пользователей имеет окончание reqres.in;
    @Test
    public void checkAvatarAndIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=1&per_page=10")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        //Убедиться что имена файлов-аватаров пользователей совпадают
        users.forEach(x-> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));

        //Убедиться, что email пользователей имеет окончание reqres.in
        Assertions.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
    }

    /*
//Тест 2
//Используя сервис https://reqres.in/ протестировать регистрацию пользователя в системе
//Необходимо создание 2 тестов:
//2.1. Успешная регистрация
//2.2. Регистрация с ошибкой из-за отсутствия пароля. Проверить коды ошибок.

    //2.1. Успешная регистрация
    @Test
    public void successRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in","eve.holt@reqres.in","pistol");
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());
        Assertions.assertEquals(id, successReg.getId());
        Assertions.assertEquals(token, successReg.getToken());
    }

    //2.2. Регистрация с ошибкой из-за отсутствия пароля. Проверить коды ошибок.
    @Test
    public void unSuccessRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());
        Register user = new Register("sydney@file","sydney@file","");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);
        Assertions.assertEquals("Missing password", unSuccessReg.getError());
    }
*/

//Тест 3
//3.1. Используя сервис https://reqres.in/ убедиться, что операция LIST<RESOURCE>
//возвращает данные, отсортированные по годам.

    @Test
    public void checkResourcesYears() {
        Specifications.installSpecification(Specifications.requestSpecWithHeader(URL), Specifications.responseSpecOK200());
        List<ResourceData> resources = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ResourceData.class);

        List<Integer> years = resources.stream().map(ResourceData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().toList();
        Assertions.assertEquals(sortedYears, years);
    }
    /*
//Тест 4.1
//4.1. Используя сервис https://reqres.in/ попробовать удалить второго пользователя и сравнить статус-код

    @Test
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

     */

    //Тест 5 Метод должен возвращать код 405 и ответ method_not_allowed.
    @Test
    public void checkMethodHintTest() {
        Specifications.installSpecification(Specifications.requestSpecWithHeader(URL), Specifications.responseSpecUnique(405));
        ErrorClass unSuccessLogin = given()
                .when()
                .get("api/app-users/login")
                .then().log().all()
                .extract().as(ErrorClass.class);

        Assertions.assertEquals("method_not_allowed", unSuccessLogin.getError());
    }
}
