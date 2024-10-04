import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: "https://keycloak.hadiubaidillah.com",
    realm: "hadi",
    clientId: "hadi-id",
});

export default keycloak;