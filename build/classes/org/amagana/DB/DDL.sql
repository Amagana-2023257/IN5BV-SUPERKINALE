drop database if exists SuperKinal;

create database if not exists SuperKinal;

-- Activar el uso del predeterminado de la base de datos
USE SuperKinal;

-- */*/*/*/*/*/*/*/*/*/* ENTIDAD CLIENTES */**/*/*/*/*/*/*/*/*/*/*
-- Crear la tabla de Clientes
CREATE TABLE Cliente (
	nit INT,
	nombre VARCHAR(20),
    apellido VARCHAR(20),
    email VARCHAR(30),
    telefono VARCHAR(45),
    direccion VARCHAR(100),
    PRIMARY KEY(nit)
) ENGINE=InnoDB;

-- */*/*/*/*/*/*/*/*/*/* ENTIDADES EMPLEADOS */**/*/*/*/*/*/*/*/*/*/*

-- Crear la tabla de CargoEmpleado
CREATE TABLE CargoEmpleado (
	id INT auto_increment,
    nombre varchar(45),
    descripcion varchar(45),
    PRIMARY KEY(id)
) ENGINE=InnoDB;

-- Crear la tabla de Empleado
CREATE TABLE Empleado (
	id INT auto_increment,
    nombre varchar(50),
    apellido varchar(50),
    sueldo decimal(10,2),
    direccion varchar(150),
    turno varchar(15),
    cargo int, 
    CONSTRAINT fk_CargoE_Empleado FOREIGN KEY (cargo) REFERENCES CargoEmpleado(id),
    PRIMARY KEY(id)
) ENGINE=InnoDB;

-- */*/*/*/*/*/*/*/*/*/* ENTIDADES PROVEEDOR */**/*/*/*/*/*/*/*/*/*/*

-- Se crea la entidad Proveedor
CREATE TABLE Proveedor(
	id INT AUTO_INCREMENT,
    nit int,
    nombre VARCHAR(60),
    apellido VARCHAR(60),
    direccion VARCHAR(150),
    razonSocial VARCHAR(20),
    contacto VARCHAR(100),
    web VARCHAR(100),
	PRIMARY KEY(id)
)ENGINE=InnoDB;

-- Se crea la entidad Email Proveedor
CREATE TABLE EmailProveedor( 
	id INT AUTO_INCREMENT,
    email VARCHAR(50),
    descripcion VARCHAR(100),
    proveedor int,
	PRIMARY KEY(id),
    CONSTRAINT fk_Email_Proveedor FOREIGN KEY (proveedor) REFERENCES Proveedor(id)
)ENGINE=InnoDB;

-- Se crea la entidad Telefono Proveedor
CREATE TABLE TelefonoProveedor( 
	id INT AUTO_INCREMENT,
    numeroP VARCHAR(50),
    numeroS VARCHAR(50),
    Observaciones VARCHAR(100),
    proveedor int,
	PRIMARY KEY(id),
    CONSTRAINT fk_Telefono_Proveedor FOREIGN KEY (proveedor) REFERENCES Proveedor(id)
)ENGINE=InnoDB;

-- */*/*/*/*/*/*/*/*/*/* ENTIDADES PRODUCTO */**/*/*/*/*/*/*/*/*/*/*

-- Se crea la entidad Tipo Producto
CREATE TABLE TipoProducto( 
	id INT AUTO_INCREMENT,
    Descripcion VARCHAR(45),
	PRIMARY KEY(id)
)ENGINE=InnoDB;

-- Se crea la entidad Producto
CREATE TABLE Producto( 
	id INT AUTO_INCREMENT,
    Descripcion VARCHAR(45),
    precioU DECIMAL(10,2),
    precioD DECIMAL(10,2),
    precioM DECIMAL(10,2),
    imagenP VARCHAR(45),
    existencia int,
    proveedor int,
    tipo int,
	PRIMARY KEY(id),
    CONSTRAINT fk_Producto_Proveedor FOREIGN KEY (proveedor) REFERENCES Proveedor(id),
    CONSTRAINT fk_Producto_Tipo FOREIGN KEY (tipo) REFERENCES TipoProducto(id)
)ENGINE=InnoDB;

-- */*/*/*/*/*/*/*/*/*/* ENTIDADES COMPRAS  */**/*/*/*/*/*/*/*/*/*/*

-- Crear la entidad compra
CREATE TABLE Compra (
	id INT auto_increment,
    fecha date,
    descripcion varchar(60),
    total decimal(10,2),
    PRIMARY KEY(id)
) ENGINE=InnoDB;

-- Crear la entidad Detalle Compra
CREATE TABLE DetalleCompra (
	id INT auto_increment,
    costoU decimal(10,2),
    cantidad int,
    producto int,
    compra int,
    PRIMARY KEY(id), 
    CONSTRAINT fk_DCompra_Compra FOREIGN KEY (compra) REFERENCES Compra(id),
    CONSTRAINT fk_DCompra_Producto FOREIGN KEY (producto) REFERENCES Producto(id)
) ENGINE=InnoDB;

-- */*/*/*/*/*/*/*/*/*/* ENTIDADES FACTURAS  */**/*/*/*/*/*/*/*/*/*/*

-- Crear la entidad Factura
CREATE TABLE Factura (
	id INT auto_increment,
    estado varchar(50),
    total decimal(10,2), 
    fecha varchar(45),
    cliente int,
    empleado int,
    PRIMARY KEY(id), 
    CONSTRAINT fk_Factura_Cliente FOREIGN KEY (cliente) REFERENCES Cliente(nit),
    CONSTRAINT fk_Factura_Empleado FOREIGN KEY (empleado) REFERENCES Empleado(id)
) ENGINE=InnoDB;


-- Crear la entidad  Detalle Factura
CREATE TABLE DetalleFactura (
	id INT auto_increment,
    precioU decimal(10,2), 
    cantidad int,
    factura int,
    producto int,
    PRIMARY KEY(id), 
    CONSTRAINT fk_DFactura_Factura FOREIGN KEY (factura) REFERENCES Factura(id),
    CONSTRAINT fk_DFactura_Producto FOREIGN KEY (producto) REFERENCES Producto(id)
) ENGINE=InnoDB;

-- Crear la tabla de Usuarios
CREATE TABLE Usuario (
	id INT AUTO_INCREMENT,
	nombre VARCHAR(20),
    apellido VARCHAR(20),
    puesto VARCHAR(20),
    contrasenia VARCHAR(15),
    PRIMARY KEY(id)
) ENGINE=InnoDB;

