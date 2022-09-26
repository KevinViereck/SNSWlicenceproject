package model
import ObjectIdAsStringSerializer
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class LogBook (
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<User> = newId(),
    val hours: Int,
    val Date: String,
    val Time : String,
    val TripDetails: String,


        )
