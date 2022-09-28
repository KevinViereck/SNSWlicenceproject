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
    @Contextual
    val dateIssued: Date,
    val practiceLogEntries: List<PracticeLogEntry>,
    val userId: String

    )

@Serializable
data class PracticeLogEntry(
    @Contextual
    val start: Date,
    @Contextual
    val end: Date,
    val instructorLed: Boolean

)