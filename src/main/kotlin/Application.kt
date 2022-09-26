import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import org.litote.kmongo.*

val client = KMongo.createClient()
val database = client.getDatabase("DATABASE_NAME")
val collectionName = database.getCollection<DATA_TYPE>("COLLECTION_NAME")

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

    routing {
        route("/contentName") {
            get{
                val data = collectionName.find().toList()
                call.respond(data)
            }
            get("/{id}"){
                val id = call.parameters["id"].toString()
                val filter = "{_id:ObjectId('$id')}"
                val entity = collectionName.findOne(filter)
                if (entity != null) {
                    call.respond(entity)
                } else {
                    call.respond((HttpStatusCode.NotFound))
                }
            }
            post{
                val entity = call.receive<DATA_TYPE>()
                collectionName.insertOne(entity)
                call.respond(HttpStatusCode.Created, entity)
            }
            put{
                val entity = call.receive<DATA_TYPE>()
                val result = collectionName.updateOne(entity)
                if (result.modifiedCount.toInt() == 1){
                    call.respond(HttpStatusCode.OK, entity)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            delete("/{id}"){
                val id = call.parameters["id"].toString()
                val filter = "{_id:ObjectId('$id')}"
                val result = collectionName.deleteOne(filter)
                if (result.deletedCount.toInt() == 1) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}

@Serializable
data class DATA_TYPE(
    val firstProperty: String, // can use any type
    val secondProperty: String,
    val thirdProperty: Int,
    @Serializable(with = ObjectIdAsStringSerializer::class) val _id: Id<DATA_TYPE> = newId(),
)