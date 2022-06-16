package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class VerbosTest {
	@Test
	public void deveSalvarUsuario(){
		given()
				.log().all()
				.contentType("application/json")
				.body("{	\"name\": \"Jose\", \"age\": 50}")
				.when()
					.post("https://restapi.wcaquino.me/users")
				.then()
					.log().all()
					.statusCode(201)
					.body("id", is(notNullValue()))
					.body("name", is("Jose"))
					.body("age", is(50))
		
		;
	}
	
	@Test
	public void deveSalvarUsuarioMAP(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
				.log().all()
				.contentType("application/json")
				.body(params)
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.body("id", is(notNullValue()))
				.body("name", is("Usuario via map"))
				.body("age", is(25))
		
		;
	}
	
	@Test
	public void deveSalvarUsuarioObjeto(){
		User user = new User("Usuario via objeto", 35);

		
		given()
				.log().all()
				.contentType("application/json")
				.body(user)
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.body("id", is(notNullValue()))
				.body("name", is("Usuario via objeto"))
				.body("age", is(35))
		
		;
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario(){
		User user = new User("Usuario deserializado", 35);
		
		
	User usuarioInserido =	given()
				.log().all()
				.contentType("application/json")
				.body(user)
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class)
		;
	System.out.println(usuarioInserido);
	assertThat(usuarioInserido.getId(), notNullValue() );
		assertEquals("Usuario deserializado", usuarioInserido.getName());
		assertThat(usuarioInserido.getAge(), is(35));
	}
	
	
	@Test
	public void naoDeveSalvarUsuarioSemNome(){
		given()
				.log().all()
				.contentType("application/json")
				.body("{ \"age\": 50}")
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(400)
				.body("id", is(nullValue()))
				.body("error", is("Name é um atributo obrigatório"))
		
		;
	}
	@Test
	public void deveSalvarUsuarioXML(){
		given()
				.log().all()
				.contentType("application/xml")
				.body("<user><name>Joao</name><age>30</age></user>")
			.when()
				.post("https://restapi.wcaquino.me/usersXML")
			.then()
				.log().all()
				.statusCode(201)
			 	.body("user.@id", is(notNullValue()))
				.body("user.name", is("Joao"))
			    .body("user.age", is("30"))
		
		;
	}
	
	@Test
	public void deveSalvarUsuarioXMLUsandoObjeto(){
		User user = new User("Usuario XML", 42);
		given()
				.log().all()
				.contentType("application/xml")
				.body(user)
			.when()
				.post("https://restapi.wcaquino.me/usersXML")
			.then()
				.log().all()
				.statusCode(201)
				.body("user.@id", is(notNullValue()))
				.body("user.name", is("Usuario XML"))
				.body("user.age", is("42"))
		
		;
	}
	
	@Test
	public void deveDeserializarXMLAoSalvarUsuario(){
		User user = new User("Usuario XML", 42);
		
	User usuarioInserido =	given()
				.log().all()
				.contentType(ContentType.XML)
				.body(user)
			.when()
				.post("https://restapi.wcaquino.me/usersXML")
			.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class)
			;
	assertThat(usuarioInserido.getId(), notNullValue());
	assertThat(usuarioInserido.getName(), is("Usuario XML"));
	assertThat(usuarioInserido.getAge(), is(42));
	
	}
	
	
	@Test
	public void deveAlterarUsuario(){
		given()
				.log().all()
				.contentType("application/json")
				.body("{	\"name\": \"Usuario Alterado\", \"age\": 90}")
			.when()
				.put("https://restapi.wcaquino.me/users/1")
			.then()
				.log().all()
				.statusCode(200)
				.body("id", is(1))
				.body("name", is("Usuario Alterado"))
				.body("age", is(90))
				.body("salary", is(1234.5678f))
		
		;
	}
	@Test
	public void devoCustomizarURL(){
		given()
				.log().all()
				.contentType("application/json")
				.body("{	\"name\": \"Usuario Alterado\", \"age\": 90}")
				.pathParams("entidade", "users")
				.pathParams("userId", 1)
			.when()
				.put("https://restapi.wcaquino.me/{entidade}/{userId}")
			.then()
				.log().all()
				.statusCode(200)
				.body("id", is(1))
				.body("name", is("Usuario Alterado"))
				.body("age", is(90))
				.body("salary", is(1234.5678f))
		
		;
	}
	@Test
	public void devoRemoverUsuario(){
		given()
				.log().all()
			.when()
				.delete("https://restapi.wcaquino.me/users/1")
			.then()
				.log().all()
				.statusCode(204)
				;
		
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente(){
		given()
				.log().all()
			.when()
				.delete("https://restapi.wcaquino.me/users/1000")
			.then()
				.log().all()
				.statusCode(400)
				.body("error", is("Registro inexistente"))
		;
		
	}
}


