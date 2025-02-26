import Keycloak from 'keycloak-js';

export const keycloak = new Keycloak();

//const GATEWAY_ADDRESS = "http://dev.hadiubaidillah.localhost"
//const GATEWAY_ADDRESS = "https://dev.hadiubaidillah.my.id"
//const GATEWAY_ADDRESS = "https://gateway.hadiubaidillah.my.id"
const GATEWAY_ADDRESS = "http://www.hadiubaidillah.local/api"
//const GATEWAY_ADDRESS = "http://0.0.0.0:48100"

class ResponseStatusError extends Error {
  constructor(message: string/*, private status: number, private statusText?: string*/) {
    super(message);
  }
}

async function simplefetch(path: string, init: RequestInit) {
  const request = fetch(`${GATEWAY_ADDRESS}${path}`, {
    ...init,
    headers: {
      ...init?.headers,
      'Authorization': `Bearer ${keycloak.token}`,
      'Content-Type': 'application/json'
    }
  });

  return request.then(async (response) => {
    if (response.status >= 200 
      && response.status < 400) {
      return response
    }
    if (response.status === 401) {
      throw new ResponseStatusError("User is not authenticated."/*, response.status, response.statusText*/);
    }
  
    const json = await response.json() as Record<string, any>
    if (!json?.message) {
      throw new ResponseStatusError("An error occurred while making a request"/*, response.status, response.statusText*/)
    }
  
    throw new ResponseStatusError(json.message/*, response.status, response.statusText*/);
  })
}

async function fetcher<T>(path: string) {
  return simplefetch(path, {}).then(response => response.json() as T);
}

async function poster<T>(path: string, args: any) {
  return simplefetch(path, {
    method: 'POST',
    body: JSON.stringify(args?.arg || args),
  }).then(response => response.text())
  .then(value => value.length == 0 ? undefined : JSON.parse(value) as T);
}

function customRequester(init: RequestInit) {
  async function requester<T>(path: string, args?: any) {
    const response = await simplefetch(path, {
      ...init,
      body: JSON.stringify(args?.arg || args)
    });

    const text = await response.text()
    return text.length == 0 ? undefined : JSON.parse(text) as T
  }

  return requester;
}

export { fetcher, poster, customRequester }