# NearBy

App has two operational modes, “Realtime" and “Single Update”. “Realtime” allows app to always display to the user the current near by places based on his location, data should be seamlessly updated if user moved by 500 m from the location of last retrieved places. 

“Single update” mode means the app is updated once in app is launched and doesn’t update again 

User should be able to switch between the two modes, default mode is “Realtime”. App should remember user choices for next launches


Using:

-MVVM Architecture pattern.
-Dagger2 Dependency Injection.
-Retrofit.
-RxJava
