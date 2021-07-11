import axios from 'axios'

const http = axios.create()

http.interceptors.response.use(data => data, (error): Promise<unknown> => {
  if (error.response?.status === 401) {
    window.location.reload()
  }
  return Promise.reject(error)
})

export default http
