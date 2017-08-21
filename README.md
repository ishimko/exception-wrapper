# Exception Wrapper
[![Build Status](https://travis-ci.org/vanashimko/exception-wrapper.svg?branch=master)](https://travis-ci.org/vanashimko/exception-wrapper)
[![codecov](https://codecov.io/gh/vanashimko/exception-wrapper/branch/master/graph/badge.svg)](https://codecov.io/gh/vanashimko/exception-wrapper)

Small library for convinient work with exceptions in Java.

Includes:
- ```MappingExceptionWrapper```: wraps exceptions from one type to another, supports only unchecked exceptions, because this procedure makes no sense for checked exceptions: such exceptions should be declared in method signature, so wrapper or `Callable<V>` method will be marked as `throws Exceptions`, and your methods, that will use them should be marked too. The only (and very ugly) solution would be to manually check exception type inside `try...catch` block;
- ```ExceptionToOptionalWrapper```: wraps exceptions (of specified types or all exceptions, if no types are provided) to ```Optional.empty()```;
- ```ExceptionToDefaultValueWrapper```: wraps exceptions (of specified types or all exceptions, if no types are provided) to some predefined default value. Includes helper methods `bindExceptions` and `bindDefaultValue` returning functions `defaultValue -> wrapper` and `exceptions -> wrapper` respectively. This functions will create wrapper from provided argument using another value from `bindExceptions` and `bindDefaultValue` methods;
- ```CheckedToRuntimeWrapper```: wraps checked exceptions into ```RuntimeException```, made as utility class, because does not require any configuration. **Checked exceptions concept is arguable. If you're crazy about checked exceptions in Java, please forgive me for this code. **

Every wrapper includes methods for decorating some standard functional interfaces (```Supplier<T>```, ```Runnable``` and their versions, throwing checked exceptions) and methods for immediate executing provided delegate. 

## Usage

You are free to create and use wrappers the way you like, all objects are immutable and don't save any state between accessing applying or wrapping methods, so their instances are fully reusable. 

To make the code cleaner you can create static class (just like `Collectors` from Stream API or `ExpectedConditions` from Selenium WebDriver API), which will store instances of the preconfigured wrappers:

```java
public class Wrappers {
    private static final MappingExceptionWrapper DAO_TO_SERVICE =
            new MappingExceptionWrapper(
                    new WrappingConfiguration<>(UncheckedDaoException.class, UncheckedServiceException::new)
            );
    private static final ExceptionToOptionalWrapper DAO_EXCEPTION_TO_OPTIONAL =
            new ExceptionToOptionalWrapper(
                    CheckedDaoException.class, UncheckedDaoException.class
            );
    private static final ExceptionToDefaultValueWrapper<Integer> DAO_EXCEPTION_TO_DEFAULT =
            new ExceptionToDefaultValueWrapper<>(
                    0,
                    CheckedDaoException.class, UncheckedDaoException.class
            );

    public static MappingExceptionWrapper daoToServiceException() {
        return DAO_TO_SERVICE;
    }

    public static ExceptionToOptionalWrapper daoExceptionToOptional() {
        return DAO_EXCEPTION_TO_OPTIONAL;
    }

    public static ExceptionToDefaultValueWrapper<Integer> daoExceptionToDefaultValue() {
        return DAO_EXCEPTION_TO_DEFAULT;
    }
}
```

Actually you can instantiate wrappers every time directly inside the method, but as I already mentioned, it is not necessary. Anyway static methods usage provides ability to change this behavior in the future.

To use wrapper in your code simply get an appropriate one from `Wrappers` (or create directly in place) and use the method you need (`apply` for decorating existing delegate and using new delegate later or `wrap` to executing provided delegate with applied wrapping):

```java
public void someServiceMethod() {
    Wrappers.daoToServiceException().wrap(dao::dropSomeTables); 
    /* 
        Assuming daoToServiceException method defined in the previous example, 
        UncheckedDaoException's will be wrapped into UncheckedServiceException 
    */
}

public Optional<VeryBusinessObject> getSomethingFromSomewhere() {
    // Let's create business object getter, that will wrap DAO exceptions into Optional.empty()
    Supplier<Optional<BusinessObject>> businessObjectGetter = Wrappers.daoExceptionToOptional().applyTo(dao::getBusinessObject); 
  	// ... and pass it into another method, that will produce VeryBusinessObject (optional, of course)
    return anotherServiceMethod(businessObjectGetter);
}

@Deprecated
@OldLegacyCode
private String readSomeAncientSettingEntryFromAncientSettingFile() {  
    return new ExceptionToDefaultWrapper("default-value")
               .wrap(() -> Files.readAllLines("/home/admin/trash/config.cfg")
               .get(0));
    /*  
        We've just instantiated wrapper in place, though it is not very efficient, 
        to wrap all exceptions occured while reading this moldy file and even possible IndexOutOfBoundException,
        accessing result list, to "default-value" 
    */
}
```
