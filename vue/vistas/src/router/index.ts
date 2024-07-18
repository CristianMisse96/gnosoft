import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router"
import _lis from "@/components/obs_fact/_lis.vue"
import _agr from "@/components/obs_fact/_agr.vue"
import _mod from "@/components/obs_fact/_mod.vue"

const routes : Array<RouteRecordRaw> = [
    {
        path: '/',
        redirect: to=> {return {name: 'facturas'}}
    },
    {
        path: '/facturas',
        name: 'facturas',
        component:_lis
    },
    {
        path: '/add/factura',
        name: 'add',
        component:_agr
    },
    {
        path: '/factura/:id',
        name: 'mod',
        component:_mod
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router