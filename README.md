# ParadigmasExamen2 – Registro de exámenes de sangre (Arquitectura Hexagonal)

Proyecto académico de **Ingeniería de Software 2**. Parte del proyecto base
[WolframDev/ParadigmasExamen2](https://github.com/WolframDev/ParadigmasExamen2), una aplicación JavaFX con la interfaz
terminada pero sin lógica, y lo refactoriza hacia **Arquitectura Hexagonal (Puertos y Adaptadores)** aplicando tres
patrones GoF: **Builder**, **Facade** y **Strategy**.

## Objetivo

Registrar exámenes de sangre (código, paciente, costo, tipo de sangre y factor RH), validarlos, evitar códigos
duplicados, listarlos y guardarlos/recuperarlos de un archivo en formato **binario** o **CSV**.

## Arquitectura

```
 Usuario ─► PrimaryController ─► [ExamenesFachada] ─► [Casos de uso] ─► Servicios ─► Dominio
            (adaptador entrada)    (puerto entrada)    (puertos entrada)  (aplicación)
                                                                              │
                                                              [ExamenRepositorio] [RespaldoExamenesPuerto]
                                                                 (puertos de salida)
                                                                              │
                                                       ExamenRepositorioMemoria   RespaldoArchivoAdapter
                                                          (adaptador salida)        (adaptador salida)
```

La dependencia siempre apunta hacia adentro: el dominio no conoce a nadie; la aplicación solo conoce el dominio y sus
propios puertos; la infraestructura implementa o usa los puertos.

### Organización de paquetes (`co.edu.poli.examen2`)

| Paquete | Responsabilidad |
|---|---|
| `dominio.modelo` | `ExamenSangre` (entidad con su `Builder`), `TipoSangre`, `FactorRH`. Reglas de validación de los datos. |
| `dominio.excepcion` | `ExamenInvalidoException`, `ExamenDuplicadoException`. |
| `aplicacion.puerto.entrada` | Lo que la aplicación ofrece: 4 casos de uso + `ExamenesFachada`. |
| `aplicacion.puerto.salida` | Lo que la aplicación necesita: `ExamenRepositorio`, `RespaldoExamenesPuerto`. |
| `aplicacion.puerto` | `TipoFormato`: formato de respaldo elegido por el usuario (BINARIO, CSV). |
| `aplicacion.servicio` | Servicios que implementan los casos de uso y `ExamenesFachadaImpl`. Incluye la regla de código duplicado. |
| `infraestructura.entrada.ui` | `PrimaryController` (JavaFX). Solo captura datos, llama a la fachada y muestra mensajes. |
| `infraestructura.salida.persistencia` | `ExamenRepositorioMemoria`. |
| `infraestructura.salida.archivo` | `RespaldoArchivoAdapter`, `FormatoRespaldo`, `FormatoBinario`, `FormatoCsv`. |
| `configuracion` | `App` (arranque JavaFX) y `Ensamblador` (crea objetos e inyecta dependencias, sin frameworks). |

## Patrones GoF

| Patrón | Categoría | Dónde | Problema que resuelve |
|---|---|---|---|
| **Builder** | Creacional | `ExamenSangre.Builder` | Crear exámenes siempre válidos. `build()` valida todo y reporta **todos** los errores juntos, no solo el primero. |
| **Facade** | Estructural | `ExamenesFachada` / `ExamenesFachadaImpl` | El controlador depende de una sola interfaz (registrar, listar, respaldar, restaurar) en vez de cuatro casos de uso. |
| **Strategy** | Comportamiento | `FormatoRespaldo` → `FormatoBinario`, `FormatoCsv`; contexto: `RespaldoArchivoAdapter` | Separar **dónde** se guarda (archivo) de **cómo** se codifica (binario o CSV). Agregar un formato nuevo no modifica el adaptador. |

## Requisitos

- JDK 11 o superior (el proyecto compila con `release 11`; funciona con 17 y 21).
- Maven 3.8 o superior.
- Conexión a internet la primera vez, para descargar dependencias (JavaFX 17, JUnit 5).

JavaFX 17 se usa a propósito: es la última línea compatible con Java 11 (JavaFX 21 exige Java 17).

## Instalación y ejecución en Visual Studio Code

1. Instala el JDK y Maven, y comprueba con `java -version` y `mvn -v`.
2. En VS Code instala la extensión **Extension Pack for Java**.
3. Abre la carpeta `Examen2` (la que contiene `pom.xml`).
4. En una terminal dentro de esa carpeta ejecuta:

```bash
mvn clean javafx:run
```

Los respaldos se guardan en la carpeta `respaldos/` (`examenes.ser` y `examenes.csv`), junto a donde se ejecuta la
aplicación. La ruta se define una sola vez en `Ensamblador.DIRECTORIO_RESPALDOS`.

### Uso de la aplicación

1. Completa el formulario y pulsa **Guardar**. Si faltan datos, se listan todos los errores a la vez.
2. **Listar** muestra los exámenes registrados. **Limpiar** vacía el formulario.
3. Elige el **formato de respaldo** (Binario o CSV) y pulsa **Serializar** para guardar o **Deserializar** para recuperar.
   Al deserializar se reemplazan los exámenes en memoria por los del archivo.

## Pruebas

```bash
mvn clean test
```

Las pruebas (JUnit 5, sin Mockito ni JavaFX) cubren: Builder y sus validaciones, registro, rechazo de duplicados,
listado, Facade, serialización y recuperación binaria y CSV (con comas, comillas, saltos de línea y tildes),
el adaptador de archivos y un flujo completo a través del `Ensamblador`.


## Declaración de uso de IA

| Herramienta de IA | Actividad en la que se usó |
|---|---|
| Claude (Anthropic) | Generación del código del proyecto: refactorización a arquitectura hexagonal, implementación de los patrones Builder, Facade y Strategy, y pruebas unitarias. |
| ChatGPT (OpenAI) | Redacción de los textos de los informes en PDF. |
| ChatGPT (OpenAI) | Apoyo para agilizar la creación de los diagramas solicitados, que fueron ejecutados y organizados en draw.io. |

El código generado fue revisado, compilado y probado por el autor (`mvn clean test` y análisis con SonarQube).
