[![Build Status](https://travis-ci.org/stefanodacchille/fast-bookmark.svg)](https://travis-ci.org/stefanodacchille/fast-bookmark)

# fast-bookmark

Simply copy a link to add it to your favourite read later service

# Project goals:
- Getting some experience writing kotlin for prod and test code
- Trying the [MVVM pattern](https://www.objc.io/issues/13-architecture/mvvm/) out
- Trying out how having a Android instead of a POJO prenter impacts testing
- Trying out Dagger 2
- Integrating Travis CI or some other cloud service for testing

# Open questions:
- how do I mock a kotlin class with mockito? is it even possible? Something like
`AppPreferences::class` returns a `kotlin.reflect.KClass` reference and not a `java.lang.Class`
one, needed by mockito. Using `AppPreferences::class.java` returns the java class but the mocking
 does not work.
