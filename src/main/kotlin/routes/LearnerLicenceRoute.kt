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
import model.User
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

        post{
            val entity = call.receive<LearnerLicence>()
            logbook.insertOne(entity)
            call.respond(HttpStatusCode.Created, entity)
        }


        put{
            val entity = call.receive<LearnerLicence>()
            val result = logbook.updateOne(entity)
            if (result.modifiedCount.toInt() == 1){
                call.respond(HttpStatusCode.OK, entity)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        delete("/{id}"){
            val id = call.parameters["id"].toString()
            val filter = "{_id:ObjectId('$id')}"
            val result = logbook.deleteOne(filter)
            if (result.deletedCount.toInt() == 1) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}


