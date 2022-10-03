package routes
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.LogEntries
import org.litote.kmongo.*


fun Route.logEntries (db: MongoDatabase) {
    val loghours = db.getCollection<LogEntries>("loghours")

    route("/loghours") {

        get {
            val logEntries = loghours.find().toList()
            call.respond(logEntries)
        }

        post{
            val logEntries = call.receive<LogEntries>()
        }

    }
}