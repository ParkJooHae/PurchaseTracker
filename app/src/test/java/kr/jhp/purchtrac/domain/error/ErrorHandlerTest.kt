package kr.jhp.purchtrac.domain.error

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for ErrorHandler
 *
 * Tests error handling and conversion:
 * - Converting exceptions to PurchaseTrackerException
 * - Generating user-friendly error messages
 * - Determining error recoverability
 * - Detailed error logging
 */
class ErrorHandlerTest {

    @Test
    fun testDatabaseExceptionHandling() {
        // Arrange
        val cause = Exception("Connection failed")
        val exception = PurchaseTrackerException.DatabaseException(cause)

        // Act
        val userMessage = ErrorHandler.getUserMessage(exception)
        val detailed = ErrorHandler.getDetailedMessage(exception)

        // Assert
        assertEquals("데이터베이스 오류가 발생했습니다", userMessage)
        assertTrue { detailed.contains("DatabaseException") }
        assertTrue { ErrorHandler.isRecoverable(exception) }
    }

    @Test
    fun testEntityNotFoundExceptionHandling() {
        // Arrange
        val exception = PurchaseTrackerException.EntityNotFoundException(
            entityName = "Memo",
            entityId = 123
        )

        // Act
        val userMessage = ErrorHandler.getUserMessage(exception)
        val detailed = ErrorHandler.getDetailedMessage(exception)

        // Assert
        assertEquals("Memo을(를) 찾을 수 없습니다", userMessage)
        assertTrue { detailed.contains("EntityNotFoundException") }
        assertTrue { detailed.contains("ID: 123") }
        assertFalse { ErrorHandler.isRecoverable(exception) }
    }

    @Test
    fun testValidationExceptionHandling() {
        // Arrange
        val exception = PurchaseTrackerException.ValidationException(
            fieldName = "email",
            message = "email is invalid"
        )

        // Act
        val userMessage = ErrorHandler.getUserMessage(exception)
        val detailed = ErrorHandler.getDetailedMessage(exception)

        // Assert
        assertEquals("email 값이 유효하지 않습니다", userMessage)
        assertTrue { detailed.contains("ValidationException") }
        assertTrue { ErrorHandler.isRecoverable(exception) }
    }

    @Test
    fun testConflictExceptionHandling() {
        // Arrange
        val exception = PurchaseTrackerException.ConflictException("Duplicate entry")

        // Act
        val userMessage = ErrorHandler.getUserMessage(exception)

        // Assert
        assertEquals("중복되는 데이터가 있습니다", userMessage)
        assertTrue { ErrorHandler.isRecoverable(exception) }
    }

    @Test
    fun testPermissionExceptionHandling() {
        // Arrange
        val exception = PurchaseTrackerException.PermissionException()

        // Act
        val userMessage = ErrorHandler.getUserMessage(exception)

        // Assert
        assertEquals("권한이 없습니다", userMessage)
        assertFalse { ErrorHandler.isRecoverable(exception) }
    }

    @Test
    fun testUnknownExceptionHandling() {
        // Arrange
        val cause = RuntimeException("Some unknown error")
        val exception = PurchaseTrackerException.UnknownException(cause)

        // Act
        val userMessage = ErrorHandler.getUserMessage(exception)
        val detailed = ErrorHandler.getDetailedMessage(exception)

        // Assert
        assertEquals("알 수 없는 오류가 발생했습니다", userMessage)
        assertTrue { detailed.contains("UnknownException") }
        assertTrue { ErrorHandler.isRecoverable(exception) }
    }

    @Test
    fun testHandleExceptionConvertsThrowableToException() {
        // Arrange
        val throwable = RuntimeException("Test error")

        // Act
        val handled = ErrorHandler.handleException(throwable)

        // Assert
        assertTrue { handled is PurchaseTrackerException.UnknownException }
    }

    @Test
    fun testHandleExceptionRetainsAlreadyConvertedException() {
        // Arrange
        val original = PurchaseTrackerException.DatabaseException(Exception("DB error"))

        // Act
        val handled = ErrorHandler.handleException(original)

        // Assert
        assertTrue { handled === original }
    }

    @Test
    fun testErrorTypeDisplayMessages() {
        // Arrange & Act & Assert
        assertEquals("Database error occurred", ErrorType.DATABASE_ERROR.displayMessage)
        assertEquals("Item not found", ErrorType.NOT_FOUND.displayMessage)
        assertEquals("Invalid input", ErrorType.VALIDATION_ERROR.displayMessage)
        assertEquals("Data conflict", ErrorType.CONFLICT.displayMessage)
        assertEquals("Permission denied", ErrorType.PERMISSION_DENIED.displayMessage)
        assertEquals("An unexpected error occurred", ErrorType.UNKNOWN.displayMessage)
    }
}
