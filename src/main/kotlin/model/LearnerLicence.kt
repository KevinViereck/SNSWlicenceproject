package model
import ObjectIdAsStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDate
import java.util.*

@Serializable
data class LearnerLicence (
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence> = newId(),
    val dateRegistered:String,
    val remainingHours: Int,
    val email:String,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val userId: Id<User>

    )

