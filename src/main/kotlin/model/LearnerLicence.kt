package model
import ObjectIdAsStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class LearnerLicence (
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LearnerLicence> = newId(),
    val dateIssued: Long = System.currentTimeMillis(),
    val practiceLogEntries: List<PracticeLogEntry>,
    val userId: Id<User>

    )

@Serializable
data class PracticeLogEntry(
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val instructorLed: Boolean

)