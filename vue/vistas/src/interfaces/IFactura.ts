import { Items } from "./Items";

export interface IFactura {
    
    id?:number
    subtotal: number;
    iva: number;
    total: number;
    items: Items[];
    fecha?: Date;
    numeroFactura?:number;
    nombreCliente: string;
}
