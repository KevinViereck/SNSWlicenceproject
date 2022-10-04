package model
import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class User (
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val dateOfBirth: String = "",
    val mobile: String = "",
    val password:String,
    val roles: List<String> = listOf(),
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<User> = newId()
        )

@Serializable
data class UserDTOLogin(
    val email: String,
    val roles:List<String>,
    val token:String,
    val authenticated:Boolean,
)