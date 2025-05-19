-- consulta numero 3
SELECT EmpresaAsociada.nombreEmpresa, COUNT(*) AS entregas_fallidas  -- Selecciona el nombre de la empresa y cuenta cuántos pedidos fallidos tiene
FROM OrderEntity                                                     -- Desde la tabla de pedidos (OrderEntity)
JOIN EmpresaAsociada                                                 -- Une con la tabla EmpresaAsociada usando la clave foránea que vincula cada pedido con su empresa
  ON OrderEntity.idEmpresaAsociada = EmpresaAsociada.idEmpresaAsociada
WHERE OrderEntity.estadoPedido ILIKE 'Fallido'                       -- Filtra solo los pedidos cuyo estado sea 'Fallido'
GROUP BY EmpresaAsociada.nombreEmpresa                               -- Agrupa los resultados por nombre de empresa
ORDER BY entregas_fallidas DESC;                                     -- Ordena de mayor a menor según la cantidad de fallos



-- consulta numero 4
SELECT repartidor.nombre AS nombre_repartidor,                       -- Selecciona el nombre del repartidor
       ROUND(                                                        -- Redondea el resultado del promedio a un número entero
         AVG(                                                        -- Calcula el promedio de:
           EXTRACT(EPOCH FROM (OrderEntity.fechaEntrega - OrderEntity.fechaPedido)) / 60  -- Extrae la diferencia de tiempo entre la fecha de entrega y la de pedido en segundos, y lo convierte a minutos
         )
       ) AS tiempo_promedio_minutos
FROM OrderEntity                                                     -- Desde la tabla de pedidos
JOIN repartidor                                                      -- Une con la tabla Repartidor usando la clave foránea correspondiente
  ON OrderEntity.repartidor_id = repartidor.repartidor_id
WHERE OrderEntity.fechaEntrega IS NOT NULL                           -- Asegura que ambos timestamps existan (evita errores por NULL)
  AND OrderEntity.fechaPedido IS NOT NULL
GROUP BY repartidor.nombre
ORDER BY tiempo_promedio_minutos;
