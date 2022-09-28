package model
import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class User (
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobile: Int,
    val password:String,
    val roles: Array<String> = arrayOf(""),
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<User> = newId()
        )