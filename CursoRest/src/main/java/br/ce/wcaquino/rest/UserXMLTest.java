package br.ce.wcaquino.rest;

import io.restassured.internal.path.xml.NodeImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserXMLTest {
	
	@Test
		public void trabalharComXML(){
		given()
			.when()
			    .get("https://restapi.wcaquino.me/usersXML/3")
			.then()
					.statusCode(200)
					.rootPath("user") 
					.body("name", is("Ana Julia"))
					.body("@id", is("3"))
				
					.rootPath("user.filhos")
					.body("name.size()", is (2))
			
					.detachRootPath("filhos") 
					.body("filhos.name[0]", is("Zezinho"))
				
					.appendRootPath("filhos")
					.body("name", hasItem("Luizinho"))
					.body("name", hasItems("Luizinho", "Zezinho"))
			;
	}
	
	@Test
	public void PesquisarXMLAvancado(){
		given()
			.when()
				.get("https://restapi.wcaquino.me/usersXML")
			.then()
				.statusCode(200)
				.rootPath("users.user")
				.body("size()", is(3))
				.body("findAll{it.age.toInteger() <=25}.size()", is(2))
				.body("@id", hasItems("1", "2", "3"))
				.body("find{it.age ==25}.name", is("Maria Joaquina"))
				.body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
				.body("salary.find{it =! null}.toDouble()", is(1234.5678d))
				.body("age.collect{it.toInteger() * 2}", hasItems(40,50,60))
				.body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}",
				is("MARIA JOAQUINA"))
			;
	}
	
	@Test
	public void XMLJava(){
		ArrayList<NodeImpl> nomes = given()
			.when()
				.get("https://restapi.wcaquino.me/usersXML")
			.then()
				.statusCode(200)
				.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		     Assert.assertEquals(2, nomes.size());
			 Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
			 Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
		;
		
	}
	@Test
	public void XPath(){
		given()
			.when()
				.get("https://restapi.wcaquino.me/usersXML")
			.then()
				.statusCode(200)
				.body(hasXPath("count(/users/user)", is("3")))
				.body(hasXPath("/users/user[@id = '1']"))
				.body(hasXPath("//user[@id = '2']"))
				.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
				.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos",
				allOf(containsString("Zezinho"), containsString("Luizinho"))))
				.body(hasXPath("/users/user/name", is("João da Silva")))
				.body(hasXPath("//name", is ("João da Silva")))
				.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
				.body(hasXPath("/users/user[last()]/name", is ("Ana Julia")))
				.body(hasXPath("count(/users/user/name[contains(., 'n')])", is ("2")))
				.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
				.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
				.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
		;
	}
	
}
