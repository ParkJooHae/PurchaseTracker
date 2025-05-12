package kr.jhp.purchtrac

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.jhp.purchtrac.domain.repository.UserRepository
import kr.jhp.purchtrac.domain.usecase.app.InitializeAppUseCase
import javax.inject.Inject

@HiltAndroidApp
class PurchaseTrackerApplication : Application() {

    @Inject
    lateinit var initializeAppUseCase: InitializeAppUseCase

    override fun onCreate() {
        super.onCreate()
        // 앱 초기화 시 기본 사용자 생성 (한 번만)
        CoroutineScope(Dispatchers.IO).launch {
            initializeAppUseCase()
        }
    }
}