package br.ce.wcaquino.rest;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.hamcrest.Matcher;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class AuthTest {
	@Test
	public void acessarSWAPI(){
		given()
				.log().all()
			.when()
				.get("https://swapi.dev/api/people/1")
			.then()
				.log().all()
				.statusCode(200)
				.body("name", is("Luke Skywalker"))
				;
	}
	
	@Test
	public void obterClima(){
		given()
				.log().all()
				.queryParam("q" ,"Fortaleza,BR")
				.queryParam("appid", "8ea3d7bb4f90bd85569ce795e8ef32df")
				.queryParam("units" ,"metric")
			.when()
				.get("http://api.openweathermap.org/data/2.5/weather")
			.then()
				.log().all()
				.statusCode(200)
				.body("name", is ("Fortaleza"))
			;
	}
	
	@Test
	public void naoAcessarSemSenha() {
		given()
				.log().all()
			.when()
				.get("https://restapi.wcaquino.me/basicauth")
			.then()
				.log().all()
				.statusCode(401)
		;
	}
	@Test
	public void autenticarAPI(){
		given()
				.log().all()
			.when()
				.get("https://admin:senha@restapi.wcaquino.me/basicauth")
			.then()
				.log().all()
				.statusCode(200)
				;
		
	}
	
	@Test
	public void autenticarAPI2(){
		given()
				.log().all()
				.auth().basic("admin", "senha")
			.when()
				.get("https://restapi.wcaquino.me/basicauth")
			.then()
				.log().all()
				.statusCode(200)
				.body("status", is("logado"))
		;
		
	}
	
	@Test
	public void autenticarAPIChallenge(){
		given()
				.log().all()
				.auth().preemptive().basic("admin", "senha")
			.when()
				.get("https://restapi.wcaquino.me/basicauth2")
			.then()
				.log().all()
				.statusCode(200)
				.body("status", is("logado"))
		;
		
	}
	
	@Test
	public void autenticarToken(){
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "teste@anna");
		login.put("senha", "12345");
		
		//Login na API e Receber Token
	String token = given()
				.log().all()
				.body(login)
				.contentType(ContentType.JSON)
			.when()
				.post("https://barrigarest.wcaquino.me/signin")
			.then()
				.log().all()
				.statusCode(200)
				.extract().path("token");
			
		//Obter as contas
			given()
				.log().all()
					.header("Authorization", "JWT " + token)
			.when()
				.get("https://barrigarest.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
					.body("nome", hasItem("Teste"))
			;
	}
	
	@Test
	public void acessarAplicacaoWeb(){
		//Login
		String cookie =	given()
					.log().all()
					.formParam("email", "teste@anna")
					.formParam("senha", "12345")
					.contentType(ContentType.URLENC.withCharset("UTF-8"))
				.when()
					.post("https://seubarriga.wcaquino.me/logar")
				.then()
					.log().all()
					.statusCode(200)
					.extract().header("set-cookie")
				;
		
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println("---------------------");
		System.out.println(cookie);
		
		//Obter a conta
		
		String body =	given()
					.log().all()
				    .cookie("connect.sid", cookie)
				.when()
					.get("https://seubarriga.wcaquino.me/contas")
				.then()
					.log().all()
					.statusCode(200)
				.body("html.body.table.tbody.tr[0].td[0]", is("Teste"))
				.extract().body().asString();
		
		System.out.println("--------------------------");
		XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
		
		}
	
	
}
