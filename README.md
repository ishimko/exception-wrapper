# Exception Wrapper
[![Build Status](https://travis-ci.com/vanashimko/exception-wrapper.svg?token=DEPCzb7kbvwzsqzYek4Q&branch=master)](https://travis-ci.com/vanashimko/exception-wrapper)

Small library for convinient work with exceptions in Java.

Every wrapper includes methods for decorating some standard functional interfaces (```Supplier<T>```, ```Runnable``` and their versions, throwing checked exceptions) and methods for immediate executing provided delegate.

Includes:

- ```MappingExceptionWrapper```: wraps exceptions from one type to another, supports only unchecked exceptions, because this procedure makes no sense for checked exceptions, which should be declared in method signature;
- ```ExceptionToOptionalWrapper```: wraps exceptions (of specified types or all exceptions) to ```Optional.empty()```;
- ```ExceptionToDefaultWrapper```: wraps exceptions (of specified types or all exceptions) to some predefined default value;
- ```CheckedToRuntimeWrapper```: wraps checked exceptions into ```RuntimeException```, made as utility class, because does not require any configuration.
