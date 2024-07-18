import { IFactura } from "@/interfaces/IFactura";
import { ref, Ref } from "vue";

class FacturaService{
    
    private facturas:Ref<Array<IFactura>>;
   
    constructor() {
        this.facturas= ref<Array<IFactura>>([]);
    }

    getFacturas():Ref<Array<IFactura>>{
        return this.facturas
    }

    async fetchAll(): Promise<void>{
        try {
            debugger;
            const url= "http://localhost:8080/api/factura";
            const response= await fetch(url);
            const json= await response.json();
            this.facturas.value = json.negocio.map((factura: any) => ({
                ...factura,
                fecha:new Date(factura.fecha)
              }));
              console.log(this.facturas);
        } catch (error) {
            console.log(error);
        }
    }

    async guardarFactura(factura: IFactura): Promise<any> {
        debugger;
        const url = "http://localhost:8080/api/factura";
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(factura),
        });
            
        return response.json();
       
    }

    async fetchById(id:number): Promise<any>{
        const url = `http://localhost:8080/api/factura/${id}`;
        const response= await fetch(url);

        return response.json();
    }

    async updateFactura(factura: IFactura, id: number): Promise<any> {
        const url = `http://localhost:8080/api/factura/${id}`;

        const response= await fetch(url,{
            method:"PUT",
            headers:{
                "Content-Type": "application/json",
            },
            body: JSON.stringify(factura)
        });

        return response.json();
    }

    async borrarFactura(id: number | undefined): Promise<any>  {
        const url = `http://localhost:8080/api/factura/${id}`;
        const response= await fetch(url,{
            method:"DELETE",
        });

        return response.json();
    }

}


export default FacturaService