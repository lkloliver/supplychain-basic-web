import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

const app = createApp(App)

// 注册 Ant Design Vue
app.use(Antd)
app.use(router)

app.mount('#app') 