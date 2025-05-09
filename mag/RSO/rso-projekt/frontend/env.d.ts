/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string;
  // more env variables...
  readonly VITE_BACKEND_URL: string;
  readonly VITE_BACKEND_RABBITMQ: string;
  readonly VITE_RABBITMQ_USER: string;
  readonly VITE_RABBITMQ_PASS: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
