package com.example.tracker.di.scopes

import androidx.lifecycle.ViewModel
import dagger.MapKey
import java.lang.annotation.Documented
import javax.xml.transform.OutputKeys.METHOD
import kotlin.reflect.KClass

@MapKey
@Documented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)