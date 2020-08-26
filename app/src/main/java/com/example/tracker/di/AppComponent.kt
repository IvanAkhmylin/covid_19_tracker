package com.example.tracker.di

import com.example.tracker.App
import com.example.tracker.di.module.*
import com.example.tracker.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        DataSourcesModule::class,
        ActivityModule::class,
        FragmentsModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: App)
}
