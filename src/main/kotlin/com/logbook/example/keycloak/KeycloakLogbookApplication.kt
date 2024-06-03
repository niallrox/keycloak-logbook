package com.logbook.example.keycloak

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.ClientBuilderWrapper
import org.keycloak.admin.client.JacksonProvider
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.zalando.logbook.Logbook
import org.zalando.logbook.core.DefaultStrategy
import org.zalando.logbook.jaxrs.LogbookClientFilter
import javax.net.ssl.SSLContext

@SpringBootApplication
class KeycloakLogbookApplication

fun main(args: Array<String>) {
    runApplication<KeycloakLogbookApplication>(*args)
}


@RestController
class KeycloakController {

    private lateinit var realmResource: RealmResource

    init {
        val clientBuilder = ClientBuilderWrapper.create(SSLContext.getDefault(), false) as ResteasyClientBuilder
        clientBuilder.register(JacksonProvider::class.java, 100)
        val client = clientBuilder.build()
        client.register(
            LogbookClientFilter(
                Logbook.builder()
                    .strategy(DefaultStrategy())
                    .build()
            )
        )
        val realm = "surdo"
        val keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:8081")
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId("backend")
            .clientSecret("rMxHuPOmiaFDiAaSG1SvbzntYhCC1mut")
            .resteasyClient(client)
            .build()

        realmResource = keycloak.realm(realm)
    }

    @GetMapping("/trigger")
    fun trigger(): List<UserRepresentation> {
        return realmResource.users().list()
    }
}