import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.*
import org.litote.kmongo.*
import routes.learnerLicenceRoute
import routes.userRoute


val client = KMongo.createClient()
val database = client.getDatabase("Logbook")


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.start() {
    install(CORS) {
        allowHost("localhost:3000")
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
    }
    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        jwt {
            realm = "Driving-Licence"
            verifier(JWT
                .require(Algorithm.HMAC256("secret"))
                .withAudience("http://localhost:8080")
                .withIssuer("http://localhost:8080")
                .build()
            )
            validate { token ->   JWTPrincipal(token.payload)
            }
            challenge { defaultScheme, realm ->  call.respond(HttpStatusCode.Unauthorized, "Invalid Token")
            }
        }
    }

    routing {


        userRoute(database)

        authenticate {
            learnerLicenceRoute(database)
        }
    }
    // We are changing comments again again
}