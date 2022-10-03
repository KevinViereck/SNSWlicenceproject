package model

import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import ObjectIdAsStringSerializer

@Serializable
data class LogEntries(

    @Serializable(with = ObjectIdAsStringSerializer::class)
    val licenceId: Id<LearnerLicence>,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val day: String,
    val night: String,
    val instructorLed: Boolean,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LogEntries>
) {
}