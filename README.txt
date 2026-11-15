NOMBRE DE LA APLICACIÓN Y AUTORES
- Aplicación: Calculadora de Presupuestos
- Autores: Javier Fernández Ramos y Iván Herranz Rizo

QUÉ PERMITE HACER Y CÓMO SE UTILIZA
La aplicación permite calcular el coste total de distintos planes o gastos y guardar un listado de los resultados durante la sesión, mostrando además el número total de planes y el presupuesto acumulado.
1. En la pantalla principal se muestra el resumen acumulado (0 planes y 0.00 € al inicio) y la lista de planes guardados.
2. Pulsa el botón flotante (+) para abrir la pantalla de creación de un nuevo plan.
3. Introduce el nombre del plan, el coste base por unidad (€), la cantidad/personas y selecciona la categoría del gasto en el Spinner.
4. La aplicación calcula en tiempo real el total estimado aplicando el porcentaje de recargo/IVA asociado a la categoría elegida.
5. Pulsa "Guardar" para validar y añadir el plan al listado principal, o "Cancelar" (o botón Atrás) para volver sin añadir ningún dato.

DÓNDE SE CUMPLE CADA REQUISITO TÉCNICO
1. Al menos dos actividades:
   - MainActivity.java (pantalla principal con resumen y listado dinámico).
   - CrearItemActivity.java (pantalla para introducir datos, calcular presupuesto y devolverlo).
2. Controles de interacción (EditText, botones y Spinner con función real):
   - activity_crear_item.xml y CrearItemActivity.java: campos txtNombrePlan, txtCosteBase, txtCantidad, botones btnCancelar y btnGuardar, y Spinner spCategoria.
   - El Spinner selecciona la categoría del gasto e influye directamente en el cálculo aplicando su porcentaje de recargo/IVA (0%, 10%, 15% o 21%).
3. Modelo de datos propio:
   - modelos/Presupuesto.java: implementa Serializable, tiene 6 atributos (nombrePlan, categoria, costeBase, cantidad, porcentajeRecargo, costeTotal), constructor, getters y setters.
4. Intercambio de datos entre actividades (Intent + ActivityResultLauncher):
   - MainActivity.java usa ActivityResultLauncher<Intent> (launcherCrear) para abrir CrearItemActivity y recibir el objeto Presupuesto en RESULT_OK.
   - CrearItemActivity.java devuelve el objeto mediante Bundle + Intent con setResult(RESULT_OK, intent) al guardar, o setResult(RESULT_CANCELED) al cancelar.
5. Colección de objetos:
   - MainActivity.java gestiona listaPresupuestos (ArrayList<Presupuesto>).
6. Vistas creadas dinámicamente:
   - res/layout/item_fila.xml representa cada elemento visual.
   - MainActivity.java (método mostrarElementos) limpia contenedorMain con removeAllViews() para evitar duplicados, recorre listaPresupuestos, infla item_fila.xml con LayoutInflater, rellena sus TextViews y los añade al LinearLayout dentro de ScrollView (content_main.xml).
7. Recursos de texto:
   - res/values/strings.xml define todos los textos fijos de la interfaz y el array categorias_presupuesto del Spinner.

COMPROBACIÓN DEL FUNCIONAMIENTO
1. Abrir la aplicación y comprobar estado inicial:
   - Se muestra el resumen "Planes: 0  |  Presupuesto total: 0.00 €" y el texto indicando que no hay presupuestos guardados.
2. Incorporar al menos tres objetos a la colección:
   - Se añaden tres planes distintos (ej. Ocio al 0%, Alimentación al +10% y Viaje al +15%), comprobando que se muestran en la lista con su formato y se suma su importe al presupuesto total acumulado.
3. Cancelar una operación o regresar sin confirmarla:
   - Al pulsar "Cancelar" o el botón Atrás del sistema en CrearItemActivity, regresa a MainActivity mostrando "Operación cancelada" sin incorporar ningún elemento indebido.
4. Navegar entre actividades y comprobar actualización sin duplicados:
   - Al añadir sucesivos planes, se conservan los anteriores en la lista, se añade el nuevo al final sin duplicar filas (gracias a removeAllViews()) y el total general se recalcula correctamente.
