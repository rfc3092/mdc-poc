Experimenting a bit with Spring Aspect.

Goal:
- Create an annotation `@MyCustomMdcAnnotation` that fills in MDC from a given source, `MdcContextProvider`.
- If the class containing a method annotated with `@MyCustomMdcAnnotation` implements `MdcContextProvider`, add context from provider,
  otherwise log a warning. 

Also:
- Added an autocloseable `MdcContext` to illustrate a low effort alternative doing what I wanted to achieve using AOP.
