package routes
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.LogEntries
import org.litote.kmongo.*
import model.LogEntriesDTO
import model.toDTO


fun Route.logEntries (db: MongoDatabase) {
    val loghours = db.getCollection<LogEntries>("loghours")

    route("/loghours") {

        get("/{id}") {

            val principal = call.principal<JWTPrincipal>()
            val id = principal?.payload?.getClaim("licenceId").toString().replace("\"", "")
            val filter = "{objectId:/^${id}$/i}"
            val logEntries = loghours.find(filter).toList()
            call.respond(logEntries.map { it. toDTO()})
//            val logEntries = loghours.find().toList()
//            call.respond(logEntries)
        }

        post{
            val logEntries = call.receive<LogEntries>()
            val principal = call.principal<JWTPrincipal>()
            val id = principal?.payload?.getClaim("_id").toString().replace("\"", "")

            val logentry = LogEntries(startTime = logEntries.startTime,
                endTime = logEntries.endTime,
                day= logEntries.day,
                night = logEntries.night,
                instructorLed = logEntries.instructorLed,);

                loghours.insertOne(logentry)

                call.respond(HttpStatusCode.Created,logentry);
        }

    }
}