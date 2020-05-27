import Vue from "vue";
import VueRouter from "vue-router";
import MatchList from "../components/MatchList.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: MatchList
  },
  {
    path: "/matches",
    name: "MatchList",
    component: MatchList
  },
  {
    path: "/matches/:id",
    name: "Match",
    component: () => import("../components/Match.vue")
  },
  {
    path: "/matches/:id/handicap",
    name: "HandicapList",
    component: () => import("../components/HandicapList.vue")
  },
  {
    path: "/matches/:id/handicap/:providerId",
    name: "Handicap",
    component: () => import("../components/Handicap.vue")
  },
  {
    path: "/matches/:id/odds",
    name: "OddsList",
    component: () => import("../components/OddsList.vue")
  },
  {
    path: "/matches/:id/odds/:providerId",
    name: "Odds",
    component: () => import("../components/Odds.vue")
  },
  {
    path: "/matches/:id/overunder",
    name: "OverunderList",
    component: () => import("../components/OverunderList.vue")
  },
  {
    path: "/matches/:id/overunder/:providerId",
    name: "Overunder",
    component: () => import("../components/Overunder.vue")
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
