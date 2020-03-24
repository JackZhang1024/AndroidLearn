package com.lucky.androidlearn.dagger2learn.learn03.component;

import com.lucky.androidlearn.dagger2learn.learn03.module.ClassModule;
import com.lucky.androidlearn.dagger2learn.learn03.module.SchoolModule;

import dagger.Component;
import dagger.Subcomponent;


// /**
// * A subcomponent that inherits the bindings from a parent {@link Component} or
// * {@link Subcomponent}. The details of how to associate a subcomponent with a parent are described
// * in the documentation for {@link Component}.
// *
// * @author Gregory Kick
// * @since 2.0
// */

@Subcomponent(modules = ClassModule.class)
public interface Dagger2Main3ClassComponent {

    Dagger2Main3SchoolComponent plus(SchoolModule schoolModule);

}
