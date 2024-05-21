 
-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE USUARIOO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
-- LOGIN
DELIMITER $$
CREATE PROCEDURE sp_login(IN _nombre VARCHAR(20), IN _contrasenia VARCHAR(15))
BEGIN
	SELECT * FROM Usuario
    WHERE nombre = _nombre AND contrasenia = _contrasenia;
END $$
DELIMITER ;

-- CREAR
DELIMITER $$
CREATE PROCEDURE sp_crear_usuario 
(
    IN _nombre VARCHAR(20),
    IN _apellido VARCHAR(20),
    IN _puesto VARCHAR(20),
    IN _contrasenia VARCHAR(15)
)
BEGIN
    INSERT INTO Usuario (nombre, apellido, puesto, contrasenia)
    VALUES (_nombre, _apellido, _puesto, _contrasenia);
END $$
DELIMITER ;


-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
-- CREAR
DELIMITER $$
CREATE PROCEDURE sp_crear_cliente 
(
    IN _nit INT,
    IN _nombre VARCHAR(20),
    IN _apellido VARCHAR(20),
    IN _email VARCHAR(30),
    IN _telefono VARCHAR(45),
    IN _direccion VARCHAR(100)
)
BEGIN
    INSERT INTO Cliente (nit, nombre, apellido, email, telefono, direccion)
    VALUES (_nit, _nombre, _apellido, _email, _telefono, _direccion);
END $$
DELIMITER ;

-- LISTAR
DELIMITER $$
CREATE PROCEDURE sp_listar_clientes()
BEGIN
    SELECT * FROM Cliente;
END $$
DELIMITER ;

-- BUSCAR
DELIMITER $$
CREATE PROCEDURE sp_buscar_cliente(IN _nit INT)
BEGIN
    SELECT * FROM Cliente
    WHERE nit = _nit;
END $$
DELIMITER ;

-- ACTUALIZAR
DELIMITER $$
CREATE PROCEDURE sp_actualizar_cliente 
(
    IN _nit INT,
    IN _nombre VARCHAR(20),
    IN _apellido VARCHAR(20),
    IN _email VARCHAR(30),
    IN _telefono VARCHAR(45),
    IN _direccion VARCHAR(100)
)
BEGIN
    UPDATE Cliente
    SET
        nombre = _nombre,
        apellido = _apellido,
        email = _email,
        telefono = _telefono,
        direccion = _direccion
    WHERE
        nit = _nit;
END $$
DELIMITER ;

-- ELIMINAR
DELIMITER $$
CREATE PROCEDURE sp_eliminar_cliente(IN _nit INT)
BEGIN
    DELETE FROM Cliente
    WHERE nit = _nit;
END $$
DELIMITER ;

-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE EMPLEADOS CON CARGO UNIDOS */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

-- CREAR
DELIMITER $$
CREATE PROCEDURE sp_crear_empleado_con_cargo 
(
    IN _nombre_empleado VARCHAR(50),
    IN _apellido_empleado VARCHAR(50),
    IN _sueldo DECIMAL(10,2),
    IN _direccion VARCHAR(150),
    IN _turno VARCHAR(15),
    IN _nombre_cargo VARCHAR(45),
    IN _descripcion_cargo VARCHAR(45)
)
BEGIN
    DECLARE cargo_id INT;
    
    -- Insertar en CargoEmpleado y obtener el ID del nuevo cargo
    INSERT INTO CargoEmpleado (nombre, descripcion)
    VALUES (_nombre_cargo, _descripcion_cargo);
    
    SET cargo_id = LAST_INSERT_ID();
    
    -- Insertar en Empleado con el ID del cargo
    INSERT INTO Empleado (nombre, apellido, sueldo, direccion, turno, cargo)
    VALUES (_nombre_empleado, _apellido_empleado, _sueldo, _direccion, _turno, cargo_id);
END $$
DELIMITER ;

-- LISTAR
DELIMITER $$
CREATE PROCEDURE sp_listar_empleados_con_cargo()
BEGIN
    SELECT e.id, e.nombre, e.apellido, e.sueldo, e.direccion, e.turno, c.nombre as cargo, c.descripcion
    FROM Empleado e
    INNER JOIN CargoEmpleado c ON e.cargo = c.id;
END $$
DELIMITER ;

-- BUSCAR
DELIMITER $$
CREATE PROCEDURE sp_buscar_empleado_con_cargo(IN _id INT)
BEGIN
    SELECT e.id, e.nombre, e.apellido, e.sueldo, e.direccion, e.turno, c.nombre as cargo, c.descripcion
    FROM Empleado e
    INNER JOIN CargoEmpleado c ON e.cargo = c.id
    WHERE e.id = _id;
END $$
DELIMITER ;

-- ACTUALIZAR
DELIMITER $$
CREATE PROCEDURE sp_actualizar_empleado_con_cargo 
(
    IN _id INT,
    IN _nombre_empleado VARCHAR(50),
    IN _apellido_empleado VARCHAR(50),
    IN _sueldo DECIMAL(10,2),
    IN _direccion VARCHAR(150),
    IN _turno VARCHAR(15),
    IN _nombre_cargo VARCHAR(45),
    IN _descripcion_cargo VARCHAR(45)
)
BEGIN
    DECLARE cargo_id INT;
    
    -- Actualizar o insertar en CargoEmpleado y obtener el ID del cargo
    SELECT id INTO cargo_id FROM CargoEmpleado WHERE nombre = _nombre_cargo;
    
    IF cargo_id IS NULL THEN
        -- Insertar nuevo cargo si no existe
        INSERT INTO CargoEmpleado (nombre, descripcion)
        VALUES (_nombre_cargo, _descripcion_cargo);
        
        SET cargo_id = LAST_INSERT_ID();
    ELSE
        -- Actualizar descripción del cargo si existe
        UPDATE CargoEmpleado SET descripcion = _descripcion_cargo WHERE id = cargo_id;
    END IF;
    
    -- Actualizar Empleado con el ID del cargo
    UPDATE Empleado
    SET
        nombre = _nombre_empleado,
        apellido = _apellido_empleado,
        sueldo = _sueldo,
        direccion = _direccion,
        turno = _turno,
        cargo = cargo_id
    WHERE
        id = _id;
END $$
DELIMITER ;

-- ELIMINAR
DELIMITER $$
CREATE PROCEDURE sp_eliminar_empleado_con_cargo(IN _id INT)
BEGIN
    DELETE FROM Empleado WHERE id = _id;
END $$
DELIMITER ;

-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE PROVEEDOR CON EMAIL Y TELEFONO UNIDOS */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

-- CREAR
DELIMITER $$
CREATE PROCEDURE sp_crear_proveedor_con_contacto 
(
    IN _nit INT,
    IN _nombre VARCHAR(60),
    IN _apellido VARCHAR(60),
    IN _direccion VARCHAR(150),
    IN _razon_social VARCHAR(20),
    IN _contacto VARCHAR(100),
    IN _web VARCHAR(100),
    IN _email VARCHAR(50),
    IN _email_descripcion VARCHAR(100),
    IN _numero_principal VARCHAR(50),
    IN _numero_secundario VARCHAR(50),
    IN _telefono_observaciones VARCHAR(100)
)
BEGIN
	DECLARE proveedor_id INT;
    
    -- Insertar en Proveedor y obtener el ID del nuevo proveedor
    INSERT INTO Proveedor (nit, nombre, apellido, direccion, razonSocial, contacto, web)
    VALUES (_nit, _nombre, _apellido, _direccion, _razon_social, _contacto, _web);
    
    SET proveedor_id = LAST_INSERT_ID();
    
    -- Insertar en EmailProveedor
    INSERT INTO EmailProveedor (email, Descripcion, proveedor)
    VALUES (_email, _email_descripcion, proveedor_id);
    
    -- Insertar en TelefonoProveedor
    INSERT INTO TelefonoProveedor (numeroP, numeroS, Observaciones, proveedor)
    VALUES (_numero_principal, _numero_secundario, _telefono_observaciones, proveedor_id);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listar_proveedores_con_contacto()
BEGIN
    SELECT p.id, p.nit, p.nombre, p.apellido, p.direccion, p.razonSocial, p.contacto, p.web, 
           e.email, e.descripcion AS descripcion_email, 
           t.numeroP AS numero_principal, t.numeroS AS numero_secundario, t.Observaciones AS telefono_observaciones
    FROM Proveedor p
    INNER JOIN EmailProveedor e ON p.id = e.proveedor
    INNER JOIN TelefonoProveedor t ON p.id = t.proveedor;
END $$
DELIMITER ;


-- BUSCAR
DELIMITER $$
CREATE PROCEDURE sp_buscar_proveedor_con_contacto(IN _id INT)
BEGIN
    SELECT p.id, p.nit, p.nombre, p.apellido, p.direccion, p.razonSocial, p.contacto, p.web, e.email, e.Descripcion AS email_descripcion, t.numeroP AS numero_principal, t.numeroS AS numero_secundario, t.Observaciones AS telefono_observaciones
    FROM Proveedor p
    INNER JOIN EmailProveedor e ON p.id = e.proveedor
    INNER JOIN TelefonoProveedor t ON p.id = t.proveedor
    WHERE p.id = _id;
END $$
DELIMITER ;

-- ACTUALIZAR
DELIMITER $$
CREATE PROCEDURE sp_actualizar_proveedor_con_contacto 
(
    IN _id INT,
    IN _nit INT,
    IN _nombre VARCHAR(60),
    IN _apellido VARCHAR(60),
    IN _direccion VARCHAR(150),
    IN _razon_social VARCHAR(20),
    IN _contacto VARCHAR(100),
    IN _web VARCHAR(100),
    IN _email VARCHAR(50),
    IN _email_descripcion VARCHAR(100),
    IN _numero_principal VARCHAR(50),
    IN _numero_secundario VARCHAR(50),
    IN _telefono_observaciones VARCHAR(100)
)
BEGIN
    -- Actualizar Proveedor
    UPDATE Proveedor
    SET
        nit = _nit,
        nombre = _nombre,
        apellido = _apellido,
        direccion = _direccion,
        razonSocial = _razon_social,
        contacto = _contacto,
        web = _web
    WHERE
        id = _id;
    
    -- Actualizar EmailProveedor
    UPDATE EmailProveedor
    SET
        email = _email,
        Descripcion = _email_descripcion
    WHERE
        proveedor = _id;
    
    -- Actualizar TelefonoProveedor
    UPDATE TelefonoProveedor
    SET
        numeroP = _numero_principal,
        numeroS = _numero_secundario,
        Observaciones = _telefono_observaciones
    WHERE
        proveedor = _id;
END $$
DELIMITER ;

-- ELIMINAR
DELIMITER $$
CREATE PROCEDURE sp_eliminar_proveedor_con_contacto(IN _id INT)
BEGIN
    -- Eliminar correos electrónicos asociados al proveedor
    DELETE FROM EmailProveedor WHERE proveedor = _id;

    -- Eliminar números de teléfono asociados al proveedor
    DELETE FROM TelefonoProveedor WHERE proveedor = _id;

    -- Finalmente, eliminar al proveedor
    DELETE FROM Proveedor WHERE id = _id;
END $$
DELIMITER ;



-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE PRODUCTO CON TIPO PRODUCTO UNIDOS */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

-- PROCEDIMIENTO PARA CREAR PRODUCTO CON TIPO
DELIMITER $$
CREATE PROCEDURE sp_crear_producto_con_tipo 
(
    IN _descripcion_producto VARCHAR(45),
    IN _precio_unitario DECIMAL(10,2),
    IN _precio_docena DECIMAL(10,2),
    IN _precio_mayorista DECIMAL(10,2),
    IN _imagen VARCHAR(45),
    IN _existencia INT,
    IN _nombre_proveedor VARCHAR(60),
    IN _nombre_tipo_producto VARCHAR(45)
)
BEGIN
    DECLARE proveedor_id INT;
    DECLARE tipo_producto_id INT;
    
    -- Obtener ID del proveedor
    SELECT id INTO proveedor_id FROM Proveedor WHERE nombre = _nombre_proveedor;
    
    -- Obtener ID del tipo de producto o insertar si no existe
    INSERT INTO TipoProducto (Descripcion) VALUES (_nombre_tipo_producto)
    ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id);
    
    SET tipo_producto_id = LAST_INSERT_ID();
    
    -- Insertar en Producto
    INSERT INTO Producto (Descripcion, precioU, precioD, precioM, imagenP, existencia, proveedor, tipo)
    VALUES (_descripcion_producto, _precio_unitario, _precio_docena, _precio_mayorista, _imagen, _existencia, proveedor_id, tipo_producto_id);
END $$
DELIMITER ;

-- PROCEDIMIENTO PARA ACTUALIZAR PRODUCTO CON TIPO
DELIMITER $$
CREATE PROCEDURE sp_actualizar_producto_con_tipo 
(
    IN _id INT,
    IN _descripcion_producto VARCHAR(45),
    IN _precio_unitario DECIMAL(10,2),
    IN _precio_docena DECIMAL(10,2),
    IN _precio_mayorista DECIMAL(10,2),
    IN _imagen VARCHAR(45),
    IN _existencia INT,
    IN _nombre_proveedor VARCHAR(60),
    IN _nombre_tipo_producto VARCHAR(45)
)
BEGIN
    DECLARE proveedor_id INT;
    DECLARE tipo_producto_id INT;
    
    -- Obtener ID del proveedor
    SELECT id INTO proveedor_id FROM Proveedor WHERE nombre = _nombre_proveedor;
    
    -- Obtener ID del tipo de producto o insertar si no existe
    INSERT INTO TipoProducto (Descripcion) VALUES (_nombre_tipo_producto)
    ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id);
    
    SET tipo_producto_id = LAST_INSERT_ID();
    
    -- Actualizar Producto
    UPDATE Producto
    SET
        Descripcion = _descripcion_producto,
        precioU = _precio_unitario,
        precioD = _precio_docena,
        precioM = _precio_mayorista,
        imagenP = _imagen,
        existencia = _existencia,
        proveedor = proveedor_id,
        tipo = tipo_producto_id
    WHERE
        id = _id;
END $$
DELIMITER ;

-- PROCEDIMIENTO PARA ELIMINAR PRODUCTO CON TIPO
DELIMITER $$
CREATE PROCEDURE sp_eliminar_producto_con_tipo(IN _id INT)
BEGIN
    DECLARE tipo_producto_id INT;
    DECLARE count_products INT;
    
    -- Obtener el ID del tipo de producto asociado al producto que se va a eliminar
    SELECT tipo INTO tipo_producto_id FROM Producto WHERE id = _id;

    -- Eliminar el producto
    DELETE FROM Producto WHERE id = _id;

    -- Contar cuántos productos quedan asociados al tipo de producto
    SELECT COUNT(*) INTO count_products FROM Producto WHERE tipo = tipo_producto_id;

    -- Si no hay más productos asociados al tipo de producto, eliminar el tipo de producto también
    IF count_products = 0 THEN
        DELETE FROM TipoProducto WHERE id = tipo_producto_id;
    END IF;
END $$
DELIMITER ;


-- LISTAR
DELIMITER $$
CREATE PROCEDURE sp_listar_productos_con_tipo()
BEGIN
    SELECT p.id, p.Descripcion, p.precioU, p.precioD, p.precioM, p.imagenP, p.existencia, pv.nombre AS proveedor, tp.Descripcion AS tipo_producto
    FROM Producto p
    INNER JOIN Proveedor pv ON p.proveedor = pv.id
    INNER JOIN TipoProducto tp ON p.tipo = tp.id;
END $$
DELIMITER ;

-- BUSCAR
DELIMITER $$
CREATE PROCEDURE sp_buscar_producto_con_tipo(IN _id INT)
BEGIN
    SELECT p.id, p.Descripcion, p.precioU, p.precioD, p.precioM, p.imagenP, p.existencia, pv.nombre AS proveedor, tp.Descripcion AS tipo_producto
    FROM Producto p
    INNER JOIN Proveedor pv ON p.proveedor = pv.id
    INNER JOIN TipoProducto tp ON p.tipo = tp.id
    WHERE p.id = _id;
END $$
DELIMITER ;


-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE COMPRA CON DETLLA COMPRA UNIDOS */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

DELIMITER $$
CREATE PROCEDURE sp_crear_compra_con_detalle 
(
    IN _fecha DATE,
    IN _descripcion VARCHAR(60),
    IN _total DECIMAL(10,2),
    IN _producto_id INT,
    IN _cantidad INT,
    IN _costo_unitario DECIMAL(10,2)
)
BEGIN
    DECLARE compra_id INT;
    
    -- Insertar en Compra y obtener el ID de la nueva compra
    INSERT INTO Compra (fecha, descripcion, total)
    VALUES (_fecha, _descripcion, _total);
    
    SET compra_id = LAST_INSERT_ID();
    
    -- Insertar en DetalleCompra
    INSERT INTO DetalleCompra (costoU, cantidad, producto, compra)
    VALUES (_costo_unitario, _cantidad, _producto_id, compra_id);
END $$
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_listar_compras_con_detalle()
BEGIN
    -- Seleccionar información de las compras junto con sus detalles
    SELECT 
        c.id AS id_compra, 
        c.fecha, 
        c.descripcion AS descripcion_compra, 
        c.total,
        dc.id AS id_detalle_compra,
        dc.costoU,
        dc.cantidad,
        p.descripcion AS nombre_producto  -- Corregir el nombre de la columna aquí
    FROM Compra c
    INNER JOIN DetalleCompra dc ON c.id = dc.compra
    INNER JOIN Producto p ON dc.producto = p.id;
END $$

DELIMITER ;


DELIMITER ;
;

-- BUSCAR
DELIMITER $$
CREATE PROCEDURE sp_buscar_compra_con_detalle(IN _id INT)
BEGIN
    SELECT c.id, c.fecha, c.descripcion, c.total, GROUP_CONCAT(CONCAT(p.Descripcion, ' (', dc.cantidad, ')') SEPARATOR ', ') AS detalle_productos
    FROM Compra c
    INNER JOIN DetalleCompra dc ON c.id = dc.compra
    INNER JOIN Producto p ON dc.producto = p.id
    WHERE c.id = _id
    GROUP BY c.id;
END $$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_actualizar_compra 
(
    IN _id INT,
    IN _fecha DATE,
    IN _descripcion VARCHAR(60),
    IN _total DECIMAL(10,2),
    IN _costoU DECIMAL(10,2),
    IN _cantidad INT,
    IN _producto INT
)
BEGIN
    -- Actualizar la tabla Compra
    UPDATE Compra
    SET
        fecha = _fecha,
        descripcion = _descripcion,
        total = _total
    WHERE
        id = _id;

    -- Actualizar la tabla DetalleCompra
    UPDATE DetalleCompra
    SET
        costoU = _costoU,
        cantidad = _cantidad,
        producto = _producto
    WHERE
        compra = _id;
END $$

DELIMITER ;


-- ELIMINAR
DELIMITER $$
CREATE PROCEDURE sp_eliminar_compra(IN _id INT)
BEGIN
    DECLARE tipo_producto_id INT;
    DECLARE count_compras INT;
    
    -- Obtener el ID del tipo de producto asociado a la compra que se va a eliminar
    SELECT tipo INTO tipo_producto_id FROM Producto WHERE id = (SELECT producto FROM DetalleCompra WHERE compra = _id LIMIT 1);

    -- Eliminar la compra
    DELETE FROM Compra WHERE id = _id;

    -- Contar cuántas compras quedan asociadas al tipo de producto
    
    SELECT COUNT(*) INTO count_compras FROM DetalleCompra WHERE producto IN (SELECT id FROM Producto WHERE tipo = tipo_producto_id);

    -- Si no hay más compras asociadas al tipo de producto, eliminar el tipo de producto también
    IF count_compras = 0 THEN
        DELETE FROM TipoProducto WHERE id = tipo_producto_id;
    END IF;
END $$
DELIMITER ;

-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* PROCEDIMIENTOS ALMACENADOS DE FACTURA CON DETALLE FACTURA UNIDOS */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
DELIMITER $$
CREATE PROCEDURE sp_crear_factura_con_detalle 
(
    IN _estado VARCHAR(50),
    IN _total DECIMAL(10,2),
    IN _fecha VARCHAR(45),
    IN _cliente_nit INT,
    IN _empleado_id INT,
    IN _producto_id INT,
    IN _cantidad INT,
    IN _precio_unitario DECIMAL(10,2)
)
BEGIN
    DECLARE factura_id INT;
    
    -- Insertar en Factura y obtener el ID de la nueva factura
    INSERT INTO Factura (estado, total, fecha, cliente, empleado)
    VALUES (_estado, _total, _fecha, _cliente_nit, _empleado_id);
    
    SET factura_id = LAST_INSERT_ID();
    
    -- Insertar en DetalleFactura
    INSERT INTO DetalleFactura (precioU, cantidad, factura, producto)
    VALUES (_precio_unitario, _cantidad, factura_id, _producto_id);
END $$
DELIMITER ;


-- LISTAR
DELIMITER $$
CREATE PROCEDURE sp_listar_facturas_con_detalle()
BEGIN
    SELECT f.id, f.estado, f.total, f.fecha, c.nit AS cliente_nit, CONCAT(c.nombre, ' ', c.apellido) AS cliente_nombre, e.nombre AS empleado_nombre, e.apellido AS empleado_apellido, GROUP_CONCAT(CONCAT(p.Descripcion, ' (', df.cantidad, ')') SEPARATOR ', ') AS detalle_productos
    FROM Factura f
    INNER JOIN Cliente c ON f.cliente = c.nit
    INNER JOIN Empleado e ON f.empleado = e.id
    INNER JOIN DetalleFactura df ON f.id = df.factura
    INNER JOIN Producto p ON df.producto = p.id
    GROUP BY f.id;
END $$
DELIMITER ;

-- BUSCAR
DELIMITER $$
CREATE PROCEDURE sp_buscar_factura_con_detalle(IN _id INT)
BEGIN
    SELECT f.id, f.estado, f.total, f.fecha, c.nit AS cliente_nit, CONCAT(c.nombre, ' ', c.apellido) AS cliente_nombre, e.nombre AS empleado_nombre, e.apellido AS empleado_apellido, GROUP_CONCAT(CONCAT(p.Descripcion, ' (', df.cantidad, ')') SEPARATOR ', ') AS detalle_productos
    FROM Factura f
    INNER JOIN Cliente c ON f.cliente = c.nit
    INNER JOIN Empleado e ON f.empleado = e.id
    INNER JOIN DetalleFactura df ON f.id = df.factura
    INNER JOIN Producto p ON df.producto = p.id
    WHERE f.id = _id
    GROUP BY f.id;
END $$
DELIMITER ;

-- actualizar
DELIMITER $$
CREATE PROCEDURE sp_actualizar_factura 
(
    IN _id INT,
    IN _estado VARCHAR(50),
    IN _total DECIMAL(10,2),
    IN _fecha VARCHAR(45),
    IN _cliente_nit INT,
    IN _empleado_id INT,
    IN _producto_id INT,
    IN _cantidad INT,
    IN _precio_unitario DECIMAL(10,2)
)
BEGIN
    -- Actualizar la información de la factura
    UPDATE Factura
    SET
        estado = _estado,
        total = _total,
        fecha = _fecha,
        cliente = _cliente_nit,
        empleado = _empleado_id
    WHERE
        id = _id;
    
    -- Eliminar los detalles de factura asociados a esta factura
    DELETE FROM DetalleFactura WHERE factura = _id;
    
    -- Insertar los nuevos detalles de factura
    INSERT INTO DetalleFactura (precioU, cantidad, factura, producto)
    VALUES (_precio_unitario, _cantidad, _id, _producto_id);
END $$
DELIMITER ;


-- ELIMINAR (Incluyendo la eliminación de DetalleFactura)
DELIMITER $$
CREATE PROCEDURE sp_eliminar_factura(IN _id INT)
BEGIN
    -- Eliminar el detalle de la factura
    DELETE FROM DetalleFactura WHERE factura = _id;

    -- Eliminar la factura
    DELETE FROM Factura WHERE id = _id;
END $$
DELIMITER ;





-- //*/*/*/*/*/*/*/*/*/*/*/*/*/*/* CALL */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
-- Llamados a procedimientos almacenados
CALL sp_crear_cliente(123456789, 'Juan', 'Pérez', 'juan@example.com', '1234567890', 'Calle 123, Ciudad');
CALL sp_listar_clientes();
CALL sp_buscar_cliente(123456789);
CALL sp_actualizar_cliente(123456789, 'Juan', 'Pérez', 'nuevo_email@example.com', '9876543210', 'Nueva dirección');

CALL sp_crear_empleado_con_cargo('Juan', 'Pérez', 1500.00, 'Calle 123', 'Matutino', 'Gerente', 'Responsable de la gestión del personal');
CALL sp_listar_empleados_con_cargo();
CALL sp_buscar_empleado_con_cargo(1);
CALL sp_actualizar_empleado_con_cargo(1, 'Juan', 'Pérez', 1700.00, 'Nueva Dirección', 'Vespertino', 'Gerente', 'Responsable de la gestión del personal');

CALL sp_crear_proveedor_con_contacto(123456789, 'Nombre', 'ApellidoProveedor', 'DirecciónProveedor', 'RazónSocial', 'ContactoProveedor', 'www.proveedor.com', 'proveedor@example.com', 'DescripciónEmail', '123456789', '987654321', 'ObservacionesTeléfono');
CALL sp_listar_proveedores_con_contacto();
CALL sp_buscar_proveedor_con_contacto(1);
CALL sp_actualizar_proveedor_con_contacto(1, 987654321, 'NombreProveedorActualizado', 'ApellidoProveedorActualizado', 'NuevaDirecciónProveedor', 'NuevaRazónSocial', 'NuevoContactoProveedor', 'www.proveedoractualizado.com', 'proveedoractualizado@example.com', 'NuevaDescripciónEmail', '987654321', '123456789', 'NuevasObservaciones');

CALL sp_crear_producto_con_tipo('Descripción del producto', 10.99, 15.99, 20.99, 'imagen.jpg', 100, 'NombreProveedorActualizado', 'TipoProducto');
CALL sp_listar_productos_con_tipo();
CALL sp_buscar_producto_con_tipo(1);
CALL sp_actualizar_producto_con_tipo(1, 'Nueva descripción', 12.99, 17.99, 22.99, 'nueva_imagen.jpg', 150, 'NuevoProveedor', 'NuevoTipoProducto');

CALL sp_crear_compra_con_detalle('2024-05-08', 'Descripción de la compra', 100.50, 1, 5, 10.25);
CALL sp_listar_compras_con_detalle();
CALL sp_buscar_compra_con_detalle(1);
CALL sp_actualizar_compra(1, '2024-05-09', 'Nueva descripción de la compra', 150.75, 10.5, 1, 1);

CALL sp_crear_factura_con_detalle('Estado de la factura', 250.75, '2024-05-08', 123456789, 1, 1, 1, 10.25);
CALL sp_listar_facturas_con_detalle();
CALL sp_buscar_factura_con_detalle(1);
CALL sp_actualizar_factura(1, 'Nuevo estado', 150.25, '2024-05-08', 123456789, 1, 1, 5, 10.25);

CALL sp_crear_usuario('a', 'Perez', 'Administrador', '1');


set global time_zone = '-6:00';

-- Funciones

DELIMITER $$

CREATE FUNCTION CalcularPreciosProductos(total DECIMAL(10,2), cantidad INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE precio DECIMAL(10,2);
    SET precio = total / cantidad;
    RETURN precio;
END ;

CREATE FUNCTION CalcularExistenciaProductos(idProducto INT, cantidad INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE existenciaActual INT;
    SELECT existencia INTO existenciaActual FROM Producto WHERE id = idProducto;
    SET existenciaActual = existenciaActual + cantidad;
    RETURN existenciaActual;
END $$

DELIMITER ;


-- Triggers

DELIMITER $$

CREATE TRIGGER TriggerDetalleCompra AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
    DECLARE totalCompra DECIMAL(10,2);
    DECLARE cantidadCompra INT;
    DECLARE idProducto INT;

    SELECT costoU, cantidad, producto INTO totalCompra, cantidadCompra, idProducto FROM DetalleCompra WHERE id = NEW.id;

    -- Actualizar precios de productos
    UPDATE Producto
    SET precioU = CalcularPreciosProductos(totalCompra, cantidadCompra),
        precioD = CalcularPreciosProductos(totalCompra * 12, cantidadCompra * 12),
        precioM = CalcularPreciosProductos(totalCompra * 20, cantidadCompra * 20)
    WHERE id = idProducto;

    -- Actualizar existencia de productos
    UPDATE Producto
    SET existencia = CalcularExistenciaProductos(idProducto, cantidadCompra)
    WHERE id = idProducto;
END $$

CREATE TRIGGER TriggerDetalleFactura BEFORE INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
    DECLARE idProducto INT;

    SELECT producto INTO idProducto FROM DetalleFactura WHERE id = NEW.id;

    -- Obtener precio unitario del producto
    SELECT precioU INTO NEW.precioU FROM Producto WHERE id = idProducto;
END ;

CREATE TRIGGER TriggerTotalDocumento AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
    DECLARE totalDocumento DECIMAL(10,2);
    DECLARE idCompra INT;

    SELECT compra INTO idCompra FROM DetalleCompra WHERE id = NEW.id;

    -- Calcular total del documento
    SELECT SUM(costoU * cantidad) INTO totalDocumento FROM DetalleCompra WHERE compra = idCompra;

    -- Actualizar total del documento en Compra
    UPDATE Compra
    SET total = totalDocumento
    WHERE id = idCompra;
END ;

CREATE TRIGGER TriggerTotalFactura AFTER INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
    DECLARE totalFactura DECIMAL(10,2);
    DECLARE idFactura INT;

    SELECT factura INTO idFactura FROM DetalleFactura WHERE id = NEW.id;

    -- Calcular total de la factura
    SELECT SUM(precioU * cantidad) INTO totalFactura FROM DetalleFactura WHERE factura = idFactura;

    -- Actualizar total de la factura en Factura
    UPDATE Factura
    SET total = totalFactura
    WHERE id = idFactura;
END 

DELIMITER ;

