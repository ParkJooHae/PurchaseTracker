package kr.jhp.purchtrac.domain.error

/**
 * Base exception for all PurchaseTracker-related errors.
 * Provides a consistent error handling mechanism across the application.
 *
 * @property errorType The type of error that occurred
 * @property message A user-friendly error message
 */
sealed class PurchaseTrackerException(
    val errorType: ErrorType,
    override val message: String
) : Exception(message) {

    /**
     * Database operation failed
     */
    data class DatabaseException(
        val exception: Throwable,
        override val message: String = "Database operation failed: ${exception.message}"
    ) : PurchaseTrackerException(ErrorType.DATABASE_ERROR, message)

    /**
     * Entity not found in database
     */
    data class EntityNotFoundException(
        val entityName: String,
        val entityId: Long,
        override val message: String = "$entityName with id $entityId not found"
    ) : PurchaseTrackerException(ErrorType.NOT_FOUND, message)

    /**
     * Invalid data provided
     */
    data class ValidationException(
        val fieldName: String,
        override val message: String = "$fieldName is invalid"
    ) : PurchaseTrackerException(ErrorType.VALIDATION_ERROR, message)

    /**
     * Data operation conflict (e.g., duplicate entry)
     */
    data class ConflictException(
        override val message: String = "Data conflict occurred"
    ) : PurchaseTrackerException(ErrorType.CONFLICT, message)

    /**
     * Unknown error occurred
     */
    data class UnknownException(
        val exception: Throwable,
        override val message: String = "An unexpected error occurred: ${exception.message}"
    ) : PurchaseTrackerException(ErrorType.UNKNOWN, message)

    /**
     * Permission denied
     */
    data class PermissionException(
        override val message: String = "Permission denied"
    ) : PurchaseTrackerException(ErrorType.PERMISSION_DENIED, message)
}

/**
 * Enum representing different types of errors that can occur in the application.
 */
enum class ErrorType(val displayMessage: String) {
    DATABASE_ERROR("Database error occurred"),
    NOT_FOUND("Item not found"),
    VALIDATION_ERROR("Invalid input"),
    CONFLICT("Data conflict"),
    PERMISSION_DENIED("Permission denied"),
    UNKNOWN("An unexpected error occurred")
}
