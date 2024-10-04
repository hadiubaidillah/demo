import React, { useEffect, useState } from "react";
//import keycloak from './keycloak'
import axios from "axios";
import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "https://keycloak.hadiubaidillah.com",
    realm: "hadi",
    clientId: "todo-client",
});


function App2() {
    const [authenticated, setAuthenticated] = useState(false);

    useEffect(() => {
        keycloak.init({ onLoad: "login-required" }).then(authenticated => {
            console.log("authenticated: ", authenticated);
            setAuthenticated(authenticated);
            if (authenticated) {
                // Set Authorization header for all axios requests
                axios.defaults.headers.common["Authorization"] = `Bearer ${keycloak.token}`;
            }
        }).catch((e) => {
            console.log("Failed to initialize Keycloak: ", e);
        });

        // Refresh token periodically
        setInterval(() => {
            keycloak.updateToken(70).catch(() => keycloak.logout());
            console.log("Token refreshed: ", keycloak.tokenParsed);
        }, 60000);
    }, []);

    if (!authenticated) {
        return <div>Loading...</div>;
    }
    console.log(keycloak);
    console.log(keycloak.token);
    console.log(keycloak.tokenParsed);
    return (
        <div>
            <h1>{keycloak.token}</h1><br/>
            <h1>Welcome {keycloak.tokenParsed?.preferred_username}</h1>
            {/*<h1>Welcome {keycloak.tokenParsed}</h1>*/}
            <button onClick={() =>
                axios.get('https://www.hadiubaidillah.com/account')
                    .then(function (response) {
                        // handle success
                        console.log(response);
                    })
                    .catch(function (error) {
                        // handle error
                        console.log(error);
                    })
                    .finally(function () {
                        // always executed
                    })
            }>Account
            </button>
            <br/>
            <button onClick={() => keycloak.logout()}>Logout</button>
        </div>
    );
}

export default App2;