package routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.LearnerLicence
import model.User

import model.UserDTOLogin
import org.litote.kmongo.*
import org.litote.kmongo.util.idValue
import org.mindrot.jbcrypt.BCrypt
import java.util.Date


fun getJWT(user:User):String{

    val expiry = Date(System.currentTimeMillis() +86400000)
    return JWT.create()
        .withAudience("http://localhost:8080")
        .withIssuer("http://localhost:8080")
        .withClaim("email",user.email)
        .withClaim("roles",user.roles)
        .withClaim("userId",user._id.toString())
        .withExpiresAt(expiry)
        .sign(Algorithm.HMAC256("secret"))
}

fun Route.userRoute (db: MongoDatabase) {
    val user = db.getCollection<User>("user")
    val licences = db.getCollection<LearnerLicence>("logbook")

    route("/user") {

        post("/register"){
            val data = call.receive<User>()
            val hashed= BCrypt.hashpw(data.password,BCrypt.gensalt())
            val newUser = User(
                data.firstName,
                data.lastName,
                data.email,
                data.dateOfBirth,
                data.mobile,
                password=hashed,
                roles = listOf("customer"))
            user.insertOne(newUser)
            val licence = LearnerLicence(
                email=newUser.email,
                userId = newUser._id,
            )

            licences.insertOne(licence)

//            added general licence in UserRouts.kt
//            line 51-56 to give every user individual licence numbers in
//            the backend of things


            val token = getJWT(newUser)
            call.respond(HttpStatusCode.Created, token)
        }


        post("/login"){
            val data= call.receive<User>()

            val filter = "{email:/^${data.email}\$/i}"
            val newUser = user.findOne(filter)

            if(newUser == null){
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            val valid = BCrypt.checkpw(data.password,newUser.password)
            if(!valid) {
                return@post call.respond(HttpStatusCode.BadRequest)
            }
            val expiry = Date(System.currentTimeMillis() + 86400000)
            val token = JWT.create()
                .withAudience("http://localhost:8080")
                .withIssuer("http://localhost:8080")
                .withClaim("email",newUser?.email)
                .withClaim("roles",newUser?.roles)
                .withClaim( "userId", newUser?._id.toString() )
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256("secret"))
            val loginDTO= UserDTOLogin(
                email = newUser.email,
                roles = newUser.roles,
                token= token,
                authenticated = true,
            )

            return@post call.respond(loginDTO)
//
//            val token = getJWT(newUser)
//            return@post call.respond(token)

        }

    }
}

