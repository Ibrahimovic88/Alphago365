import Vue from 'vue'
import App from './App.vue'
import router from './routes'

Vue.config.productionTip = false

Vue.filter('toPrettyDateTime', function(value) {
	var date = new Date(value.toString())
	var month = (date.getMonth() + 1)
	var day = date.getDate()
	var hours = date.getHours()
	if (hours.toString().length < 2) {
		hours = hours + '0'
	}
	var minutes = date.getMinutes()
	if (minutes.toString().length < 2) {
		minutes = minutes + '0'
	}
	return ''.concat(month)
		.concat('/')
		.concat(day)
		.concat(' ')
		.concat(hours)
		.concat(':').concat(minutes)
})

new Vue({
	router,
	render: h => h(App),
}).$mount('#app')
