package model

import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import ObjectIdAsStringSerializer
import org.litote.kmongo.newId

@Serializable
data class LogEntries(

    // stores the data of customers for their log hours and log history
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val licenceId: Id<LearnerLicence>,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val day: String,
    val night: String,
    val instructorLed: Boolean,
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LogEntries>
)

@Serializable
data class LogEntriesDTO(
    @Serializable(with = ObjectIdAsStringSerializer::class)
    val _id: Id<LogEntries> = newId(),
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val day: String,
    val night: String,
    val instructorLed: Boolean,
)

fun LogEntries.toDTO(): LogEntriesDTO{
    return LogEntriesDTO(
        _id = this._id,
        startTime = this.startTime,
        endTime = this.endTime,
        day = this.day,
        night = this.night,
        instructorLed = this.instructorLed,
    )
}