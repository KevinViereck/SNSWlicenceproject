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
import model.LogEntries
import model.LogEntriesDTO
import org.litote.kmongo.*
import model.toDTO
import org.litote.kmongo.id.IdGenerator


fun Route.logEntries(db: MongoDatabase) {
    val loghours = db.getCollection<LogEntries>("loghours")

    route("/loghours") {

        get("/{id}") {

            val principal = call.principal<JWTPrincipal>()
            val id = principal?.payload?.getClaim("licenceId").toString().replace("\"", "")
            val filter = "{objectId:/^${id}$/i}"
            val logEntries = loghours.find(filter).toList()
            call.respond(logEntries.map { it.toDTO() })
//            val logEntries = loghours.find().toList()
//            call.respond(logEntries)
        }

        get {
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.payload?.getClaim("licenceId").toString().replace("\"", "")
            val filter = "{licenceId:ObjectId('$id')}"
            val matchingLogHours = loghours.find(filter).toList()
            call.respond(matchingLogHours)


        }

        post {
            val logEntries = call.receive<LogEntriesDTO>()
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.payload?.getClaim("licenceId").toString().replace("\"", "")

            val logEntry = LogEntries(
                licenceId = IdGenerator.defaultGenerator.create(id) as Id<LearnerLicence>,
                startTime = logEntries.startTime,
                endTime = logEntries.endTime,
                isNight = logEntries.isNight,
                instructorLed = logEntries.instructorLed,
                );

            loghours.insertOne(logEntry)
            call.respond(HttpStatusCode.Created, logEntry);
        }

        delete("/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val licenceId = principal?.payload?.getClaim("licenceId").toString().replace("\"", "")
            val id = call.parameters["id"].toString()
            val filter = "{_id:ObjectId('$id')}"
            val logEntry = loghours.findOne(filter)

            if (logEntry == null) {
                return@delete call.respond(HttpStatusCode.NotFound)

            }
            if (logEntry.licenceId.toString() != licenceId)
                return@delete call.respond(HttpStatusCode.Unauthorized)

            loghours.deleteOne(filter)
            return@delete call.respond(HttpStatusCode.OK)


        }


    }
}