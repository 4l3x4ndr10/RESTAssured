package api;
import api.pojo.ProductData;
import api.pojo.Rating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class MockaeTest {

    public final static String URL = "https://api.mockae.com/fakeapi/";

    //Тест 1. Получения списка продуктов
    @Test
    public void getProductsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<ProductData> products = given()
                .when()
                .get("products")
                .then().log().all()
                .extract().body().jsonPath().getList("",ProductData.class);

        //Проверка окончания ссылки на изображение продукта
        products.forEach(x -> Assertions.assertTrue(x.getImageUrl().endsWith(".jpg")));
        //Проверка, что название продукта не Null
        products.forEach(x -> Assertions.assertNotNull(x.getName()));
        //Проверка, что цена продукта не Null
        products.forEach(x -> Assertions.assertNotNull(x.getPrice()));

    }

    //Тест 2.1. Проверка получения одного продукта
    @Test
    public void getSingleProduct200Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        ProductData product = given()
                .when()
                .get("products/23")
                .then().log().all()
                .extract().as(ProductData.class);

        //Проверка названия продукта Gaming Monitor 24"
        Assertions.assertEquals("Gaming Monitor 24\"", product.getName());

    }

    //Тест 2.2. Проверка получения несуществующего продукта
    @Test
    public void getSingleProduct404Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError404());
        given()
                .when()
                .get("products/51")
                .then().log().all();

    }

    //Тест 3. Создание нового продукта
    @Test
    public void postProductTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(201));
        ProductData product = new ProductData("70","Air Fryer","Air Fryer for Home",39.99, "Home",
                100, "AIRF-070","https://example.com/images/airfryer7.jpg", new Rating(4.9, 100));
        ProductData productResponse = given()
                .body(product)
                .when()
                .post("products")
                .then().log().all()
                .extract().as(ProductData.class);
        //В ответе должны получить созданный продукт
        Assertions.assertEquals(product.getName(), productResponse.getName());
    }

    //Тест 4.1 Обновление продукта
    @Test
    public void putProduct200Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        ProductData product = new ProductData("23","Gaming Monitor 27",
                "27-inch gaming monitor with 144Hz refresh rate and low response time.",
                319.99,"Displays",120,"GMON27-023",
                "https://example.com/images/monitor27.jpg", new Rating(4.7, 200));
        ProductData productResponse = given()
                .body(product)
                .when()
                .put("products/23")
                .then().log().all()
                .extract().as(ProductData.class);
        //В ответе должны получить обновленный продукт
        Assertions.assertEquals(product.getName(), productResponse.getName());
    }

    //Тест 4.2 Обновление несуществующего продукта
    @Test
    public void putProduct404Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError404());
        ProductData product = new ProductData("23","Gaming Monitor 27",
                "27-inch gaming monitor with 144Hz refresh rate and low response time.",
                319.99,"Displays",120,"GMON27-023",
                "https://example.com/images/monitor27.jpg", new Rating(4.7, 200));
        given()
                .body(product)
                .when()
                .put("products/51")
                .then().log().all();
    }

    //Тест 5.1 Частичное обновление продукта
    @Test
    public void patchProduct200Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        ProductData product = new ProductData("23", 319.99, "Gaming Monitor 27");
        ProductData productResponse = given()
                .body(product)
                .when()
                .put("products/23")
                .then().log().all()
                .extract().as(ProductData.class);
        //В ответе должны получить обновленный продукт
        Assertions.assertEquals(product.getName(), productResponse.getName());
    }

    //Тест 5.2 Частичное обновление, несуществующего продукта
    @Test
    public void patchProduct404Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError404());
        ProductData product = new ProductData("23", 319.99, "Gaming Monitor 27");
        given()
                .body(product)
                .when()
                .put("products/51")
                .then().log().all();
    }

    //Тест 6.1. Удаление продукта 50 должно вернуть код 200
    @Test
    public void deleteWith200Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        given()
                .when()
                .delete("products/50")
                .then().log().all();
    }

    //Тест 6.2. Удаление продукта 51 должно вернуть код 404, так как такого продукта нет
    @Test
    public void deleteWith404Test() {
        Specifications.installSpecification(Specifications.requestSpec(URL),Specifications.responseSpecError404());
        given()
                .when()
                .delete("products/51")
                .then().log().all();
    }

}
