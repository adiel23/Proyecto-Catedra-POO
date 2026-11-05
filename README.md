# Proyecto de Cátedra POO901 - Sistema de Gestión Bancaria Académica

## Banco Académico UDB - Fase 1

### Descripción
Aplicación Desktop en Java desarrollada bajo el paradigma de **Programación Orientada a Objetos (POO)** con interfaz gráfica en **Java Swing**, persistencia de datos en **MySQL** y conectividad mediante **JDBC**.

---

### Arquitectura del Proyecto (Patrón por Capas / DAO)

```
Proyecto-Catedra-POO/
├── .gitignore
├── README.md
├── database/
│   └── banco_academico_completo.sql      # Script de creación de BD y tablas
├── lib/
│   └── mysql-connector-j-8.3.0.jar       # Conector JDBC para MySQL
└── src/
    ├── Main.java                         # Punto de entrada (Lanza UI)
    ├── conexion/
    │   └── Conexion.java                 # Conexión JDBC a MySQL con manejo de env var
    ├── dao/
    │   ├── ClienteDAO.java               # CRUD completo de Clientes (SQL + JDBC)
    │   └── CuentaBancariaDAO.java        # CRUD completo de Cuentas Bancarias
    ├── excepciones/
    │   ├── ValidacionBancariaException.java # Excepción personalizada de negocio
    │   └── ValidadorBanco.java           # Validaciones de DUI, saldo, textos y formato
    ├── modelo/
    │   ├── Cliente.java                  # Clase modelo Cliente (Encapsulamiento)
    │   └── CuentaBancaria.java           # Clase modelo Cuenta Bancaria
    └── vista/
        ├── MenuPrincipalForm.java        # Dashboard principal con accesos y estado
        ├── ClienteForm.java              # Ventana de gestión de Clientes (CRUD + JTable)
        └── CuentaBancariaForm.java       # Ventana de gestión de Cuentas (CRUD + JTable)
```

---

### Requisitos y Configuración de Base de Datos

1. Tener un servidor MySQL activo (puerto por defecto: `3306`).
2. Ejecutar el script SQL ubicado en `database/banco_academico_completo.sql`.
3. Por defecto, el usuario es `root`. Si tu MySQL tiene contraseña, puedes definir la variable de entorno `DB_PASSWORD` o ajustar la propiedad en `src/conexion/Conexion.java`.

---

### Cómo Ejecutar el Proyecto

#### Desde Línea de Comandos:
```bash
# 1. Compilar todas las clases
javac -cp "lib/mysql-connector-j-8.3.0.jar;src" -d bin src/Main.java src/modelo/*.java src/dao/*.java src/conexion/*.java src/excepciones/*.java src/vista/*.java

# 2. Ejecutar la aplicación
java -cp "bin;lib/mysql-connector-j-8.3.0.jar" Main
```

#### Desde tu IDE preferido (IntelliJ IDEA, NetBeans, Eclipse, VS Code):
1. Abrir la carpeta raíz `Proyecto-Catedra-POO`.
2. Asegurarse de que `src/` esté marcado como **Source Root**.
3. Agregar la librería `lib/mysql-connector-j-8.3.0.jar` como dependencia de bibliotecas.
4. Ejecutar la clase `Main.java`.
