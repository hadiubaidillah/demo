import ReactDOM from 'react-dom/client';
import { ChakraProvider } from '@chakra-ui/react';
import { ReactKeycloakProvider } from '@react-keycloak/web';

import LoadingComponent from './components/layout/LoadingComponent';
import App2 from './App2';


ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <ChakraProvider>
    {/*<ReactKeycloakProvider*/}
    {/*    LoadingComponent={<LoadingComponent />}*/}
    {/*    authClient={keycloak}*/}
    {/*    initOptions={{ onLoad: 'login-required', scope: 'openid profile email' }}*/}
    {/*>*/}
      <App2 />
    {/*</ReactKeycloakProvider>*/}
  </ChakraProvider>
);