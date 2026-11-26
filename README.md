# Proyecto de Cátedra POO901 - Sistema de Gestión Bancaria Académica

## Banco Académico UDB - Fase 2

### Descripción
Aplicación Desktop en Java desarrollada bajo el paradigma de **Programación Orientada a Objetos (POO)** con interfaz gráfica en **Java Swing**, persistencia de datos en **MySQL** mediante **JDBC**, control transaccional atómico (`commit`/`rollback`), manejo jerárquico de excepciones personalizadas y auditoría de eventos con **Apache Log4j 2**.

---

### Arquitectura del Proyecto (Patrón por Capas / DAO)

```
Proyecto-Catedra-POO/
├── .gitignore
├── README.md
├── Proyecto-Catedra-POO.iml
├── database/
│   ├── banco_academico_completo.sql          # Script SQL completo (clientes, cuentas y movimientos)
│   ├── diagrama_clases_fase2.png             # Diagrama de clases UML actualizado (Fase 2)
│   ├── modelo_relacional_fase2.png           # Modelo relacional actualizado (Fase 2)
│   ├── Diagrama bancario 1.drawio            # Diagrama entidad-relación base editable
│   └── Diagrama bancario 1.drawio.png        # Imagen base de Fase 1
├── docs/
│   └── FASE2_Documento_Tecnico_POO901.pdf    # Documento técnico completo exportado (Fase 2)
├── lib/
│   ├── mysql-connector-j-8.3.0.jar           # Conector JDBC para MySQL
│   ├── log4j-api-2.26.1.jar                  # API de Apache Log4j 2
│   └── log4j-core-2.26.1.jar                 # Núcleo de Apache Log4j 2
├── logs/
│   └── banco-academico.log                   # Archivo generado por Log4j 2 en ejecución
└── src/
    ├── Main.java                             # Punto de entrada (Lanza UI principal)
    ├── log4j2.xml                            # Configuración de consola y archivo para Log4j 2
    ├── conexion/
    │   └── Conexion.java                     # Conexión JDBC a MySQL con trazabilidad Log4j 2
    ├── dao/
    │   ├── ClienteDAO.java                   # CRUD completo de Clientes (SQL + JDBC)
    │   ├── CuentaBancariaDAO.java            # CRUD completo de Cuentas Bancarias
    │   ├── TransaccionDAO.java               # Depósitos, retiros, transferencias ACID e historial
    │   └── ReporteDAO.java                   # Consultas y los 8 reportes bancarios de Fase 2
    ├── excepciones/
    │   ├── ValidacionBancariaException.java  # Excepción para validaciones de entrada y formato
    │   ├── TransaccionBancariaException.java # Excepción base de reglas de negocio bancarias
    │   ├── CuentaNoEncontradaException.java  # Cuenta inexistente en BD
    │   ├── CuentaInactivaException.java      # Cuenta no activa (Inactiva o Bloqueada)
    │   ├── SaldoInsuficienteException.java   # Fondos insuficientes en retiro o transferencia
    │   └── ValidadorBanco.java               # Validadores de DUI, teléfono, cuenta, montos e IDs
    ├── modelo/
    │   ├── Cliente.java                      # Entidad Cliente
    │   ├── CuentaBancaria.java               # Entidad CuentaBancaria
    │   ├── MovimientoBancario.java           # Entidad MovimientoBancario (Fase 2)
    │   └── ReporteMovimiento.java            # DTO para reportes multitabla de movimientos
    └── vista/
        ├── MenuPrincipalForm.java            # Menú principal integrado de Fase 2
        ├── ClienteForm.java                  # Ventana de gestión de Clientes (CRUD)
        ├── CuentaBancariaForm.java           # Ventana de gestión de Cuentas Bancarias
        ├── TransaccionesForm.java            # Ventana de Depósitos, Retiros, Transferencias e Historial
        └── ReportesForm.java                 # Ventana de Reportes y Totales en tiempo real
├── web/                                      # Capa de Interfaz Web (Fase 3 - Tomcat 10.1.x)
│   ├── css/
│   │   └── styles.css                        # Estilos responsivos y tema bancario institucional
│   ├── js/
│   │   └── main.js                           # Validaciones cliente, formateo (DUI, teléfono) y confirmaciones
│   ├── index.jsp                             # Enrutador principal según sesión/rol
│   ├── login.jsp                             # Pantalla de inicio de sesión con selección de rol
│   ├── WEB-INF/
│   │   └── web.xml                           # Descriptor de despliegue Jakarta EE 10 (Servlet 6.0)
│   └── jsp/
│       ├── common/                           # Plantillas modulares (navbar, footer, alerts, error 404/500)
│       ├── admin/                            # Dashboard, CRUD Clientes, Cuentas y Reportes
│       ├── cajero/                           # Dashboard, Depósitos, Retiros y Transferencias
│       └── cliente/                          # Portal del Cliente (Mis Cuentas, Movimientos y Transferencias)
```

---

### Requisitos y Configuración de Base de Datos

1. Tener un servidor MySQL activo (puerto por defecto: `3306`).
2. Ejecutar el script SQL ubicado en `database/banco_academico_completo.sql` para crear la base de datos `banco_academico` y las tablas `clientes`, `cuentas_bancarias` y `movimientos`.
3. Por defecto, el usuario es `root`. Define la variable de entorno `DB_PASSWORD` con la contraseña de tu servidor MySQL (en IntelliJ IDEA puede configurarse en la Run Configuration de `Main`).

---

### Cómo Compilar y Ejecutar el Proyecto

#### Desde Línea de Comandos (PowerShell en Windows):
```powershell
# 1. Compilar todas las clases y copiar la configuración de Log4j 2
mkdir -Force bin | Out-Null
$sources = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/*;src" -d bin $sources
Copy-Item src\log4j2.xml bin\log4j2.xml

# 2. Definir contraseña de MySQL (si aplica) y ejecutar
$env:DB_PASSWORD = "1234"
java -cp "bin;lib/*" Main
```

#### Desde IntelliJ IDEA:
1. Abrir la carpeta raíz `Proyecto-Catedra-POO`.
2. Verificar que `src/` esté marcado como **Source Root** (esto incluye `src/log4j2.xml` en el classpath).
3. Verificar que `Proyecto-Catedra-POO.iml` incluya las librerías de `lib/` (`mysql-connector-j-8.3.0.jar`, `log4j-api-2.26.1.jar`, `log4j-core-2.26.1.jar`).
4. Configurar `DB_PASSWORD` si tu usuario `root` tiene contraseña y ejecutar `Main.java`.
