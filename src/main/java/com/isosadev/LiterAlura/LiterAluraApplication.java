package com.isosadev.LiterAlura;

// Importaciones necesarias para el funcionamiento de la aplicación
import com.isosadev.LiterAlura.principal.Principal; // Importa la clase Principal que maneja el menú del usuario
import com.isosadev.LiterAlura.repository.AutorRepository; // Importa el repositorio para la entidad Autor
import com.isosadev.LiterAlura.repository.GeneroRepository; // Importa el repositorio para la entidad Género
import com.isosadev.LiterAlura.repository.IdiomaRepository; // Importa el repositorio para la entidad Idioma
import com.isosadev.LiterAlura.repository.LibroRepository; // Importa el repositorio para la entidad Libro
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired para inyección de dependencias
import org.springframework.boot.CommandLineRunner; // Importa la interfaz CommandLineRunner para ejecutar código al inicio de la aplicación
import org.springframework.boot.SpringApplication; // Importa la clase SpringApplication para iniciar la aplicación Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importa la anotación SpringBootApplication para configurar la aplicación

// Anotación que marca esta clase como la principal de la aplicación Spring Boot
@SpringBootApplication(scanBasePackages = "com.isosadev.LiterAlura") // Escanea el paquete especificado para encontrar componentes (como repositorios)
public class LiterAluraApplication implements CommandLineRunner {

	// Inyección de dependencias de los repositorios utilizando @Autowired
	@Autowired
	private LibroRepository repository; // Repositorio para la entidad Libro

	@Autowired
	private AutorRepository autorRepository; // Repositorio para la entidad Autor

	@Autowired
	private GeneroRepository generoRepository; // Repositorio para la entidad Género

	@Autowired
	private IdiomaRepository idiomaRepository; // Repositorio para la entidad Idioma

	// Método principal de la aplicación (punto de entrada)
	public static void main(String[] args) {
		// Inicia la aplicación Spring Boot
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	// Método que se ejecuta después de que la aplicación Spring Boot se inicia
	@Override
	public void run(String... args) throws Exception {
		System.out.println("\nProyecto Spring LiterAlura\n"); // Imprime un mensaje de bienvenida en la consola
		// Crea una instancia de la clase Principal, inyectando los repositorios necesarios
		Principal principal = new Principal(repository, autorRepository, generoRepository, idiomaRepository);
		// Llama al método menuUsuario() de la clase Principal para mostrar el menú al usuario
		principal.menuUsuario();
	}
}