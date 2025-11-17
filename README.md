# Franchise Service (Spring WebFlux + Hexagonal + MongoDB)

API reactiva para administrar una **lista de franquicias**, sus **sucursales** y los **productos** de cada sucursal.

Permite:

- Crear franquicias, sucursales y productos.
- Eliminar productos.
- Actualizar nombre de franquicia, sucursal y producto.
- Actualizar stock de un producto.
- Obtener el **producto con mayor stock por sucursal** para una franquicia.

Cumple con los criterios de la prueba:

- Spring Boot + WebFlux.
- Arquitectura hexagonal (puertos y adaptadores).
- Persistencia reactiva en MongoDB.
- Uso de operadores reactivos (`map`, `flatMap`, `switchIfEmpty`, `thenMany`, `reduce`, etc.).
- Manejo correcto de señales `onNext`, `onError`, `onComplete` con logging.
- Pruebas unitarias con cobertura > 60%.
- API RESTful bien definida.
- Empaquetado con Docker.

---

## Stack Tecnológico

- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux**
- **Spring Data Reactive MongoDB**
- **MongoDB** (local / Docker / nube)
- **Lombok**
- **SLF4J / Logback** para logging
- **JUnit 5**, **Mockito**, **Reactor Test**
- **Docker** (multi-stage build)

---

## Arquitectura

Se sigue una **arquitectura hexagonal (ports & adapters / clean architecture)**:

- **Dominio (`domain`)**
  - `model`: `Franchise`, `Branch`, `Product`
  - `repository`: puertos de persistencia (`FranchiseRepositoryPort`, `BranchRepositoryPort`, `ProductRepositoryPort`)
  - `exception`: `FranchiseNotFoundException`, `BranchNotFoundException`, `ProductNotFoundException`

- **Aplicación (`application`)**
  - `usecase`:
    - `CreateFranchiseUseCase`
    - `AddBranchToFranchiseUseCase`
    - `AddProductToBranchUseCase`
    - `DeleteProductUseCase`
    - `UpdateProductStockUseCase`
    - `UpdateFranchiseNameUseCase` (plus)
    - `UpdateBranchNameUseCase` (plus)
    - `UpdateProductNameUseCase` (plus)
    - `GetMaxStockProductPerBranchUseCase` (lógica del producto con mayor stock por sucursal)
  - `usecase.model`:
    - `BranchMaxStockProduct`: modelo de salida para el reporte de máximo stock.

- **Infraestructura**
  - **Persistencia Mongo** (`infrastructure.persistence.mongo`)
    - `document`: `FranchiseDocument`, `BranchDocument`, `ProductDocument`
    - `repository`: `FranchiseReactiveRepository`, `BranchReactiveRepository`, `ProductReactiveRepository`
    - `adapter`: implementaciones de los puertos de dominio:
      - `FranchiseRepositoryAdapter`
      - `BranchRepositoryAdapter`
      - `ProductRepositoryAdapter`
  - **Entrypoints REST** (`infrastructure.entrypoint.rest`)
    - `FranchiseController`
    - `BranchController`
    - `ProductController`
    - `dto`: DTOs de request/response para separar contrato REST del modelo de dominio.
  - **Error handling** (`infrastructure.error`)
    - `GlobalExceptionHandler` + `ApiErrorResponse`

La clase principal de Spring Boot es:

```java
@SpringBootApplication(scanBasePackages = "com.example.franchise")
public class FranchiseServiceApplication { ... }
