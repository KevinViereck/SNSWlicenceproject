package model
import ObjectIdAsStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDate
import java.util.*

//this class is to store the licence details of every customer and only viewable by CSR.
@Serializable
data class LearnerLicence (
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence> = newId(),
    val dateRegistered:Long = System.currentTimeMillis(),
    val remainingHours: Int = 120,
    val email:String,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val userId: Id<User>

    )

