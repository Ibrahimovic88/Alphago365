import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
    mode: 'history', // Use browser history
    routes: [
        {
            path: "/",
            name: "Root",
            component: () => import("./components/MatchList"),
        },
        {
            path: "/matches",
            name: "MatchList",
            component: () => import("./components/MatchList"),
        },
        {
            path: "/matches/:id",
            name: "Match",
            component: () => import("./components/Match"),
        },
    ]
});

export default router;