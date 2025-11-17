import axios, {
  AxiosInstance,
  InternalAxiosRequestConfig,
} from 'axios';
import { API_BASE } from './config';

const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error: unknown): Promise<never> => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
