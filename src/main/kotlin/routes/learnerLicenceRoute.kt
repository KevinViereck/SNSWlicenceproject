package routes

import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.LearnerLicence
import org.litote.kmongo.*


fun Route.learnerLicenceRoute (db: MongoDatabase) {
    val logbook = db.getCollection<LearnerLicence>("logbook")

    route("/logbook") {

        get{
            val data = logbook.find().toList()
            call.respond(data)
        }

        get("/email"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId").toString().replace("\"", "")
            val filter = "{userId:ObjectId('$userId')}"
            val username = logbook.findOne(filter)
            if (username != null) {
                call.respond(username)
            } else {
                call.respond((HttpStatusCode.NotFound))
            }
        }

        put{

            // to do update when the remaining hours are equal to or more than 120 hours
            // to issue P plate
            val entity = call.receive<LearnerLicence>()
            val result = logbook.updateOne(entity)
            if (result.modifiedCount.toInt() == 1){
                call.respond(HttpStatusCode.OK, entity)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

    }
}


