-- Salsas Base con todos los campos obligatorios
INSERT INTO productos (nombre, descripcion, stock, precio, activo, categoria, nivel_picor) 
VALUES ('Habanero Infierno', 'Extra picante para valientes', 50, 4500, true, 'Salsas', 5);

INSERT INTO productos (nombre, descripcion, stock, precio, activo, categoria, nivel_picor) 
VALUES ('Jalapeño Verde', 'Picante suave y fresco', 100, 3800, true, 'Salsas', 2);

INSERT INTO productos (nombre, descripcion, stock, precio, activo, categoria, nivel_picor) 
VALUES ('Rocoto Ahumado', 'Sabor profundo con picor medio', 30, 4200, true, 'Salsas', 3);

-- Recetas para probar el "Muro de Pago" de la Fase 2
INSERT INTO recetas (nombre, salsa, dificultad, tiempo, descripcion, ingredientes, instrucciones) 
VALUES ('Tacos de Pescado Jalapeño', 'Jalapeño Verde', 'Media', '30 min', 'Tacos frescos con un toque verde.', 'Pescado blanco, Tortillas, Repollo, Jalapeño La Chefa', '1. Fríe el pescado.\n2. Arma el taco.\n3. Agrega abundante salsa.');