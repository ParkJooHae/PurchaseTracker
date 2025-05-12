package kr.jhp.purchtrac.data.local.database

import androidx.room.TypeConverter
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.domain.model.UserType

/**
 * Room 데이터베이스에서 사용할 타입 컨버터 클래스
 * Enum 값을 문자열로 변환하여 저장하고, 문자열을 Enum으로 변환하여 로드합니다.
 */
class AppTypeConverters {
    @TypeConverter
    fun userTypeToString(userType: UserType): String {
        return userType.name
    }

    @TypeConverter
    fun stringToUserType(value: String): UserType {
        return UserType.valueOf(value)
    }

    @TypeConverter
    fun productStatusToString(productStatus: ProductStatus): String {
        return productStatus.name
    }

    @TypeConverter
    fun stringToProductStatus(value: String): ProductStatus {
        return ProductStatus.valueOf(value)
    }
}
