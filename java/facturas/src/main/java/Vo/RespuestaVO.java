/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author cristian
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaVO<T> {
    
        @JsonAlias
	private T negocio;
	private String mensaje;
	private boolean ok;
        private int status;
        
    public RespuestaVO(T negocio, String mensaje, boolean ok, int status) {
        this.negocio = negocio;
        this.mensaje = mensaje;
        this.ok = ok;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RespuestaVO() {
    }
    
    public T getNegocio() {
        return negocio;
    }

    public void setNegocio(T negocio) {
        this.negocio = negocio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
        
        
}
