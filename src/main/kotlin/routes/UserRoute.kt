package routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.User
import model.UserDTO
import org.litote.kmongo.*
import org.mindrot.jbcrypt.BCrypt
import java.util.Date


fun Route.userRoute (db: MongoDatabase) {
    val user = db.getCollection<User>("user")

    route("/user") {

        post("/register"){
            val data = call.receive<User>()
            val hashed= BCrypt.hashpw(data.password,BCrypt.gensalt())
            val newUser = User(data.firstName,data.lastName, data.email,data.mobile, password=hashed, roles = listOf("customer"))
            user.insertOne(newUser)
            call.respond(HttpStatusCode.Created)
        }


        post("/login"){

            val principal = call.principal<JWTPrincipal>()
            val data= call.receive<UserDTO>()

            val filter = "{email:/^${data.email}\$/i}"
            val newUser = user.findOneById(filter)

            if(newUser == null){
                return@post call.respond(HttpStatusCode.BadRequest)
            }

            val valid = BCrypt.checkpw(data.password,newUser.password)
            if(!valid){
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            val userid = principal?.payload?.getClaim("id").toString()
            val expiry = Date(System.currentTimeMillis() +86400000)
            val token = JWT.create()
                .withAudience("http://localhost:8080")
                .withIssuer("http://localhost:8080")
                .withClaim("id",newUser?._id.toString())
                .withClaim("roles",newUser?.roles)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256("secret"))

            return@post call.respond(token)




        }

    }
}

