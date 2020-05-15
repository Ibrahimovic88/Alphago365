import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
    mode: 'history', // Use browser history
    routes: [
        {
            path: "/",
            name: "Root",
            component: () => import("./components/Matches"),
        },
        {
            path: "/matches",
            name: "Matches",
            component: () => import("./components/Matches"),
        },
        {
            path: "/matches/:id",
            name: "Match",
            component: () => import("./components/Match"),
        },
    ]
});

export default router;