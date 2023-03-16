#  Shelters App

## Summary
This application try to help our customers to find the closest shelter when they find a lost pet.

## Key concepts
+ SOLID
+ Clean Architecture
+ Dependency Injection
+ MVVM Design Pattern

## Key technologies
+ JetPack Compose 
+ Dagger Hilt
+ Google Maps SDK
+ Vapor

## Key Functionalities
+ **Google Map**
    Functionality where the user's location is shown on Google Maps and the shelters available
+ **Map Closest Locations**
    Functionality where the shelter closest to the user's location is shown
+ **Sign Up**
    Functionality where the shelter can register in the application
+ **Sign In**
    Functionality where the shelter can login in the application

## Dependency diagram
![dependency_diagram](./docs/ShelterAppDependencyDiagram.png)

## Stage
### V1
Apps -> basic MVVM structure, welcome view & empty map view for Android
Server ->  basic structure, shelter model, authorization & shelters endpoint

### V2
Apps -> Populate map with backend data, initiate testing, shelter modal & login functionality
Server -> Image upload & register endpoint

### V3
Apps -> Register, detail & get closest shelter functionality. Also we finished mainly testing components
Server -> Protect image upload endpoint by token JWT, add new shelter type attribute & documentation 

## Future
Things we need to do in future improvements of the app:

+ Implement local data management
+ Google Maps API
+ Take into account the shelters opening hours
+ Recover user password
+ Filter by shelter type
+ Delete user account
+ App translation to English & French






