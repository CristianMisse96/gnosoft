/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vo;

/**
 *
 * @author cristian
 */
public class ItemFacturaVO {
    
    private Long id;
    private String articulo;
    private int cantidad;
    private double valorItem;
    private double totalItem;

    public ItemFacturaVO() {
    }

    public ItemFacturaVO(String articulo, int cantidad, double valorItem, double totalItem) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.valorItem = valorItem;
        this.totalItem = totalItem;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorItem() {
        return valorItem;
    }

    public void setValorItem(double valorItem) {
        this.valorItem = valorItem;
    }

    public double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(double totalItem) {
        this.totalItem = totalItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
