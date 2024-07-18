<template>
  <h1>LISTADO DE FACTURAS</h1>
  <router-link :to="{ name: 'add' }" class="btn btn-success btn-sm float-end">
    Crear factura
  </router-link>
  
  <table v-if="facturas.length" class="table table-striped table-hover table-bordered my-5">
    <thead>
      <tr>
        <th>Nro.Factura</th>
        <th>Cliente</th>
        <th>Fecha</th>
        <th>SubTotal</th>
        <th>Iva</th>
        <th>Total</th>
        <th>Editar</th>
        <th>Eliminar</th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="factura in facturas" :key="factura.id">
        <td>{{ factura.numeroFactura }}</td>
        <td>{{ factura.nombreCliente }}</td>
        <td>{{factura.fecha.toLocaleDateString()}}</td>
        <td>{{ factura.subtotal }}</td>
        <td>{{ factura.iva }}</td>
        <td>{{ factura.total }}</td>
        <td style="width: 50px;">
          <router-link :to="{ name: 'mod', params:{id: factura.id}}" class="btn btn-primary btn-sm">
            Editar
          </router-link>
        </td>
        <td style="width: 50px;">
          <center>
            <button class="btn btn-danger btn-sm" @click="deleteFactura(factura)">X</button>
          </center>
        </td>
      </tr>
    </tbody>
  </table>

  <div v-else class="alert alert-info mt-5">
    No hay Facturas registradas!
  </div>
  
</template>

<script lang="ts" setup>
    import FacturaService from '@/services/FacturaService'
    import {onMounted} from 'vue'
    import Swal from 'sweetalert2';
    import { IFactura } from '@/interfaces/IFactura';
import { useRouter } from 'vue-router';
    
    const service= new FacturaService();
    const facturas= service.getFacturas();
    const router= useRouter();
    
    onMounted(async () => {
        await service.fetchAll();
    })

    const deleteFactura=(factura: IFactura)=>{
      Swal.fire({
        title: "Borrar Factura!!!",
        text: `Esta a punto de borrar la factura numero ${factura.numeroFactura}`,
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "si, eliminarlo!"
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            const response= await service.borrarFactura(factura.id);
            if (response.ok) {
              Swal.fire({
                title: "Factura eliminada",
                text: `La factura numero ${factura.numeroFactura} ha sido eliminado correctamente`,
                icon: "success"
              }).then(() => {
                  router.go(0);
              });
            } else {
              throw new Error(response.mensaje);
            }
          } catch (error: any) {
            Swal.fire({
              icon: "error",
              title: "Oops...",
              text: `${error.mensaje}`,
              
            }).then(() => {
                router.go(0);
            });
          }
          
        }
      });
    }
    
</script>

<style>
    
</style>