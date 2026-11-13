package javier.fernandez.examen;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import modelos.Presupuesto;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {
    @Test
    public void calcularCosteTotal_aplicaCantidadYRecargoCorrectamente() {
        double total = Presupuesto.calcularCosteTotal(50.0, 2, 21.0);
        assertEquals(121.0, total, 0.001);
    }

    @Test
    public void obtenerPorcentajePorCategoria_devuelveRecargoEsperado() {
        assertEquals(0.0, Presupuesto.obtenerPorcentajePorCategoria(0), 0.001);
        assertEquals(0.0, Presupuesto.obtenerPorcentajePorCategoria(1), 0.001);
        assertEquals(10.0, Presupuesto.obtenerPorcentajePorCategoria(2), 0.001);
        assertEquals(15.0, Presupuesto.obtenerPorcentajePorCategoria(3), 0.001);
        assertEquals(21.0, Presupuesto.obtenerPorcentajePorCategoria(4), 0.001);
        assertEquals(21.0, Presupuesto.obtenerPorcentajePorCategoria(5), 0.001);
    }

    @Test
    public void presupuestoModel_actualizaCosteTotalAlModificarCampos() {
        Presupuesto p = new Presupuesto("Hotel", "Viaje (+15% tasa)", 100.0, 2, 15.0);
        assertEquals(230.0, p.getCosteTotal(), 0.001);

        p.setCantidad(3);
        assertEquals(345.0, p.getCosteTotal(), 0.001);

        p.setPorcentajeRecargo(10.0);
        assertEquals(330.0, p.getCosteTotal(), 0.001);
    }

    @Test
    public void presupuestoModel_esSerializable() throws Exception {
        Presupuesto original = new Presupuesto("Cena", "Alimentación (+10% IVA)", 25.0, 4, 10.0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }

        Presupuesto deserializado;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            deserializado = (Presupuesto) ois.readObject();
        }

        assertEquals(original.getNombrePlan(), deserializado.getNombrePlan());
        assertEquals(original.getCategoria(), deserializado.getCategoria());
        assertEquals(original.getCosteBase(), deserializado.getCosteBase(), 0.001);
        assertEquals(original.getCantidad(), deserializado.getCantidad());
        assertEquals(original.getCosteTotal(), deserializado.getCosteTotal(), 0.001);
    }
}
