FROM quay.io/keycloak/keycloak:23.0.0
USER root
ADD https://github.com/moparisthebest/static-curl/releases/download/v8.7.1/curl-amd64 /usr/bin/curl
RUN chmod +x /usr/bin/curl
USER 1000