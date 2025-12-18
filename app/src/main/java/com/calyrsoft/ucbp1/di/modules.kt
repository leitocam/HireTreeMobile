package com.calyrsoft.ucbp1.di

import com.calyrsoft.ucbp1.features.auth.data.repository.AuthRepositoryImpl
import com.calyrsoft.ucbp1.features.auth.domain.repository.AuthRepository
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SignInUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SignOutUseCase
import com.calyrsoft.ucbp1.features.auth.domain.usecase.SignUpUseCase
import com.calyrsoft.ucbp1.features.auth.presentation.AuthViewModel
import com.calyrsoft.ucbp1.features.interview.data.api.GeminiService
import com.calyrsoft.ucbp1.features.interview.data.repository.InterviewRepositoryImpl
import com.calyrsoft.ucbp1.features.interview.domain.repository.InterviewRepository
import com.calyrsoft.ucbp1.features.interview.domain.usecase.CompleteInterviewUseCase
import com.calyrsoft.ucbp1.features.interview.domain.usecase.SendMessageUseCase
import com.calyrsoft.ucbp1.features.interview.domain.usecase.StartInterviewUseCase
import com.calyrsoft.ucbp1.features.interview.presentation.InterviewViewModel
import com.calyrsoft.ucbp1.features.login.data.datasource.LoginDataStore
import com.calyrsoft.ucbp1.features.login.data.repository.RepositoryDataStore
import com.calyrsoft.ucbp1.features.login.domain.repository.IRepositoryDataStore
import com.calyrsoft.ucbp1.features.login.domain.usecase.GetTokenUseCase
import com.calyrsoft.ucbp1.features.login.domain.usecase.SaveTokenUseCase
import com.calyrsoft.ucbp1.features.profile.application.ProfileViewModel
import com.calyrsoft.ucbp1.features.profile.data.repository.ProfileRepository
import com.calyrsoft.ucbp1.features.profile.domain.repository.IProfileRepository
import com.calyrsoft.ucbp1.features.profile.domain.usecase.GetProfileUseCase
import com.calyrsoft.ucbp1.navigation.NavigationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo de dependencias de HireTree Mobile
 * Solo incluye features relacionadas con entrevistas y evaluación de soft skills
 */
val appModule = module {

    // ============================================
    // FIREBASE
    // ============================================
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // ============================================
    // AUTH MODULE
    // ============================================
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    viewModel { AuthViewModel(get(), get(), get(), get()) }

    // ============================================
    // INTERVIEW MODULE (CORE - HireTree)
    // ============================================
    single { GeminiService() }
    single<InterviewRepository> { InterviewRepositoryImpl(get(), get()) }
    factory { StartInterviewUseCase(get()) }
    factory { SendMessageUseCase(get()) }
    factory { CompleteInterviewUseCase(get()) }
    viewModel { InterviewViewModel(get(), get(), get()) }

    // ============================================
    // PROFILE MODULE
    // ============================================
    single<IProfileRepository> { ProfileRepository() }
    factory { GetProfileUseCase(get()) }
    viewModel { ProfileViewModel(get(), get()) }

    // ============================================
    // NAVIGATION
    // ============================================
    viewModel { NavigationViewModel() }

    // ============================================
    // LOGIN / DATASTORE
    // ============================================
    single { LoginDataStore(androidContext()) }
    single<IRepositoryDataStore> { RepositoryDataStore(get()) }
    factory { GetTokenUseCase(get()) }
    factory { SaveTokenUseCase(get()) }

}