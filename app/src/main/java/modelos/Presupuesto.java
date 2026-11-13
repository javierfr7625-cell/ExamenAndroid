package modelos;

import java.io.Serializable;

/**
 * Modelo de datos que representa un presupuesto calculado para un plan.
 */
public class Presupuesto implements Serializable {
    private static final long serialVersionUID = 1L;

    // Índices alineados con @array/categorias_presupuesto en strings.xml
    private static final double[] RECARGOS_POR_POSICION = {0.0, 0.0, 10.0, 15.0, 21.0, 21.0};

    private String nombrePlan;
    private String categoria;
    private double costeBase;
    private int cantidad;
    private double porcentajeRecargo;
    private double costeTotal;

    public Presupuesto(String nombrePlan, String categoria, double costeBase, int cantidad, double porcentajeRecargo) {
        this.nombrePlan = nombrePlan;
        this.categoria = categoria;
        this.costeBase = costeBase;
        this.cantidad = cantidad;
        this.porcentajeRecargo = porcentajeRecargo;
        this.costeTotal = calcularCosteTotal(costeBase, cantidad, porcentajeRecargo);
    }

    /**
     * Calcula el coste total aplicando cantidad y recargo porcentual de la categoría.
     */
    public static double calcularCosteTotal(double costeBase, int cantidad, double porcentajeRecargo) {
        double subtotal = costeBase * cantidad;
        return subtotal * (1.0 + (porcentajeRecargo / 100.0));
    }

    /**
     * Devuelve el porcentaje de recargo asociado a la posición del Spinner de categorías.
     */
    public static double obtenerPorcentajePorCategoria(int posicion) {
        if (posicion >= 0 && posicion < RECARGOS_POR_POSICION.length) {
            return RECARGOS_POR_POSICION[posicion];
        }
        return 0.0;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getCosteBase() {
        return costeBase;
    }

    public void setCosteBase(double costeBase) {
        this.costeBase = costeBase;
        this.costeTotal = calcularCosteTotal(this.costeBase, this.cantidad, this.porcentajeRecargo);
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.costeTotal = calcularCosteTotal(this.costeBase, this.cantidad, this.porcentajeRecargo);
    }

    public double getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public void setPorcentajeRecargo(double porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
        this.costeTotal = calcularCosteTotal(this.costeBase, this.cantidad, this.porcentajeRecargo);
    }

    public double getCosteTotal() {
        return costeTotal;
    }

    public void setCosteTotal(double costeTotal) {
        this.costeTotal = costeTotal;
    }

    @Override
    public String toString() {
        return "Presupuesto{" +
                "nombrePlan='" + nombrePlan + '\'' +
                ", categoria='" + categoria + '\'' +
                ", costeBase=" + costeBase +
                ", cantidad=" + cantidad +
                ", porcentajeRecargo=" + porcentajeRecargo +
                ", costeTotal=" + costeTotal +
                '}';
    }
}
