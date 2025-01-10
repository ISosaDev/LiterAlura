# LiterAlura
Gestor de Libros con Integración de Base de Datos y API

Descripción del Proyecto
LiterAlura es una aplicación desarrollada en Java utilizando Spring Boot que permite a los usuarios buscar y registrar libros en una base de datos. La aplicación consume una API externa para obtener información sobre libros y autores, y almacena los datos relevantes en una base de datos local. LiterAlura está diseñada para facilitar la gestión y consulta de libros y autores, proporcionando una interfaz de usuario sencilla y funcional.

Funcionalidades

* Buscar libro por título: Permite a los usuarios buscar libros en la base de datos local o en la API externa utilizando palabras clave.
* Listar libros registrados: Muestra todos los libros registrados en la base de datos, incluyendo detalles como título, autor, géneros, idiomas y número de descargas.
* Listar autores registrados: Muestra todos los autores registrados en la base de datos, incluyendo detalles como nombre, año de nacimiento y año de fallecimiento.
* Listar autores vivos en un determinado año: Permite a los usuarios listar autores que estaban vivos en un año específico.
* Listar libros por idioma: Permite a los usuarios listar libros registrados en la base de datos por idioma.

Cómo Usar la Aplicación
Clonar el repositorio:

git clone https://github.com/ISosaDev/LiterAlura
cd LiterAlura
Configurar la base de datos:

Asegúrate de tener una base de datos PostgreSQL configurada y actualiza las credenciales en el archivo application.properties.
Construir y ejecutar la aplicación:

mvn clean install
mvn spring-boot:run
Interacción con la aplicación:

La aplicación se ejecutará en http://localhost:8080.
Utiliza la interfaz de usuario para buscar libros, listar libros y autores, y realizar otras operaciones disponibles.
Ayuda y Soporte
Si necesitas ayuda o tienes alguna pregunta sobre el proyecto, puedes abrir un issue en GitHub o contactar a los autores del proyecto.

Autor del Proyecto
Isael Sosa - ISosaDev
