package kr.jhp.purchtrac.domain.error

/**
 * Utility object for handling and converting exceptions to user-friendly messages.
 * This provides a centralized place for error message formatting and mapping.
 */
object ErrorHandler {

    /**
     * Convert any exception to a PurchaseTrackerException
     *
     * @param exception The exception to convert
     * @return A PurchaseTrackerException instance
     */
    fun handleException(exception: Throwable): PurchaseTrackerException {
        return when (exception) {
            is PurchaseTrackerException -> exception
            else -> PurchaseTrackerException.UnknownException(exception)
        }
    }

    /**
     * Get user-friendly error message from exception
     *
     * @param exception The exception to get message from
     * @return User-friendly error message
     */
    fun getUserMessage(exception: Throwable): String {
        return when (val handled = handleException(exception)) {
            is PurchaseTrackerException.DatabaseException -> "데이터베이스 오류가 발생했습니다"
            is PurchaseTrackerException.EntityNotFoundException -> "${handled.entityName}을(를) 찾을 수 없습니다"
            is PurchaseTrackerException.ValidationException -> "${handled.fieldName} 값이 유효하지 않습니다"
            is PurchaseTrackerException.ConflictException -> "중복되는 데이터가 있습니다"
            is PurchaseTrackerException.PermissionException -> "권한이 없습니다"
            is PurchaseTrackerException.UnknownException -> "알 수 없는 오류가 발생했습니다"
        }
    }

    /**
     * Get developer-friendly error message with full details
     *
     * @param exception The exception to get detailed message from
     * @return Detailed error message for logging
     */
    fun getDetailedMessage(exception: Throwable): String {
        return when (val handled = handleException(exception)) {
            is PurchaseTrackerException.DatabaseException -> {
                "DatabaseException: ${handled.message}\nCause: ${handled.exception}"
            }
            is PurchaseTrackerException.EntityNotFoundException -> {
                "EntityNotFoundException: ${handled.entityName} (ID: ${handled.entityId})"
            }
            is PurchaseTrackerException.ValidationException -> {
                "ValidationException: ${handled.fieldName} - ${handled.message}"
            }
            is PurchaseTrackerException.ConflictException -> {
                "ConflictException: ${handled.message}"
            }
            is PurchaseTrackerException.PermissionException -> {
                "PermissionException: ${handled.message}"
            }
            is PurchaseTrackerException.UnknownException -> {
                "UnknownException: ${handled.message}\nCause: ${handled.exception}"
            }
        }
    }

    /**
     * Check if an exception is recoverable (user can retry)
     *
     * @param exception The exception to check
     * @return True if the error is recoverable
     */
    fun isRecoverable(exception: Throwable): Boolean {
        return when (handleException(exception)) {
            is PurchaseTrackerException.DatabaseException -> true
            is PurchaseTrackerException.EntityNotFoundException -> false
            is PurchaseTrackerException.ValidationException -> true
            is PurchaseTrackerException.ConflictException -> true
            is PurchaseTrackerException.PermissionException -> false
            is PurchaseTrackerException.UnknownException -> true
        }
    }
}
