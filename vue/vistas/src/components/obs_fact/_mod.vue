<template>
    <h2>EDITAR FACTURA</h2>
    <router-link :to="{ name: 'facturas' }" class="btn btn-info btn-sm float-end">
        Volver
    </router-link>

    <!-- Campos de la factura -->
    <form @submit.prevent="submitFactura" class="mt-5">
      <div class="mb-3 row">
        <label for="nombreCliente" class="col-2 col-form-label">Nombre Cliente:</label>
        <div class="col-sm-6">
            <input type="text" class="form-control" id="nombreCliente" v-model="factura.nombreCliente" required>
        </div>
      </div>

      <div class="mb-3 row">
        <label for="subtotal" class="col-2 col-form-label">Subtotal:</label>
        <div class="col-sm-6">
            <input type="number" readonly class="form-control" id="subtotal" v-model.number="factura.subtotal" required>
        </div>
      </div>

      <div class="mb-3 row">
        <label for="iva" class="col-2 col-form-label">IVA:</label>
        <div class="col-sm-6">
            <input type="number" readonly class="form-control" id="iva" v-model.number="factura.iva" required>
        </div>
      </div>

      <div class="mb-3 row">
        <label for="total" class="col-2 col-form-label">Total:</label>
        <div class="col-sm-6">
            <input type="number" readonly class="form-control" id="total" v-model.number="factura.total" required>
        </div>
      </div>

      <!-- Botón para agregar item -->
      <button type="button" class="btn btn-primary float-end mb-3" @click="addItem">Agregar Item</button>
      <!-- Tabla de items -->
      <table class="table table-striped table-hover table-bordered">
        <thead>
          <tr>
            <th>Artículo</th>
            <th>Cantidad</th>
            <th>Valor por Item</th>
            <th>Total del Item</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in factura.items" :key="index">
            <td><input type="text" class="form-control" v-model="item.articulo" required></td>
            <td><input type="number" class="form-control" v-model.number="item.cantidad" @input="calculateTotalItem(item)" required></td>
            <td><input type="number" min="1" class="form-control" v-model.number="item.valorItem" @input="calculateTotalItem(item)" required></td>
            <td><input type="number" readonly class="form-control" v-model.number="item.totalItem" required></td>
            <td><button type="button" class="btn btn-danger" @click="removeItem(index)">Eliminar</button></td>
          </tr>
        </tbody>
      </table>

    
      <!-- Botón para enviar la factura -->
      <button type="submit" class="btn btn-secondary float-end">Editar Factura</button>
    </form>
</template>

<script lang="ts" setup>
import { IFactura } from '@/interfaces/IFactura';
import FacturaService from '@/services/FacturaService';
import { onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {Items} from '@/interfaces/Items';
import { useToast } from 'vue-toast-notification';

    const service= new FacturaService();
    const route= useRoute();
    const toast= useToast();
    const router= useRouter();
    const elm= Number(route.params.id);

     // Datos de la factura
     const factura: IFactura = reactive({
      subtotal: 0,
      iva: 0,
      total: 0,
      items: [],
      nombreCliente: '',
    });
    
    // Función para agregar un nuevo item
    const addItem = () => {
      factura.items.push({
        articulo: '',
        cantidad: 0,
        valorItem: 0,
        totalItem: 0,
      });
    };

    // Función para eliminar un item
    const removeItem = (index: number) => {
      factura.items.splice(index, 1);
      calculateTotals();
    };

    // Función para calcular el total del item
    const calculateTotalItem = (item: Items) => {
      if (item.cantidad > 0 && item.valorItem > 0) {
        item.totalItem = item.cantidad * item.valorItem;
      }
      calculateTotals();
    };

     // Función para calcular el subtotal, IVA y total
     const calculateTotals = () => {
        factura.subtotal = factura.items.reduce((total, item) => total + item.totalItem, 0);
        factura.iva = factura.subtotal * 0.19;
        factura.total = factura.subtotal + factura.iva;
    };

    onMounted(async () => {
        
        try {
            const response = await service.fetchById(elm);

            if (response.ok) {
                factura.nombreCliente= response.negocio.nombreCliente;
                factura.subtotal = response.negocio.subtotal;
                factura.iva= response.negocio.iva;
                factura.total= response.negocio.total;
                factura.numeroFactura=response.negocio.numeroFactura;

                // Mapeo de items
                factura.items = response.negocio.items.map((item:any):Items =>({
                    id: item.id,
                    articulo: item.articulo,
                    cantidad: item.cantidad,
                    valorItem: item.valorItem,
                    totalItem: item.totalItem
                }));
                
            } else {
                throw new Error(response.mensaje);
            }
        } catch (error: any) {
            toast.error(error.message || "Error al editar la factura");
            router.push({ name: "facturas" });
        }
    })

    
    const submitFactura = async() => {
        debugger;
        try {
            const response = await service.updateFactura(factura,elm);
            if (response.ok) {
                toast.success(response.mensaje);
                router.push({ name: "facturas" });
            }else{
                throw new Error(response.mensaje);
            }
        } catch (error:any) {
            toast.error(error.message || "Error al editar la factura");
        }
    }
</script>

    
<style>
    
</style>