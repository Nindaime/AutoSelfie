# AutoSelfie
A sample application that integrates the camera to test OpenCV_3.4 features on android

- Create Opencvclass.java

```
package app0.com.autoselfie;

public class OpencvClass {
    public native static void faceDetection(long addrRgba);
}

```

- open terminal
cd app/src/main
  
- run 

```
javah -d jni -classpath ../../build/intermediates/javac/debug/classes app0.com.autoselfie.OpencvClass 
```

### this step should show no error

inside /src/main directory, you will see a jni folder. Inside the jni folder you will see
app0_com_autoselfie_OpencvClass.h file
copy and paste the file. rename into a .cpp file.

delete some portion of the file. Make it equivalent to
```
JNIEXPORT void JNICALL Java_app0_com_autoselfie_OpencvClass_faceDetection
  (JNIEnv *, jclass, jlong);
```




https://www.youtube.com/watch?v=Tu1808Mum8Q
https://www.youtube.com/watch?v=Z2vrioEr9OI&list=PLYKqj_5SByj41YzTMOypYMWqyXMSwkXaV&index=2