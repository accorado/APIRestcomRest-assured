package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class OlaMundoTest {

    @Test
    public void testOlaMundo(){
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("O status code deveria ser 200", response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }
    @Test
    public void outrasFormasRestAssured(){
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("https://restapi.wcaquino.me/ola").then().statusCode(200);
    }
    @Test
    public void ModoFluente(){
        given()
            .when()
                .get("https://restapi.wcaquino.me/ola").
            then()
                .statusCode(200);
    }

    @Test
    public void devoConhecerMatchersHamcrest() {
            assertThat("Maria", is("Maria"));
            assertThat(100, is(100));
            assertThat(100, isA(Integer.class));
            assertThat(100d, isA(Double.class));
            assertThat(100, greaterThan(90));
            assertThat(100, lessThan(200));

        List<Integer> impares = Arrays.asList(1,3,5,7,9,11);
        assertThat(impares, hasSize(6));
        assertThat(impares, contains(1,3,5,7,9,11));
        assertThat(impares, containsInAnyOrder(5,7,9,1,3,11));
        assertThat(impares, hasItem(1));

        assertThat("Maria", is(not("Pedro")));
        assertThat("Maria", not("Pedro"));
        assertThat("Antonio", anyOf(is("Maria"), is ("Antonio")));
        assertThat("Antonio", allOf(startsWith("Ant"), endsWith("nio"),containsString("oni")));
    }
    @Test
    public void ValidarBody(){
        given()
            .when()
                .get("https://restapi.wcaquino.me/ola").
            then()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"))
                .body(is(not(nullValue())));

    }

    }

