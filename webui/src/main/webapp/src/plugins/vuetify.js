import Vue from 'vue'
import Vuetify from 'vuetify/lib'
import 'vuetify/src/styles/main.sass'
//import 'vuetify/src/stylus/app.styl'
Vue.use(Vuetify)

export default new Vuetify({
    icons: {
        iconfont: 'md', // default - only for display purposes
    },
    theme: {
        dark: false
    }
})