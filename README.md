# Mis apuntes del proyecto de productos

Este README es para recordar, paso a paso, qué fui haciendo en mi proyecto con Spring Boot.

La idea es tener una página donde pueda ver productos y, poco a poco, aprender a crearlos, editarlos y borrarlos.

## ¿Qué estoy usando?

- **Java 21:** el lenguaje del proyecto.
- **Spring Boot:** ayuda a levantar la aplicación web.
- **MySQL:** guarda los productos.
- **Spring Data JPA:** ayuda a trabajar con la base de datos sin escribir todas las consultas a mano.
- **Thymeleaf:** muestra los datos de Java en las páginas HTML.
- **Maven:** descarga y organiza las herramientas del proyecto.

## Antes de empezar

Necesito tener instalado:

- Java 21
- Maven
- MySQL
- Un editor para Java, por ejemplo VS Code con Extension Pack for Java

La versión de Java de `pom.xml` tiene que coincidir con la que uso para ejecutar el proyecto. En este proyecto puse Java 21.

## Preparar MySQL

Primero creo la base de datos:

```sql
CREATE DATABASE parcial_springboot;
```

Después abro `src/main/resources/application.properties` y reviso que tenga la dirección de MySQL, el usuario y la contraseña de mi computadora.

**Ojo:** no debo compartir ni subir mi contraseña a GitHub.

Spring usa la clase `Producto` para conocer los datos de cada producto. Con la configuración actual, Hibernate puede crear o actualizar la tabla `productos` al iniciar la aplicación.

Para tener productos de prueba, puedo agregarlos desde MySQL:

```sql
USE parcial_springboot;

INSERT INTO productos (nombre, categoria, precio, cantidad_disponible, descripcion)
VALUES
('Cuaderno universitario', 'Papelería', 3.50, 40, 'Cuaderno de 100 hojas'),
('Lápiz HB', 'Papelería', 0.75, 100, 'Lápiz de grafito'),
('Mochila escolar', 'Accesorios', 24.99, 15, 'Mochila con varios compartimentos'),
('Calculadora científica', 'Electrónica', 18.50, 12, 'Calculadora para operaciones científicas');
```

Para revisar si quedaron guardados:

```sql
SELECT * FROM productos;
```

## ¿Cómo inicio el proyecto?

Abro una terminal en la carpeta donde está `pom.xml` y escribo:

```bash
mvn spring-boot:run
```

Luego abro esta dirección en el navegador: <http://localhost:8080>.

## ¿Dónde está cada cosa?

```text
src/main/java/com/example/Parcial/
├── config/WebConfig.java
├── controlador/controller.java
├── model/Producto.java
├── repository/ProductoRepository.java
└── service/ProductoImageStorage.java

src/main/resources/
├── templates/
│   ├── crear.html
│   ├── editar.html
│   └── inicio.html
└── application.properties
```

- **`Producto.java`** es como la ficha de un producto. Dice que tiene un ID, nombre, categoría, precio, cantidad, descripción y nombre del archivo de imagen.
- **`ProductoRepository.java`** es el ayudante que busca y guarda productos en la base de datos.
- **`controller.java`** recibe lo que pide el navegador y decide qué página mostrar o qué acción hacer.
- **`ProductoImageStorage.java`** valida y guarda las imágenes en `uploads/productos/`; MySQL solo guarda el nombre del archivo.
- **`WebConfig.java`** permite que el navegador pueda mostrar las imágenes guardadas.
- **`templates`** tiene las páginas HTML que ve la persona en el navegador.
- **`application.properties`** tiene la configuración para conectarse a MySQL.

## Lo que ya aprendí y funciona

### Ver los productos

Cuando abro la página principal `/`, el controlador pide los productos con:

```java
productoRepository.findAll()
```

`findAll()` significa “dame todos los productos”. El controlador manda esa lista a `inicio.html` con el nombre `productos`.

En la página, esta línea:

```html
<tr th:each="producto : ${productos}">
```

significa “por cada producto de la lista, dibuja una fila”. Por eso veo los productos de MySQL en la tabla. Si todavía no hay productos, la lista viene vacía y no se dibujan filas.

### Crear un producto

El formulario de `crear.html` tiene espacios para escribir el nombre, la categoría, el precio, la cantidad y la descripción.

Así viajan los datos:

1. Abro `/Crear` y el controlador prepara un producto vacío.
2. Escribo los datos en el formulario.
3. Selecciono una imagen JPG, PNG o GIF de hasta 5 MB.
4. Al apretar **Guardar producto**, el formulario manda los datos y la imagen a `POST /Crear`.
5. La aplicación guarda el archivo en `uploads/productos/` y el nombre del archivo en MySQL.
6. La página vuelve al inicio y puedo ver el producto en una card con su imagen.

La carpeta de imágenes está ignorada por Git. Si cambio de computadora o despliego la aplicación, también debo copiar esa carpeta o configurar un almacenamiento persistente.

### Editar un producto

Desde la card de un producto, el enlace **Editar producto** abre `/Editar/{id}`. El controlador busca ese producto en MySQL y `editar.html` muestra sus datos actuales. Al enviar el formulario, `POST /Editar/{id}` carga de nuevo el registro existente y actualiza sus campos para no reemplazar por accidente su ID o el nombre de la imagen.

La imagen es opcional al editar: si no selecciono otra, se conserva la actual. Si selecciono una imagen válida, se guarda el archivo nuevo y se actualiza el nombre asociado al producto.

## El repositorio y sus métodos

`ProductoRepository` extiende `JpaRepository<Producto, Integer>`. Esto le dice a Spring: “quiero trabajar con productos, y su ID es un número entero”.

Spring Data JPA ya trae algunos métodos útiles:

- `findAll()` trae todos los productos.
- `findById(id)` busca un producto por su ID.
- `save(producto)` guarda un producto nuevo o guarda sus cambios.
- `deleteById(id)` borra un producto por su ID.

`findById(id)` puede encontrar un producto o no. Por eso devuelve una cajita llamada `Optional`.

```java
productoRepository.findById(id).orElseThrow();
```

`orElseThrow()` significa: “si no encuentro el producto, lanza un error”. No significa “no muestres nada”.

## ¿Qué falta hacer?

- **Ver productos:** ya funciona.
- **Crear productos:** ya funciona.
- **Editar productos:** el enlace de la tabla manda el ID y el controlador busca ese producto. Falta hacer el formulario para cambiar sus datos y guardar los cambios.
- **Borrar productos:** todavía falta.
- **Mostrar mensajes bonitos si algo sale mal:** todavía falta.

El siguiente paso será hacer que `editar.html` muestre los datos del producto y permita guardarlos después de cambiarlos.

## Cosas que quiero recordar

- Java sí distingue entre mayúsculas y minúsculas. `ProductoRepository` es el nombre del tipo; `productoRepository` es el nombre de la variable.
- En VS Code, puedo poner el cursor sobre una clase y probar **Ctrl+.** para agregar un import. También puedo escribir el import a mano debajo de `package`.
- Las páginas de `templates` se abren usando una ruta del controlador, como `/Crear`. No debo poner `crear.html` en el enlace.
- Si aparece `Cannot load driver class: com.mysql.cj.jdbc.Driver`, debo revisar que esté `mysql-connector-j` en `pom.xml` y recargar Maven.
