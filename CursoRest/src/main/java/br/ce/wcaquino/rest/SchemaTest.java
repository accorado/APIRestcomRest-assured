package br.ce.wcaquino.rest;


import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import static io.restassured.RestAssured.*;

public class SchemaTest {
	@Test
	
	public void validarSchemaXML(){
		given()
				.log().all()
			.when()
				.get("https://restapi.wcaquino.me/v2/users?format=xml")
			.then()
				.log().all()
				.statusCode(200)
				.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
				;
	}
	@Test(expected = SAXParseException.class)
	public void naoValidarSchemaXMLInvalido() {
		given()
				.log().all()
			.when()
				.get("https://restapi.wcaquino.me/invalidUsersXML")
			.then()
				.log().all()
				.statusCode(200)
				.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		;
	}
	@Test
	public void validarSchemaJson(){
		given()
				.log().all()
			.when()
				.get("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(200)
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
				;
	}
}
