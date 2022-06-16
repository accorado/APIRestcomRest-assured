package br.ce.wcaquino.rest;


import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import java.io.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class FileTest {
	
	@Test
	public void deveObrigarEnvioArquivo(){
		given()
				.log().all()
			.when()
				.post("http://restapi.wcaquino.me/upload")
			.then()
				.log().all()
				.statusCode(404)
				.body("error", is("Arquivo não enviado"))
		
		;
	}
	@Test
	public void deveFazerUploadArquivo(){
		given()
				.log().all()
				.multiPart("arquivo", new File("src/main/resources/imagem.pdf"))
			.when()
				.post("http://restapi.wcaquino.me/upload")
			.then()
				.log().all()
				.statusCode(200)
				.body("name", is("imagem.pdf"))
			
				;
	}
	
	@Test
	public void naodeveFazerUploadArquivoGrande(){
		given()
				.log().all()
				.multiPart("arquivo", new File("src/main/resources/imagemalta.jpg"))
			.when()
				.post("http://restapi.wcaquino.me/upload")
			.then()
				.log().all()
				.time(lessThan(10000L))
				.statusCode(413)
				
		;
	}
	@Test
	public void deveBaixarArquivo() throws IOException {
		byte[] image = given()
				.log().all()
			.when()
				.get("http://restapi.wcaquino.me/download")
			.then()
				.statusCode(200)
				.extract().asByteArray();
		;
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
		System.out.println(imagem.length());
		assertThat(imagem.length(), lessThan(100000L));
		
	}
}
